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

package com.jazzkuh.mtwapens.compatibility;

import com.jazzkuh.mtwapens.compatibility.versions.*;
import lombok.Getter;
import org.bukkit.Bukkit;

public class CompatibilityManager {

    private final String bukkitVersion = Bukkit.getServer().getClass().getPackage().getName();
    private final @Getter String version = bukkitVersion.substring(bukkitVersion.lastIndexOf('.') + 1);

    public CompatibilityLayer registerCompatibilityLayer() {
        switch (this.version) {
            case "v1_12_R1": {
                return new v1_12_2();
            }
            case "v1_13_R2": {
                return new v1_13_2();
            }
            case "v1_14_R1": {
                return new v1_14_3();
            }
            case "v1_15_R1": {
                return new v1_15_2();
            }
            case "v1_16_R3": {
                return new v1_16_4();
            }
            default: {
                return null;
            }
        }
    }
}
