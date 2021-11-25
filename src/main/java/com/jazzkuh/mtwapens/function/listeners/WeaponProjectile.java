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

import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.function.objects.Weapon;
import com.jazzkuh.mtwapens.utils.ProjectileTrajectory;
import com.jazzkuh.mtwapens.utils.datawatcher.DataWatcher;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class WeaponProjectile {

    public @Getter Weapon weapon;
    public @Getter Weapon.WeaponTypes weaponType;

    public WeaponProjectile(Weapon weapon, Weapon.WeaponTypes weaponType) {
        this.weapon = weapon;
        this.weaponType = weaponType;
    }

    public void fireProjectile(Player player) {
        switch (this.weaponType) {
            case FLAME_THROWER: {
                Vector playerVector = player.getLocation().getDirection();
                playerVector.multiply(8);

                Vector particleVector = playerVector.clone();
                particleVector.divide(new Vector(5, 5, 5));

                Location location = particleVector.toLocation(player.getWorld()).add(player.getLocation()).add(0, 1.05, 0);
                for (int i = 0; i < 10; i++) {
                    Vector vector = playerVector.clone();
                    vector.add(new Vector(Math.random() - Math.random(), Math.random() - Math.random(), Math.random() - Math.random()));
                    Location offsetLocation = vector.toLocation(player.getWorld());
                    player.getWorld().spawnParticle(Particle.FLAME, location, 0,
                            offsetLocation.getX(), offsetLocation.getY(), offsetLocation.getZ(), 0.1);
                }

                int range = (int) weapon.getParameter(Weapon.WeaponParameters.RANGE);
                for (Entity entity : player.getNearbyEntities(range, range, range)){
                    if (!(entity instanceof LivingEntity)) continue;

                    Location eye = player.getEyeLocation();
                    LivingEntity livingEntity = (LivingEntity) entity;
                    Vector vector = livingEntity.getEyeLocation().toVector().subtract(eye.toVector());

                    if (vector.normalize().dot(eye.getDirection()) < 0.99D) continue;
                    entity.setFireTicks(100);
                }

                for (Player target : player.getLocation().getWorld().getPlayers()) {
                    if (target.getLocation().distance(player.getLocation()) <= 16D) {
                        target.playSound(player.getLocation(),
                                Sound.ENTITY_BLAZE_SHOOT, 100, 1F);
                    }
                }
                break;
            }
            case FLARE: {
                Firework firework = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
                FireworkMeta fireworkMeta = firework.getFireworkMeta();
                FireworkEffect effect = FireworkEffect.builder()
                        .with(FireworkEffect.Type.BURST)
                        .with(FireworkEffect.Type.BALL_LARGE)
                        .withColor(Color.fromRGB((int) weapon.getParameter(Weapon.WeaponParameters.COLOR)))
                        .build();
                fireworkMeta.addEffect(effect);
                fireworkMeta.setPower(2);
                firework.setFireworkMeta(fireworkMeta);
                break;
            }
            case RPG: {
                Fireball fireball = player.launchProjectile(Fireball.class);
                fireball.setMetadata("mtwapens_rpg_bullet", new FixedMetadataValue(Main.getInstance(), true));
                fireball.setIsIncendiary(false);

                for (Player target : player.getLocation().getWorld().getPlayers()) {
                    if (target.getLocation().distance(player.getLocation()) <= 16D) {
                        target.playSound(player.getLocation(),
                                weapon.getParameter(Weapon.WeaponParameters.SOUND).toString(), 100, 1F);
                    }
                }
                break;
            }
            case SNIPER:
            case SINGLE_BULLET:
            case MULTIPLE_BULLET:
            default: {
                Location playerLocation = player.getLocation();
                Snowball bullet = player.launchProjectile(Snowball.class);
                bullet.setCustomName("" + (double) weapon.getParameter(Weapon.WeaponParameters.DAMAGE));
                bullet.setShooter(player);
                //bullet.setVelocity(bullet.getVelocity().multiply(2D));
                bullet.setVelocity(player.getLocation().getDirection().multiply(4.6D));

                ProjectileTrajectory projectileTrajectory = new ProjectileTrajectory(bullet, bullet.getVelocity());
                projectileTrajectory.start();

                bullet.setMetadata("mtwapens_bullet", new FixedMetadataValue(Main.getInstance(), weapon.getWeaponType()));

                for (Player target : player.getLocation().getWorld().getPlayers()) {
                    if (target.getLocation().distance(player.getLocation()) <= 16D) {
                        target.playSound(player.getLocation(),
                                weapon.getParameter(Weapon.WeaponParameters.SOUND).toString(), 100, 1F);
                    }
                }

                // setup a async datawatcher that runs every tick
                DataWatcher<Location> locationDataWatcher = new DataWatcher<>(Main.getInstance(), false, 1);
                locationDataWatcher.setFeeder(bullet::getLocation);
                locationDataWatcher.setTask((bulletLocation) -> {
                    if (playerLocation.distance(bulletLocation) >= (double) weapon.getParameter(Weapon.WeaponParameters.WEAPON_RANGE)) {
                        projectileTrajectory.cancelTask();
                        bullet.remove();
                        locationDataWatcher.stop();
                    }
                });
                break;
            }
        }
    }
}
