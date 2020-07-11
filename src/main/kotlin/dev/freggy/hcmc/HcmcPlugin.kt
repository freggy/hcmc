package dev.freggy.hcmc

import dev.freggy.hcmc.hcloud.HetznerCloud
import dev.freggy.hcmc.hcloud.model.Action
import dev.freggy.hcmc.hcloud.model.ActionCommand
import dev.freggy.hcmc.hcloud.model.ActionStatus
import dev.freggy.hcmc.hcloud.model.Server
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.plugin.java.JavaPlugin
import org.joda.time.DateTime
import java.util.concurrent.ConcurrentHashMap


class HcmcPlugin : JavaPlugin() {

    // pool of current servers mapped id -> server
    val serverPool = ConcurrentHashMap<Int, PlacedServer>()
    val serverStatusEvents = Channel<Action>()
    val serverPresenceEvents = Channel<Action>()

    private lateinit var hcloud: HetznerCloud

    override fun onEnable() {
        hcloud = HetznerCloud("") // TODO: read token from command
        hcloud.servers.getAllServers().let {
            placeServers(it).forEach { placed ->
                serverPool[placed.inner.id] = placed
            }
        }

        GlobalScope.launch {
            var lastCheck = System.currentTimeMillis()
            while (true) {
                // 2 second delay here so we can have 1 different api request per second
                delay(500)
                val page = hcloud.actions.getActions(listOf(Pair("sort", "finished:desc")))
                page.actions
                    .filter { DateTime(it.finished).isAfter(lastCheck) }
                    .filter { it.status == ActionStatus.SUCCESS }
                    .forEach {
                        Bukkit.broadcastMessage(it.toString())
                        Bukkit.broadcastMessage("---")
                        if (it.command.isServerPresence) {
                            serverPresenceEvents.send(it)
                            return@forEach
                        }
                        if (it.command.isServerStatus) {
                            serverStatusEvents.send(it)
                            return@forEach
                        }
                    }
                lastCheck = System.currentTimeMillis()
            }
        }

        GlobalScope.launch(PluginContext(this)) {
            while (true) {
                val event = serverStatusEvents.receive()
                when (event.command) {
                    ActionCommand.STOP_SERVER -> {
                        event.resources.forEach {
                            // entry can be absent because STOP_SERVER is triggered after DELETE_SERVER
                            serverPool[it.id]?.let { placed ->
                                Bukkit.broadcastMessage("STOPPED server ${it.id} at ${placed.location}")
                                val ctx = coroutineContext[PluginContext]!!
                                Bukkit.getScheduler().callSyncMethod(ctx.plugin) {
                                    placed.stop()
                                }
                                placed.updateInner(hcloud)
                            }
                        }
                    }
                }
            }
        }

        GlobalScope.launch(PluginContext(this)) {
            while (true) {
                val event = serverPresenceEvents.receive()
                when (event.command) {
                    ActionCommand.DELETE_SERVER -> {
                        event.resources.forEach {
                            serverPool[it.id]?.let { server ->
                                Bukkit.broadcastMessage("REMOVED server ${it.id} at ${server.location}")
                                val ctx = coroutineContext[PluginContext]!!
                                Bukkit.getScheduler().callSyncMethod(ctx.plugin) {
                                    server.remove()
                                }
                                serverPool.remove(it.id)
                            }
                        }
                    }
                    ActionCommand.CREATE_SERVER -> {
                    }
                }
            }
        }
    }

    override fun onDisable() {
        this.serverStatusEvents.close()
        this.serverPresenceEvents.close()
    }

    fun placeServers(servers: List<Server>): MutableList<PlacedServer> {
        val world = Bukkit.getWorld("world")
        val placed = mutableListOf<PlacedServer>()
        var x = 0
        var z = 0

        servers.forEach {
            val chunk = world!!.getChunkAt(x, z)
            // draw chunk borders
            for (i in 0..15) {
                chunk.getBlock(i, 3, 0).type = Material.BEDROCK
                chunk.getBlock(15, 3, i).type = Material.BEDROCK
                chunk.getBlock(0, 3, i).type = Material.BEDROCK
                chunk.getBlock(i, 3, 15).type = Material.BEDROCK
            }
            z++
            x++
            val place = PlacedServer(it, chunk.getBlock(8, 5, 8).location)
            place.place()
            placed.add(place)
        }
        return placed
    }
}
