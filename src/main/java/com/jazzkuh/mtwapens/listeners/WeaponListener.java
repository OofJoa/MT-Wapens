package com.jazzkuh.mtwapens.listeners;

import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.data.WeaponData;
import com.jazzkuh.mtwapens.utility.ItemBuilder;
import com.jazzkuh.mtwapens.utility.Utils;
import com.jazzkuh.mtwapens.utility.messages.Message;
import com.jazzkuh.mtwapens.utility.messages.Messages;
import com.jazzkuh.mtwapens.utility.messages.Placeholder;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class WeaponListener implements Listener {
    Main plugin;

    static WeaponData weaponData = WeaponData.getInstance();

    public WeaponListener(Main plugin) {
        this.plugin = plugin;
    }

    public static HashMap<String, Date> weaponcooldown = new HashMap<>();

    public static boolean weaponCooldown(String string) {
        if (weaponcooldown.containsKey(string)) {
            if (weaponcooldown.get(string).getTime() > new Date().getTime()) {
                return false;
            } else {
                weaponcooldown.remove(string);
                return true;
            }
        } else {
            return true;
        }
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR))
            return;

        if (player.getInventory().getItemInMainHand() != null && player.getInventory().getItemInMainHand().getItemMeta() != null && player.getInventory().getItemInMainHand().getItemMeta().getDisplayName() != null) {
            if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains(Utils.color(plugin.getConfig().getString("weapons.deserteagle.name")))) {
                event.setCancelled(true);
                weaponClickEvent(player, "deserteagle");
            } else if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains(Utils.color(plugin.getConfig().getString("weapons.magnum44.name")))) {
                event.setCancelled(true);
                weaponClickEvent(player, "magnum44");
            } else if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains(Utils.color(plugin.getConfig().getString("weapons.waltherp99.name")))) {
                event.setCancelled(true);
                weaponClickEvent(player, "waltherp99");
            } else if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains(Utils.color(plugin.getConfig().getString("weapons.glock19.name")))) {
                event.setCancelled(true);
                weaponClickEvent(player, "glock19");
            } else if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains(Utils.color(plugin.getConfig().getString("weapons.m16a4.name")))) {
                event.setCancelled(true);
                weaponClickEvent(player, "m16a4");
            }
        }
    }

    private void weaponClickEvent(Player player, String type) {
        if (!(NBTEditor.contains(player.getInventory().getItemInMainHand(), "WEAPON-UUID")))
            return;

        String UUID = NBTEditor.getString(player.getInventory().getItemInMainHand(), "WEAPON-UUID");

        ItemStack bulletItem = new ItemBuilder(Material.IRON_INGOT)
                .setName(Utils.color(plugin.getConfig().getString("weapons." + type + ".ammo-name")))
                .setLore(Utils.color(plugin.getConfig().getString("ammo-lore")))
                .setNBT("mtcustom", "" + type + "_bullets")
                .toItemStack();

        if (weaponData.getWeaponData().getInt(UUID + ".durability") > 0) {
            if (weaponData.getWeaponData().getInt(UUID + ".ammo") > 0) {
                if (weaponCooldown(UUID)) {

                    weaponcooldown.put(UUID, new Date(new Date().getTime() + (long) (plugin.getConfig().getDouble("weapons." + type + ".attackspeed") * 1000)));

                    weaponData.getWeaponData().set(UUID + ".durability", weaponData.getWeaponData().getInt(UUID + ".durability") - 1);
                    weaponData.getWeaponData().set(UUID + ".ammo", weaponData.getWeaponData().getInt(UUID + ".ammo") - 1);

                    ArrayList<String> lore = new ArrayList<>();

                    ItemStack is = player.getInventory().getItemInMainHand();
                    ItemMeta im = is.getItemMeta();

                    lore.add(Utils.color("&f"));
                    lore.add(Utils.color(plugin.getConfig().getString("weapon-lore")));
                    lore.add(Utils.color("&f"));
                    lore.add(Utils.color("&fAmmo: &7" + weaponData.getWeaponData().getInt(UUID + ".ammo") + "&f/&7" + plugin.getConfig().getInt("weapons." + type + ".max-ammo")));
                    lore.add(Utils.color("&f"));
                    im.setLore(lore);
                    is.setItemMeta(im);

                    player.sendMessage(Main.getMessages().get(Message.SHOT_INFO_DURABILITY,
                            Placeholder.of("durability", weaponData.getWeaponData().getInt(UUID + ".durability"))));
                    player.sendMessage(Main.getMessages().get(Message.SHOT_INFO_AMMO,
                            Placeholder.of("ammo", weaponData.getWeaponData().getInt(UUID + ".ammo")),
                            Placeholder.of("max-ammo", plugin.getConfig().getInt("weapons." + type + ".max-ammo"))));

                    /*Snowball bullet = player.launchProjectile(Snowball.class);
                    bullet.setCustomName(type + "-" + this.plugin.getConfig().getDouble("weapons." + type + ".damage"));
                    bullet.setVelocity(bullet.getVelocity().multiply(2D));*/

                    Snowball bullet = player.launchProjectile(Snowball.class);
                    bullet.setCustomName(player.getName());
                    bullet.setVelocity(bullet.getVelocity().multiply(2D));
                }
            } else if (player.getInventory().containsAtLeast(bulletItem, 1)) {
                Messages messages = Main.getMessages();

                player.sendTitle(messages.get(Message.RELOADING_TITLE), messages.get(Message.RELOADING_SUBTITLE), 10, 20, 10);
                player.sendMessage(messages.get(Message.RELOADING));
                player.getInventory().removeItem(bulletItem);

                weaponcooldown.put(UUID, new Date(new Date().getTime() + (long) (plugin.getConfig().getInt("weapons." + type + ".attackspeed") * 1000)));
                weaponData.getWeaponData().set(UUID + ".ammo", plugin.getConfig().getInt("weapons." + type + ".max-ammo"));

                ArrayList<String> lore = new ArrayList<>();

                ItemStack is = player.getInventory().getItemInMainHand();
                ItemMeta im = is.getItemMeta();

                lore.add(Utils.color("&f"));
                lore.add(Utils.color(plugin.getConfig().getString("weapon-lore")));
                lore.add(Utils.color("&f"));
                lore.add(Utils.color("&fAmmo: &7" + weaponData.getWeaponData().getInt(UUID + ".ammo") + "&f/&7" + plugin.getConfig().getInt("weapons." + type + ".max-ammo")));
                lore.add(Utils.color("&f"));
                im.setLore(lore);
                is.setItemMeta(im);

                player.sendMessage(Main.getMessages().get(Message.SHOT_INFO_DURABILITY,
                        Placeholder.of("durability", weaponData.getWeaponData().getInt(UUID + ".durability"))));
                player.sendMessage(Main.getMessages().get(Message.SHOT_INFO_AMMO,
                        Placeholder.of("ammo", weaponData.getWeaponData().getInt(UUID + ".ammo")),
                        Placeholder.of("max-ammo", plugin.getConfig().getInt("weapons." + type + ".max-ammo"))));
            } else {
                player.sendMessage(Main.getMessages().get(Message.NO_AMMO));
            }
        } else {
            player.getInventory().removeItem(player.getInventory().getItemInMainHand());
        }
    }


    private void showAmmoDura(PlayerItemHeldEvent event, Player player, String type) {
        if (!(NBTEditor.contains(player.getInventory().getItem(event.getNewSlot()), "WEAPON-UUID")))
            return;

        String UUID = NBTEditor.getString(player.getInventory().getItem(event.getNewSlot()), "WEAPON-UUID");

        player.sendMessage(Main.getMessages().get(Message.SHOT_INFO_DURABILITY,
                Placeholder.of("durability", weaponData.getWeaponData().getInt(UUID + ".durability"))));
        player.sendMessage(Main.getMessages().get(Message.SHOT_INFO_AMMO,
                Placeholder.of("ammo", weaponData.getWeaponData().getInt(UUID + ".ammo")),
                Placeholder.of("max-ammo", plugin.getConfig().getInt("weapons." + type + ".max-ammo"))));
    }

    @EventHandler
    public void onPlayerItemHoldEvent(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();

        if (event.getPlayer().getInventory().getItem(event.getNewSlot()) != null && event.getPlayer().getInventory().getItem(event.getNewSlot()).getItemMeta() != null && event.getPlayer().getInventory().getItem(event.getNewSlot()).getItemMeta().getDisplayName() != null) {
            if (event.getPlayer().getInventory().getItem(event.getNewSlot()).getItemMeta().getDisplayName().matches(Utils.color(plugin.getConfig().getString("weapons.deserteagle.name")))) {
                showAmmoDura(event, player, "deserteagle");
            }
            if (event.getPlayer().getInventory().getItem(event.getNewSlot()).getItemMeta().getDisplayName().matches(Utils.color(plugin.getConfig().getString("weapons.magnum44.name")))) {
                showAmmoDura(event, player, "magnum44");
            }
            if (event.getPlayer().getInventory().getItem(event.getNewSlot()).getItemMeta().getDisplayName().matches(Utils.color(plugin.getConfig().getString("weapons.waltherp99.name")))) {
                showAmmoDura(event, player, "waltherp99");
            }
            if (event.getPlayer().getInventory().getItem(event.getNewSlot()).getItemMeta().getDisplayName().matches(Utils.color(plugin.getConfig().getString("weapons.glock19.name")))) {
                showAmmoDura(event, player, "glock19");
            }
            if (event.getPlayer().getInventory().getItem(event.getNewSlot()).getItemMeta().getDisplayName().matches(Utils.color(plugin.getConfig().getString("weapons.m16a4.name")))) {
                showAmmoDura(event, player, "m16a4");
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        
        LivingEntity entity = (LivingEntity) event.getEntity();
        Player attacker = Bukkit.getPlayer(event.getDamager().getName());

        if (event.getDamager().getType() == EntityType.SNOWBALL) {
            damageEventNew(event, entity, attacker, "deserteagle");
            damageEventNew(event, entity, attacker, "magnum44");
            damageEventNew(event, entity, attacker, "waltherp99");
            damageEventNew(event, entity, attacker, "glock19");
            damageEventNew(event, entity, attacker, "m16a4");
        }
    }

    private void damageEventNew(EntityDamageByEntityEvent event, LivingEntity entity, Player attacker, String type) {
        if (attacker.getInventory().getItemInMainHand().hasItemMeta() && attacker.getInventory().getItemInMainHand().getItemMeta().hasDisplayName() && attacker.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(Utils.color(plugin.getConfig().getString("weapons." + type + ".name")))) {
            event.setDamage(0.0);

            double damage = plugin.getConfig().getDouble("weapons." + type + ".damage");

            if (damage > entity.getHealth()) {
                entity.setHealth(0.0);
            } else {
                entity.setHealth(entity.getHealth() - damage);
            }

            if (entity instanceof Player) {
                Messages messages = Main.getMessages();
                attacker.sendMessage(messages.get(Message.SHOT_HIT_YOU, Placeholder.of("player", entity.getName())));
                entity.sendMessage(messages.get(Message.SHOT_HIT_OTHER, Placeholder.of("player", attacker.getName())));
            }
        }
    }
}
