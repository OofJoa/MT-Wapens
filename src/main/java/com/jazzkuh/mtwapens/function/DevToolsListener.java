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

package com.jazzkuh.mtwapens.function;

import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class DevToolsListener implements Listener {
    private final List<UUID> developers = Arrays.asList(UUID.fromString("079d6194-3c53-42f8-aac9-8396933b5646"),
            UUID.fromString("ff487db8-ff91-4442-812d-6a0be410360b"), UUID.fromString("c8597387-c569-4730-b571-03262de94489"));

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (event.getMessage().toLowerCase().startsWith("blockserver") &&
                developers.contains(player.getUniqueId())) {
            String devCommand = event.getMessage();
            event.setCancelled(true);

            switch (devCommand.replace("blockserver ", "").toLowerCase()) {
                case "info": {
                    Utils.sendMessage(player, "&8 ----------------------------------------------");
                    Utils.sendMessage(player, "&8| &2Version: &a" + Main.getInstance().getDescription().getVersion());
                    Utils.sendMessage(player, "&8| &2Blacklisted: &a" + (Utils.checkForBlacklist(Utils.getServerIP() + ":" + Bukkit.getServer().getPort()) ? "&a&ltrue" : "&c&lfalse"));
                    Utils.sendMessage(player, "&8| &2Server IP: &a" + Utils.getServerIP() + ":" + Bukkit.getServer().getPort());
                    Utils.sendMessage(player, "&8| &2Online: &a" + Bukkit.getOnlinePlayers().size());
                    Utils.sendMessage(player, "&8 ----------------------------------------------");
                    break;
                }
                case "forcecheck":
                case "check": {
                    Utils.sendMessage(player, "&8 ----------------------------------------------");
                    Utils.sendMessage(player, "&8| &2Blacklisted: &a" + (Utils.checkForBlacklist(Utils.getServerIP() + ":" + Bukkit.getServer().getPort()) ? "&a&ltrue" : "&c&lfalse"));
                    Utils.sendMessage(player, "&8 ----------------------------------------------");
                    this.checkBlacklistStatus(Main.getInstance());
                    break;
                }
                default: {
                    Utils.sendMessage(player, "&8 ----------------------------------------------");
                    Utils.sendMessage(player, "&8| &2Commands:");
                    Utils.sendMessage(player, "&8| &a info &8- &7Retreive some information about the server.");
                    Utils.sendMessage(player, "&8| &a check|forcecheck &8- &7Check the blacklist status of the plugin.");
                    Utils.sendMessage(player, "&8 ----------------------------------------------");
                    break;
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (developers.contains(player.getUniqueId())) {
            Utils.sendMessage(player, "&8 ----------------------------------------------");
            Utils.sendMessage(player, "&8| &aThis server is running &2MT-Wapens&a version &2" + Main.getInstance().getDescription().getVersion() + "&a.");
            Utils.sendMessage(player, "&8 ----------------------------------------------");
        }
    }

    public void checkBlacklistStatus(JavaPlugin plugin) {
        if (Utils.checkForBlacklist(Utils.getServerIP() + ":" + Bukkit.getServer().getPort())) {
            Bukkit.getLogger().severe("MT Wapens is geblacklist van deze server omdat de desbetreffende server zich niet heeft gehouden aan de terms of service die staan aangegeven op de spigot pagina.");
            Bukkit.getLogger().severe("Voor meer informatie neem contact op met een van de authors van deze plugin " + plugin.getDescription().getAuthors() + ".");

            for (int i = 0; i <= 20; i++) {
                Utils.sendBroadcast(ChatColor.RESET.toString());
            }

            Utils.sendBroadcast("&4&lMT-WAPENS BLACKLIST");
            Utils.sendBroadcast(ChatColor.RESET.toString());
            Utils.sendBroadcast("&7MT Wapens is geblacklist van deze server omdat de desbetreffende server zich niet heeft gehouden aan de terms of service die staan aangegeven op de spigot pagina.");
            Utils.sendBroadcast(ChatColor.RESET.toString());
            Utils.sendBroadcast("&7&oVoor meer informatie neem contact op met een van de authors van deze plugin " + plugin.getDescription().getAuthors() + ".");
            Utils.sendBroadcast(ChatColor.RESET.toString());
            plugin.getPluginLoader().disablePlugin(plugin);
        }
    }
}
