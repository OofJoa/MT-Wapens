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

package com.jazzkuh.mtwapens.function;

import com.jazzkuh.mtwapens.function.enums.Recoil;
import com.jazzkuh.mtwapens.function.objects.Weapon;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;

public class RecoilUtils {

    public @Getter Weapon weapon;
    public @Getter
    Recoil recoil;

    public RecoilUtils(Weapon weapon, Recoil recoil) {
        this.weapon = weapon;
        this.recoil = recoil;
    }

    public void performRecoil(Player player) {
        Location location = player.getLocation();
        float pitch = location.getPitch();
        location.setPitch(pitch - (float) recoil.getPitchIncrement());

        // Use a cause other then PLUGIN or COMMAND because essentials sucks lol.
        Vector playerVelocity = player.getVelocity();
        player.teleport(location, PlayerTeleportEvent.TeleportCause.UNKNOWN);
        player.setVelocity(playerVelocity);

        Vector vector = player.getLocation().getDirection().normalize().multiply(recoil.getPushBack()).setY(0);
        player.setVelocity(vector);
    }
}
