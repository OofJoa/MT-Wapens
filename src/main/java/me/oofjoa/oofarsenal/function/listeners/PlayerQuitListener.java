package me.oofjoa.oofarsenal.function.listeners;

import me.oofjoa.oofarsenal.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Main.getReloadDelay().remove(String.valueOf(event.getPlayer().getUniqueId()));
    }
}
