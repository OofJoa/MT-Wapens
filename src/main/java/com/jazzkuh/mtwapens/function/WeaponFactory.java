package com.jazzkuh.mtwapens.function;

import com.cryptomorin.xseries.XMaterial;
import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.function.objects.Ammo;
import com.jazzkuh.mtwapens.function.objects.Weapon;
import com.jazzkuh.mtwapens.utils.ItemBuilder;
import com.jazzkuh.mtwapens.utils.Utils;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class WeaponFactory {

    Player player;

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

        ItemStack itemStack = new ItemBuilder((Material) weapon.getParameter(Weapon.WeaponParameters.MATERIAL))
                .setName(Utils.color(weapon.getParameter(Weapon.WeaponParameters.NAME).toString()))
                .setNBT(weapon.getParameter(Weapon.WeaponParameters.NBT).toString(), weapon.getParameter(Weapon.WeaponParameters.NBTVALUE).toString())
                .setNBT("ammo", (int) weapon.getParameter(Weapon.WeaponParameters.MAXAMMO))
                .setNBT("durability", durability)
                .setNBT("mtwapens_weapon", weapon.getWeaponType())
                .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .setLore(weaponLore)
                .toItemStack();

        this.player.getInventory().addItem(itemStack);
    }

    public void buildAmmo(Ammo ammoType) {
        ItemStack itemStack = new ItemBuilder((Material) ammoType.getParameter(Ammo.AmmoParameters.MATERIAL))
                .setName(ammoType.getParameter(Ammo.AmmoParameters.NAME).toString())
                .setNBT(ammoType.getParameter(Ammo.AmmoParameters.NBT).toString(), ammoType.getParameter(Ammo.AmmoParameters.NBTVALUE).toString())
                .setLore((List<String>) ammoType.getParameter(Ammo.AmmoParameters.LORE))
                .toItemStack();

        player.getInventory().addItem(itemStack);
    }
}
