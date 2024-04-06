package com.massivecraft.factions.extensions.saber.commands.handlers;

import com.massivecraft.factions.entity.Board;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.extensions.saber.commands.handlers.wands.Wand;
import com.massivecraft.factions.extensions.saber.commands.handlers.wands.impl.CraftWand;
import com.massivecraft.factions.extensions.saber.commands.handlers.wands.impl.LightningWand;
import com.massivecraft.factions.extensions.saber.commands.handlers.wands.impl.SandWand;
import com.massivecraft.factions.extensions.saber.commands.handlers.wands.impl.SmeltWand;
import com.massivecraft.factions.extensions.saber.utils.Message;
import com.massivecraft.factions.extensions.saber.utils.XMaterial;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class WandHandler implements Listener {

    @EventHandler
    public void onWandUse(PlayerInteractEvent e) {
        if (e.getClickedBlock() == null
                || (!SandWand.sandWandItemsToRemove.contains(e.getClickedBlock().getType().toString())
                && e.getClickedBlock().getType() != XMaterial.CHEST.parseMaterial()
                && e.getClickedBlock().getType() != XMaterial.TRAPPED_CHEST.parseMaterial())
                || e.getItem() == null
                || !Wand.isWand(e.getItem())) {
            return;
        }

        Player player = e.getPlayer();
        Faction faction = MPlayer.get(player.getUniqueId()).getFaction();
        Faction wilderness = FactionColl.get().getNone();
        ItemStack eventItem = e.getItem();
        Block block = e.getClickedBlock();
        Chest chest = null;
        if (block.getState() instanceof Chest) {
            chest = (Chest) block.getState();
        }


        Faction factionAt = (Board.get(e.getClickedBlock().getLocation().getWorld().getName()).getFactionAt(PS.valueOf(e.getClickedBlock().getLocation())));
        if (!factionAt.equals(wilderness) && !factionAt.equals(faction)) {
            player.sendMessage(Message.WAND_CANNOT_USE_HERE.getMessage());
            return;
        }

        e.setCancelled(true);
        if (CraftWand.isCraftWand(eventItem)) {
            CraftWand craftWand = new CraftWand(eventItem, player, chest);
            craftWand.takeWand();
            craftWand.run();
        }

        if (SmeltWand.isSmeltWand(eventItem)) {
            SmeltWand smeltWand = new SmeltWand(eventItem, player, chest);
            smeltWand.takeWand();
            smeltWand.run();
        }

        if (SandWand.isSandWand(eventItem)) {
            SandWand sandWand = new SandWand(eventItem, player, block);
            sandWand.takeWand();
            sandWand.run();
        }
    }

    @EventHandler
    public void onLightning(PlayerInteractAtEntityEvent e) {
        if (e.getRightClicked() == null
                || e.getRightClicked().getType() != EntityType.CREEPER
                || e.getPlayer().getItemInHand() == null) return;

        Creeper creeper = (Creeper) e.getRightClicked();

        if (creeper.isPowered()) {
            e.setCancelled(true);
            return;
        }

        Faction factionAt = (Board.get(e.getRightClicked().getLocation().getWorld().getName()).getFactionAt(PS.valueOf(e.getRightClicked().getLocation())));

        if (Objects.equals(factionAt.getId(), FactionColl.get().getWarzone().getId()) || Objects.equals(factionAt.getId(), FactionColl.get().getSafezone().getId())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(Message.WAND_CANNOT_USE_HERE.getMessage());
            return;
        }
        ItemStack eventItem = e.getPlayer().getItemInHand();
        if (LightningWand.isLightningWand(eventItem)) {
            LightningWand lightningWand = new LightningWand(eventItem, e.getPlayer(), e.getRightClicked());
            lightningWand.takeWand();
            lightningWand.run();
        }
    }
}
