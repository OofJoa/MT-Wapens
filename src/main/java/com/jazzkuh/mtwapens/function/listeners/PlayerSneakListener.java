package com.jazzkuh.mtwapens.function.listeners;

import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.function.objects.Weapon;
import com.jazzkuh.mtwapens.utils.ItemBuilder;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerSneakListener implements Listener {

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        if (player.getInventory().getItemInMainHand().getType() == Material.AIR) return;
        if (player.getInventory().getItemInMainHand().getItemMeta() == null) return;
        if (!player.getInventory().getItemInMainHand().getItemMeta().hasDisplayName()) return;

        ItemStack itemStack = player.getInventory().getItemInMainHand();

        if (!NBTEditor.contains(itemStack, "mtwapens_weapon")) return;
        String weaponType = NBTEditor.getString(itemStack, "mtwapens_weapon");
        Weapon weapon = new Weapon(weaponType);

        if (!(boolean) weapon.getParameter(Weapon.WeaponParameters.SNEAKINGMODIFIESITEM)) return;

        if (event.isSneaking()) {
            ItemBuilder itemBuilder = new ItemBuilder(itemStack)
                    .setNBT(weapon.getParameter(Weapon.WeaponParameters.MODIFIED_NBT).toString(), weapon.getParameter(Weapon.WeaponParameters.MODIFIED_NBTVALUE).toString());

            int customModelData = (int) weapon.getParameter(Weapon.WeaponParameters.MODIFIED_CUSTOMMODELDATA);
            if (customModelData > 0) {
                try {
                    itemBuilder.setCustomModelData(customModelData);
                } catch (Exception e) {
                    Main.getInstance().getLogger().warning("Custom Model Data is not supported for your server version.");
                }
            }

            ItemStack is = itemBuilder.toItemStack();
            is.setType((Material) weapon.getParameter(Weapon.WeaponParameters.MODIFIED_MATERIAL));
            ItemMeta im = is.getItemMeta();
            itemStack.setItemMeta(im);
        } else {
            ItemBuilder itemBuilder = new ItemBuilder(itemStack)
                    .setNBT(weapon.getParameter(Weapon.WeaponParameters.NBT).toString(), weapon.getParameter(Weapon.WeaponParameters.NBTVALUE).toString());

            ItemStack is = itemBuilder.toItemStack();
            is.setType((Material) weapon.getParameter(Weapon.WeaponParameters.MATERIAL));
            ItemMeta im = is.getItemMeta();
            itemStack.setItemMeta(im);
        }
    }
}
