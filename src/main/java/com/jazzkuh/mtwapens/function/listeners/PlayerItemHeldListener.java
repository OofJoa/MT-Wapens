package com.jazzkuh.mtwapens.function.listeners;

import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.function.objects.Weapon;
import com.jazzkuh.mtwapens.utils.Utils;
import com.jazzkuh.mtwapens.utils.messages.DefaultMessages;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerItemHeldListener implements Listener {

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();

        if (event.getPlayer().getInventory().getItem(event.getNewSlot()) == null) return;

        ItemStack itemStack = event.getPlayer().getInventory().getItem(event.getNewSlot());

        if (!itemStack.hasItemMeta()) return;
        if (!itemStack.getItemMeta().hasDisplayName()) return;

        if (NBTEditor.contains(itemStack, "mtwapens_weapon")) return;

        for (String weaponType : Main.getWeapons().getConfig().getConfigurationSection("weapons.").getKeys(false)) {
            if (!itemStack.getItemMeta().getDisplayName().equals(
                    Utils.color(Main.getWeapons().getConfig().getString("weapons." + weaponType + ".name")))) continue;
            Utils.applyNBTTag(itemStack, "mtwapens_weapon", weaponType);
            Utils.sendMessage(player, "&eYour weapon has been adjusted to fully work with the new MT-Wapens 3.0 update.");
        }
    }

    @EventHandler
    public void onWeaponHold(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();

        if (Main.getReloadDelay().containsKey(String.valueOf(player.getUniqueId()))) {
            event.setCancelled(true);
            return;
        }

        if (event.getPlayer().getInventory().getItem(event.getNewSlot()) == null) return;

        ItemStack itemStack = event.getPlayer().getInventory().getItem(event.getNewSlot());

        if (itemStack == null) return;
        if (!NBTEditor.contains(itemStack, "mtwapens_weapon")) return;
        if (!NBTEditor.contains(itemStack, "ammo")) return;

        String weaponType = NBTEditor.getString(itemStack, "mtwapens_weapon");
        Weapon weapon = new Weapon(weaponType);

        Utils.sendMessage(player, Main.getMessages().get(DefaultMessages.AMMO_DURABILITY)
                .replace("<Durability>", String.valueOf(NBTEditor.getInt(itemStack, "durability")))
                .replace("<Ammo>", String.valueOf(NBTEditor.getInt(itemStack, "ammo")))
                .replace("<MaxAmmo>", weapon.getParameter(Weapon.WeaponParameters.MAXAMMO).toString()));
    }
}
