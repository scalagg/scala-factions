package com.massivecraft.factions.extensions.saber.exempt;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.extensions.saber.utils.Message;
import com.massivecraft.factions.extensions.saber.utils.SaberUtils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;

public class SpawnerMine implements Listener {

    @EventHandler
    public void onSpawnerMine(BlockBreakEvent e) {
        if(!e.getBlock().getType().equals(Material.MOB_SPAWNER)) return;

        if (nearbyEnemies(e.getPlayer(), SaberUtils.config.getDouble("AntiSpawnerMine.Radius"), null) && !e.getPlayer().hasPermission("sabercore.spawnermine,bypass")) {
            e.setCancelled(true);
        }
        if(e.isCancelled()){
            e.getPlayer().sendMessage(Message.ANTI_SPAWNER_MINE_PLAYERS_NEAR.getMessage());
        }
    }

    public boolean nearbyEnemies(Player player, double d, String str) {
        List<Entity> nearbyEntities = player.getNearbyEntities(d, d, d);
        MPlayer byPlayer = MPlayer.get(player.getUniqueId());
        for (Entity entity : nearbyEntities) {
            if ((entity instanceof Player) && (str == null || entity.hasPermission(str))) {
                MPlayer byPlayer2 = MPlayer.get(entity.getUniqueId());
                if (!(byPlayer2.getFactionId().equals(byPlayer.getFactionId()) || byPlayer2.getFaction().getRelationWish(byPlayer.getFaction()) == Rel.ALLY)) {
                    return true;
                }
            }
        }
        return false;
    }
}
