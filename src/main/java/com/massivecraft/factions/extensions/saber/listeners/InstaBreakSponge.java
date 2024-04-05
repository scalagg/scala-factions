package com.massivecraft.factions.extensions.saber.listeners;

import com.massivecraft.factions.*;
import com.massivecraft.factions.entity.Board;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class InstaBreakSponge implements Listener {


    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }
        Block block = event.getClickedBlock();
        if (block.getType() == Material.SPONGE) {
            MPlayer fPlayer = MPlayer.get(player.getUniqueId());
            Faction faction = fPlayer.getFaction();
            Faction location = Board.get(event.getClickedBlock().getWorld().getName()).getFactionAt(PS.valueOf(block.getLocation()));

            if (faction.getId().equals(location.getId()) || (location.isNone())) {
                block.breakNaturally();
                event.setCancelled(true);
            }
        }
    }
}
