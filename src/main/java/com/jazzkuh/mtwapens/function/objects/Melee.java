package com.jazzkuh.mtwapens.function.objects;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XPotion;
import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.utils.Utils;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Collection;

public class Melee {

    public @Getter String meleeType;
    private final FileConfiguration config = Main.getMelee().getConfig();

    public Melee(String meleeType) {
        this.meleeType = meleeType;
    }

    public Object getParameter(MeleeParameters meleeParameter) {
        String configString = "melee." + this.meleeType + ".";
        switch (meleeParameter) {
            case NAME: {
                return config.getString(configString + "name");
            }
            case TYPE: {
                return GrenadeTypes.valueOf(config.getString(configString + "type"));
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
            case CUSTOMMODELDATA: {
                return config.getInt(configString + "custommodeldata") != 0
                        ? config.getInt(configString + "custommodeldata")
                        : 0;
            }
            case DAMAGE: {
                return config.getDouble(configString + "damage") != 0D
                        ? config.getDouble(configString + "damage")
                        : 0D;
            }
            case DISABLEDURABILITY: {
                return config.getBoolean(configString + "disable-durability");
            }
            default:
                break;
        }

        return null;
    }

    public enum MeleeParameters {
        NAME, TYPE, LORE, MATERIAL, NBT, NBTVALUE, CUSTOMMODELDATA, DAMAGE, DISABLEDURABILITY
    }

    public enum GrenadeTypes {
        STAB
    }
}
