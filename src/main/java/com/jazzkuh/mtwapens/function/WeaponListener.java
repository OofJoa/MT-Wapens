package com.jazzkuh.mtwapens.function;

import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.utils.ItemBuilder;
import com.jazzkuh.mtwapens.utils.Utils;
import com.jazzkuh.mtwapens.utils.messages.DefaultMessages;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class WeaponListener implements Listener {
    Main plugin;

    public WeaponListener(Main plugin) {
        this.plugin = plugin;
    }

    private final HashMap<String, Date> weaponCooldown = new HashMap<>();
    private final HashMap<String, Boolean> reloadDelay = new HashMap<>();

    private boolean weaponCooldown(String string) {
        if (weaponCooldown.containsKey(string)) {
            if (weaponCooldown.get(string).getTime() > new Date().getTime()) {
                return false;
            } else {
                weaponCooldown.remove(string);
                return true;
            }
        } else {
            return true;
        }
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {

        Player player = event.getPlayer();

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            if (player.getInventory().getItemInMainHand() != null && player.getInventory().getItemInMainHand().getItemMeta() != null && player.getInventory().getItemInMainHand().getItemMeta().getDisplayName() != null) {
                for (String type : plugin.getConfig().getConfigurationSection("weapons.").getKeys(false)) {
                    if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(Utils.color(plugin.getConfig().getString("weapons." + type + ".name")))) {
                        event.setCancelled(true);
                        weaponClickEvent(event, player, type);
                    }
                }
            }
        } else if (event.getAction() == Action.LEFT_CLICK_AIR) {
            if (player.getInventory().getItemInMainHand() != null && player.getInventory().getItemInMainHand().getItemMeta() != null && player.getInventory().getItemInMainHand().getItemMeta().getDisplayName() != null) {
                for (String type : plugin.getConfig().getConfigurationSection("weapons.").getKeys(false)) {
                    if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(Utils.color(plugin.getConfig().getString("weapons." + type + ".name")))) {
                        if (plugin.getConfig().getBoolean("weapons." + type + ".scope")) {
                            if (player.hasPotionEffect(PotionEffectType.SLOW)) {
                                player.removePotionEffect(PotionEffectType.SLOW);
                            } else {
                                int amplifier = Utils.isInt(String.valueOf(plugin.getConfig().getInt("weapons." + type + ".scope-size"))) ? plugin.getConfig().getInt("weapons." + type + ".scope-size") : 8;
                                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 600, amplifier), true);
                            }
                        }
                    }
                }
            }
        }
    }

    private void setNBT(Player player, String key, int value) {
        ItemStack itemStack = NBTEditor.set(player.getInventory().getItemInMainHand(), value, key);
        ItemMeta itemMeta = itemStack.getItemMeta();
        player.getInventory().getItemInMainHand().setItemMeta(itemMeta);
    }

    private void weaponClickEvent(PlayerInteractEvent event, Player player, String type) {
        if (!(NBTEditor.contains(player.getInventory().getItemInMainHand(), "ammo")))
            return;

        String ammoType = plugin.getConfig().getString("weapons." + type + ".ammo-type");

        ArrayList<String> bulletLore = new ArrayList<>();
        for (String lorePart : plugin.getConfig().getStringList("ammo." + ammoType + ".lore")) {
            bulletLore.add(Utils.color(lorePart));
        }

        ItemStack bulletItem = new ItemBuilder(Material.valueOf(plugin.getConfig().getString("ammo." + ammoType + ".material")))
                .setName(Utils.color(plugin.getConfig().getString("ammo." + ammoType + ".name")))
                .setNBT(plugin.getConfig().getString("ammo." + ammoType + ".nbt"), plugin.getConfig().getString("ammo." + ammoType + ".nbtvalue"))
                .setLore(bulletLore)
                .toItemStack();

        String cooldownId = plugin.getConfig().getBoolean("weaponCooldownPerWeapon") ? player.getUniqueId() + "-" + event.getPlayer().getInventory().getHeldItemSlot() : String.valueOf(player.getUniqueId());

        if (NBTEditor.getInt(player.getInventory().getItemInMainHand(), "durability") > 0) {
            if (NBTEditor.getInt(player.getInventory().getItemInMainHand(), "ammo") > 0) {
                if (weaponCooldown(cooldownId)) {

                    weaponCooldown.put(cooldownId, new Date(new Date().getTime() + (long) (plugin.getConfig().getDouble("weapons." + type + ".attackspeed") * 1000)));

                    setNBT(player, "ammo", NBTEditor.getInt(player.getInventory().getItemInMainHand(), "ammo") - 1);

                    if (!plugin.getConfig().getBoolean("weapons." + type + ".disable-durability")) {
                        setNBT(player, "durability", NBTEditor.getInt(player.getInventory().getItemInMainHand(), "durability") - 1);
                    }

                    ItemStack is = player.getInventory().getItemInMainHand();
                    ItemMeta im = is.getItemMeta();

                    ArrayList<String> weaponLore = new ArrayList<>();
                    for (String lorePart : plugin.getConfig().getStringList("weapons." + type + ".lore")) {
                        String finalPart = lorePart
                                .replace("<Ammo>", String.valueOf(NBTEditor.getInt(player.getInventory().getItemInMainHand(), "ammo")))
                                .replace("<MaxAmmo>", String.valueOf(plugin.getConfig().getInt("weapons." + type + ".max-ammo")))
                                .replace("<Damage>", String.valueOf(plugin.getConfig().getDouble("weapons." + type + ".damage")))
                                .replace("<Durability>", String.valueOf(NBTEditor.getInt(player.getInventory().getItemInMainHand(), "durability")));
                        weaponLore.add(Utils.color(finalPart));
                    }

                    im.setLore(weaponLore);
                    is.setItemMeta(im);

                    Utils.sendMessage(player, Main.getMessages().get(DefaultMessages.DURABILITY)
                            .replace("<Durability>", String.valueOf(NBTEditor.getInt(player.getInventory().getItemInMainHand(), "durability"))));
                    Utils.sendMessage(player, Main.getMessages().get(DefaultMessages.AMMO)
                            .replace("<Ammo>", String.valueOf(NBTEditor.getInt(player.getInventory().getItemInMainHand(), "ammo")))
                            .replace("<MaxAmmo>", String.valueOf(plugin.getConfig().getInt("weapons." + type + ".max-ammo"))));

                    Snowball bullet = player.launchProjectile(Snowball.class);
                    bullet.setCustomName(player.getName());
                    bullet.setShooter(player);
                    bullet.setVelocity(bullet.getVelocity().multiply(2D));

                    for (Player other : player.getLocation().getWorld().getPlayers()) {
                        if (other.getLocation().distance(player.getLocation()) <= (double) 16) {
                            other.playSound(player.getLocation(), plugin.getConfig().getString("weapons." + type + ".sound"), 100, 1F);
                        }
                    }
                }
            } else if (player.getInventory().containsAtLeast(bulletItem, 1) && !reloadDelay.containsKey(String.valueOf(player.getUniqueId()))) {
                for (Player other : player.getLocation().getWorld().getPlayers()) {
                    if (other.getLocation().distance(player.getLocation()) <= (double) 16) {
                        other.playSound(player.getLocation(), plugin.getConfig().getString("weapons." + type + ".reload-sound"), 100, 1F);
                    }
                }

                player.sendTitle(Main.getMessages().get(DefaultMessages.RELOADING_TITLE), Main.getMessages().get(DefaultMessages.RELOADING_SUBTITLE), 10, 20, 10);
                Utils.sendMessage(player, Main.getMessages().get(DefaultMessages.RELOADING));
                player.getInventory().removeItem(bulletItem);

                reloadDelay.put(String.valueOf(player.getUniqueId()), true);
                weaponCooldown.put(cooldownId, new Date(new Date().getTime() + (long) (plugin.getConfig().getInt("weapons." + type + ".attackspeed") * 1000)));

                new BukkitRunnable() {
                    public void run() {
                        setNBT(player, "ammo", plugin.getConfig().getInt("weapons." + type + ".max-ammo"));

                        ItemStack is = player.getInventory().getItemInMainHand();
                        ItemMeta im = is.getItemMeta();

                        ArrayList<String> weaponLore = new ArrayList<>();
                        for (String lorePart : plugin.getConfig().getStringList("weapons." + type + ".lore")) {
                            String finalPart = lorePart
                                    .replace("<Ammo>", String.valueOf(NBTEditor.getInt(player.getInventory().getItemInMainHand(), "ammo")))
                                    .replace("<MaxAmmo>", String.valueOf(plugin.getConfig().getInt("weapons." + type + ".max-ammo")))
                                    .replace("<Damage>", String.valueOf(plugin.getConfig().getDouble("weapons." + type + ".damage")))
                                    .replace("<Durability>", String.valueOf(NBTEditor.getInt(player.getInventory().getItemInMainHand(), "durability")));
                            weaponLore.add(Utils.color(finalPart));
                        }

                        im.setLore(weaponLore);
                        is.setItemMeta(im);

                        Utils.sendMessage(player, Main.getMessages().get(DefaultMessages.DURABILITY)
                                .replace("<Durability>", String.valueOf(NBTEditor.getInt(player.getInventory().getItemInMainHand(), "durability"))));
                        Utils.sendMessage(player, Main.getMessages().get(DefaultMessages.AMMO)
                                .replace("<Ammo>", String.valueOf(NBTEditor.getInt(player.getInventory().getItemInMainHand(), "ammo")))
                                .replace("<MaxAmmo>", String.valueOf(plugin.getConfig().getInt("weapons." + type + ".max-ammo"))));

                        reloadDelay.remove(String.valueOf(player.getUniqueId()));
                    }
                }.runTaskLater(plugin, 35);
            } else {
                if (reloadDelay.containsKey(String.valueOf(player.getUniqueId())))
                    return;

                Utils.sendMessage(player, Main.getMessages().get(DefaultMessages.NO_AMMO));
                player.playSound(player.getLocation(), plugin.getConfig().getString("empty-sound"), 100, 1F);
            }
        } else {
            player.getInventory().removeItem(player.getInventory().getItemInMainHand());
        }
    }

    @EventHandler
    public void onPlayerItemHoldEvent(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();

        if (reloadDelay.containsKey(String.valueOf(player.getUniqueId()))) {
            event.setCancelled(true);
            return;
        }

        if (event.getPlayer().getInventory().getItem(event.getNewSlot()) != null && event.getPlayer().getInventory().getItem(event.getNewSlot()).getItemMeta() != null && event.getPlayer().getInventory().getItem(event.getNewSlot()).getItemMeta().getDisplayName() != null) {
            for (String type : plugin.getConfig().getConfigurationSection("weapons.").getKeys(false)) {
                if (event.getPlayer().getInventory().getItem(event.getNewSlot()).getItemMeta().getDisplayName().equals(Utils.color(plugin.getConfig().getString("weapons." + type + ".name")))) {
                    if (!(NBTEditor.contains(player.getInventory().getItem(event.getNewSlot()), "ammo")))
                        return;

                    Utils.sendMessage(player, Main.getMessages().get(DefaultMessages.DURABILITY)
                            .replace("<Durability>", String.valueOf(NBTEditor.getInt(player.getInventory().getItem(event.getNewSlot()), "durability"))));
                    Utils.sendMessage(player, Main.getMessages().get(DefaultMessages.AMMO)
                            .replace("<Ammo>", String.valueOf(NBTEditor.getInt(player.getInventory().getItem(event.getNewSlot()), "ammo")))
                            .replace("<MaxAmmo>", String.valueOf(plugin.getConfig().getInt("weapons." + type + ".max-ammo"))));
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;

        if (event.getEntity() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) event.getEntity();
            Player attacker = Bukkit.getPlayer(event.getDamager().getName());

            if (event.getDamager() instanceof Snowball) {
                for (String type : plugin.getConfig().getConfigurationSection("weapons.").getKeys(false)) {
                    if (attacker.getInventory().getItemInMainHand().hasItemMeta() && attacker.getInventory().getItemInMainHand().getItemMeta().hasDisplayName() && attacker.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(Utils.color(plugin.getConfig().getString("weapons." + type + ".name")))) {
                        event.setDamage(0.0);

                        double damage = plugin.getConfig().getDouble("weapons." + type + ".damage");

                        if (damage > entity.getHealth()) {
                            entity.setHealth(0.0);
                        } else {
                            entity.setHealth(entity.getHealth() - damage);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        reloadDelay.remove(String.valueOf(event.getPlayer().getUniqueId()));
    }
}
