package com.massivecraft.factions.extensions.saber.commands.handlers.wands.impl;

import itemnbtapi.NBTItem;
import com.massivecraft.factions.extensions.saber.commands.handlers.wands.Wand;
import com.massivecraft.factions.extensions.saber.utils.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

public class LightningWand extends Wand {

    private Entity entity;

    public LightningWand(ItemStack wandItemStack, Player player, Entity entity) {
        this.wandItemStack = wandItemStack;
        this.player = player;
        this.entity = entity;
    }

    public static ItemStack buildItem(int uses) {
        ItemStack itemStack = Wand.buildItem(XMaterial.matchXMaterial(SaberUtils.config.getString("Lightning-Wand.Item.Type")).parseMaterial());
        NBTItem nbtItem = new NBTItem(itemStack);
        nbtItem.setBoolean("Lightning", true);
        nbtItem.setInteger("Uses", uses);
        itemStack = nbtItem.getItem();
        if(SaberUtils.config.getBoolean("Lightning-Wand.Item.Glow")){
            itemStack.addUnsafeEnchantment(Glow.getGlow(), 1);
        }
        return new ItemBuilder(itemStack)
                .name(SaberUtils.color(SaberUtils.config.getString("Lightning-Wand.Item.Display-Name")))
                .lore(SaberUtils.colorWithPlaceholders(SaberUtils.config.getStringList("Lightning-Wand.Item.Lore")
                        , new Placeholder("{uses}", uses + "")))
                .build();
    }

    public static boolean isLightningWand(ItemStack itemStack) {
        if (!Wand.isWand(itemStack)) {
            return false;
        }
        NBTItem nbtItem = new NBTItem(itemStack);
        return nbtItem.hasKey("Lightning");
    }

    public void run() {
        if(entity.getType() != EntityType.CREEPER) {
            player.sendMessage(Message.WAND_CANNOT_USE_HERE.getMessage());
            return;
        }

        wandUsed = true;
        chargeCreeper((Creeper) entity);
        updateWand();
    }

    private void chargeCreeper(Creeper creeper) {
        creeper.setPowered(true);
    }
}
