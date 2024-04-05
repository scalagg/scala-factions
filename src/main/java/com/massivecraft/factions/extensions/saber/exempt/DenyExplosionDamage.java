package com.massivecraft.factions.extensions.saber.exempt;

import com.massivecraft.factions.extensions.saber.commands.handlers.PlayerData;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DenyExplosionDamage implements Listener {
	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent event){
		if(event.getEntity() instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION || event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)
			event.setCancelled(true);
	}
}
