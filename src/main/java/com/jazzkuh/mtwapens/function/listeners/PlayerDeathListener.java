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

import com.jazzkuh.mtwapens.function.objects.Weapon;
import io.github.bananapuncher714.nbteditor.NBTEditor;
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

        if (event.getKeepInventory()) return;

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
