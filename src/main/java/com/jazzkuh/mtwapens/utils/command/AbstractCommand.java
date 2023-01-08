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

package com.jazzkuh.mtwapens.utils.command;

import com.google.common.collect.ImmutableList;
import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.utils.Utils;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.lucko.commodore.Commodore;
import me.lucko.commodore.CommodoreProvider;
import me.lucko.commodore.file.CommodoreFileFormat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractCommand implements TabExecutor {
    public CommandSender sender;
    public Command command;
    public String commandName;
    public Argument[] arguments;

    public AbstractCommand(String commandName) {
        this.commandName = commandName;
        this.arguments = new Argument[]{};
    }

    public AbstractCommand(String commandName, Argument... arguments) {
        this.commandName = commandName;
        this.arguments = arguments;
    }

    public void register(Main plugin) {
        PluginCommand cmd = plugin.getCommand(commandName);
        if (cmd != null) {
            cmd.setExecutor(this);
        }

        //Commodore
        if (CommodoreProvider.isSupported()) {
            Commodore commodore = CommodoreProvider.getCommodore(plugin);
            try {
                InputStream inputStream = plugin.getResource(commandName + ".commodore");
                if (inputStream == null) return;

                LiteralCommandNode<?> literalCommandNode = CommodoreFileFormat.parse(inputStream);
                commodore.register(cmd, literalCommandNode);
            } catch (IOException ignored) {}
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        this.command = command;
        this.sender = sender;
        this.execute(sender, command, label, args);
        return true;
    }

    public abstract void execute(CommandSender sender, Command command, String label, String[] args);

    protected boolean senderIsPlayer() {
        return sender instanceof Player;
    }

    protected boolean hasPermission(String permission, boolean silent) {
        if (sender.hasPermission(permission)) {
            return true;
        }

        if (!silent) {
            sender.sendMessage(Utils.color("&cTo use this command, you need permission " + permission + "."));
        }
        return false;
    }

    protected void addIfPermission(Collection<String> options, String permission, String option) {
        if (sender.hasPermission(permission)) {
            options.add(option);
        }
    }

    protected String getBasePermission() {
        return "mtwapens.command." + commandName;
    }

    protected void sendNotEnoughArguments(AbstractCommand command) {
        sender.sendMessage(Utils.color("&8 ----------------------------------------------"));
        sender.sendMessage(Utils.color("&8| &2Commands:"));
        for (Argument argument : this.arguments) {
            if (argument.getPermission() == null || sender.hasPermission(argument.getPermission())) {
                sender.sendMessage(Utils.color("&8|  &a/" + command.command.getName() + " " + argument.getArguments() + "&8 - &7" + argument.getDescription()));
            }
        }
        sender.sendMessage(Utils.color("&8 ----------------------------------------------"));
    }

    protected List<String> getApplicableTabCompleters(String arg, Collection<String> completions) {
        return StringUtil.copyPartialMatches(arg, completions, new ArrayList<>(completions.size()));
    }
}
