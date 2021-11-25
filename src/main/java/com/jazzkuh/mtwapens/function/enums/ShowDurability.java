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

package com.jazzkuh.mtwapens.function.enums;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;

public class ShowDurability {

    private static @Getter @Setter ShowDurability instance;

    public ShowDurability() {
        setInstance(this);
    }

    public ShowDurability.Options isDurabilityShown(@Nullable String option) {
        if (option == null) return Options.BOTH;

        switch (option.toUpperCase()) {
            case "SHOOT":
                return Options.SHOOT;
            case "SWITCH":
                return Options.SWITCH;
            case "NONE":
                return Options.NONE;
            case "BOTH":
            default:
                return Options.BOTH;
        }
    }

    public enum Options {
        SHOOT, SWITCH, BOTH, NONE;
    }
}
