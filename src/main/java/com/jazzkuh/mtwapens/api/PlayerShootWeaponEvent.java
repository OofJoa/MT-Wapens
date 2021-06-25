package com.jazzkuh.mtwapens.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class PlayerShootWeaponEvent extends Event implements Cancellable {
    private final String weapon;
    private final Player shooter;
    private boolean isCancelled;
    private static final HandlerList handlers = new HandlerList();

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

    public String getWeapon() {
        return weapon;
    }

    public Player getShooter() {
        return shooter;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
