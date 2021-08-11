package com.jazzkuh.mtwapens.function.listeners;

import com.jazzkuh.mtwapens.Main;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class WeaponDamageListener implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity) || !(event.getDamager() instanceof Snowball)) return;
        if (event.isCancelled()) {
            event.setCancelled(true);
            return;
        }

        Snowball bullet = (Snowball) event.getDamager();
        if (!bullet.hasMetadata("mtwapens_bullet")) return;

        if (!(bullet.getShooter() instanceof Player)) return;
        Player attacker = (Player) bullet.getShooter();

        LivingEntity entity = (LivingEntity) event.getEntity();
        event.setDamage(0D);

        double damage = Double.parseDouble(bullet.getName());
        if (Main.getInstance().getConfig().getBoolean("projectileProtectionReducesDamage") && entity.getEquipment().getChestplate() != null
                && entity.getEquipment().getChestplate().getEnchantments().containsKey(Enchantment.PROTECTION_PROJECTILE)) {

            int enchantmentLevel = entity.getEquipment().getChestplate().getEnchantments().get(Enchantment.PROTECTION_PROJECTILE);
            int percentage = Main.getInstance().getConfig().getInt("damageReductionPercentagePerLevel") != 0
                    ? Main.getInstance().getConfig().getInt("damageReductionPercentagePerLevel")
                    : 5;

            damage = damage - ((damage / 100 * percentage) * enchantmentLevel);
        }

        if (damage > entity.getHealth()) {
            entity.setHealth(0D);
        } else {
            entity.setHealth(entity.getHealth() - damage);
        }
    }
}
