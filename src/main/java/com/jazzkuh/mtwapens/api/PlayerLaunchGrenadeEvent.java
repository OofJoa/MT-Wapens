package com.jazzkuh.mtwapens.api;

import com.jazzkuh.mtwapens.function.objects.Grenade;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class PlayerLaunchGrenadeEvent extends Event implements Cancellable {
    private final Grenade grenade;
    private final Player shooter;
    private boolean cancelled;
    private final HandlerList handlers = new HandlerList();
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public PlayerLaunchGrenadeEvent(Player shooter, Grenade grenade) {
        this.grenade = grenade;
        this.shooter = shooter;
        this.cancelled = false;
    }

    public Player getShooter() {
        return shooter;
    }

    public Grenade getGrenade() {
        return grenade;
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
