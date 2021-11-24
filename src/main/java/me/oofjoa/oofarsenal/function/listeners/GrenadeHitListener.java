package me.oofjoa.oofarsenal.function.listeners;

import me.oofjoa.oofarsenal.Main;
import me.oofjoa.oofarsenal.function.objects.Grenade;
import me.oofjoa.oofarsenal.utils.Utils;
import de.slikey.effectlib.effect.ParticleEffect;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.potion.PotionEffect;

import java.util.Collection;
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
                case EFFECT: {
                    block.getWorld().spawnParticle(Particle.SMOKE_LARGE, block.getLocation(), 1);
                    break;
                }
                case SMOKE: {
                    int iterations = (int) grenade.getParameter(Grenade.GrenadeParameters.ITERATIONS);
                    for (int i = 0; i <= iterations; i++) {
                        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                            ParticleEffect particleEffect = new ParticleEffect(Main.getEffectManager());
                            particleEffect.particle = Particle.SMOKE_LARGE;
                            particleEffect.particleSize = 50;
                            particleEffect.particleCount = 50;
                            particleEffect.iterations = 1;
                            particleEffect.particleOffsetX = new Random().nextInt(2);
                            particleEffect.particleOffsetZ = new Random().nextInt(2);
                            particleEffect.particleOffsetY = new Random().nextInt(2);
                            particleEffect.setLocation(block.getLocation());
                            particleEffect.start();
                        }, 20L * i);
                    }
                    break;
                }
            }
        }

        switch (Grenade.GrenadeTypes.valueOf(grenade.getParameter(Grenade.GrenadeParameters.TYPE).toString())) {
            case EXPLODE: {
                for (Entity target : grenadeItem.getNearbyEntities(range, range, range)) {
                    if (!(target instanceof LivingEntity)) continue;

                    LivingEntity livingEntity = (LivingEntity) target;
                    livingEntity.damage(damage);
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
            case EFFECT: {
                for (Entity target : grenadeItem.getNearbyEntities(range, range, range)) {
                    if (!(target instanceof LivingEntity)) continue;

                    LivingEntity livingEntity = (LivingEntity) target;

                    if (damage != 0D) {
                        livingEntity.damage(damage);
                    }

                    livingEntity.addPotionEffects((Collection<PotionEffect>) grenade.getParameter(Grenade.GrenadeParameters.EFFECTS));

                    if (livingEntity instanceof Player) {
                        ((Player) target).playSound(livingEntity.getLocation(), Sound.ENTITY_SPLASH_POTION_BREAK, 100, 1F);
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
