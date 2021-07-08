package com.jazzkuh.mtwapens.function;

import com.cryptomorin.xseries.XMaterial;
import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.utils.ItemBuilder;
import com.jazzkuh.mtwapens.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class WeaponFactory {

    Player player;

    public WeaponFactory(Player player) {
        this.player = player;
    }
    
    public void buildWeapon(String type, int durability) {
        type = type.toLowerCase();

        ArrayList<String> lore = new ArrayList<>();
        for (String lorePart : Main.getInstance().getConfig().getStringList("weapons." + type + ".lore")) {
            lorePart = Utils.color(lorePart)
                    .replace("<Ammo>", String.valueOf(Main.getInstance().getConfig().getInt("weapons." + type + ".max-ammo")))
                    .replace("<MaxAmmo>", String.valueOf(Main.getInstance().getConfig().getInt("weapons." + type + ".max-ammo")))
                    .replace("<Damage>", String.valueOf(Main.getInstance().getConfig().getDouble("weapons." + type + ".damage")))
                    .replace("<Durability>", String.valueOf(durability));
            lore.add(lorePart);
        }

        ItemStack itemStack = new ItemBuilder(XMaterial.matchXMaterial(Main.getInstance().getConfig().getString("weapons." + type + ".material")).get().parseMaterial())
                .setName(Utils.color(Main.getInstance().getConfig().getString("weapons." + type + ".name")))
                .setNBT(Main.getInstance().getConfig().getString("weapons." + type + ".nbt"), Main.getInstance().getConfig().getString("weapons." + type + ".nbtvalue"))
                .setNBT("ammo", Main.getInstance().getConfig().getInt("weapons." + type + ".max-ammo"))
                .setNBT("durability", durability)
                .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .setLore(lore)
                .toItemStack();

        this.player.getInventory().addItem(itemStack);
    }

    public void buildAmmo(String type) {
        type = type.toLowerCase();

        ArrayList<String> lore = new ArrayList<>();
        for (String lorePart : Main.getInstance().getConfig().getStringList("ammo." + type + ".lore")) {
            lore.add(Utils.color(lorePart));
        }

        ItemStack itemStack = new ItemBuilder(XMaterial.matchXMaterial(Main.getInstance().getConfig().getString("ammo." + type + ".material")).get().parseMaterial())
                .setName(Utils.color( Main.getInstance().getConfig().getString("ammo." + type + ".name")))
                .setNBT(Main.getInstance().getConfig().getString("ammo." + type + ".nbt"),  Main.getInstance().getConfig().getString("ammo." + type + ".nbtvalue"))
                .setLore(lore)
                .toItemStack();

        player.getInventory().addItem(itemStack);
    }
}
