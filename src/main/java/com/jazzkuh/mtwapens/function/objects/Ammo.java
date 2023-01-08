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
