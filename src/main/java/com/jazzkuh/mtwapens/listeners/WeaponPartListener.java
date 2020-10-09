package com.jazzkuh.mtwapens.listeners;

import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.data.WeaponType;
import com.jazzkuh.mtwapens.utility.ItemBuilder;
import com.jazzkuh.mtwapens.utility.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class WeaponPartListener implements Listener {
    private static ItemStack weaponPart(String part) {
        ArrayList<String> lore = new ArrayList<>();
        lore.add("MT WAPEN PART MENU ITEM");

        return new ItemBuilder(Material.IRON_INGOT)
                .setName(Utils.color("&8" + part.replace("_", " ")))
                .setNBT("mtcustom", part)
                .setLore(lore)
                .toItemStack();
    }

    public static void weaponPartMenu(Player player, int page) {
        LinkedList<String> parts = new LinkedList<>();
        for (WeaponType weaponType : Main.getWeaponManager().getWeaponTypes()) {
            parts.addAll(weaponType.getParts().stream().map(part -> weaponType.getType() + "_" + part)
                    .map(String::toLowerCase).collect(Collectors.toSet()));
        }

        int size = (int) (Math.ceil(Math.min(4 * 9, parts.size()) / 9.0) * 9) + 9;
        Inventory menu = Bukkit.getServer().createInventory(player, size, "MT Wapen Parts Menu");
        menu.addItem(parts.stream().map(WeaponPartListener::weaponPart).skip(page * 36).limit(36).toArray(ItemStack[]::new));

        menu.setItem(size - 5, new ItemBuilder(Material.BARRIER).setName(ChatColor.RED + "Annuleer").toItemStack());

        if (page > 0) {
            menu.setItem(size - 6, new ItemBuilder(Material.SIGN)
                    .setName(ChatColor.GOLD + "Ga naar pagina " + page).toItemStack());
        }

        if (parts.size() - page * 36 > 36) {
            menu.setItem(size - 4, new ItemBuilder(Material.SIGN)
                    .setName(ChatColor.GOLD + "Ga naar pagina " + (page + 2)).toItemStack());
        }

        player.openInventory(menu);
    }

    @EventHandler
    public void inventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getView().getTitle().contains("MT Wapen Parts Menu")) {
            event.setCancelled(true);

            ItemStack item = event.getCurrentItem();

            if (item.getType() == Material.BARRIER) {
                player.closeInventory();
            } else if (item.getType() == Material.SIGN) {
                int newPage = Integer.parseInt(item.getItemMeta().getDisplayName().replaceAll("([a-zA-Z]|\\s|§\\d)+", "")) - 1;
                weaponPartMenu(player, newPage);
            }

            if ((event.getCurrentItem() == null) || (event.getCurrentItem().getType().equals(Material.AIR))) {
                return;
            }

            if (item.hasItemMeta() && item.getItemMeta().hasLore() && item.getItemMeta().getLore().contains("MT WAPEN PART MENU ITEM")) {
                ArrayList<String> clearLore = new ArrayList<>();
                ItemMeta im = item.getItemMeta();

                im.setLore(clearLore);
                item.setItemMeta(im);

                player.getInventory().addItem(item);
                player.closeInventory();
            }
        }
    }
}
