package com.jazzkuh.mtwapens.function.listeners;

import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.function.objects.Weapon;
import lombok.Getter;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.*;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;

public class WeaponProjectile {

    public @Getter Weapon weapon;
    public @Getter Weapon.WeaponTypes weaponType;

    public WeaponProjectile(Weapon weapon, Weapon.WeaponTypes weaponType) {
        this.weapon = weapon;
        this.weaponType = weaponType;
    }

    public void fireProjectile(Player player) {
        switch (this.weaponType) {
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
            default: {
                Snowball bullet = player.launchProjectile(Snowball.class);
                bullet.setCustomName("" + (double) weapon.getParameter(Weapon.WeaponParameters.DAMAGE));
                bullet.setShooter(player);
                bullet.setVelocity(bullet.getVelocity().multiply(2D));
                bullet.setMetadata("mtwapens_bullet", new FixedMetadataValue(Main.getInstance(), true));

                for (Player target : player.getLocation().getWorld().getPlayers()) {
                    if (target.getLocation().distance(player.getLocation()) <= 16D) {
                        target.playSound(player.getLocation(),
                                weapon.getParameter(Weapon.WeaponParameters.SOUND).toString(), 100, 1F);
                    }
                }
                break;
            }
        }
    }
}
