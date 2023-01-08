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
import com.jazzkuh.mtwapens.function.enums.ShowDurability;
import com.jazzkuh.mtwapens.function.objects.Melee;
import com.jazzkuh.mtwapens.function.objects.Weapon;
import com.jazzkuh.mtwapens.messages.Messages;
import com.jazzkuh.mtwapens.utils.Utils;
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

        if (Main.getWeapons().getConfig().getString("weapons." + weaponType + ".name") == null) {
            player.getInventory().removeItem(itemStack);
            Utils.sendMessage(player, "&cYour weapon has been removed from the config files and has therefore been destroyed.");
            return;
        }

        Weapon weapon = new Weapon(weaponType);

        String showDurability = Main.getInstance().getConfig().getString("showDurability");
        if (ShowDurability.getInstance().isDurabilityShown(showDurability) == ShowDurability.Options.SWITCH || ShowDurability.getInstance().isDurabilityShown(showDurability) == ShowDurability.Options.BOTH) {
            String holdingMessage = weapon.isUsingAmmo() ? Messages.AMMO_DURABILITY.get() : Messages.DURABILITY.get();
            Utils.sendMessage(player, holdingMessage
                    .replace("<Durability>", String.valueOf(NBTEditor.getInt(itemStack, "durability")))
                    .replace("<Ammo>", String.valueOf(NBTEditor.getInt(itemStack, "ammo")))
                    .replace("<MaxAmmo>", weapon.getParameter(Weapon.WeaponParameters.MAXAMMO).toString()));
        }
    }

    @EventHandler
    public void onMeleeHold(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();

        if (event.getPlayer().getInventory().getItem(event.getNewSlot()) == null) return;

        ItemStack itemStack = event.getPlayer().getInventory().getItem(event.getNewSlot());

        if (itemStack == null) return;
        if (!NBTEditor.contains(itemStack, "mtwapens_melee")) return;

        String meleeType = NBTEditor.getString(itemStack, "mtwapens_melee");

        if (Main.getMelee().getConfig().getString("melee." + meleeType + ".name") == null) {
            player.getInventory().removeItem(itemStack);
            Utils.sendMessage(player, "&cYour melee weapon has been removed from the config files and has therefore been destroyed.");
            return;
        }

        Melee melee = new Melee(meleeType);

        String showDurability = Main.getInstance().getConfig().getString("showDurability");
        if (ShowDurability.getInstance().isDurabilityShown(showDurability) == ShowDurability.Options.SWITCH || ShowDurability.getInstance().isDurabilityShown(showDurability) == ShowDurability.Options.BOTH) {
            Utils.sendMessage(player, Messages.DURABILITY.get()
                    .replace("<Durability>", String.valueOf(NBTEditor.getInt(itemStack, "durability"))));
        }
    }
}
