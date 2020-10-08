package com.jazzkuh.mtwapens.listeners;

import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.data.Weapon;
import com.jazzkuh.mtwapens.data.WeaponType;
import com.jazzkuh.mtwapens.utility.ItemBuilder;
import com.jazzkuh.mtwapens.utility.Utils;
import com.jazzkuh.mtwapens.utility.messages.Message;
import com.jazzkuh.mtwapens.utility.messages.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

    private static ItemStack createWeaponItem(WeaponType weaponType) {
        ItemStack weaponItem = new ItemBuilder(Material.WOOD_HOE)
                .setName(weaponType.getDisplayName())
                .setNBT("mtcustom", weaponType.getType() + "_fullmodel")
                .toItemStack();

        ItemMeta im = weaponItem.getItemMeta();
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        weaponItem.setItemMeta(im);

        return weaponItem;
    }

    private static ItemStack createAmmoItem(WeaponType weaponType) {
        ItemStack bulletItem = new ItemBuilder(Material.IRON_INGOT)
                .setName(weaponType.getDisplayName())
                .setNBT("mtcustom", "" + weaponType.getType() + "_bullets")
                .toItemStack();

        ItemMeta im = bulletItem.getItemMeta();
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        bulletItem.setItemMeta(im);

        return bulletItem;
    }

    private static int durability = 0;

    public static void weaponMenu(Player player, int page) {
        ArrayList<WeaponType> weaponTypes = Main.getWeaponManager().getWeaponTypes();

        Inventory menu = Bukkit.getServer().createInventory(player, 3 * 9, "MT Wapens Menu");
        for (int i = 0; i < Math.min(weaponTypes.size() - page * 9, 9); i++) {
            int index = i + page * 9;
            menu.setItem(i, createWeaponItem(weaponTypes.get(index)));
            menu.setItem(i + 9, createAmmoItem(weaponTypes.get(index)));
        }

        menu.setItem(22, new ItemBuilder(Material.BARRIER).setName(ChatColor.RED + "Annuleer").toItemStack());

        if (page > 0) {
            menu.setItem(21, new ItemBuilder(Material.SIGN)
                    .setName(ChatColor.GOLD + "Ga naar pagina " + page).toItemStack());
        }

        if (weaponTypes.size() - page * 9 > 9) {
            menu.setItem(23, new ItemBuilder(Material.SIGN)
                    .setName(ChatColor.GOLD + "Ga naar pagina " + (page + 2)).toItemStack());
        }

        durability = 0;

        player.openInventory(menu);
    }

    public void durabilityMenu(Player player, String string) {

        Inventory menu = Bukkit.getServer().createInventory(player, 9 * 5, "Durability Modifier " + string);

        menu.setItem(11, createWool("&c-1 Durability", (short) 14));
        menu.setItem(20, createWool("&c-5 Durability", (short) 14));
        menu.setItem(29, createWool("&c-10 Durability", (short) 14));

        menu.setItem(22, new ItemBuilder(Material.WORKBENCH).setColoredName("&c" + durability + " &6Durability").toItemStack());

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

            if (event.getSlot() == 22 && (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Utils.color("&c" + durability + " &6Durability")))) {
                event.setCancelled(true);
                if (!(durability >= 0)) {
                    player.sendMessage(Main.getMessages().get(Message.INVALID_DURABILITY));
                } else {
                    getWeapon(player, durability, weapon);
                    durability = 0;
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

            ItemStack item = event.getCurrentItem();
            String displayName = item.getItemMeta().getDisplayName();

            if (item.getType() == Material.BARRIER) {
                player.closeInventory();
            } else if (item.getType() == Material.SIGN) {
                int newPage = Integer.parseInt(displayName.replaceAll("([a-zA-Z]|\\s|§\\d)+", "")) - 1;
                weaponMenu(player, newPage);
            } else if (event.getSlot() < 9) {
                durabilityMenu(player, Main.getWeaponManager().getWeaponType(displayName).getType());
            } else {
                getAmmo(player, Main.getWeaponManager().getWeaponType(displayName).getType());
                player.closeInventory();
            }
        }
    }

    private void createSlot(InventoryClickEvent event, Player player, String string, Integer integer, Boolean negative, Integer modify, String weapon) {
        if (event.getSlot() == integer && (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Utils.color(string)))) {
            event.setCancelled(true);
            if (negative) {
                if (!(durability - modify < 0)) {
                    durability -= modify;
                    durabilityMenu(player, weapon);
                }
            } else {
                durability += modify;
                durabilityMenu(player, weapon);
            }
            //player.closeInventory();
        }
    }

    private void getWeapon(Player player, int dura, String string) {
        int UUID = Utils.randomValue(999999999, 10000);

        String type = string.toLowerCase();

        ArrayList<String> lore = new ArrayList<>();
        lore.add(Utils.color("&f"));
        lore.add(Utils.color(plugin.getConfig().getString("weapon-lore")));
        lore.add(Utils.color("&f"));
        lore.add(Utils.color("&fAmmo: &7" + plugin.getConfig().getInt("weapons." + type + ".max-ammo") + "&f/&7" + plugin.getConfig().getInt("weapons." + type + ".max-ammo")));
        lore.add(Utils.color("&f"));

        ItemStack weapon = new ItemBuilder(Material.WOOD_HOE)
                .setName(Utils.color(plugin.getConfig().getString("weapons." + type + ".name")))
                .setNBT("mtcustom", type + "_fullmodel")
                .setNBT("WEAPON-UUID", String.valueOf(UUID))
                .setLore(lore)
                .toItemStack();

        ItemMeta im = weapon.getItemMeta();
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        weapon.setItemMeta(im);

        Utils.createWeaponData(UUID, dura, plugin.getConfig().getInt("weapons." + type + ".max-ammo"));

        player.getInventory().addItem(weapon);
        player.sendMessage(Main.getMessages().get(Message.WEAPON_RECEIVED,
                Placeholder.of("weapontype", type), Placeholder.of("durability", dura)));
    }

    private void getAmmo(Player player, String string) {
        String type = string.toLowerCase();

        ItemStack bulletItem = new ItemBuilder(Material.IRON_INGOT)
                .setName(Utils.color(plugin.getConfig().getString("weapons." + type + ".ammo-name")))
                .setLore(Utils.color(plugin.getConfig().getString("ammo-lore")))
                .setNBT("mtcustom", "" + type + "_bullets")
                .toItemStack();

        player.getInventory().addItem(bulletItem);
        player.sendMessage(Main.getMessages().get(Message.AMMO_RECEIVED, Placeholder.of("weapontype", type)));
    }
}
