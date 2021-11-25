/*
 *     MT-Wapens
 *     Copyright © 2021 Jazzkuh. All rights reserved.
 *
 *     “Commons Clause” License Condition v1.0
 *
 *    The Software is provided to you by the Licensor under the License, as defined below, subject to the following condition.
 *
 *     Without limiting other conditions in the License, the grant of rights under the License will not include, and the License does not grant to you, the right to Sell the Software.
 *
 *     For purposes of the foregoing, “Sell” means practicing any or all of the  rights granted to you under the License to provide to third parties, for a fee  or other consideration (including without limitation fees for hosting or  consulting/ support services related to the Software), a product or service  whose value derives, entirely or substantially, from the functionality of the  Software. Any license notice or attribution required by the License must also  include this Commons Clause License Condition notice.
 *
 *     Software: MT-Wapens
 *     License: GNU-LGPL v2.1 with Commons Clause
 *     Licensor: [Jazzkuh](https://github.com/Jazzkuh)
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
import com.google.common.base.Enums;
import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.function.enums.Recoil;
import com.jazzkuh.mtwapens.utils.Utils;
import lombok.Getter;
import org.apache.commons.lang.enums.EnumUtils;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

public class Weapon {

    public @Getter String weaponType;
    private final FileConfiguration config = Main.getWeapons().getConfig();

    public Weapon(String weaponType) {
        this.weaponType = weaponType;
    }

    public boolean isUsingAmmo() {
        switch (WeaponTypes.valueOf(this.getParameter(WeaponParameters.TYPE).toString())) {
            case RPG:
            case FLARE:
            case FLAME_THROWER: {
                return false;
            }
            default: {
                return true;
            }
        }
    }

    public Object getParameter(WeaponParameters weaponParameter) {
        String configString = "weapons." + this.weaponType + ".";
        switch (weaponParameter) {
            case NAME: {
                return config.getString(configString + "name");
            }
            case TYPE: {
                return WeaponTypes.valueOf(config.getString(configString + "type") != null
                        ? config.getString(configString + "type") : "SINGLE_BULLET");
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
            case SOUND: {
                return config.getString(configString + "sound") != null
                        ? config.getString(configString + "sound")
                        : "none";
            }
            case RELOADSOUND: {
                return config.getString(configString + "reload-sound") != null
                        ? config.getString(configString + "reload-sound")
                        : "none";
            }
            case DAMAGE: {
                return config.getDouble(configString + "damage") != 0D
                        ? config.getDouble(configString + "damage")
                        : 0D;
            }
            case WEAPON_RANGE: {
                return config.getDouble(configString + "range") != 0
                        ? config.getDouble(configString + "range")
                        : 30D;
            }
            case MAXAMMO: {
                return config.getInt(configString + "max-ammo") != 0
                        ? config.getInt(configString + "max-ammo")
                        : 0;
            }
            case ATTACKSPEED: {
                return config.getDouble(configString + "attackspeed") * 1000;
            }
            case AMMOTYPE: {
                return config.getString(configString + "ammo-type");
            }
            case SCOPE_AMPLIFIER: {
                return config.getInt("weapons." + weaponType + "type-specific.scope.amplifier") != 0
                        ? config.getInt("weapons." + weaponType + "type-specific.scope.amplifier")
                        : 8;
            }
            case SCOPE_LIMITED: {
                return config.getBoolean(configString + "type-specific.scope.limited");
            }
            case DISABLEDURABILITY: {
                return config.getBoolean(configString + "disable-durability");
            }
            case COLOR: {
                return config.getInt(configString + "type-specific.color.rgb-int") != 0
                        ? config.getInt(configString + "type-specific.color.rgb-int")
                        : 16711680;
            }
            case RANGE: {
                return config.getInt(configString + "type-specific.range") != 0
                        ? config.getInt(configString + "type-specific.range")
                        : 7;
            }
            case ITERATIONS: {
                return config.getInt(configString + "type-specific.iterations") != 0
                        ? config.getInt(configString + "type-specific.iterations")
                        : 3;
            }
            case SNEAKINGMODIFIESITEM: {
                return config.getBoolean(configString + "type-specific.sneakingModifiesItem");
            }
            case MODIFIED_MATERIAL: {
                if (!XMaterial.matchXMaterial(config.getString(configString + "type-specific.modifiedItem.material")).isPresent()) {
                    return Material.STICK;
                }

                XMaterial xMaterial = XMaterial.matchXMaterial(config.getString(configString + "type-specific.modifiedItem.material")).get();
                return xMaterial.parseMaterial();
            }
            case MODIFIED_NBT: {
                return config.getString(configString + "type-specific.modifiedItem.nbt");
            }
            case MODIFIED_NBTVALUE: {
                return config.getString(configString + "type-specific.modifiedItem.nbtvalue");
            }
            case MODIFIED_CUSTOMMODELDATA: {
                return config.getInt(configString + "type-specific.modifiedItem.custommodeldata") != 0
                        ? config.getInt(configString + "type-specific.modifiedItem.custommodeldata")
                        : 0;
            }
            case HEADSHOT_DAMAGE: {
                return config.getDouble(configString + "headshot-damage") != 0D
                        ? config.getDouble(configString + "headshot-damage")
                        : 0D;
            }
            case RECOIL: {
                return config.getString(configString + "recoil") != null && Enums.getIfPresent(Recoil.class, config.getString(configString + "recoil").toUpperCase()).isPresent()
                        ? Recoil.valueOf(config.getString(configString + "recoil").toUpperCase())
                        : null;
            }
            case SOULBOUND: {
                return config.getBoolean(configString + "soulbound");
            }
            default:
                break;
        }

        return null;
    }

    public enum WeaponParameters {
        NAME, LORE, TYPE, MATERIAL, NBT, NBTVALUE, SOUND, RELOADSOUND, DAMAGE, MAXAMMO,
        ATTACKSPEED, AMMOTYPE, SCOPE_AMPLIFIER, SCOPE_LIMITED, DISABLEDURABILITY, COLOR,
        RANGE, ITERATIONS, CUSTOMMODELDATA, SNEAKINGMODIFIESITEM, MODIFIED_MATERIAL, MODIFIED_NBT, MODIFIED_NBTVALUE,
        MODIFIED_CUSTOMMODELDATA, WEAPON_RANGE, HEADSHOT_DAMAGE, RECOIL, SOULBOUND
    }

    public enum WeaponTypes {
        SINGLE_BULLET, // Single bullet, default mtwapens.
        MULTIPLE_BULLET, // More mullets, type-specific.iterations?
        SNIPER, // Scope
        FLARE, // Firework rocket?
        FLAME_THROWER, // Shoot flames?
        RPG // Fireball
    }
}
