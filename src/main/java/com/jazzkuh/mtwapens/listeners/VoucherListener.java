package com.jazzkuh.mtwapens.listeners;

import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.data.WeaponData;
import com.jazzkuh.mtwapens.utility.ItemBuilder;
import com.jazzkuh.mtwapens.utility.Utils;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class VoucherListener implements Listener {
    Main plugin;

    static WeaponData weaponData = WeaponData.getInstance();

    public VoucherListener(Main plugin) {
        this.plugin = plugin;
    }

    private ItemStack voucherItem(int dura, String string) {

        String type = string.toLowerCase();

        ArrayList<String> Lore = new ArrayList<String>();
        Lore.add(Utils.color("&8&m-----------------"));
        Lore.add(Utils.color("&7Gebruik &6rechterklik&7 op deze voucher om"));
        Lore.add(Utils.color("&7hem te verzilveren!"));
        Lore.add(Utils.color("&f"));
        Lore.add(Utils.color("&7Deze &6voucher&7 is houdbaar tot:"));
        Lore.add(Utils.color("&6permanent"));
        Lore.add(Utils.color("&8&m-----------------"));


        ItemStack voucher = new ItemBuilder(Material.PAPER)
                .setName(Utils.color("&6Voucher " + type + " " + dura + " Durability"))
                .setNBT("VOUCHER", "true")
                .setLore(Lore)
                .toItemStack();

        return voucher;
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {

        Player player = event.getPlayer();

        if (!(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR))
            return;

        if (!(NBTEditor.contains(player.getInventory().getItemInMainHand(), "VOUCHER")))
            return;

        if (player.getInventory().getItemInMainHand() != null && player.getInventory().getItemInMainHand().getItemMeta() != null && player.getInventory().getItemInMainHand().getItemMeta().getDisplayName() != null) {
            String string = player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().replace("&6Voucher ", "").replace(" Durability", "");
            String[] parts = string.split(" ");
            String type = parts[1];
            int durability = Integer.parseInt(parts[2]);

            if (player.getInventory().containsAtLeast(voucherItem(durability, type), 1)) {
                player.getInventory().removeItem(voucherItem(durability, type));
                getWeapon(player, durability, type);
            }
        }

    }

    private void getWeapon(Player player, int dura, String type) {
        int UUID = Utils.randomValue(999999999, 10000);

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
        player.sendMessage(Utils.color("&6Je hebt succesvol het wapen &c" + type + "&6 (&c" + dura + " Durability&6) ontvangen."));
    }
}
