/*
 *     MT-Wapens
 *     Copyright © 2021 Jazzkuh. All rights reserved.
 *
 *     “Commons Clause” License Condition v1.0
 *
 *    The Software is provided to you by the Licensor under the License, as defined below, subject to the following condition.
 *
 *     Without limiting other conditions in the License, the grant of rights under the License will not include, and the License does not grant to you, the right to Sell the Software.
 *
 *     For purposes of the foregoing, “Sell” means practicing any or all of the  rights granted to you under the License to provide to third parties, for a fee  or other consideration (including without limitation fees for hosting or  consulting/ support services related to the Software), a product or service  whose value derives, entirely or substantially, from the functionality of the  Software. Any license notice or attribution required by the License must also  include this Commons Clause License Condition notice.
 *
 *     Software: MT-Wapens
 *     License: GNU-LGPL v2.1 with Commons Clause
 *     Licensor: [Jazzkuh](https://github.com/Jazzkuh)
 *
 *     This library is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU Lesser General Public
 *     License as published by the Free Software Foundation; either
 *     version 2.1 of the License, or (at your option) any later version.
 *
 *     This library is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *     Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public
 *     License along with this library; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 *     USA
 */

package com.jazzkuh.mtwapens.function.listeners;

import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.api.PlayerShootWeaponEvent;
import com.jazzkuh.mtwapens.api.PrePlayerShootWeaponEvent;
import com.jazzkuh.mtwapens.function.RecoilUtils;
import com.jazzkuh.mtwapens.function.WeaponFactory;
import com.jazzkuh.mtwapens.function.enums.Recoil;
import com.jazzkuh.mtwapens.function.enums.ShowDurability;
import com.jazzkuh.mtwapens.function.objects.Ammo;
import com.jazzkuh.mtwapens.function.objects.Weapon;
import com.jazzkuh.mtwapens.messages.Messages;
import com.jazzkuh.mtwapens.utils.Utils;
import de.slikey.effectlib.effect.ParticleEffect;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

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
            Utils.sendMessage(player, "&cYour weapon has been removed from the config files and has therefore been destroyed.");
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

            updateWeaponMeta(itemStack, weapon);

            String showDurability = Main.getInstance().getConfig().getString("showDurability");
            if (NBTEditor.getInt(itemStack, "ammo") < 1) {
                Utils.sendMessage(player, Messages.SHOT_LAST_BULLET.get());
            } else if (ShowDurability.getInstance().isDurabilityShown(showDurability) == ShowDurability.Options.SHOOT || ShowDurability.getInstance().isDurabilityShown(showDurability) == ShowDurability.Options.BOTH) {
                String holdingMessage = weapon.isUsingAmmo() ? Messages.AMMO_DURABILITY.get() : Messages.DURABILITY.get();
                Utils.sendMessage(player, holdingMessage
                        .replace("<Durability>", String.valueOf(NBTEditor.getInt(itemStack, "durability")))
                        .replace("<Durability>", String.valueOf(NBTEditor.getInt(itemStack, "durability")))
                        .replace("<Ammo>", String.valueOf(NBTEditor.getInt(itemStack, "ammo")))
                        .replace("<MaxAmmo>", weapon.getParameter(Weapon.WeaponParameters.MAXAMMO).toString()));
            }

            Weapon.WeaponTypes weaponType = Weapon.WeaponTypes.valueOf(weapon.getParameter(Weapon.WeaponParameters.TYPE).toString());

            if (weapon.getParameter(Weapon.WeaponParameters.RECOIL) != null) {
                new RecoilUtils(weapon, (Recoil) weapon.getParameter(Weapon.WeaponParameters.RECOIL)).performRecoil(player);
            }

            ParticleEffect particleEffect = new ParticleEffect(Main.getEffectManager());
            particleEffect.particle = Particle.FLAME;
            particleEffect.particleSize = 1;
            particleEffect.particleCount = 8;
            particleEffect.iterations = 1;
            particleEffect.particleOffsetX = 0.3F;
            particleEffect.particleOffsetY = 0.3F;
            particleEffect.particleOffsetZ = 0.3F;
            particleEffect.setLocation(Utils.getRightSide(player.getEyeLocation(), 0.55).subtract(0, .5, 0));
            particleEffect.start();

            new WeaponProjectile(weapon, weaponType).fireProjectile(player);

            Weapon.WeaponTypes weaponTypes = Weapon.WeaponTypes.valueOf(weapon.getParameter(Weapon.WeaponParameters.TYPE).toString());
            if (weaponTypes == Weapon.WeaponTypes.MULTIPLE_BULLET) {
                int iterations = (int) weapon.getParameter(Weapon.WeaponParameters.ITERATIONS) - 1;
                for (int i = 0; i < iterations; i++) {
                    Bukkit.getScheduler().runTaskLater(Main.getInstance(),
                            () -> {
                                if (NBTEditor.getInt(itemStack, "ammo") < 1) return;
                                Utils.applyNBTTag(itemStack, "ammo", NBTEditor.getInt(itemStack, "ammo") - 1);
                                new WeaponProjectile(weapon, weaponTypes).fireProjectile(player);
                            }, 8L * (i + 1));
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

            Utils.sendMessage(player, Messages.RELOADING_START.get());
            player.getInventory().removeItem(bulletItem);

            Main.getReloadDelay().put(player.getUniqueId(), true);
            weaponCooldown.put(cooldownId, new Date(new Date().getTime() + (long) ((double) weapon.getParameter(Weapon.WeaponParameters.ATTACKSPEED))));

            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                Utils.applyNBTTag(itemStack, "ammo", weapon.getParameter(Weapon.WeaponParameters.MAXAMMO));
                updateWeaponMeta(itemStack, weapon);

                String showDurability = Main.getInstance().getConfig().getString("showDurability");
                if (ShowDurability.getInstance().isDurabilityShown(showDurability) == ShowDurability.Options.SHOOT || ShowDurability.getInstance().isDurabilityShown(showDurability) == ShowDurability.Options.BOTH) {
                    Utils.sendMessage(player, Messages.AMMO_DURABILITY.get()
                            .replace("<Durability>", String.valueOf(NBTEditor.getInt(itemStack, "durability")))
                            .replace("<Ammo>", String.valueOf(NBTEditor.getInt(itemStack, "ammo")))
                            .replace("<MaxAmmo>", weapon.getParameter(Weapon.WeaponParameters.MAXAMMO).toString()));
                }

                Main.getReloadDelay().remove(player.getUniqueId());
                Utils.sendMessage(player, Messages.RELOADING_FINISHED.get());
            }, 35);
        } else {
            Utils.sendMessage(player, Messages.NO_AMMO.get());
            player.playSound(player.getLocation(), Main.getInstance().getConfig().getString("empty-sound"), 100, 1F);
        }
    }

    @SuppressWarnings("unchecked")
    private void updateWeaponMeta(ItemStack itemStack, Weapon weapon) {
        String name = weapon.getParameter(Weapon.WeaponParameters.NAME).toString().replace("<Ammo>", String.valueOf(NBTEditor.getInt(itemStack, "ammo")))
                .replace("<MaxAmmo>", weapon.getParameter(Weapon.WeaponParameters.MAXAMMO).toString())
                .replace("<Damage>", weapon.getParameter(Weapon.WeaponParameters.DAMAGE).toString())
                .replace("<Durability>", String.valueOf(NBTEditor.getInt(itemStack, "durability")));

        ItemMeta im = itemStack.getItemMeta();
        ArrayList<String> weaponLore = new ArrayList<>();
        for (String string : (List<String>) weapon.getParameter(Weapon.WeaponParameters.LORE)) {
            string = string.replace("<Ammo>", String.valueOf(NBTEditor.getInt(itemStack, "ammo")))
                    .replace("<MaxAmmo>", weapon.getParameter(Weapon.WeaponParameters.MAXAMMO).toString())
                    .replace("<Damage>", weapon.getParameter(Weapon.WeaponParameters.DAMAGE).toString())
                    .replace("<Durability>", String.valueOf(NBTEditor.getInt(itemStack, "durability")));

            weaponLore.add(string);
        }
        im.setDisplayName(Utils.color(name));
        im.setLore(weaponLore);
        itemStack.setItemMeta(im);
    }
}
