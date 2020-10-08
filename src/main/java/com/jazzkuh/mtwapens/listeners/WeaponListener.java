package com.jazzkuh.mtwapens.listeners;

import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.data.Weapon;
import com.jazzkuh.mtwapens.data.WeaponManager;
import com.jazzkuh.mtwapens.data.WeaponType;
import com.jazzkuh.mtwapens.utility.ItemBuilder;
import com.jazzkuh.mtwapens.utility.Utils;
import com.jazzkuh.mtwapens.utility.messages.Message;
import com.jazzkuh.mtwapens.utility.messages.Messages;
import com.jazzkuh.mtwapens.utility.messages.Placeholder;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class WeaponListener implements Listener {
    public HashMap<String, Long> weaponCooldown = new HashMap<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        WeaponManager weaponManager = Main.getWeaponManager();
        Messages messages = Main.getMessages();

        ItemStack item = player.getInventory().getItemInMainHand();
        if (!(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) ||
                !NBTEditor.contains(player.getInventory().getItemInMainHand(), "WEAPON-UUID") ||
                item == null || item.getItemMeta() == null) return;

        WeaponType weaponType = Main.getWeaponManager().getWeaponType(item.getItemMeta().getDisplayName());
        if (weaponType == null) return;

        FileConfiguration weaponData = Main.getWeaponManager().getWeaponData();
        String uuid = NBTEditor.getString(item, "WEAPON-UUID");

        if (!(weaponCooldown.getOrDefault(uuid, -1L) < System.currentTimeMillis())) return;

        Weapon weapon = Main.getWeaponManager().getWeapon(uuid);
        if (weapon == null) {
            weapon = weaponManager.putWeapon(uuid, new Weapon(weaponType, Integer.parseInt(NBTEditor.getString(item, "WEAPON-UUID")),
                    weaponData.getInt(uuid + ".durability"), weaponData.getInt(uuid + ".ammo")));
        }

        if (weapon.getDurability() <= 0) {
            player.getInventory().remove(item);
            return;
        }

        if (weapon.getAmmo() <= 0) {
            ItemStack bulletItem = new ItemBuilder(Material.IRON_INGOT)
                .setName(weaponType.getAmmoName())
                .setLore(messages.getAmmoLore())
                .setNBT("mtcustom", weaponType.getType() + "_bullets")
                .toItemStack();

            if (!player.getInventory().contains(bulletItem)) {
                player.sendMessage(Main.getMessages().get(Message.NO_AMMO));
                return;
            }

            player.sendTitle(messages.get(Message.RELOADING_TITLE), messages.get(Message.RELOADING_SUBTITLE), 10, 20, 10);
            player.sendMessage(messages.get(Message.RELOADING));
            player.getInventory().removeItem(bulletItem);

            weapon.setAmmo(weaponType.getMaxAmmo());
        } else {
            /*Snowball bullet = player.launchProjectile(Snowball.class);
            bullet.setCustomName(type + "-" + this.plugin.getConfig().getDouble("weapons." + type + ".damage"));
            bullet.setVelocity(bullet.getVelocity().multiply(2D));*/

            weapon.setAmmo(weapon.getAmmo() - 1);
            weapon.setDurability(weapon.getDurability() - 1);

            Snowball bullet = player.launchProjectile(Snowball.class);
            bullet.setCustomName(weaponType.getDamage() + "");
            bullet.setShooter(player);
            bullet.setVelocity(bullet.getVelocity().multiply(2D));
        }

        ItemMeta meta = item.getItemMeta();

        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.RESET.toString());
        lore.add(Main.getMessages().getWeaponLore());
        lore.add(ChatColor.RESET.toString());
        lore.add(Utils.color("&fAmmo: &7" + weapon.getAmmo() + "&f/&7" + weaponType.getMaxAmmo()));
        lore.add(ChatColor.RESET.toString());
        meta.setLore(lore);

        item.setItemMeta(meta);

        showWeaponInfo(player, weapon);

        weaponCooldown.put(uuid, System.currentTimeMillis() + ((long) (weaponType.getAttackSpeed() * 1000)));
    }

    private void showWeaponInfo(Player player, Weapon weapon) {
        player.sendMessage(Main.getMessages().get(Message.SHOT_INFO_DURABILITY,
                Placeholder.of("durability", weapon.getDurability())));
        player.sendMessage(Main.getMessages().get(Message.SHOT_INFO_AMMO,
                Placeholder.of("ammo", weapon.getAmmo()),
                Placeholder.of("max-ammo", weapon.getWeaponType().getMaxAmmo())));
    }

    @EventHandler
    public void onPlayerItemHoldEvent(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItem(event.getNewSlot());
        if (item == null) return;
        if (!NBTEditor.contains(item, "WEAPON-UUID")) return;
        Weapon weapon = Main.getWeaponManager().getWeapon(NBTEditor.getString(item, "WEAPON-UUID"));
        if (weapon == null) return;

        showWeaponInfo(player, weapon);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;

        Entity bullet = event.getDamager();
        if (bullet.getType() != EntityType.SNOWBALL || !NumberUtils.isNumber(bullet.getCustomName())) return;

        event.setDamage(Double.parseDouble(bullet.getCustomName()));

        if (((Snowball) event.getDamager()).getShooter() instanceof Player && event.getEntity() instanceof Player) {
            Messages messages = Main.getMessages();
            Player attacker = (Player) ((Snowball) event.getDamager()).getShooter();
            event.getEntity().sendMessage(messages.get(Message.SHOT_HIT_OTHER, Placeholder.of("player", attacker.getName())));
            attacker.sendMessage(messages.get(Message.SHOT_HIT_YOU, Placeholder.of("player", event.getEntity().getName())));
        }
    }
}
