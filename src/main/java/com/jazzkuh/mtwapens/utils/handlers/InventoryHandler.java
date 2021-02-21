package com.jazzkuh.mtwapens.utils.handlers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;

public abstract class InventoryHandler implements InventoryHolder {

    public static void init(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onClick(InventoryClickEvent event) {
                if (event.getInventory() == null) return;
                if (event.getInventory().getHolder() == null) return;
                if (!(event.getInventory().getHolder() instanceof InventoryHandler)) return;
                ((InventoryHandler) event.getInventory().getHolder()).onClick(event);
            }
        }, plugin);
    }

    protected Inventory inventory;

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public abstract void onClick(InventoryClickEvent event);

    public void open(Player player){
        player.openInventory(inventory);
    }
}
