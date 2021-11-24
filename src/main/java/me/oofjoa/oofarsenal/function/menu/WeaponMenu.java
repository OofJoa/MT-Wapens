package me.oofjoa.oofarsenal.function.menu;

import com.cryptomorin.xseries.XMaterial;
import me.oofjoa.oofarsenal.Main;
import me.oofjoa.oofarsenal.messages.Messages;
import me.oofjoa.oofarsenal.utils.ItemBuilder;
import me.oofjoa.oofarsenal.utils.Utils;
import me.oofjoa.oofarsenal.utils.menu.GUIHolder;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class WeaponMenu extends GUIHolder {
    public WeaponMenu(Player player, int page) {
        int pageSize = 9 * 5;

        ArrayList<String> weapons = new ArrayList<>(Main.getWeapons().getConfig().getConfigurationSection("weapons.").getKeys(false));
        this.inventory = Bukkit.createInventory(this, 6 * 9, Messages.MENU_WEAPON_TITLE.get());

        for (int i = 0; i < Math.min(weapons.size() - page * pageSize, pageSize); i++) {
            int index = i + page * pageSize;
            String type = weapons.get(index);

            ItemStack weapon = new ItemBuilder(XMaterial.matchXMaterial(Main.getWeapons().getConfig().getString("weapons." + type + ".material")).get().parseMaterial())
                    .setName(Utils.color("&a" + type))
                    .setNBT(Main.getWeapons().getConfig().getString("weapons." + type + ".nbt"), Main.getWeapons().getConfig().getString("weapons." + type + ".nbtvalue"))
                    .setNBT("menuItem", "true")
                    .toItemStack();

            this.inventory.setItem(i, weapon);
        }

        this.inventory.setItem(pageSize + 4, new ItemBuilder(Material.ARROW)
                .setName(Messages.MENU_SWITCHER.get()
                        .replace("<Menu>", Messages.MENU_AMMO_TITLE.get()))
                .setNBT("switcher", "true")
                .toItemStack());

        if (page > 0) {
            this.inventory.setItem(pageSize + 3, new ItemBuilder(XMaterial.OAK_SIGN.parseMaterial())
                    .setName(Messages.MENU_WEAPON_BUTTON_PAGE.get()
                            .replace("<Page>", String.valueOf(page)))
                    .toItemStack());
        }

        if (weapons.size() - page * pageSize > pageSize) {
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

            new DurabilityBuilderMenu(type, 0, DurabilityBuilderMenu.BuilderType.WEAPON).open(player);
        }

        if (NBTEditor.contains(item, "switcher")) {
            new AmmoMenu(player, 0).open(player);
        } else if (item.getType() == XMaterial.OAK_SIGN.parseMaterial()) {
            int newPage = Integer.parseInt(ChatColor.stripColor(item.getItemMeta().getDisplayName()).replaceAll("([a-zA-Z]|\\s|§\\d)+", "")) - 1;
            new WeaponMenu(player, newPage).open(player);
        }
    }
}