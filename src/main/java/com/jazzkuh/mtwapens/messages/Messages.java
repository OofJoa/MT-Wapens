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

package com.jazzkuh.mtwapens.messages;

import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.utils.Utils;

public enum Messages {
    NO_AMMO("messages.outOfAmmo", "&cJe geweer is leeg!"),
    SHOT_LAST_BULLET("messages.shotLastBullet","&cJe hebt je laatste kogel uit je magazijn geschoten."),
    DURABILITY("messages.durability","&aDurability: &f<Durability>"),
    AMMO_DURABILITY("messages.ammoDurability", "&aDurability: &f<Durability>\n&aAmmo: &7<Ammo>&8/&7<MaxAmmo>"),
    RELOADING_START("messages.reloading.start", "&aJe wapen is nu aan het herladen..."),
    RELOADING_FINISHED("messages.reloading.finished", "&aJe wapen is herladen."),
    MENU_BUILDER_CRAFT("messages.menu.builder.craft", "&aCraft <Type>: &2<Durability> &aDurability"),
    MENU_SWITCHER("messages.menu.button.switch", "&aSwitch to &2<Menu>"),
    MENU_WEAPON_TITLE("messages.menu.weapon.title", "Weapon Menu"),
    MENU_WEAPON_BUTTON_PAGE("messages.menu.button.page", "&aPage <Page>"),
    MENU_AMMO_TITLE("messages.menu.ammo.title", "Ammo Menu"),
    MENU_GRENADE_TITLE("messages.menu.grenade.title", "Grenade Menu"),
    MENU_MELEE_TITLE("messages.menu.melee.title", "Melee Weapon Menu"),
    WEAPON_CANT_SHOOT_WIHTOUT_SCOPE("messages.weapon.cantShootWithoutScope", "&cSorry bro, no 360 noscope for you...");


    private final String path;
    private final String message;
    Messages(String path, String message) {
        this.path = path;
        this.message = message;
    }

    private String getMessage() {
        return message;
    }

    private String getPath() {
        return path;
    }

    public String get() {
        String msg = Main.getMessages().getConfig().getString(this.getPath());
        return Utils.color(msg);
    }

    public static void init() {
        for (Messages msg : Messages.values()) {
            if (Main.getMessages().getConfig().getString(msg.getPath()) == null) {
                Main.getMessages().getConfig().set(msg.getPath(), msg.getMessage());
            }
        }
    }
}
