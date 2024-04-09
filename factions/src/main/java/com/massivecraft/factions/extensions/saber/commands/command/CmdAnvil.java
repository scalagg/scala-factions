package com.massivecraft.factions.extensions.saber.commands.command;

import com.massivecraft.factions.extensions.saber.utils.Message;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.MassiveCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

public class CmdAnvil extends MassiveCommand {

    public CmdAnvil()
    {
        this.aliases.add("anvil");
    }

    private static CmdAnvil i = new CmdAnvil();
    public static CmdAnvil get() { return i; }

    @Override
    public void perform() throws MassiveException
    {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Message.NOT_PLAYER.getMessage());
            return;
        }

        if (!sender.hasPermission("sabercore.anvil")) {
            sender.sendMessage(Message.NO_PERMISSION.getMessage());
            return;
        }

        Player player = (Player) sender;
        player.closeInventory();
        player.openInventory(Bukkit.createInventory(null, InventoryType.ANVIL));
        player.sendMessage(Message.ANVIL_OPENED.getMessage());
    }
}
