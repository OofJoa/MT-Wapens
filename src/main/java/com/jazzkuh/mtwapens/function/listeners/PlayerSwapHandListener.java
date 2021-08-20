package com.jazzkuh.mtwapens.function.listeners;

import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.function.WeaponFactory;
import com.jazzkuh.mtwapens.function.objects.Ammo;
import com.jazzkuh.mtwapens.function.objects.Weapon;
import com.jazzkuh.mtwapens.messages.Messages;
import com.jazzkuh.mtwapens.utils.Utils;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PlayerSwapHandListener implements Listener {

    @EventHandler
    public void onPlayerSwapHand(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();

        ItemStack itemStack = player.getInventory().getItemInMainHand();

        if (!NBTEditor.contains(itemStack, "mtwapens_weapon")) return;
        if (!NBTEditor.contains(itemStack, "ammo")) return;

        event.setCancelled(true);

        if (Main.getReloadDelay().containsKey(String.valueOf(player.getUniqueId()))) return;

        if (NBTEditor.getInt(player.getInventory().getItemInMainHand(), "durability") <= 0) {
            player.getInventory().removeItem(player.getInventory().getItemInMainHand());
            return;
        }

        if (!NBTEditor.contains(itemStack, "mtwapens_weapon")) return;
        String weaponType = NBTEditor.getString(itemStack, "mtwapens_weapon");

        if (Main.getWeapons().getConfig().getString("weapons." + weaponType + ".name") == null) {
            player.getInventory().removeItem(player.getInventory().getItemInMainHand());
            Utils.sendMessage(player, "&cYour weapon has been removed from the config files and has therefore been destroyed.");
            return;
        }

        Weapon weapon = new Weapon(weaponType);

        ItemStack bulletItem = null;
        if (weapon.isUsingAmmo()) {
            WeaponFactory weaponFactory = new WeaponFactory(player);
            weaponFactory.buildAmmo(new Ammo(weapon.getParameter(Weapon.WeaponParameters.AMMOTYPE).toString()));

            bulletItem = weaponFactory.getItemStack();
        }

        if (NBTEditor.getInt(itemStack, "ammo") >= (int) weapon.getParameter(Weapon.WeaponParameters.MAXAMMO)) return;

        if (player.getInventory().containsAtLeast(bulletItem, 1)) {
            for (Player target : player.getLocation().getWorld().getPlayers()) {
                if (target.getLocation().distance(player.getLocation()) <= 16D) {
                    target.playSound(player.getLocation(), weapon.getParameter(Weapon.WeaponParameters.RELOADSOUND).toString(), 100, 1F);
                }
            }

            player.sendTitle(Messages.RELOADING_TITLE.get(), Messages.RELOADING_SUBTITLE.get(), 10, 20, 10);
            Utils.sendMessage(player, Messages.RELOADING.get());
            player.getInventory().removeItem(bulletItem);

            Main.getReloadDelay().put(player.getUniqueId().toString(), true);

            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                Utils.applyNBTTag(itemStack, "ammo", weapon.getParameter(Weapon.WeaponParameters.MAXAMMO));
                updateWeaponLore(itemStack, weapon);

                Utils.sendMessage(player, Messages.AMMO_DURABILITY.get()
                        .replace("<Durability>", String.valueOf(NBTEditor.getInt(itemStack, "durability")))
                        .replace("<Ammo>", String.valueOf(NBTEditor.getInt(itemStack, "ammo")))
                        .replace("<MaxAmmo>", weapon.getParameter(Weapon.WeaponParameters.MAXAMMO).toString()));

                Main.getReloadDelay().remove(String.valueOf(player.getUniqueId()));
            }, 35);
        } else {
            Utils.sendMessage(player, Messages.NO_AMMO.get());
            player.playSound(player.getLocation(), Main.getInstance().getConfig().getString("empty-sound"), 100, 1F);
        }
    }

    private void updateWeaponLore(ItemStack itemStack, Weapon weapon) {
        ItemMeta im = itemStack.getItemMeta();
        ArrayList<String> weaponLore = new ArrayList<>();
        for (String string : (List<String>) weapon.getParameter(Weapon.WeaponParameters.LORE)) {
            string = string.replace("<Ammo>", String.valueOf(NBTEditor.getInt(itemStack, "ammo")))
                    .replace("<MaxAmmo>", weapon.getParameter(Weapon.WeaponParameters.MAXAMMO).toString())
                    .replace("<Damage>", weapon.getParameter(Weapon.WeaponParameters.DAMAGE).toString())
                    .replace("<Durability>", String.valueOf(NBTEditor.getInt(itemStack, "durability")));

            weaponLore.add(string);
        }
        im.setLore(weaponLore);
        itemStack.setItemMeta(im);
    }
}
