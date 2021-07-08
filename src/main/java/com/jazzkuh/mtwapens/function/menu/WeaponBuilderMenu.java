package com.jazzkuh.mtwapens.function.menu;

import com.cryptomorin.xseries.XMaterial;
import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.commands.WeaponCMD;
import com.jazzkuh.mtwapens.function.WeaponFactory;
import com.jazzkuh.mtwapens.function.objects.Weapon;
import com.jazzkuh.mtwapens.utils.ItemBuilder;
import com.jazzkuh.mtwapens.utils.menu.GUIHolder;
import com.jazzkuh.mtwapens.utils.messages.DefaultMessages;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class WeaponBuilderMenu extends GUIHolder {
    String type;
    int durability;

    public WeaponBuilderMenu(String type, int durability) {
        this.type = type;
        this.durability = durability;
        this.inventory = Bukkit.createInventory(this, 9 * 5, Main.getMessages().get(DefaultMessages.MENU_DURABILITY_TITLE)
                .replace("<WeaponType>", type));

        if (this.durability >= 1) {
            this.inventory.setItem(11, woolItem(true, 1, XMaterial.RED_WOOL.parseMaterial()));
            this.inventory.setItem(20, woolItem(true, 5, XMaterial.RED_WOOL.parseMaterial()));
            this.inventory.setItem(29, woolItem(true, 10, XMaterial.RED_WOOL.parseMaterial()));
        }

        this.inventory.setItem(22,
                new ItemBuilder(XMaterial.CRAFTING_TABLE.parseMaterial())
                        .setName(Main.getMessages().get(DefaultMessages.MENU_DURABILITY_CRAFT)
                                .replace("<Durability>", String.valueOf(this.durability)))

                        .setNBT("craftweapon", "true")
                        .toItemStack());

        this.inventory.setItem(15, woolItem(false, 1, XMaterial.LIME_WOOL.parseMaterial()));
        this.inventory.setItem(24, woolItem(false, 5, XMaterial.LIME_WOOL.parseMaterial()));
        this.inventory.setItem(33, woolItem(false, 10, XMaterial.LIME_WOOL.parseMaterial()));
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public int getDurability() {
        return this.durability;
    }

    public String getType() {
        return this.type;
    }

    private ItemStack woolItem(boolean negative, int durability, Material material) {
        String identifier = "+";
        String message = "MENU_DURABILITY_ADD";

        if (negative) {
            identifier = "-";
            message = "MENU_DURABILITY_REMOVE";
        }

        return new ItemBuilder(material)
                .setName(Main.getMessages().get(DefaultMessages.valueOf(message))
                        .replace("<Identifier>", identifier)
                        .replace("<Durability>", String.valueOf(durability)))

                .setNBT("durability", String.valueOf(durability))
                .setNBT("identifier", String.valueOf(negative))
                .toItemStack();
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        event.setCancelled(true);

        if (event.getCurrentItem() == null) return;
        if (event.getCurrentItem().getType() == Material.AIR) return;

        ItemStack item = event.getCurrentItem();
        String type = ((WeaponBuilderMenu) inventory.getHolder()).getType();

        if (NBTEditor.contains(item, "durability") && NBTEditor.contains(item, "identifier") && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            if (NBTEditor.getString(item, "identifier").equalsIgnoreCase("false")) {
                int durability = ((WeaponBuilderMenu) inventory.getHolder()).getDurability() + Integer.parseInt(NBTEditor.getString(item, "durability"));

                new WeaponBuilderMenu(type, durability).open(player);
            } else if (NBTEditor.getString(item, "identifier").equalsIgnoreCase("true")) {
                int durability = ((WeaponBuilderMenu) inventory.getHolder()).getDurability() - Integer.parseInt(NBTEditor.getString(item, "durability"));

                if (durability >= 0) {
                    new WeaponBuilderMenu(type, durability).open(player);
                }
            }
        } else {
            if (NBTEditor.contains(item, "craftweapon") && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                if (event.getSlot() == 22 && item.getType().equals(XMaterial.CRAFTING_TABLE.parseMaterial())) {
                    int durability = ((WeaponBuilderMenu) inventory.getHolder()).getDurability();
                    Weapon weapon = new Weapon(type);
                    new WeaponFactory(player).buildWeapon(weapon, durability);
                    player.closeInventory();
                }
            }
        }
    }
}