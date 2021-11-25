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
