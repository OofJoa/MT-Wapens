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

package com.jazzkuh.mtwapens.function.listeners;

import com.cryptomorin.xseries.XBlock;
import com.cryptomorin.xseries.XMaterial;
import com.jazzkuh.mtwapens.Main;
import org.bukkit.block.Block;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class ProjectileHitListener implements Listener {

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Snowball)) return;
        if (event.getHitBlock() == null) return;

        Snowball bullet = (Snowball) event.getEntity();
        Block block = event.getHitBlock();

        if (!bullet.hasMetadata("mtwapens_bullet")) return;
        if (XBlock.isType(block, XMaterial.GRASS) || XBlock.isType(block, XMaterial.TALL_GRASS) || block.getType().name().toLowerCase().contains("flower") || XBlock.isContainer(block) || XBlock.isCake(block.getType())) return;
        Main.getCompatibilityLayer().sendBlockBreakPacket(block);
    }
}
