package com.massivecraft.factions.extensions.saber.commands.handlers;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.extensions.saber.hooks.HookManager;
import com.massivecraft.factions.extensions.saber.hooks.impl.FactionHook;
import com.massivecraft.factions.extensions.saber.hooks.impl.WorldGuardHook;
import com.massivecraft.factions.extensions.saber.utils.Message;
import com.massivecraft.factions.extensions.saber.utils.SaberUtils;
import com.massivecraft.factions.extensions.saber.utils.XMaterial;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.HashMap;
import java.util.HashSet;

import static com.massivecraft.factions.extensions.saber.utils.SaberUtils.color;

public class ChunkbusterListener implements Listener {
    public static HashMap<Chunk, Location> beingBusted = new HashMap<>();

    private HashSet<Chunk> waterChunks = new HashSet<>();

    @EventHandler
    @SuppressWarnings("deprecation")
    public void onBusterPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        Block block = e.getBlock();

        if (e.getItemInHand().getType().equals(XMaterial.END_PORTAL_FRAME.parseMaterial())) {
            if (e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(color(SaberUtils.config.getString("Chunkbuster.Item.DisplayName")))) {
                Block b = e.getBlockPlaced();

                if (HookManager.getPluginMap().get("WorldGuard") != null) {
                    WorldGuardHook wgHook = ((WorldGuardHook) HookManager.getPluginMap().get("WorldGuard"));
                    if (!wgHook.canBuild(player, block)) {
                        e.getPlayer().sendMessage(Message.CHUNKBUSTER_CANT_PLACE.getMessage());
                        return;
                    }
                }

                FactionHook facHook = (FactionHook) HookManager.getPluginMap().get("Factions");
                if (!facHook.canBuild(block, player)) {
                    e.setCancelled(true);
                    return;
                }


                if (beingBusted.containsKey(block.getLocation().getChunk())) {
                    e.setCancelled(true);
                    e.getPlayer().sendMessage(Message.CHUNKBUSTER_ALREADY_BEING_BUSTED.getMessage());
                    return;
                }

                Entity[] entities = e.getBlock().getChunk().getEntities();
                for (int i = 0; i <= entities.length - 1; i++) {
                    if (entities[i] instanceof HumanEntity) {
                        entities[i].sendMessage(Message.CHUNKBUSTER_USE_MESSAGE.getMessage());
                    }
                }

                World world = e.getBlock().getWorld();
                int bx = b.getChunk().getX() << 4;
                int bz = b.getChunk().getZ() << 4;
                Bukkit.getScheduler().scheduleSyncDelayedTask(Factions.get(), () -> {
                    beingBusted.put(block.getLocation().getChunk(), block.getLocation());
                    waterChunks.add(b.getChunk());
                    for (int xx = bx; xx < bx + 16; xx++) {
                        for (int zz = bz; zz < bz + 16; zz++) {
                            for (int yy = e.getBlockPlaced().getY(); yy >= 0; yy--) {
                                Block block1 = world.getBlockAt(xx, yy, zz);
                                if (block1.getType().equals(Material.AIR) || block1.getType().equals(XMaterial.SPAWNER.parseMaterial()) || block1.getType().equals(XMaterial.CHEST.parseMaterial()) || block1.getType().equals(XMaterial.END_PORTAL_FRAME.parseMaterial()) || block1.getType().equals(XMaterial.TRAPPED_CHEST.parseMaterial())) {
                                    continue;
                                }
                                if (!block1.getType().equals(Material.BEDROCK)) {
                                    block1.setType(XMaterial.GLASS.parseMaterial());
                                }
                            }
                        }
                    }
                }, 0);
                if (Bukkit.getPluginManager().getPlugin("HolographicDisplays") != null) {
                    if (SaberUtils.config.getBoolean("Chunkbuster.Hologram.Enabled")) {
                        Hologram hologram = HologramsAPI.createHologram(Factions.get(), b.getLocation().add(0.5, 1.5, 0.5));
                        hologram.appendTextLine(color(SaberUtils.config.getString("Chunkbuster.Hologram.Format")));
                        Bukkit.getScheduler().runTaskTimer(Factions.get(), new Runnable() {
                            int timer = SaberUtils.config.getInt("Chunkbuster.Warmup");

                            @Override
                            public void run() {
                                if (timer == 0) {
                                    hologram.delete();
                                    return;
                                }
                                if (hologram.size() == 2) {
                                    hologram.getLine(1).removeLine();
                                }
                                hologram.appendTextLine((ChatColor.DARK_RED + "" + timer));
                                timer--;
                            }
                        }, 0L, 20L);
                    }
                }
                if (SaberUtils.config.getBoolean("Chunkbuster.Async-Mode")) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Factions.get(), () -> {
                        int multiplier = 0;
                        for (int yy = e.getBlockPlaced().getY(); yy >= 0; yy--) {
                            multiplier++;
                            int dy = yy;
                            Bukkit.getScheduler().scheduleSyncDelayedTask(Factions.get(), () -> {
                                for (int zz = bz; zz < bz + 16; zz++) {
                                    for (int xx = bx; xx < bx + 16; xx++) {
                                        Block block12 = world.getBlockAt(xx, dy, zz);
                                        if (block12.getType().equals(Material.AIR) || block12.getType().equals(XMaterial.SPAWNER.parseMaterial()) || block12.getType().equals(XMaterial.CHEST.parseMaterial()) || block12.getType().equals(XMaterial.TRAPPED_CHEST.parseMaterial())) {
                                            continue;
                                        }
                                        if (!block12.getType().equals(Material.BEDROCK)) {
                                            block12.setType(Material.AIR);
                                        }
                                    }
                                    beingBusted.remove(e.getBlock().getChunk());
                                }
                            }, 20L * multiplier);

                        }
                    }, SaberUtils.config.getInt("Chunkbuster.Warmup") * 20L);
                } else {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Factions.get(), () -> {
                        for (int xx = bx; xx < bx + 16; xx++) {
                            for (int zz = bz; zz < bz + 16; zz++) {
                                for (int yy = 0; yy < 255; yy++) {
                                    Block block13 = world.getBlockAt(xx, yy, zz);
                                    if (block13.getType().equals(Material.AIR) || block13.getType().equals(XMaterial.SPAWNER.parseMaterial()) || block13.getType().equals(XMaterial.CHEST.parseMaterial()) || block13.getType().equals(XMaterial.TRAPPED_CHEST.parseMaterial())) {
                                        continue;
                                    }
                                    if (!block13.getType().equals(Material.BEDROCK)) {
                                        block13.setType(Material.AIR);
                                    }
                                }
                                beingBusted.remove(e.getBlock().getChunk());
                            }
                        }
                    }, SaberUtils.config.getInt("Chunkbuster.Warmup") * 20L);
                }
            }
        }
    }

    public HashSet<Chunk> getWaterChunks() {
        return waterChunks;
    }

    @EventHandler
    public void onWaterFlow(BlockFromToEvent e) {
        if (Bukkit.getVersion().contains("1.13")) {
            if (e.getBlock().getType().equals(Material.WATER) || e.getBlock().getType().equals(Material.LAVA)) {
                if (!getWaterChunks().contains(e.getBlock().getChunk()) && getWaterChunks().contains(e.getToBlock().getChunk())) {
                    e.setCancelled(true);
                }
            }
        } else {
            if (e.getBlock().getType().equals(Material.WATER) || e.getBlock().getType().equals(Material.valueOf("STATIONARY_WATER"))
                    || e.getBlock().getType().equals(Material.LAVA) || e.getBlock().getType().equals(Material.valueOf("STATIONARY_LAVA"))) {
                if (!getWaterChunks().contains(e.getBlock().getChunk()) && getWaterChunks().contains(e.getToBlock().getChunk())) {
                    e.setCancelled(true);
                }
            }
        }
    }
}
