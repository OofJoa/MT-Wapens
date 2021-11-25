/*
 *     MT-Wapens
 *     Copyright © 2021 Jazzkuh. All rights reserved.
 *
 *     This library is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU Lesser General Public
 *     License as published by the Free Software Foundation; either
 *     version 2.1 of the License, or (at your option) any later version.
 *
 *     This library is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *     Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public
 *     License along with this library; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 *     USA
 */

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
            case EFFECTS: {
                Collection<PotionEffect> potionEffects = new ArrayList<>();
                if (config.getConfigurationSection(configString + "type-specific.effects") != null) {
                    for (String potionEffect : config.getConfigurationSection(configString + "type-specific.effects").getKeys(false)) {
                        int duration = config.getInt(configString + "type-specific.effects." + potionEffect + ".duration") != 0
                                ? config.getInt(configString + "type-specific.effects." + potionEffect + ".duration")
                                : 10;
                        int amplifier = config.getInt(configString + "type-specific.effects." + potionEffect + ".amplifier") != 0
                                ? config.getInt(configString + "type-specific.effects." + potionEffect + ".amplifier")
                                : 1;

                        duration = duration * 20;

                        if (XPotion.matchXPotion(potionEffect).isPresent()) {
                            potionEffects.add(XPotion.matchXPotion(potionEffect).get().parsePotion(duration, amplifier));
                        }
                    }
                }

                return potionEffects;
            }
            case ITERATIONS: {
                return config.getInt(configString + "type-specific.iterations") != 0
                        ? config.getInt(configString + "type-specific.iterations")
                        : 5;
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

    public enum GrenadeParameters {
        NAME, TYPE, LORE, MATERIAL, NBT, NBTVALUE, RANGE, RANGED_DAMAGE, COOLDOWN, EFFECTS, ITERATIONS, CUSTOMMODELDATA
    }

    public enum GrenadeTypes {
        EXPLODE, MOLOTOV, EFFECT, SMOKE
    }
}
