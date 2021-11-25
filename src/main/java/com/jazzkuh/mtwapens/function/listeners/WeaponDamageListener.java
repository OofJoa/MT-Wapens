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
import org.bukkit.Effect;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class WeaponDamageListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getEntity() instanceof LivingEntity) || !(event.getDamager() instanceof Snowball)) return;

        Snowball bullet = (Snowball) event.getDamager();
        if (!bullet.hasMetadata("mtwapens_bullet")) return;

        if (!(bullet.getShooter() instanceof Player)) return;
        Player attacker = (Player) bullet.getShooter();

        // Probably not the best way but it works.
        Weapon weapon = new Weapon(bullet.getMetadata("mtwapens_bullet").get(0).asString());

        LivingEntity entity = (LivingEntity) event.getEntity();
        event.setDamage(0D);

        double bulletYLoc = bullet.getLocation().getY();
        double victimYLoc = entity.getLocation().getY();
        boolean isHeadshot = bulletYLoc - victimYLoc > 1.35D;

        double damage = isHeadshot && (double) weapon.getParameter(Weapon.WeaponParameters.HEADSHOT_DAMAGE) != 0D
                ? (double) weapon.getParameter(Weapon.WeaponParameters.HEADSHOT_DAMAGE)
                : Double.parseDouble(bullet.getName());

        if (Main.getInstance().getConfig().getBoolean("projectileProtectionReducesDamage") && entity.getEquipment() != null) {
            if (isHeadshot && entity.getEquipment().getHelmet() != null
                    && entity.getEquipment().getHelmet().getEnchantments().containsKey(Enchantment.PROTECTION_PROJECTILE)) {

                int enchantmentLevel = entity.getEquipment().getHelmet().getEnchantments().get(Enchantment.PROTECTION_PROJECTILE);
                int percentage = Main.getInstance().getConfig().getInt("damageReductionPercentagePerLevel") != 0
                        ? Main.getInstance().getConfig().getInt("damageReductionPercentagePerLevel")
                        : 5;

                damage = damage - ((damage / 100 * percentage) * enchantmentLevel);
            } else if (entity.getEquipment().getChestplate() != null
                    && entity.getEquipment().getChestplate().getEnchantments().containsKey(Enchantment.PROTECTION_PROJECTILE)) {

                int enchantmentLevel = entity.getEquipment().getChestplate().getEnchantments().get(Enchantment.PROTECTION_PROJECTILE);
                int percentage = Main.getInstance().getConfig().getInt("damageReductionPercentagePerLevel") != 0
                        ? Main.getInstance().getConfig().getInt("damageReductionPercentagePerLevel")
                        : 5;

                damage = damage - ((damage / 100 * percentage) * enchantmentLevel);
            }
        }

        if (entity.getLocation().getWorld() != null) {
            entity.getLocation().getWorld().playEffect(entity.getLocation().add(0.0D, 1.0D, 0.0D), Effect.STEP_SOUND, 152);
        }

        if (damage > entity.getHealth()) {
            entity.setHealth(0D);
        } else {
            entity.setHealth(entity.getHealth() - damage);
        }
    }
}
