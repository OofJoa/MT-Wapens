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
