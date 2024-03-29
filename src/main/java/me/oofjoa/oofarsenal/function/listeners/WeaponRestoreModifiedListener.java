package me.oofjoa.oofarsenal.function.listeners;

import me.oofjoa.oofarsenal.Main;
import me.oofjoa.oofarsenal.function.objects.Weapon;
import me.oofjoa.oofarsenal.utils.ItemBuilder;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

public class WeaponRestoreModifiedListener implements Listener {

    @EventHandler
    public void onItemHeldAfterModify(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();

        if (player.getInventory().getItem(event.getPreviousSlot()) == null) return;

        ItemStack itemStack = player.getInventory().getItem(event.getPreviousSlot());
        if (itemStack == null) return;

        if (!NBTEditor.contains(itemStack, "mtwapens_weapon")) return;

        String weaponType = NBTEditor.getString(itemStack, "mtwapens_weapon");
        Weapon weapon = new Weapon(weaponType);

        if (Weapon.WeaponTypes.valueOf(weapon.getParameter(Weapon.WeaponParameters.TYPE).toString()) == Weapon.WeaponTypes.SNIPER) {
            if (player.hasPotionEffect(PotionEffectType.SLOW)) {
                player.removePotionEffect(PotionEffectType.SLOW);
            }
        }

        if ((boolean) weapon.getParameter(Weapon.WeaponParameters.SNEAKINGMODIFIESITEM)) {
            restoreWeapon(weapon, itemStack);
        }
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        ItemStack itemStack = event.getItemDrop().getItemStack();

        if (!NBTEditor.contains(itemStack, "mtwapens_weapon")) return;

        String weaponType = NBTEditor.getString(itemStack, "mtwapens_weapon");
        Weapon weapon = new Weapon(weaponType);

        if (Weapon.WeaponTypes.valueOf(weapon.getParameter(Weapon.WeaponParameters.TYPE).toString()) == Weapon.WeaponTypes.SNIPER) {
            if (player.hasPotionEffect(PotionEffectType.SLOW)) {
                player.removePotionEffect(PotionEffectType.SLOW);
            }
        }

        if ((boolean) weapon.getParameter(Weapon.WeaponParameters.SNEAKINGMODIFIESITEM)) {
            restoreWeapon(weapon, itemStack);
        }
    }

    @EventHandler
    public void onOffhandSwitch(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();

        ItemStack itemStack = event.getOffHandItem();
        if (itemStack == null) return;

        if (!NBTEditor.contains(itemStack, "mtwapens_weapon")) return;

        String weaponType = NBTEditor.getString(itemStack, "mtwapens_weapon");
        Weapon weapon = new Weapon(weaponType);

        if (Weapon.WeaponTypes.valueOf(weapon.getParameter(Weapon.WeaponParameters.TYPE).toString()) == Weapon.WeaponTypes.SNIPER) {
            if (player.hasPotionEffect(PotionEffectType.SLOW)) {
                player.removePotionEffect(PotionEffectType.SLOW);
            }
        }

        if ((boolean) weapon.getParameter(Weapon.WeaponParameters.SNEAKINGMODIFIESITEM)) {
            restoreWeapon(weapon, itemStack);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null) return;

        if (!NBTEditor.contains(itemStack, "mtwapens_weapon")) return;

        String weaponType = NBTEditor.getString(itemStack, "mtwapens_weapon");
        Weapon weapon = new Weapon(weaponType);

        if (Weapon.WeaponTypes.valueOf(weapon.getParameter(Weapon.WeaponParameters.TYPE).toString()) == Weapon.WeaponTypes.SNIPER) {
            if (player.hasPotionEffect(PotionEffectType.SLOW)) {
                player.removePotionEffect(PotionEffectType.SLOW);
            }
        }

        if ((boolean) weapon.getParameter(Weapon.WeaponParameters.SNEAKINGMODIFIESITEM)) {
            restoreWeapon(weapon, itemStack);
        }
    }

    private void restoreWeapon(Weapon weapon, ItemStack itemStack) {
        ItemBuilder itemBuilder = new ItemBuilder(itemStack)
                .setNBT(weapon.getParameter(Weapon.WeaponParameters.NBT).toString(), weapon.getParameter(Weapon.WeaponParameters.NBTVALUE).toString());

        int customModelData = (int) weapon.getParameter(Weapon.WeaponParameters.CUSTOMMODELDATA);
        if (customModelData > 0) {
            try {
                itemBuilder.setCustomModelData(customModelData);
            } catch (Exception e) {
                Main.getInstance().getLogger().warning("Custom Model Data is not supported for your server version.");
            }
        }

        ItemStack is = itemBuilder.toItemStack();
        is.setType((Material) weapon.getParameter(Weapon.WeaponParameters.MATERIAL));
        ItemMeta im = is.getItemMeta();
        itemStack.setItemMeta(im);
    }
}
