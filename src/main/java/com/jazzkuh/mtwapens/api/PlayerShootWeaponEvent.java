package com.jazzkuh.mtwapens.api;

import com.jazzkuh.mtwapens.function.objects.Weapon;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class PlayerShootWeaponEvent extends Event implements Cancellable {
    private final Weapon weapon;
    private final Player shooter;
    private boolean cancelled;
    private final HandlerList handlers = new HandlerList();
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public PlayerShootWeaponEvent(Player shooter, Weapon weapon) {
        this.weapon = weapon;
        this.shooter = shooter;
        this.cancelled = false;
    }

    public Player getShooter() {
        return shooter;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
