package com.jazzkuh.mtwapens.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerShootWeaponEvent extends Event implements Cancellable {
    private String weapon;
    private Player shooter;
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private boolean isCancelled;

    public PlayerShootWeaponEvent(Player shooter, String weapon) {
        this.weapon = weapon;
        this.shooter = shooter;
        this.isCancelled = false;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.isCancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    public String getWeapon() {
        return weapon;
    }

    public Player getShooter() {
        return shooter;
    }
}
