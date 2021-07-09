package com.jazzkuh.mtwapens.function.listeners;

import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.api.PlayerShootWeaponEvent;
import com.jazzkuh.mtwapens.function.objects.Weapon;
import com.jazzkuh.mtwapens.messages.Messages;
import com.jazzkuh.mtwapens.utils.Utils;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class WeaponFireListener implements Listener {
    private final HashMap<String, Date> weaponCooldown = new HashMap<>();

    private boolean weaponCooldown(String string) {
        if (weaponCooldown.containsKey(string)) {
            if (weaponCooldown.get(string).getTime() > new Date().getTime()) return false;

            weaponCooldown.remove(string);
        }
        return true;
    }
    
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (player.getInventory().getItemInMainHand().getType() == Material.AIR) return;
        if (player.getInventory().getItemInMainHand().getItemMeta() == null) return;
        if (!player.getInventory().getItemInMainHand().getItemMeta().hasDisplayName()) return;

        ItemStack itemStack = player.getInventory().getItemInMainHand();

        if (!NBTEditor.contains(itemStack, "mtwapens_weapon")) return;
        String weaponType = NBTEditor.getString(itemStack, "mtwapens_weapon");
        Weapon weapon = new Weapon(weaponType);

        switch (event.getAction()) {
            case RIGHT_CLICK_BLOCK:
            case RIGHT_CLICK_AIR: {
                event.setCancelled(true);

                PlayerShootWeaponEvent shootWeaponEvent = new PlayerShootWeaponEvent(player, weaponType);
                Bukkit.getServer().getPluginManager().callEvent(shootWeaponEvent);
                if (shootWeaponEvent.isCancelled()) return;

                executeWeaponFire(event, player, weapon);
                break;
            }
            case LEFT_CLICK_AIR: {
                if (!(boolean) weapon.getParameter(Weapon.WeaponParameters.SCOPE)) return;

                if (player.hasPotionEffect(PotionEffectType.SLOW)) {
                    player.removePotionEffect(PotionEffectType.SLOW);
                } else {
                    int scopeSize = Main.getInstance().getConfig().getInt("weapons." + weaponType + ".scope-size");
                    int amplifier =  scopeSize != 0 ? scopeSize : 8;
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 600, amplifier, true));
                }
                break;
            }
            default:
                break;
        }
    }

    private void executeWeaponFire(PlayerInteractEvent event, Player player, Weapon weapon) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();

        if (!NBTEditor.contains(itemStack, "mtwapens_weapon")) return;
        if (!NBTEditor.contains(itemStack, "ammo")) return;

        if (Main.getReloadDelay().containsKey(String.valueOf(player.getUniqueId()))) return;

        String cooldownId = Main.getInstance().getConfig().getBoolean("weaponCooldownPerWeapon")
                ? player.getUniqueId() + "-" + event.getPlayer().getInventory().getHeldItemSlot()
                : String.valueOf(player.getUniqueId());

        if (NBTEditor.getInt(player.getInventory().getItemInMainHand(), "durability") <= 0) {
            player.getInventory().removeItem(player.getInventory().getItemInMainHand());
            return;
        }

        ItemStack bulletItem = (ItemStack) weapon.getParameter(Weapon.WeaponParameters.AMMOITEM);

        if (NBTEditor.getInt(itemStack, "ammo") > 0) {
            if ((boolean) weapon.getParameter(Weapon.WeaponParameters.SHOOTONLYWHENSCOPED) &&
                    !player.hasPotionEffect(PotionEffectType.SLOW)) {
                Utils.sendMessage(player, Messages.WEAPON_CANT_SHOOT_WIHTOUT_SCOPE.get());
                return;
            }

            if (!weaponCooldown(cooldownId)) return;
            weaponCooldown.put(cooldownId, new Date(new Date().getTime() + (long) ((double) weapon.getParameter(Weapon.WeaponParameters.ATTACKSPEED))));

            Utils.applyNBTTag(itemStack, "ammo", NBTEditor.getInt(itemStack, "ammo") - 1);

            if (!(boolean) weapon.getParameter(Weapon.WeaponParameters.DISABLEDURABILITY)) {
                Utils.applyNBTTag(itemStack, "durability", NBTEditor.getInt(itemStack, "durability") - 1);
            }

            updateWeaponLore(itemStack, weapon);

            Utils.sendMessage(player, Messages.AMMO_DURABILITY.get()
                    .replace("<Durability>", String.valueOf(NBTEditor.getInt(itemStack, "durability")))
                    .replace("<Ammo>", String.valueOf(NBTEditor.getInt(itemStack, "ammo")))
                    .replace("<MaxAmmo>", weapon.getParameter(Weapon.WeaponParameters.MAXAMMO).toString()));

            Snowball bullet = player.launchProjectile(Snowball.class);
            bullet.setCustomName("" + (double) weapon.getParameter(Weapon.WeaponParameters.DAMAGE));
            bullet.setShooter(player);
            bullet.setVelocity(bullet.getVelocity().multiply(2D));
            bullet.setMetadata("mtwapens_bullet", new FixedMetadataValue(Main.getInstance(), true));

            for (Player target : player.getLocation().getWorld().getPlayers()) {
                if (target.getLocation().distance(player.getLocation()) <= 16D) {
                    target.playSound(player.getLocation(), weapon.getParameter(Weapon.WeaponParameters.SOUND).toString(), 100, 1F);
                }
            }
        } else if (player.getInventory().containsAtLeast(bulletItem, 1) && NBTEditor.getInt(itemStack, "ammo") <= 0) {
            for (Player target : player.getLocation().getWorld().getPlayers()) {
                if (target.getLocation().distance(player.getLocation()) <= 16D) {
                    target.playSound(player.getLocation(), weapon.getParameter(Weapon.WeaponParameters.RELOADSOUND).toString(), 100, 1F);
                }
            }

            player.sendTitle(Messages.RELOADING_TITLE.get(), Messages.RELOADING_SUBTITLE.get(), 10, 20, 10);
            Utils.sendMessage(player, Messages.RELOADING.get());
            player.getInventory().removeItem(bulletItem);

            Main.getReloadDelay().put(player.getUniqueId().toString(), true);
            weaponCooldown.put(cooldownId, new Date(new Date().getTime() + (long) ((double) weapon.getParameter(Weapon.WeaponParameters.ATTACKSPEED))));

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
