package me.oofjoa.oofarsenal.function.listeners;

import me.oofjoa.oofarsenal.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String resourcePackURL = Main.getInstance().getConfig().getString("resourcePackURL");
        if (resourcePackURL == null || resourcePackURL.equalsIgnoreCase("none")) return;

        try {
            player.setResourcePack(resourcePackURL);
        } catch (Exception e) {
            Main.getInstance().getLogger().warning("Could not load resourcecpack provided in the config.");
        }
    }
}
