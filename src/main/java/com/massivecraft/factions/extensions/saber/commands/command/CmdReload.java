package com.massivecraft.factions.extensions.saber.commands.command;

import com.massivecraft.factions.Factions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static com.massivecraft.factions.extensions.saber.utils.SaberUtils.color;

public class CmdReload implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            if (!sender.hasPermission("sabercore.reload")) return true;
            if (args[0].equalsIgnoreCase("reload")) {
                sender.sendMessage(color("&c(!) Reloading Configs..."));
                Factions.get().reloadConfig();
                sender.sendMessage(color("&c(!) Configs Reloaded!"));
                sender.sendMessage(color("&c(!)Reload Complete!"));
                return true;
            }
        }
        return true;
    }
}

