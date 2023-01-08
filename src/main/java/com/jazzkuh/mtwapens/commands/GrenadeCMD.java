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

package com.jazzkuh.mtwapens.commands;

import com.google.common.collect.ImmutableList;
import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.function.WeaponFactory;
import com.jazzkuh.mtwapens.function.objects.Grenade;
import com.jazzkuh.mtwapens.utils.Utils;
import com.jazzkuh.mtwapens.utils.command.AbstractCommand;
import com.jazzkuh.mtwapens.utils.command.Argument;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GrenadeCMD extends AbstractCommand {

    public GrenadeCMD() {
        super("getgrenade",
                new Argument("<grenadeType> <Durability> [player]", "Grab a grenade from the config files."));
    }

    @Override
    public void execute(CommandSender sender, Command command, String label, String[] args) {
        if (!hasPermission(getBasePermission(), false)) return;

        if (args.length < 2) {
            sendNotEnoughArguments(this);
            return;
        }

        ArrayList<String> grenadeTypes = new ArrayList<>(Main.getGrenades().getConfig().getConfigurationSection("grenades.").getKeys(false));

        if (Main.getGrenades().getConfig().getString("grenades." + args[0] + ".name") == null) {
            Utils.sendMessage(sender, "&cThe given grenade type is not a valid grenade. Please choose one of the following: "
                    + StringUtils.join(grenadeTypes, ", "));
            return;
        }

        if (!Utils.isInt(args[1]) || Integer.parseInt(args[1]) < 0) {
            Utils.sendMessage(sender, "&cThe given durability is not a valid integer.");
            return;
        }

        String type = args[0];
        int durability = Integer.parseInt(args[1]);

        switch (args.length) {
            case 2: {
                if (!senderIsPlayer()) return;
                Player player = (Player) sender;
                Grenade grenade = new Grenade(type);
                WeaponFactory weaponFactory = new WeaponFactory(player);
                weaponFactory.buildGrenade(grenade, durability);
                weaponFactory.addToInventory();
                break;
            }
            case 3: {
                if (Bukkit.getPlayer(args[2]) == null) {
                    Utils.sendMessage(sender, "&cJe hebt geen geldige speler opgegeven.");
                    return;
                }

                Player target = Bukkit.getPlayer(args[2]);

                Grenade grenade = new Grenade(type);
                WeaponFactory weaponFactory = new WeaponFactory(target);
                weaponFactory.buildGrenade(grenade, durability);
                weaponFactory.addToInventory();
                Utils.sendMessage(sender, "&aSuccesfully gave " + target.getName() + " a " + type + " with " + durability + " durability.");
                break;
            }
            default: {
                sendNotEnoughArguments(this);
                break;
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(getBasePermission())) return Collections.emptyList();

        if (args.length == 1) {
            return getApplicableTabCompleters(args[0],
                    Main.getGrenades().getConfig().getConfigurationSection("grenades.").getKeys(false));
        }

        if (args.length == 3) {
            return getApplicableTabCompleters(args[2], Utils.getPlayerNames());
        }

        return Collections.emptyList();
    }
}
