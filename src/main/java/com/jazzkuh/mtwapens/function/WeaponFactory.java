package com.jazzkuh.mtwapens.function;

import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.function.objects.Ammo;
import com.jazzkuh.mtwapens.function.objects.Grenade;
import com.jazzkuh.mtwapens.function.objects.Melee;
import com.jazzkuh.mtwapens.function.objects.Weapon;
import com.jazzkuh.mtwapens.utils.ItemBuilder;
import com.jazzkuh.mtwapens.utils.Utils;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class WeaponFactory {

    public Player player;
    public @Getter ItemStack itemStack;

    public WeaponFactory(Player player) {
        this.player = player;
    }
    
    public void buildWeapon(Weapon weapon, int durability) {
        ArrayList<String> weaponLore = new ArrayList<>();
        for (String string : (List<String>) weapon.getParameter(Weapon.WeaponParameters.LORE)) {
            string = string.replace("<Ammo>", weapon.getParameter(Weapon.WeaponParameters.MAXAMMO).toString())
                    .replace("<MaxAmmo>", weapon.getParameter(Weapon.WeaponParameters.MAXAMMO).toString())
                    .replace("<Damage>", weapon.getParameter(Weapon.WeaponParameters.DAMAGE).toString())
                    .replace("<Durability>", String.valueOf(durability));
            weaponLore.add(string);
        }

        ItemBuilder itemBuilder = new ItemBuilder((Material) weapon.getParameter(Weapon.WeaponParameters.MATERIAL))
                .setName(Utils.color(weapon.getParameter(Weapon.WeaponParameters.NAME).toString()))
                .setNBT(weapon.getParameter(Weapon.WeaponParameters.NBT).toString(), weapon.getParameter(Weapon.WeaponParameters.NBTVALUE).toString())
                .setNBT("ammo", (int) weapon.getParameter(Weapon.WeaponParameters.MAXAMMO))
                .setNBT("durability", durability)
                .setNBT("mtwapens_weapon", weapon.getWeaponType())
                .makeUnbreakable(true)
                .setLore(weaponLore);

        int customModelData = (int) weapon.getParameter(Weapon.WeaponParameters.CUSTOMMODELDATA);
        if (customModelData > 0) {
            try {
                itemBuilder.setCustomModelData(customModelData);
            } catch (Exception e) {
                Main.getInstance().getLogger().warning("Custom Model Data is not supported for your server version.");
            }
        }

        this.itemStack = itemBuilder.toItemStack();
        double attackspeed = -1.27D * (((double) weapon.getParameter(Weapon.WeaponParameters.ATTACKSPEED) / 1000) * 2D);
        Utils.applyAttackSpeed(itemStack, Math.max(Math.min(attackspeed, -2.87D), -3.87D));
    }

    public void buildAmmo(Ammo ammoType) {
        ItemBuilder itemBuilder = new ItemBuilder((Material) ammoType.getParameter(Ammo.AmmoParameters.MATERIAL))
                .setName(Utils.color(ammoType.getParameter(Ammo.AmmoParameters.NAME).toString()))
                .setNBT(ammoType.getParameter(Ammo.AmmoParameters.NBT).toString(), ammoType.getParameter(Ammo.AmmoParameters.NBTVALUE).toString())
                .setLore((List<String>) ammoType.getParameter(Ammo.AmmoParameters.LORE));

        int customModelData = (int) ammoType.getParameter(Ammo.AmmoParameters.CUSTOMMODELDATA);
        if (customModelData > 0) {
            try {
                itemBuilder.setCustomModelData(customModelData);
            } catch (Exception e) {
                Main.getInstance().getLogger().warning("Custom Model Data is not supported for your server version.");
            }
        }

        this.itemStack = itemBuilder.toItemStack();
    }

    public void buildGrenade(Grenade grenade, int durability) {
        ArrayList<String> weaponLore = new ArrayList<>();
        for (String string : (List<String>) grenade.getParameter(Grenade.GrenadeParameters.LORE)) {
            string = string.replace("<Ranged-Damage>", grenade.getParameter(Grenade.GrenadeParameters.RANGED_DAMAGE).toString())
                    .replace("<Durability>", String.valueOf(durability));
            weaponLore.add(string);
        }

        ItemBuilder itemBuilder = new ItemBuilder((Material) grenade.getParameter(Grenade.GrenadeParameters.MATERIAL))
                .setName(Utils.color(grenade.getParameter(Grenade.GrenadeParameters.NAME).toString()))
                .setNBT(grenade.getParameter(Grenade.GrenadeParameters.NBT).toString(), grenade.getParameter(Grenade.GrenadeParameters.NBTVALUE).toString())
                .setNBT("durability", durability)
                .setNBT("mtwapens_grenade", grenade.getGrenadeType())
                .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .makeUnbreakable(true)
                .setLore(weaponLore);

        int customModelData = (int) grenade.getParameter(Grenade.GrenadeParameters.CUSTOMMODELDATA);
        if (customModelData > 0) {
            try {
                itemBuilder.setCustomModelData(customModelData);
            } catch (Exception e) {
                Main.getInstance().getLogger().warning("Custom Model Data is not supported for your server version.");
            }
        }

        this.itemStack = itemBuilder.toItemStack();
    }

    public void buildMelee(Melee melee, int durability) {
        ArrayList<String> weaponLore = new ArrayList<>();
        for (String string : (List<String>) melee.getParameter(Melee.MeleeParameters.LORE)) {
            string = string
                    .replace("<Durability>", String.valueOf(durability));
            weaponLore.add(string);
        }

        ItemBuilder itemBuilder = new ItemBuilder((Material) melee.getParameter(Melee.MeleeParameters.MATERIAL))
                .setName(Utils.color(melee.getParameter(Melee.MeleeParameters.NAME).toString()))
                .setNBT(melee.getParameter(Melee.MeleeParameters.NBT).toString(), melee.getParameter(Melee.MeleeParameters.NBTVALUE).toString())
                .setNBT("durability", durability)
                .setNBT("mtwapens_melee", melee.getMeleeType())
                .makeUnbreakable(true)
                .setLore(weaponLore);

        int customModelData = (int) melee.getParameter(Melee.MeleeParameters.CUSTOMMODELDATA);
        if (customModelData > 0) {
            try {
                itemBuilder.setCustomModelData(customModelData);
            } catch (Exception e) {
                Main.getInstance().getLogger().warning("Custom Model Data is not supported for your server version.");
            }
        }

        this.itemStack = itemBuilder.toItemStack();
        Utils.applyAttackSpeed(itemStack, -3.0D);
    }

    public void addToInventory() {
        this.player.getInventory().addItem(itemStack);
    }
}
