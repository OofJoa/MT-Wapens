/*
 *     MT-Wapens
 *     Copyright © 2021 Jazzkuh. All rights reserved.
 *
 *     This library is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU Lesser General Public
 *     License as published by the Free Software Foundation; either
 *     version 2.1 of the License, or (at your option) any later version.
 *
 *     This library is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *     Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public
 *     License along with this library; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 *     USA
 */

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
