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

package com.jazzkuh.mtwapens.function.menu;

import com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.ImmutableMap;
import com.jazzkuh.mtwapens.function.WeaponFactory;
import com.jazzkuh.mtwapens.function.objects.Grenade;
import com.jazzkuh.mtwapens.function.objects.Melee;
import com.jazzkuh.mtwapens.function.objects.Weapon;
import com.jazzkuh.mtwapens.messages.Messages;
import com.jazzkuh.mtwapens.utils.ItemBuilder;
import com.jazzkuh.mtwapens.utils.menu.GUIHolder;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class DurabilityBuilderMenu extends GUIHolder {
    String type;
    int durability;
    BuilderType builderType;

    private ImmutableMap<Object, Object> descendingItems = ImmutableMap.builder()
            .put(0, 1)
            .put(1, 5)
            .put(2, 10).build();

    private ImmutableMap<Object, Object> ascendingItems = ImmutableMap.builder()
            .put(6, 1)
            .put(7, 5)
            .put(8, 10).build();

    public DurabilityBuilderMenu(String type, int durability, BuilderType builderType) {
        this.type = type;
        this.durability = durability;
        this.builderType = builderType;

        String title = WordUtils.capitalize(builderType.name().toLowerCase()) + " Builder " + type;
        this.inventory = Bukkit.createInventory(this, 9, title);

        if (this.durability >= 1) {
            for (Object index : descendingItems.keySet()) {
                int itemIndex = Integer.parseInt(index.toString());
                int durabilityIndex = Integer.parseInt(descendingItems.get(index).toString());

                this.inventory.setItem(itemIndex, new ItemBuilder(XMaterial.RED_WOOL.parseItem())
                        .setColoredName("&c-" + durabilityIndex + " Durability")
                        .setNBT("durability", String.valueOf(durabilityIndex))
                        .setNBT("isNegative", "true")
                        .toItemStack());
            }
        }

        this.inventory.setItem(4,
                new ItemBuilder(XMaterial.CRAFTING_TABLE.parseMaterial())
                        .setName(Messages.MENU_BUILDER_CRAFT.get()
                                .replace("<Type>", WordUtils.capitalize(builderType.name().toLowerCase()))
                                .replace("<Durability>", String.valueOf(this.durability)))
                        .setNBT("buildItem", "true")
                        .toItemStack());

        for (Object index : ascendingItems.keySet()) {
            int itemIndex = Integer.parseInt(index.toString());
            int durabilityIndex = Integer.parseInt(ascendingItems.get(index).toString());

            this.inventory.setItem(itemIndex, new ItemBuilder(XMaterial.LIME_WOOL.parseItem())
                    .setColoredName("&a+" + durabilityIndex + " Durability")
                    .setNBT("durability", String.valueOf(durabilityIndex))
                    .setNBT("isNegative", "false")
                    .toItemStack());
        }
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public int getDurability() {
        return this.durability;
    }

    public BuilderType getBuilderType() {
        return builderType;
    }

    public String getType() {
        return this.type;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        event.setCancelled(true);

        if (event.getCurrentItem() == null) return;
        if (event.getCurrentItem().getType() == Material.AIR) return;

        ItemStack item = event.getCurrentItem();
        String type = ((DurabilityBuilderMenu) inventory.getHolder()).getType();

        if (NBTEditor.contains(item, "durability") && NBTEditor.contains(item, "isNegative")) {
            int durability = Integer.parseInt(NBTEditor.getString(item, "durability"));
            int currentDurability = ((DurabilityBuilderMenu) inventory.getHolder()).getDurability();

            if (NBTEditor.getString(item, "isNegative").equalsIgnoreCase("true")) {
                new DurabilityBuilderMenu(type, currentDurability - durability, ((DurabilityBuilderMenu) inventory.getHolder()).getBuilderType()).open(player);
            } else {
                new DurabilityBuilderMenu(type, currentDurability + durability, ((DurabilityBuilderMenu) inventory.getHolder()).getBuilderType()).open(player);
            }
        } else if (NBTEditor.contains(item, "buildItem")) {
            if (event.getSlot() != 4 || !item.getType().equals(XMaterial.CRAFTING_TABLE.parseMaterial())) return;

            int durability = ((DurabilityBuilderMenu) inventory.getHolder()).getDurability();
            switch (this.builderType) {
                case WEAPON: {
                    Weapon weapon = new Weapon(type);
                    WeaponFactory weaponFactory = new WeaponFactory(player);
                    weaponFactory.buildWeapon(weapon, durability);
                    weaponFactory.addToInventory();
                    break;
                }
                case GRENADE: {
                    Grenade grenade = new Grenade(type);
                    WeaponFactory weaponFactory = new WeaponFactory(player);
                    weaponFactory.buildGrenade(grenade, durability);
                    weaponFactory.addToInventory();
                    break;
                }
                case MELEE: {
                    Melee melee = new Melee(type);
                    WeaponFactory weaponFactory = new WeaponFactory(player);
                    weaponFactory.buildMelee(melee, durability);
                    weaponFactory.addToInventory();
                    break;
                }
            }
            player.closeInventory();
        }
    }

    public enum BuilderType {
        WEAPON, GRENADE, MELEE
    }
}