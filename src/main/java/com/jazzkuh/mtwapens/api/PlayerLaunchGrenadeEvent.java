package com.jazzkuh.mtwapens.api;

import com.jazzkuh.mtwapens.function.objects.Grenade;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class PlayerLaunchGrenadeEvent extends Event implements Cancellable {
    private final @Getter Grenade grenade;
    private final @Getter Player shooter;
    private @Getter @Setter boolean cancelled;
    private final @Getter HandlerList handlers = new HandlerList();

    public PlayerLaunchGrenadeEvent(Player shooter, Grenade grenade) {
        this.grenade = grenade;
        this.shooter = shooter;
        this.cancelled = false;
    }
}
