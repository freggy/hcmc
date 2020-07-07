package dev.freggy.hcmc

import dev.freggy.hcmc.hcloud.HetznerCloud
import org.bukkit.plugin.java.JavaPlugin

class HcmcPlugin : JavaPlugin() {

    private var hcloud: HetznerCloud = TODO()

    override fun onEnable() {
        hcloud = HetznerCloud("")
    }

    override fun onDisable() {
        hcloud.client.close()
    }
}