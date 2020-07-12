package dev.freggy.hcmc.plugin.command

import dev.freggy.hcmc.plugin.HcmcPlugin
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class SetApiKeyCommand(private val hcmc: HcmcPlugin) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (!label.equals("apikey", ignoreCase = true)) return false
        if (args.isEmpty()) {
            sender.sendMessage("USAGE: /apikey <STRING>")
            return false
        }
        hcmc.setApiKey(args[0])
        return true
    }
}
