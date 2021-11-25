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

@SuppressWarnings("unused")
public enum Recoil {
    LOW(0.8D, 0.1D),
    MEDIUM(1.5D, 0.2D),
    HIGH(2D, 0.4D);

    public final @Getter double pitchIncrement;
    public final double pushBack;

    Recoil(double pitchIncrement, double pushBack) {
        this.pitchIncrement = pitchIncrement;
        this.pushBack = pushBack;
    }

    public double getPushBack() {
        return -pushBack;
    }
}
