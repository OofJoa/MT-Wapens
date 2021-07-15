package com.jazzkuh.mtwapens.function.menu;

import com.cryptomorin.xseries.XMaterial;
import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.messages.Messages;
import com.jazzkuh.mtwapens.utils.ItemBuilder;
import com.jazzkuh.mtwapens.utils.Utils;
import com.jazzkuh.mtwapens.utils.menu.GUIHolder;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class GrenadeMenu extends GUIHolder {
    public GrenadeMenu(Player player, int page) {
        int pageSize = 9 * 5;

        ArrayList<String> grenades = new ArrayList<>(Main.getGrenades().getConfig().getConfigurationSection("grenades.").getKeys(false));
        this.inventory = Bukkit.createInventory(this, 6 * 9, Messages.MENU_GRENADE_TITLE.get());

        for (int i = 0; i < Math.min(grenades.size() - page * pageSize, pageSize); i++) {
            int index = i + page * pageSize;
            String type = grenades.get(index);

            ItemStack weapon = new ItemBuilder(XMaterial.matchXMaterial(Main.getGrenades().getConfig().getString("grenades." + type + ".material")).get().parseMaterial())
                    .setName(Utils.color("&a" + type))
                    .setNBT(Main.getGrenades().getConfig().getString("grenades." + type + ".nbt"), Main.getGrenades().getConfig().getString("grenades." + type + ".nbtvalue"))
                    .setNBT("menuItem", "true")
                    .toItemStack();

            this.inventory.setItem(i, weapon);
        }

        this.inventory.setItem(pageSize + 4, new ItemBuilder(Material.ARROW)
                .setName(Messages.MENU_SWITCHER.get()
                        .replace("<Menu>", Messages.MENU_WEAPON_TITLE.get()))
                .setNBT("switcher", "true")
                .toItemStack());

        if (page > 0) {
            this.inventory.setItem(pageSize + 3, new ItemBuilder(XMaterial.OAK_SIGN.parseMaterial())
                    .setName(Messages.MENU_WEAPON_BUTTON_PAGE.get()
                            .replace("<Page>", String.valueOf(page)))
                    .toItemStack());
        }

        if (grenades.size() - page * pageSize > pageSize) {
            this.inventory.setItem(pageSize + 5, new ItemBuilder(XMaterial.OAK_SIGN.parseMaterial())
                    .setName(Messages.MENU_WEAPON_BUTTON_PAGE.get()
                            .replace("<Page>", String.valueOf((page + 2))))
                    .toItemStack());
        }
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        event.setCancelled(true);

        if (event.getCurrentItem() == null) return;
        if (event.getCurrentItem().getType() == Material.AIR) return;

        ItemStack item = event.getCurrentItem();

        if (NBTEditor.contains(item, "menuItem") && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            String type = ChatColor.stripColor(item.getItemMeta().getDisplayName());

            new DurabilityBuilderMenu(type, 0, DurabilityBuilderMenu.BuilderType.GRENADE).open(player);
        }

        if (NBTEditor.contains(item, "switcher")) {
            new WeaponMenu(player, 0).open(player);
        } else if (item.getType() == XMaterial.OAK_SIGN.parseMaterial()) {
            int newPage = Integer.parseInt(ChatColor.stripColor(item.getItemMeta().getDisplayName()).replaceAll("([a-zA-Z]|\\s|§\\d)+", "")) - 1;
            new GrenadeMenu(player, newPage).open(player);
        }
    }
}