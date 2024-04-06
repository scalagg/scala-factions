package com.massivecraft.factions.extensions.saber.exempt;

import com.massivecraft.factions.entity.Board;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SpawnerSpawnEvent;

public class AntiWildernessSpawner implements Listener {

    @EventHandler
    public void onSpawner(SpawnerSpawnEvent e) {
        PS floc = PS.valueOf(e.getSpawner().getLocation());

        if (floc == null) {
            return;
        }

        Faction faction = Board.get(e.getLocation().getWorld().getName()).getFactionAt(floc);

        if (faction == null) {
            return;
        }
        if (faction.isNone()) {
            e.setCancelled(true);
        }
    }
}
