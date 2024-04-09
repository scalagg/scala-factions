package com.massivecraft.factions.extensions.saber.commands.command;

import com.massivecraft.factions.extensions.saber.utils.ItemCreation;
import com.massivecraft.factions.extensions.saber.utils.Message;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import org.bukkit.Bukkit;

public class CmdChunkbuster extends MassiveCommand {

    public CmdChunkbuster() {
        this.aliases.add("chunkbuster");
        this.aliases.add("cb");

        this.addParameter(TypeString.get(), "player");
        this.addParameter(TypeInteger.get(), "amount");
    }

    private static CmdChunkbuster i = new CmdChunkbuster();

    public static CmdChunkbuster get() {
        return i;
    }

    @Override
    public void perform() throws MassiveException {
        if (args.size() == 2) {
            if (!sender.hasPermission("sabercore.chunkbusters.give")) {
                sender.sendMessage(Message.NO_PERMISSION.getMessage());
                return;
            }
            if (Bukkit.getPlayer(args.get(0)) == null || !Bukkit.getPlayer(args.get(0)).isOnline()) {
                sender.sendMessage(Message.PLAYER_NOT_FOUND.getMessage().replace("%player%", args.get(1)));
                return;
            }
            int amount = Integer.parseInt(args.get(1));
            if (amount >= 1) {
                if (Bukkit.getServer().getPlayer(args.get(0)).isOnline()) {
                    Bukkit.getServer().getPlayer(args.get(0)).getInventory().addItem(ItemCreation.giveChunkBuster(amount));
                    Bukkit.getServer().getPlayer(args.get(0)).sendMessage(Message.CHUNKBUSTER_RECEIVED_MESSAGE.getMessage());
                }
            }
        } else if (sender.hasPermission("sabercore.chunkbusters.give")) {
            sender.sendMessage(Message.CHUNKBUSTER_COMMAND_USAGE.getMessage());
        }
    }
}