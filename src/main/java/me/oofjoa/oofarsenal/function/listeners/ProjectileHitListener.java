package me.oofjoa.oofarsenal.function.listeners;

import com.cryptomorin.xseries.XBlock;
import com.cryptomorin.xseries.XMaterial;
import me.oofjoa.oofarsenal.Main;
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
        if (XBlock.isCrop(XMaterial.matchXMaterial(block.getType())) || XBlock.isContainer(block) || XBlock.isCake(block.getType())) return;
        Main.getCompatibilityLayer().sendBlockBreakPacket(block);
    }
}
