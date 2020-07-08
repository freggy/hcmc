package dev.freggy.hcmc

import dev.freggy.hcmc.hcloud.HetznerCloud
import dev.freggy.hcmc.hcloud.model.Server
import kotlinx.coroutines.channels.Channel
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.util.concurrent.ConcurrentHashMap

class HcmcPlugin : JavaPlugin() {

    // pool of current servers mapped id -> server
    val serverPool = ConcurrentHashMap<Int, Server>()
    val serverEvents = Channel<Unit>()

    private var hcloud: HetznerCloud = TODO()

    override fun onEnable() {
        hcloud = HetznerCloud("")

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, Runnable {
            // TODO:
            //   * fetch actions with /actions?sort=finished:desc
            //   * save last fetch timestamp to determine which actions are new
            //   * distribute to corresponding channel
        }, 0, 20)
    }

    override fun onDisable() {
        this.serverEvents.close()
        //this.hcloud.client.close()
    }
}
