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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class DevListener implements Listener {
    Main plugin;

    public DevListener(Main plugin) {
        this.plugin = plugin;
    }

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
                    Utils.sendMessage(player, "&8| &2Version: &a" + plugin.getDescription().getVersion());
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
                    Main.getInstance().checkBlacklistStatus(plugin);
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
            Utils.sendMessage(player, "&8| &aThis server is running &2MT-Wapens&a version &2" + plugin.getDescription().getVersion() + "&a.");
            Utils.sendMessage(player, "&8 ----------------------------------------------");
        }
    }
}
