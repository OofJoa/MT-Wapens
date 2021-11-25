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

package com.jazzkuh.mtwapens.utils.datawatcher;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

/**
 * @author Mindgamesnl
 */

public class DataWatcher<T> {

    private T value;
    private final int task;
    private Feeder<T> dataFeeder;
    private Consumer<T> callback;
    private Boolean isRunning = false;

    public DataWatcher(JavaPlugin plugin, Boolean sync, int delayTicks) {
        Runnable executor = () -> {
            if (this.dataFeeder == null || this.callback == null) return;
            T newValue = dataFeeder.feed();
            if (this.value != null && !newValue.equals(this.value)) this.callback.accept(newValue);
            this.value = newValue;
        };

        if (sync) {
            this.task = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, executor, delayTicks, delayTicks);
        } else {
            this.task = Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, executor, delayTicks, delayTicks);
        }

        isRunning = true;
    }

    public DataWatcher setFeeder(Feeder<T> feeder) {
        this.dataFeeder = feeder;
        return this;
    }

    public DataWatcher setTask(Consumer<T> task) {
        this.callback = task;
        return this;
    }

    public Boolean isRunning() {
        return this.isRunning;
    }

    public void stop() {
        Bukkit.getScheduler().cancelTask(this.task);
        this.isRunning = false;
    }

}
