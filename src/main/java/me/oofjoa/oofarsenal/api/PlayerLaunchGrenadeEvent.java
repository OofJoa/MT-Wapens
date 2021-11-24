package me.oofjoa.oofarsenal.api;

import me.oofjoa.oofarsenal.function.objects.Grenade;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class PlayerLaunchGrenadeEvent extends Event implements Cancellable {
    private final Grenade grenade;
    private final Player shooter;
    private boolean cancelled;
    private final HandlerList handlers = new HandlerList();

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

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
