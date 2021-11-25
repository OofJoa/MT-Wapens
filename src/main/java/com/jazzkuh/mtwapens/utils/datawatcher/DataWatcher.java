/*
 *     MT-Wapens
 *     Copyright © 2021 Jazzkuh. All rights reserved.
 *
 *     “Commons Clause” License Condition v1.0
 *
 *    The Software is provided to you by the Licensor under the License, as defined below, subject to the following condition.
 *
 *     Without limiting other conditions in the License, the grant of rights under the License will not include, and the License does not grant to you, the right to Sell the Software.
 *
 *     For purposes of the foregoing, “Sell” means practicing any or all of the  rights granted to you under the License to provide to third parties, for a fee  or other consideration (including without limitation fees for hosting or  consulting/ support services related to the Software), a product or service  whose value derives, entirely or substantially, from the functionality of the  Software. Any license notice or attribution required by the License must also  include this Commons Clause License Condition notice.
 *
 *     Software: MT-Wapens
 *     License: GNU-LGPL v2.1 with Commons Clause
 *     Licensor: [Jazzkuh](https://github.com/Jazzkuh)
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
