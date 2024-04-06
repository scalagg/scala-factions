package com.massivecraft.factions.extensions.saber.exempt;

import com.massivecraft.factions.extensions.saber.SaberCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class AutoRespawn implements Listener {

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Bukkit.getScheduler().runTaskLater(SaberCore.getFactions(), () -> player.spigot().respawn(), 5L);
    }
}
