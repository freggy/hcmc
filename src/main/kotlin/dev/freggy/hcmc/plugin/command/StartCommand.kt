package dev.freggy.hcmc.plugin.command

import dev.freggy.hcmc.plugin.HcmcPlugin
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class StartCommand(private val hcmc: HcmcPlugin) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        Bukkit.broadcastMessage("Starting HCMC")
        hcmc.start()
        return true
    }
}
