package com.jazzkuh.mtwapens.listeners;

import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.utility.ItemBuilder;
import com.jazzkuh.mtwapens.utility.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public class WeaponMenuListener implements Listener {
    Main plugin;

    public WeaponMenuListener(Main plugin) {
        this.plugin = plugin;
    }

    private ItemStack createWool(String string, Short nshort) {
        ItemStack item = new ItemStack(Material.WOOL, 1, nshort);

        ItemMeta im = item.getItemMeta();

        im.setDisplayName(Utils.color(string));
        item.setItemMeta(im);

        return item;
    }

    private static ItemStack createWeaponItem(Plugin plugin, String type) {
        ItemStack weapon = new ItemBuilder(Material.WOOD_HOE)
                .setName(Utils.color(plugin.getConfig().getString("weapons." + type + ".name")))
                .setNBT("mtcustom", type + "_fullmodel")
                .toItemStack();

        ItemMeta im = weapon.getItemMeta();
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        weapon.setItemMeta(im);

        return weapon;
    }

    private static ItemStack createAmmoItem(Plugin plugin, String type) {
        ItemStack bulletItem = new ItemBuilder(Material.IRON_INGOT)
                .setName(Utils.color(plugin.getConfig().getString("weapons." + type + ".ammo-name")))
                .setNBT("mtcustom", "" + type + "_bullets")
                .toItemStack();

        ItemMeta im = bulletItem.getItemMeta();
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        bulletItem.setItemMeta(im);

        return bulletItem;
    }

    private static int getDurability = 0;

    public static void weaponMenu(Plugin plugin, Player player) {
        Inventory menu = Bukkit.getServer().createInventory(player, 9 * 4, "MT Wapens Menu");

        getDurability = 0;

        menu.setItem(11, createWeaponItem(plugin,"deserteagle"));
        menu.setItem(12, createWeaponItem(plugin,"magnum44"));
        menu.setItem(13, createWeaponItem(plugin,"waltherp99"));
        menu.setItem(14, createWeaponItem(plugin,"glock19"));
        menu.setItem(15, createWeaponItem(plugin,"m16a4"));

        menu.setItem(20, createAmmoItem(plugin,"deserteagle"));
        menu.setItem(21, createAmmoItem(plugin,"magnum44"));
        menu.setItem(22, createAmmoItem(plugin,"waltherp99"));
        menu.setItem(23, createAmmoItem(plugin,"glock19"));
        menu.setItem(24, createAmmoItem(plugin,"m16a4"));

        player.openInventory(menu);
    }

    public void durabilityMenu(Player player, String string) {

        Inventory menu = Bukkit.getServer().createInventory(player, 9 * 5, "Durability Modifier " + string);

        menu.setItem(11, createWool("&c-1 Durability", (short) 14));
        menu.setItem(20, createWool("&c-5 Durability", (short) 14));
        menu.setItem(29, createWool("&c-10 Durability", (short) 14));

        menu.setItem(22, new ItemBuilder(Material.WORKBENCH).setName(Utils.color("&c" + getDurability + " &6Durability")).toItemStack());

        menu.setItem(15, createWool("&a+1 Durability", (short) 5));
        menu.setItem(24, createWool("&a+5 Durability", (short) 5));
        menu.setItem(33, createWool("&a+10 Durability", (short) 5));

        player.openInventory(menu);
    }

    @EventHandler
    public void inventoryClick(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();

        if (event.getView().getTitle().contains("Durability Modifier")) {
            event.setCancelled(true);

            String tempWeapon = event.getView().getTitle();
            String weapon = tempWeapon.replace("Durability Modifier ", "");

            if ((event.getCurrentItem() == null) || (event.getCurrentItem().getType().equals(Material.AIR))) {
                return;
            }

            createSlot(event, player, "&c-1 Durability", 11, true, 1, weapon);
            createSlot(event, player, "&c-5 Durability", 20, true, 5, weapon);
            createSlot(event, player, "&c-10 Durability", 29, true, 10, weapon);

            if (event.getSlot() == 22 && (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Utils.color("&c" + getDurability + " &6Durability")))) {
                event.setCancelled(true);
                if (!(getDurability >= 0)) {
                    player.sendMessage("&cDe durability van een wapen kan niet lager dan het integer 0.");
                } else {
                    getWeapon(player, getDurability, weapon);
                    getDurability = 0;
                    player.closeInventory();
                }
            }

            createSlot(event, player, "&a+1 Durability", 15, false, 1, weapon);
            createSlot(event, player, "&a+5 Durability", 24, false, 5, weapon);
            createSlot(event, player, "&a+10 Durability", 33, false, 10, weapon);


        }

        if (event.getView().getTitle().contains("MT Wapens Menu")) {
            event.setCancelled(true);

            if ((event.getCurrentItem() == null) || (event.getCurrentItem().getType().equals(Material.AIR))) {
                return;
            }

            createWeaponSlot(event, player, "deserteagle", 11);
            createWeaponSlot(event, player, "magnum44", 12);
            createWeaponSlot(event, player, "waltherp99", 13);
            createWeaponSlot(event, player, "glock19", 14);
            createWeaponSlot(event, player, "m16a4", 15);

            createAmmoSlot(event, player, "deserteagle", 20);
            createAmmoSlot(event, player, "magnum44", 21);
            createAmmoSlot(event, player, "waltherp99", 22);
            createAmmoSlot(event, player, "glock19", 23);
            createAmmoSlot(event, player, "m16a4", 24);
        }
    }

    private void createSlot (InventoryClickEvent event, Player player, String string, Integer integer, Boolean negative, Integer modify, String weapon) {
        if (event.getSlot() == integer && (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Utils.color(string)))) {
            event.setCancelled(true);
            if (negative) {
                if (!(getDurability - modify < 0)) {
                    getDurability -= modify;
                    durabilityMenu(player, weapon);
                }
            } else {
                getDurability += modify;
                durabilityMenu(player, weapon);
            }
            //player.closeInventory();
        }
    }

    private void createWeaponSlot(InventoryClickEvent event, Player player, String string, Integer integer) {
        if (event.getSlot() == integer && (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Utils.color(plugin.getConfig().getString("weapons." + string + ".name"))))) {
            event.setCancelled(true);
            durabilityMenu(player, string);
        }
    }

    private void createAmmoSlot(InventoryClickEvent event, Player player, String string, Integer integer) {
        if (event.getSlot() == integer && (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Utils.color(plugin.getConfig().getString("weapons." + string + ".ammo-name"))))) {
            event.setCancelled(true);
            getAmmo(player, string);
            player.closeInventory();
        }
    }

    private void getWeapon(Player player, int dura, String string) {
        int UUID = Utils.randomValue(999999999, 10000);

        String type = string.toLowerCase();

        ArrayList<String> Lore = new ArrayList<String>();
        Lore.add(Utils.color("&f"));
        Lore.add(Utils.color(plugin.getConfig().getString("weapon-lore")));
        Lore.add(Utils.color("&f"));
        Lore.add(Utils.color("&fAmmo: &7" + plugin.getConfig().getInt("weapons." + type + ".max-ammo") + "&f/&7" + plugin.getConfig().getInt("weapons." + type + ".max-ammo")));
        Lore.add(Utils.color("&f"));

        ItemStack weapon = new ItemBuilder(Material.WOOD_HOE)
                .setName(Utils.color(plugin.getConfig().getString("weapons." + type + ".name")))
                .setNBT("mtcustom", type + "_fullmodel")
                .setNBT("WEAPON-UUID", String.valueOf(UUID))
                .setLore(Lore)
                .toItemStack();

        ItemMeta im = weapon.getItemMeta();
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        weapon.setItemMeta(im);

        Utils.createWeaponData(UUID, dura, plugin.getConfig().getInt("weapons." + type + ".max-ammo"));

        player.getInventory().addItem(weapon);
        player.sendMessage(Utils.color(plugin.getConfig().getString("messages.first-color") + "Je hebt succesvol het wapen " + plugin.getConfig().getString("messages.second-color") + "" + type + plugin.getConfig().getString("messages.first-color") + " (" + plugin.getConfig().getString("messages.second-color") + "" + dura + " Durability" + plugin.getConfig().getString("messages.first-color") + ") ontvangen."));
    }

    private void getAmmo(Player player, String string) {

        String type = string.toLowerCase();

        ItemStack bulletItem = new ItemBuilder(Material.IRON_INGOT)
                .setName(Utils.color(plugin.getConfig().getString("weapons." + type + ".ammo-name")))
                .setLore(Utils.color(plugin.getConfig().getString("ammo-lore")))
                .setNBT("mtcustom", "" + type + "_bullets")
                .toItemStack();

        player.getInventory().addItem(bulletItem);
        player.sendMessage(Utils.color(plugin.getConfig().getString("messages.first-color") + "Je hebt succesvol ammo voor het wapen " + plugin.getConfig().getString("messages.second-color") + "" + type + plugin.getConfig().getString("messages.first-color") + " ontvangen."));
    }
}
