/*
 *     MT-Wapens
 *     Copyright © 2021 Jazzkuh. All rights reserved.
 *
 *     “Commons Clause” License Condition v1.0
 *
 *    The Software is provided to you by the Licensor under the License, as defined below, subject to the following condition.
 *
 *     Without limiting other conditions in the License, the grant of rights under the License will not include, and the License does not grant to you, the right to Sell the Software.
 *
 *     For purposes of the foregoing, “Sell” means practicing any or all of the  rights granted to you under the License to provide to third parties, for a fee  or other consideration (including without limitation fees for hosting or  consulting/ support services related to the Software), a product or service  whose value derives, entirely or substantially, from the functionality of the  Software. Any license notice or attribution required by the License must also  include this Commons Clause License Condition notice.
 *
 *     Software: MT-Wapens
 *     License: GNU-LGPL v2.1 with Commons Clause
 *     Licensor: [Jazzkuh](https://github.com/Jazzkuh)
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
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

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

        EntityDamageByEntityEvent entityDamageEvent = new EntityDamageByEntityEvent(attacker, entity, EntityDamageEvent.DamageCause.VOID, entity.getHealth());
        event.getEntity().setLastDamageCause(entityDamageEvent);

        if (damage > entity.getHealth()) {
            entity.setHealth(0D);
        } else {
            entity.setHealth(entity.getHealth() - damage);
        }

        Bukkit.getServer().getPluginManager().callEvent(event);
    }
}
