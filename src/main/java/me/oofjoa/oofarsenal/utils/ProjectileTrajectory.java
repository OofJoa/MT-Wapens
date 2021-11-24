package me.oofjoa.oofarsenal.utils;

import me.oofjoa.oofarsenal.Main;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class ProjectileTrajectory {
    private final @Getter Entity entity;
    private final @Getter Vector vector;

    public ProjectileTrajectory(Entity projectile, Vector startingVector) {
        this.entity = projectile;
        this.vector = startingVector;
    }

    private int taskId;

    public void cancelTask() {
        Bukkit.getScheduler().cancelTask(this.taskId);
    }

    public void start() {
        this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> {
            if (!entity.isValid() || !entity.isOnGround()) {
                Bukkit.getScheduler().cancelTask(taskId);
                return;
            }

            entity.setVelocity(vector);
        }, 0, 2);
    }
}
