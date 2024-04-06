package com.massivecraft.factions.extensions.saber.hooks.impl.factions;

import com.massivecraft.factions.extensions.saber.hooks.impl.FactionHook;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class FactionUUIDHook extends FactionHook {

    @Override
    public boolean canBuild(Block block, Player player) {
        return true;
    }
}