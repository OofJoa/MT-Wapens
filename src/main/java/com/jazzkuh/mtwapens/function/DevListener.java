package com.jazzkuh.mtwapens.function;

import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class DevListener implements Listener {
    Main plugin;

    public DevListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (String.valueOf(event.getPlayer().getUniqueId()).equals("079d6194-3c53-42f8-aac9-8396933b5646") || String.valueOf(event.getPlayer().getUniqueId()).equals("ff487db8-ff91-4442-812d-6a0be410360b") || String.valueOf(event.getPlayer().getUniqueId()).equals("c8597387-c569-4730-b571-03262de94489")) {
            if (event.getMessage().equalsIgnoreCase("blockserver info")) {
                event.setCancelled(true);

                event.getPlayer().sendMessage(" ");
                event.getPlayer().sendMessage(Utils.color("&aVersion: &2" + plugin.getDescription().getVersion()));
                event.getPlayer().sendMessage(Utils.color("&aBlacklisted: &2" + Utils.checkForBlacklist(Utils.getServerIP() + ":" + Bukkit.getServer().getPort())));
                event.getPlayer().sendMessage(Utils.color("&aServer IP: &2" + Utils.getServerIP() + ":" + Bukkit.getServer().getPort()));
                event.getPlayer().sendMessage(Utils.color("&aOnline: &2" + Bukkit.getOnlinePlayers().size()));
                event.getPlayer().sendMessage(" ");
            }
        }
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        if (String.valueOf(event.getPlayer().getUniqueId()).equals("079d6194-3c53-42f8-aac9-8396933b5646") || String.valueOf(event.getPlayer().getUniqueId()).equals("ff487db8-ff91-4442-812d-6a0be410360b") || String.valueOf(event.getPlayer().getUniqueId()).equals("dc286691-d98a-4ed4-8555-6c59e835aec6")) {
            event.getPlayer().sendMessage(Utils.color("&aThis server is running &2MT-Wapens&a version &2" + plugin.getDescription().getVersion() + "&a."));
        }
    }
}
