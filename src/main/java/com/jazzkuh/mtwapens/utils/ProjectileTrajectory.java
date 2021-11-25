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

package com.jazzkuh.mtwapens.utils;

import com.jazzkuh.mtwapens.Main;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class ProjectileTrajectory {
    private final @Getter Entity entity;
    private final @Getter Vector vector;

    public ProjectileTrajectory(Entity projectile, Vector startingVector) {
        this.entity = projectile;
        this.vector = startingVector;
    }

    private int taskId;

    public void cancelTask() {
        Bukkit.getScheduler().cancelTask(this.taskId);
    }

    public void start() {
        this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> {
            if (!entity.isValid() || !entity.isOnGround()) {
                Bukkit.getScheduler().cancelTask(taskId);
                return;
            }

            entity.setVelocity(vector);
        }, 0, 2);
    }
}
