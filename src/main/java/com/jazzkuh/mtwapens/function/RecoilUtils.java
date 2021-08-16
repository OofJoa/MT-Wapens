package com.jazzkuh.mtwapens.function;

import com.jazzkuh.mtwapens.function.enums.Recoil;
import com.jazzkuh.mtwapens.function.objects.Weapon;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;

public class RecoilUtils {

    public @Getter Weapon weapon;
    public @Getter
    Recoil recoil;

    public RecoilUtils(Weapon weapon, Recoil recoil) {
        this.weapon = weapon;
        this.recoil = recoil;
    }

    public void performRecoil(Player player) {
        Location location = player.getLocation();
        float pitch = location.getPitch();
        location.setPitch(pitch - (float) recoil.getPitchIncrement());

        // Use a cause other then PLUGIN or COMMAND because essentials sucks lol.
        Vector playerVelocity = player.getVelocity();
        player.teleport(location, PlayerTeleportEvent.TeleportCause.UNKNOWN);
        player.setVelocity(playerVelocity);

        Vector vector = player.getLocation().getDirection().normalize().multiply(recoil.getPushBack()).setY(0);
        player.setVelocity(vector);
    }
}
