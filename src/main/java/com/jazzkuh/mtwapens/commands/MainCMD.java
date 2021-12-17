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
import com.jazzkuh.mtwapens.function.menu.WeaponMenu;
import com.jazzkuh.mtwapens.utils.Utils;
import com.jazzkuh.mtwapens.utils.command.AbstractCommand;
import com.jazzkuh.mtwapens.utils.command.Argument;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainCMD extends AbstractCommand {

    public MainCMD() {
        super("mtwapens",
                new Argument("reload", "Reload the plugin."),
                new Argument("menu", "Grab ammo and weapons from a menu."));
    }

    @Override
    public void execute(CommandSender sender, Command command, String label, String[] args) {
        if (!senderIsPlayer()) return;
        if (!hasPermission(getBasePermission() + ".reload", true)
                || !hasPermission(getBasePermission() + ".menu", true)) {
            sendDefaultMessage(sender);
            return;
        }

        Player player = (Player) sender;

        if (args.length > 0) {
            switch (args[0]) {
                case "reload": {
                    if (!hasPermission(getBasePermission() + ".reload", false)) return;

                    Main.getWeapons().reloadConfig();
                    Main.getAmmo().reloadConfig();
                    Main.getGrenades().reloadConfig();
                    Main.getMelee().reloadConfig();

                    Main.getMessages().reloadConfig();
                    Main.getInstance().reloadConfig();
                    Utils.sendMessage(player, "&aReloaded the configuration files, please check the console for any errors.");
                    break;
                }
                case "menu": {
                    if (!hasPermission(getBasePermission() + ".menu", false)) return;
                    new WeaponMenu(player, 0).open(player);
                    break;
                }
                default: {
                    sendNotEnoughArguments(this);
                    break;
                }
            }
        } else {
            sendNotEnoughArguments(this);
        }
    }

    private void sendDefaultMessage(CommandSender sender) {
        Utils.sendMessage(sender, "&8 ----------------------------------------------");
        Utils.sendMessage(sender, "&8| &2" + Main.getInstance().getDescription().getName() + " version: &a" + Main.getInstance().getDescription().getVersion());
        Utils.sendMessage(sender, "&8| &2Description: &a" + Main.getInstance().getDescription().getDescription());
        Utils.sendMessage(sender, "&8| &2Download: &a" + Main.getInstance().getDescription().getWebsite());
        Utils.sendMessage(sender, "&8| &2Authors: &aJazzkuh");
        Utils.sendMessage(sender, "&8 ----------------------------------------------");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(getBasePermission() + ".reload")
                || !sender.hasPermission(getBasePermission() + ".menu"))
            return Collections.emptyList();

        if (args.length == 1) {
            return getApplicableTabCompleters(args[0],
                    Arrays.asList("reload", "menu"));
        }

        return Collections.emptyList();
    }
}
