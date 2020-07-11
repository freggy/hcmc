package dev.freggy.hcmc

import dev.freggy.hcmc.hcloud.HetznerCloud
import dev.freggy.hcmc.hcloud.model.Server
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.plugin.java.JavaPlugin
import java.util.concurrent.ConcurrentHashMap

class HcmcPlugin : JavaPlugin() {

    // pool of current servers mapped id -> server
    val serverPool = ConcurrentHashMap<Int, PlacedServer>()
    val serverStatusEvents = Channel<Unit>()
    val serverPresenceEvents = Channel<Unit>()

    private lateinit var hcloud: HetznerCloud

    override fun onEnable() {
        hcloud = HetznerCloud("") // TODO: read token from command
        hcloud.servers().getAllServers().let {
            placeServers(it).forEach { placed ->
                serverPool[placed.inner.id] = placed
            }
        }

        GlobalScope.launch {
            // 2 second delay here so we can have 1 different api request per second
            delay(2000)
            // TODO:
            //   * fetch actions with /actions?sort=finished:desc
            //   * save last fetch timestamp to determine which actions are new
            //   * distribute to corresponding channel
        }

        GlobalScope.launch {
            while (true) {
                val event = serverStatusEvents.receive()
                // TODO: update placed server accordingly
            }
        }

        GlobalScope.launch {
            while (true) {
                val event = serverPresenceEvents.receive()
                // TODO:
                //   * remove placed server when delete event
                //   * place when server created and add to pool
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

            chunk.getBlock(8, 5, 8).type = Material.EMERALD_BLOCK
            z++
            x++
            placed.add(PlacedServer(it, chunk.getBlock(8, 5, 8).location))
        }
        return placed
    }
}
