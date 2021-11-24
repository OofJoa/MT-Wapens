package me.oofjoa.oofarsenal.function.listeners;

import me.oofjoa.oofarsenal.Main;
import me.oofjoa.oofarsenal.api.PlayerShootWeaponEvent;
import me.oofjoa.oofarsenal.api.PrePlayerShootWeaponEvent;
import me.oofjoa.oofarsenal.function.RecoilUtils;
import me.oofjoa.oofarsenal.function.WeaponFactory;
import me.oofjoa.oofarsenal.function.enums.Recoil;
import me.oofjoa.oofarsenal.function.objects.Ammo;
import me.oofjoa.oofarsenal.function.objects.Weapon;
import me.oofjoa.oofarsenal.messages.Messages;
import me.oofjoa.oofarsenal.utils.Utils;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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

        if (event.getHand() == EquipmentSlot.OFF_HAND) return;

        if (player.getInventory().getItemInMainHand().getType() == Material.AIR) return;
        if (player.getInventory().getItemInMainHand().getItemMeta() == null) return;
        if (!player.getInventory().getItemInMainHand().getItemMeta().hasDisplayName()) return;

        ItemStack itemStack = player.getInventory().getItemInMainHand();

        if (!NBTEditor.contains(itemStack, "mtwapens_weapon")) return;
        String weaponType = NBTEditor.getString(itemStack, "mtwapens_weapon");

        if (Main.getWeapons().getConfig().getString("weapons." + weaponType + ".name") == null) {
            player.getInventory().removeItem(player.getInventory().getItemInMainHand());
            Utils.sendMessage(player, "&cYour weapon has been removed from the database and has therefore been destroyed.");
            return;
        }

        Weapon weapon = new Weapon(weaponType);

        switch (event.getAction()) {
            case RIGHT_CLICK_BLOCK:
            case RIGHT_CLICK_AIR: {
                event.setCancelled(true);

                PrePlayerShootWeaponEvent prePlayerShootWeaponEvent = new PrePlayerShootWeaponEvent(player, weapon);
                Bukkit.getServer().getPluginManager().callEvent(prePlayerShootWeaponEvent);
                if (prePlayerShootWeaponEvent.isCancelled()) return;

                executeWeaponFire(event, player, weapon);
                break;
            }
            case LEFT_CLICK_AIR: {
                if (!(Weapon.WeaponTypes.valueOf(weapon.getParameter(Weapon.WeaponParameters.TYPE).toString()) == Weapon.WeaponTypes.SNIPER)) return;

                if (player.hasPotionEffect(PotionEffectType.SLOW)) {
                    player.removePotionEffect(PotionEffectType.SLOW);
                } else {
                    int scopeSize = Main.getInstance().getConfig().getInt("weapons." + weaponType + ".scope-size");
                    int amplifier =  scopeSize != 0 ? scopeSize : 8;
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1000000, amplifier, true));
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

        if (Main.getReloadDelay().containsKey(player.getUniqueId())) return;

        String cooldownId = Main.getInstance().getConfig().getBoolean("weaponCooldownPerWeapon")
                ? player.getUniqueId() + "-" + event.getPlayer().getInventory().getHeldItemSlot()
                : String.valueOf(player.getUniqueId());

        if (NBTEditor.getInt(player.getInventory().getItemInMainHand(), "durability") <= 0) {
            player.getInventory().removeItem(player.getInventory().getItemInMainHand());
            return;
        }

        ItemStack bulletItem = null;
        if (weapon.isUsingAmmo()) {
            WeaponFactory weaponFactory = new WeaponFactory(player);
            weaponFactory.buildAmmo(new Ammo(weapon.getParameter(Weapon.WeaponParameters.AMMOTYPE).toString()));

            bulletItem = weaponFactory.getItemStack();
        }

        if (NBTEditor.getInt(itemStack, "ammo") > 0 || !weapon.isUsingAmmo()) {
            PlayerShootWeaponEvent shootWeaponEvent = new PlayerShootWeaponEvent(player, weapon);
            Bukkit.getServer().getPluginManager().callEvent(shootWeaponEvent);
            if (shootWeaponEvent.isCancelled()) return;

            if (Weapon.WeaponTypes.valueOf(weapon.getParameter(Weapon.WeaponParameters.TYPE).toString()) == Weapon.WeaponTypes.SNIPER &&
                    !player.hasPotionEffect(PotionEffectType.SLOW)) {
                Utils.sendMessage(player, Messages.WEAPON_CANT_SHOOT_WIHTOUT_SCOPE.get());
                return;
            }

            if (!weaponCooldown(cooldownId)) return;
            weaponCooldown.put(cooldownId, new Date(new Date().getTime() + (long) ((double) weapon.getParameter(Weapon.WeaponParameters.ATTACKSPEED))));

            if (!(boolean) weapon.getParameter(Weapon.WeaponParameters.DISABLEDURABILITY)) {
                Utils.applyNBTTag(itemStack, "durability", NBTEditor.getInt(itemStack, "durability") - 1);
            }

            if (weapon.isUsingAmmo()) {
                Utils.applyNBTTag(itemStack, "ammo", NBTEditor.getInt(itemStack, "ammo") - 1);
            }

            updateWeaponLore(itemStack, weapon);

            String holdingMessage = weapon.isUsingAmmo() ? Messages.AMMO_DURABILITY.get() : Messages.USES.get();
            Utils.sendMessage(player, holdingMessage
                    .replace("<Uses>", String.valueOf(NBTEditor.getInt(itemStack, "durability")))
                    .replace("<Durability>", String.valueOf(NBTEditor.getInt(itemStack, "durability")))
                    .replace("<Ammo>", String.valueOf(NBTEditor.getInt(itemStack, "ammo")))
                    .replace("<MaxAmmo>", weapon.getParameter(Weapon.WeaponParameters.MAXAMMO).toString()));

            Weapon.WeaponTypes weaponType = Weapon.WeaponTypes.valueOf(weapon.getParameter(Weapon.WeaponParameters.TYPE).toString());

            if (weapon.getParameter(Weapon.WeaponParameters.RECOIL) != null) {
                new RecoilUtils(weapon, (Recoil) weapon.getParameter(Weapon.WeaponParameters.RECOIL)).performRecoil(player);
            }
            new WeaponProjectile(weapon, weaponType).fireProjectile(player);

            Weapon.WeaponTypes weaponTypes = Weapon.WeaponTypes.valueOf(weapon.getParameter(Weapon.WeaponParameters.TYPE).toString());
            if (weaponTypes == Weapon.WeaponTypes.MULTIPLE_BULLET) {
                int iterations = (int) weapon.getParameter(Weapon.WeaponParameters.ITERATIONS) - 1;
                for (int i = 0; i < iterations; i++) {
                    Bukkit.getScheduler().runTaskLater(Main.getInstance(),
                            () -> new WeaponProjectile(weapon, weaponTypes).fireProjectile(player), 8L * (i + 1));
                }
            }

            if (player.hasPotionEffect(PotionEffectType.SLOW)) {
                player.removePotionEffect(PotionEffectType.SLOW);
            }

            if (NBTEditor.getInt(player.getInventory().getItemInMainHand(), "durability") <= 0) {
                player.getInventory().removeItem(player.getInventory().getItemInMainHand());
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

            Main.getReloadDelay().put(player.getUniqueId(), true);
            weaponCooldown.put(cooldownId, new Date(new Date().getTime() + (long) ((double) weapon.getParameter(Weapon.WeaponParameters.ATTACKSPEED))));

            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                Utils.applyNBTTag(itemStack, "ammo", weapon.getParameter(Weapon.WeaponParameters.MAXAMMO));
                updateWeaponLore(itemStack, weapon);

                Utils.sendMessage(player, Messages.AMMO_DURABILITY.get()
                        .replace("<Durability>", String.valueOf(NBTEditor.getInt(itemStack, "durability")))
                        .replace("<Ammo>", String.valueOf(NBTEditor.getInt(itemStack, "ammo")))
                        .replace("<MaxAmmo>", weapon.getParameter(Weapon.WeaponParameters.MAXAMMO).toString()));

                Main.getReloadDelay().remove(player.getUniqueId());
            }, 35);
        } else {
            Utils.sendMessage(player, Messages.NO_AMMO.get());
            player.playSound(player.getLocation(), Main.getInstance().getConfig().getString("empty-sound"), 100, 1F);
        }
    }

    @SuppressWarnings("unchecked")
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
