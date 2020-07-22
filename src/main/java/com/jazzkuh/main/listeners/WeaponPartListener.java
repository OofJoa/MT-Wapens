package com.jazzkuh.main.listeners;

import com.jazzkuh.main.Main;
import com.jazzkuh.main.utility.ItemBuilder;
import com.jazzkuh.main.utility.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public class WeaponPartListener implements Listener {
    Main plugin;

    public WeaponPartListener(Main plugin) {
        this.plugin = plugin;
    }

    private static ItemStack weaponPart(String string, String string2) {

        String weaponType = string.toLowerCase();
        String weaponTypePart = string2.toLowerCase();

        ArrayList<String> Lore = new ArrayList<String>();
        Lore.add("MT WAPEN PART MENU ITEM");


        ItemStack weaponPart = new ItemBuilder(Material.IRON_INGOT)
                .setName(Utils.color("&8" + weaponType + " " + weaponTypePart))
                .setNBT("mtcustom", weaponType + "_" + weaponTypePart)
                .setLore(Lore)
                .toItemStack();

        return weaponPart;
    }

    public static void weaponPartMenu(Plugin plugin, Player player) {
        Inventory menu = Bukkit.getServer().createInventory(player, 9 * 5, "MT Wapen Parts Menu");

        menu.setItem(0, weaponPart("deserteagle", "frame"));
        menu.setItem(1, weaponPart("deserteagle", "magazijn"));
        menu.setItem(2, weaponPart("deserteagle", "trekker"));
        menu.setItem(3, weaponPart("deserteagle", "grip"));

        menu.setItem(9, weaponPart("magnum44", "frame"));
        menu.setItem(10, weaponPart("magnum44", "magazijn"));
        menu.setItem(11, weaponPart("magnum44", "trekker"));
        menu.setItem(12, weaponPart("magnum44", "grip"));

        menu.setItem(18, weaponPart("waltherp99", "frame"));
        menu.setItem(19, weaponPart("waltherp99", "magazijn"));
        menu.setItem(20, weaponPart("waltherp99", "trekker"));
        menu.setItem(21, weaponPart("waltherp99", "grip"));

        menu.setItem(27, weaponPart("glock19", "frame"));
        menu.setItem(28, weaponPart("glock19", "magazijn"));
        menu.setItem(29, weaponPart("glock19", "trekker"));
        menu.setItem(30, weaponPart("glock19", "grip"));

        menu.setItem(36, weaponPart("m16a4", "loop"));
        menu.setItem(37, weaponPart("m16a4", "frame"));
        menu.setItem(38, weaponPart("m16a4", "magazijn"));
        menu.setItem(39, weaponPart("m16a4", "magazijnhouder"));
        menu.setItem(40, weaponPart("m16a4", "trekker"));
        menu.setItem(41, weaponPart("m16a4", "grip"));
        menu.setItem(42, weaponPart("m16a4", "achterkant"));

        player.openInventory(menu);
    }

    @EventHandler
    public void inventoryClick(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();

        if (event.getView().getTitle().contains("MT Wapen Parts Menu")) {
            event.setCancelled(true);

            if ((event.getCurrentItem() == null) || (event.getCurrentItem().getType().equals(Material.AIR))) {
                return;
            }

            if (event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasLore() && event.getCurrentItem().getItemMeta().getLore().contains("MT WAPEN PART MENU ITEM")) {

                ArrayList<String> ClearLore = new ArrayList<String>();

                ItemStack is = event.getCurrentItem();
                ItemMeta im = is.getItemMeta();

                im.setLore(ClearLore);
                is.setItemMeta(im);

                player.getInventory().addItem(is);
                player.closeInventory();
            }
        }
    }
}
