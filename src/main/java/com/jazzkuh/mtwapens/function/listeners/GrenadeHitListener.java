package com.jazzkuh.mtwapens.function.listeners;

import com.jazzkuh.mtwapens.function.objects.Grenade;
import com.jazzkuh.mtwapens.utils.Utils;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;

import java.util.Random;

public class GrenadeHitListener implements Listener {
    @EventHandler
    public void onEggThrow(PlayerEggThrowEvent event) {
        if (!event.getEgg().hasMetadata("mtwapens_grenade")) return;
        event.setHatching(false);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Egg)) return;
        Egg grenadeItem = (Egg) event.getEntity();
        if (!grenadeItem.hasMetadata("mtwapens_grenade")) return;
        Grenade grenade = new Grenade(grenadeItem.getCustomName());

        double damage = (double) grenade.getParameter(Grenade.GrenadeParameters.RANGED_DAMAGE);
        double range = (double) grenade.getParameter(Grenade.GrenadeParameters.RANGE);

        for (Block block : Utils.getBlocksAroundCenter(grenadeItem.getLocation(), (int) range)) {
            switch (Grenade.GrenadeTypes.valueOf(grenade.getParameter(Grenade.GrenadeParameters.TYPE).toString())) {
                case EXPLODE: {
                    block.getWorld().createExplosion(block.getLocation(), 0F, false, false);
                    break;
                }
                case MOLOTOV: {
                    block.getWorld().spawnParticle(Particle.LAVA, block.getLocation(), 1);
                    break;
                }
            }
        }

        switch (Grenade.GrenadeTypes.valueOf(grenade.getParameter(Grenade.GrenadeParameters.TYPE).toString())) {
            case EXPLODE: {
                for (Entity target : grenadeItem.getNearbyEntities(range, range, range)) {
                    if (!(target instanceof LivingEntity)) continue;

                    LivingEntity livingEntity = (LivingEntity) target;
                    livingEntity.damage(damage, (Entity) grenadeItem.getShooter());
                }
                break;
            }
            case MOLOTOV: {
                for (Entity target : grenadeItem.getNearbyEntities(range, range, range)) {
                    if (!(target instanceof LivingEntity)) continue;

                    LivingEntity livingEntity = (LivingEntity) target;
                    livingEntity.setFireTicks(200);

                    if (livingEntity instanceof Player) {
                        ((Player) target).playSound(livingEntity.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 100, 1F);
                    }
                }
                break;
            }
        }

        playEffect(Effect.SMOKE, grenadeItem.getLocation(), new Random().nextInt(9));
    }

    private void playEffect(Effect effect, Location location, int type) {
        for (int i = 0; i < 10; i++) {
            location.getWorld().playEffect(location, effect, type);
        }
    }
}
