package com.jazzkuh.mtwapens.api;

import com.jazzkuh.mtwapens.function.objects.Weapon;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class PrePlayerShootWeaponEvent extends Event implements Cancellable {
    private final @Getter Weapon weapon;
    private final @Getter Player shooter;
    private @Getter @Setter boolean cancelled;
    private final @Getter HandlerList handlers = new HandlerList();

    public PrePlayerShootWeaponEvent(Player shooter, Weapon weapon) {
        this.weapon = weapon;
        this.shooter = shooter;
        this.cancelled = false;
    }
}
