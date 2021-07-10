package com.jazzkuh.mtwapens.function.objects;

import com.cryptomorin.xseries.XMaterial;
import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.utils.Utils;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

public class Grenade {

    public @Getter String grenadeType;
    private final FileConfiguration config = Main.getGrenades().getConfig();

    public Grenade(String grenadeType) {
        this.grenadeType = grenadeType;
    }

    public Object getParameter(GrenadeParameters grenadeParameter) {
        String configString = "grenades." + this.grenadeType + ".";
        switch (grenadeParameter) {
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
            case RANGE: {
                double range = config.getDouble(configString + "range") != 0D
                        ? config.getDouble(configString + "range")
                        : 3D;

                return Math.min(range, 16D);
            }
            case RANGED_DAMAGE: {
                return config.getDouble(configString + "ranged-damage");
            }
            case COOLDOWN: {
                return config.getDouble(configString + "cooldown") * 1000;
            }
            default:
                break;
        }

        return null;
    }

    public enum GrenadeParameters {
        NAME, TYPE, LORE, MATERIAL, NBT, NBTVALUE, RANGE, RANGED_DAMAGE, COOLDOWN
    }

    public enum GrenadeTypes {
        EXPLODE, MOLOTOV
    }
}
