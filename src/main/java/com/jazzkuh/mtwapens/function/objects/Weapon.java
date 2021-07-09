package com.jazzkuh.mtwapens.function.objects;

import com.cryptomorin.xseries.XMaterial;
import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.utils.ItemBuilder;
import com.jazzkuh.mtwapens.utils.Utils;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Weapon {

    public @Getter String weaponType;
    private final FileConfiguration config = Main.getWeapons().getConfig();

    public Weapon(String weaponType) {
        this.weaponType = weaponType;
    }

    public Object getParameter(WeaponParameters weaponParameter) {
        String configString = "weapons." + this.weaponType + ".";
        switch (weaponParameter) {
            case NAME: {
                return config.getString(configString + "name");
            }
            case LORE: {
                return Utils.color(config.getStringList(configString + "lore"));
            }
            case MATERIAL: {
                if (!XMaterial.matchXMaterial(config.getString(configString + "material")).isPresent()) {
                    return Material.STICK;
                }

                XMaterial xMaterial = XMaterial.matchXMaterial(config.getString(configString + "material")).get();
                return xMaterial.parseMaterial();
            }
            case NBT: {
                return config.getString(configString + "nbt");
            }
            case NBTVALUE: {
                return config.getString(configString + "nbtvalue");
            }
            case SOUND: {
                return config.getString(configString + "sound");
            }
            case RELOADSOUND: {
                return config.getString(configString + "reload-sound");
            }
            case DAMAGE: {
                return config.getDouble(configString + "damage");
            }
            case MAXAMMO: {
                return config.getInt(configString + "max-ammo");
            }
            case ATTACKSPEED: {
                return config.getDouble(configString + "attackspeed") * 1000;
            }
            case AMMOTYPE: {
                return config.getString(configString + "ammo-type");
            }
            case SCOPE: {
                return config.getBoolean(configString + "scope");
            }
            case SCOPESIZE: {
                int scopeSize = config.getInt("weapons." + weaponType + ".scope-size");
                return scopeSize != 0 ? scopeSize : 8;
            }
            case SHOOTONLYWHENSCOPED: {
                return config.getBoolean(configString + "canOnlyShootWhenScoped");
            }
            case DISABLEDURABILITY: {
                return config.getBoolean(configString + "disable-durability");
            }
            case AMMOITEM: {
                Ammo ammoType = new Ammo(this.getParameter(Weapon.WeaponParameters.AMMOTYPE).toString());

                ItemStack bulletItem = new ItemBuilder((Material) ammoType.getParameter(Ammo.AmmoParameters.MATERIAL))
                        .setName(ammoType.getParameter(Ammo.AmmoParameters.NAME).toString())
                        .setNBT(ammoType.getParameter(Ammo.AmmoParameters.NBT).toString(), ammoType.getParameter(Ammo.AmmoParameters.NBTVALUE).toString())
                        .setLore((List<String>) ammoType.getParameter(Ammo.AmmoParameters.LORE))
                        .toItemStack();

                return bulletItem;
            }
            default:
                break;
        }

        return null;
    }

    public enum WeaponParameters {
        NAME, LORE, MATERIAL, NBT, NBTVALUE, SOUND, RELOADSOUND, DAMAGE, MAXAMMO,
        ATTACKSPEED, AMMOTYPE, SCOPE, SCOPESIZE, SHOOTONLYWHENSCOPED, DISABLEDURABILITY, AMMOITEM
    }
}
