package com.massivecraft.factions.extensions.saber;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.extensions.saber.commands.command.*;
import com.massivecraft.factions.extensions.saber.commands.handlers.*;
import com.massivecraft.factions.extensions.saber.config.Persist;
import com.massivecraft.factions.extensions.saber.exempt.*;
import com.massivecraft.factions.extensions.saber.exempt.mobs.*;
import com.massivecraft.factions.extensions.saber.file.CustomFile;
import com.massivecraft.factions.extensions.saber.file.impl.MessageFile;
import com.massivecraft.factions.extensions.saber.hooks.HookManager;
import com.massivecraft.factions.extensions.saber.hooks.PluginHook;
import com.massivecraft.factions.extensions.saber.hooks.impl.FactionHook;
import com.massivecraft.factions.extensions.saber.hooks.impl.WorldGuardHook;
import com.massivecraft.factions.extensions.saber.listeners.InstaBreakSponge;
import com.massivecraft.factions.extensions.saber.tasks.RoofCannonTask;
import com.massivecraft.factions.extensions.saber.utils.SaberUtils;
import com.massivecraft.massivecore.engine.EngineMassiveCoreCommandRegistration;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class SaberCore {

    public static SaberCore instance;
    private static Logger logger;
    private Persist persist;

    public List<String> itemList = getConfig().getStringList("DeniedItemStorage.Items");
    private RoofCannonTask roofCannonTask;


    public static void log(String message) {
        logger.log(Level.INFO, message);
    }


    public static void log(Level level, String message) {
        logger.log(level, message);
    }

    /**
     * Could have just made it so that the default instace
     * was Factions but we have other modules using this call so we will just have 2
     * and hardcode all of the other functions :P
     */
    public static SaberCore getInstance() {
        return instance;
    }
    public static Factions getFactions() { return Factions.get(); }
    public static FileConfiguration getConfig() { return Factions.get().getConfig(); }
    public static Server getServer() { return Factions.get().getServer(); }
    public static File getDataFolder() { return Factions.get().getDataFolder(); }

    public void onEnable() {
        instance = this;
        logger = Factions.get().getLogger();
        registerListeners();
        registerBooleans();
        persist = new Persist();
        Collections.singletonList(new MessageFile()).forEach(CustomFile::init);
        registerCommands();
        SaberUtils.register();
        List<PluginHook<?>> hooks = new ArrayList<>();
        hooks.add(new FactionHook());
        if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null) {
            hooks.add(new WorldGuardHook());
        }
        new HookManager(hooks);

    }

    public void onDisable() {
        instance = null;
        cleanBeforeStop();
    }

    // - Makes sure all tasks are stopped before the plugin stops

    private void cleanBeforeStop() {
        if (roofCannonTask != null) {
            try {
                roofCannonTask.cancel();
            } catch (final IllegalStateException ex) {
            }
            roofCannonTask = null;
        }
    }

    private void registerListeners() {
        SaberUtils.getClassesInPackage(getFactions(), "com.massivecraft.factions.extensions.saber.listeners").stream().filter(Listener.class::isAssignableFrom).forEach(clazz -> {
            try {
                Bukkit.getPluginManager().registerEvents((Listener) clazz.newInstance(), getFactions());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    private void registerCommands() {
        getFactions().activate(
                CmdAnvil.class,
                CmdChunkbuster.class
        );

        EngineMassiveCoreCommandRegistration.updateRegistrations();
    }


    private void registerBooleans() {
        getServer().getPluginManager().registerEvents(new WandHandler(), getFactions());
        getServer().getPluginManager().registerEvents(new CmdStackPotions(), getFactions());

        if(getConfig().getBoolean("denyIronGolemsTargetZombies")){
            getServer().getPluginManager().registerEvents(new IronGolemAI(), getFactions());
        }

        if(getConfig().getBoolean("Shockwave.Enabled")){
            getServer().getPluginManager().registerEvents(new ShockwaveListener(), getFactions());
            //getFactions().getCommand("shockwave").setExecutor(new CmdShockwave());
        }

        if(getConfig().getBoolean("useOreTracker")){
            getServer().getPluginManager().registerEvents(new OreTracker(), getFactions());
        }
        if(getConfig().getBoolean("instaSpongeBreak")){
            getServer().getPluginManager().registerEvents(new InstaBreakSponge(), getFactions());
        }
        if(getConfig().getBoolean("denyPistonFarming")){
            getServer().getPluginManager().registerEvents(new PistonGlitching(), getFactions());
        }
        if(getConfig().getBoolean("denyBlazeWaterDamage")){
            getServer().getPluginManager().registerEvents(new WaterProofBlazes(), getFactions());
        }
        if(getConfig().getBoolean("denyNaturalMobSpawning")){
            getServer().getPluginManager().registerEvents(new NaturalMobSpawning(), getFactions());
        }
        if(getConfig().getBoolean("HarvesterHoe.Enabled")){
            getServer().getPluginManager().registerEvents(new HarvesterHoeListener(), getFactions());
            //getFactions().getCommand("harvesterhoe").setExecutor(new CmdHarvesterHoe());
        }
        if(getConfig().getBoolean("Chunkbuster.Enabled")){
            getServer().getPluginManager().registerEvents(new ChunkbusterListener(), getFactions());
            //getFactions().getCommand("chunkbuster").setExecutor(new CmdChunkbuster());
        }
        if(getConfig().getBoolean("cancelDragonEggTeleport")){
            getServer().getPluginManager().registerEvents(new DragonEggAntiTP(), getFactions());
        }
        if(getConfig().getBoolean("useAntiDestroySystem")){
            getServer().getPluginManager().registerEvents(new AntiDestroy(), getFactions());
        }
        if(getConfig().getBoolean("useAutoRespawn")){
            getServer().getPluginManager().registerEvents(new AutoRespawn(), getFactions());
        }
        if (getConfig().getBoolean("useAntiCobbleMonster")) {
            getServer().getPluginManager().registerEvents(new AntiCobbleMonster(), getFactions());
        }
        if (getConfig().getBoolean("useAntiWildernessSpawner")) {
            getServer().getPluginManager().registerEvents(new AntiWildernessSpawner(), getFactions());
        }
        if (getConfig().getBoolean("DisabledCommands.Enabled")) {
            getServer().getPluginManager().registerEvents(new DisabledCommands(), getFactions());
        }
        if (getConfig().getBoolean("useAntiBoatPlacement")) {
            getServer().getPluginManager().registerEvents(new BoatListener(), getFactions());
        }
        if (getConfig().getBoolean("SpawnerSponge.Enabled")) {
            getServer().getPluginManager().registerEvents(new SpawnerSponge(), getFactions());
            getServer().getPluginManager().registerEvents(new SoakSponge(), getFactions());
        }
        if (getConfig().getBoolean("useBookDisenchant")) {
            getServer().getPluginManager().registerEvents(new BookDisenchant(), getFactions());
        }
        if(getConfig().getBoolean("AntiSpawnerMine.Enabled")){
            getServer().getPluginManager().registerEvents(new SpawnerMine(), getFactions());
        }
        if(getConfig().getBoolean("StatTrack-Swords.Enabled")){
            getServer().getPluginManager().registerEvents(new StatTrackSwords(), getFactions());
        }
        if(getConfig().getBoolean("useAntiMobTargeting")){
            getServer().getPluginManager().registerEvents(new AntiMobTargeting(), getFactions());
        }
        if(getConfig().getBoolean("useAntiMobAI")){
            getServer().getPluginManager().registerEvents(new AntiMobMoving(), getFactions());
        }
        if(getConfig().getBoolean("denyExplosionDamage")){
            getServer().getPluginManager().registerEvents(new DenyExplosionDamage(), getFactions());
        }
        if(getConfig().getBoolean("reduceIronGolemHealth")){
            getServer().getPluginManager().registerEvents(new IronGolemHealth(), getFactions());
        }
        if(getConfig().getBoolean("useAntiZombieBaby")){
            getServer().getPluginManager().registerEvents(new AntiBabyZombie(), getFactions());
        }

        if(getConfig().getBoolean("useAntiRoofCannon")) {
            getServer().getPluginManager().registerEvents(new RoofCannonTask(), getFactions());

            roofCannonTask = new RoofCannonTask();
            roofCannonTask.runTaskTimer(getFactions(),20L,20L);
        }
    }

    public Persist getPersist() {
        return persist;
    }
}
