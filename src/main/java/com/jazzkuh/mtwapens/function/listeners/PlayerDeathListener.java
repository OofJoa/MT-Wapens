/*
 *     MT-Wapens
 *     Copyright © 2021 Jazzkuh. All rights reserved.
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

import com.jazzkuh.mtwapens.function.objects.Weapon;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.GameRule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class PlayerDeathListener implements Listener {

    private final HashMap<UUID, ArrayList<ItemStack>> returnableItems = new HashMap<>();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Iterator<ItemStack> items = event.getDrops().iterator();
        ArrayList<ItemStack> soulboundItems = new ArrayList<>();

        if (event.getEntity().getWorld().getGameRuleValue(GameRule.KEEP_INVENTORY)) return;

        while (items.hasNext()) {
            ItemStack itemStack = items.next();

            if (!NBTEditor.contains(itemStack, "mtwapens_weapon")) return;
            String weaponType = NBTEditor.getString(itemStack, "mtwapens_weapon");
            Weapon weapon = new Weapon(weaponType);

            if ((boolean) weapon.getParameter(Weapon.WeaponParameters.SOULBOUND)) {
                items.remove();
                soulboundItems.add(itemStack);
            }
        }

        returnableItems.put(event.getEntity().getUniqueId(), soulboundItems);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (returnableItems.containsKey(event.getPlayer().getUniqueId())) {
            for (ItemStack item : returnableItems.get(event.getPlayer().getUniqueId())) {
                event.getPlayer().getInventory().addItem(item);
            }

            returnableItems.remove(event.getPlayer().getUniqueId());
        }
    }
}
