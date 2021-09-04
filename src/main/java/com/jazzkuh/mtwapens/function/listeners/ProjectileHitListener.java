package com.jazzkuh.mtwapens.function.listeners;

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
        Main.getCompatibilityLayer().sendBlockBreakPacket(block);
    }
}
