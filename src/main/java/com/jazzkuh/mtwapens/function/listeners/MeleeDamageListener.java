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
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MeleeDamageListener implements Listener {

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity) || !(event.getDamager() instanceof Player)) return;

        Player player = (Player) event.getDamager();

        if (player.getInventory().getItemInMainHand().getType() == Material.AIR) return;
        if (player.getInventory().getItemInMainHand().getItemMeta() == null) return;
        if (!player.getInventory().getItemInMainHand().getItemMeta().hasDisplayName()) return;

        ItemStack itemStack = player.getInventory().getItemInMainHand();

        if (!NBTEditor.contains(itemStack, "mtwapens_melee")) return;
        String meleeType = NBTEditor.getString(itemStack, "mtwapens_melee");

        LivingEntity entity = (LivingEntity) event.getEntity();
        event.setCancelled(true);
        event.setDamage(0D);

        if (Main.getMelee().getConfig().getString("melee." + meleeType + ".name") == null) {
            player.getInventory().removeItem(player.getInventory().getItemInMainHand());
            Utils.sendMessage(player, "&cYour melee weapon has been removed from the config files and has therefore been destroyed.");
            return;
        }

        if (NBTEditor.getInt(player.getInventory().getItemInMainHand(), "durability") <= 0) {
            player.getInventory().removeItem(player.getInventory().getItemInMainHand());
            return;
        }

        Melee melee = new Melee(meleeType);
        Double damage = (Double) melee.getParameter(Melee.MeleeParameters.DAMAGE);

        if (!(boolean) melee.getParameter(Melee.MeleeParameters.DISABLEDURABILITY)) {
            Utils.applyNBTTag(itemStack, "durability", NBTEditor.getInt(itemStack, "durability") - 1);
        }

        String showDurability = Main.getInstance().getConfig().getString("showDurability");
        if (ShowDurability.getInstance().isDurabilityShown(showDurability) == ShowDurability.Options.SHOOT || ShowDurability.getInstance().isDurabilityShown(showDurability) == ShowDurability.Options.BOTH) {
            Utils.sendMessage(player, Messages.DURABILITY.get()
                    .replace("<Durability>", String.valueOf(NBTEditor.getInt(itemStack, "durability"))));
        }
        this.updateMeleeLore(itemStack, melee);

        if (entity.getLocation().getWorld() != null) {
            entity.getLocation().getWorld().playEffect(entity.getLocation().add(0.0D, 1.0D, 0.0D), Effect.STEP_SOUND, 152);
        }

        if (damage > entity.getHealth()) {
            entity.setHealth(0D);
        } else {
            entity.setHealth(entity.getHealth() - damage);
        }

        if (NBTEditor.getInt(player.getInventory().getItemInMainHand(), "durability") <= 0) {
            player.getInventory().removeItem(player.getInventory().getItemInMainHand());
        }
    }

    @SuppressWarnings("unchecked")
    private void updateMeleeLore(ItemStack itemStack, Melee melee) {
        ItemMeta im = itemStack.getItemMeta();
        ArrayList<String> weaponLore = new ArrayList<>();
        for (String string : (List<String>) melee.getParameter(Melee.MeleeParameters.LORE)) {
            string = string
                    .replace("<Durability>", String.valueOf(NBTEditor.getInt(itemStack, "durability")));

            weaponLore.add(string);
        }
        im.setLore(weaponLore);
        itemStack.setItemMeta(im);
    }
}
