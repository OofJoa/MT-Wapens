package com.jazzkuh.mtwapens.function.objects;

import com.cryptomorin.xseries.XMaterial;
import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.utils.Utils;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

public class Ammo {

    public @Getter String ammoType;
    private final FileConfiguration config = Main.getAmmo().getConfig();

    public Ammo(String ammoType) {
        this.ammoType = ammoType;
    }

    public Object getParameter(AmmoParameters ammoParameter) {
        String configString = "ammo." + this.ammoType + ".";
        switch (ammoParameter) {
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
            case CUSTOMMODELDATA: {
                return config.getInt(configString + "custommodeldata") != 0
                        ? config.getInt(configString + "custommodeldata")
                        : 0;
            }
            default:
                break;
        }

        return null;
    }

    public enum AmmoParameters {
        NAME, LORE, MATERIAL, NBT, NBTVALUE, CUSTOMMODELDATA
    }
}
