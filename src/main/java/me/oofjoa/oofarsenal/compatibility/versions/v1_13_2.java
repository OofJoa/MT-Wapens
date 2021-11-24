package me.oofjoa.oofarsenal.compatibility.versions;

import me.oofjoa.oofarsenal.compatibility.CompatibilityLayer;
import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.PacketPlayOutBlockBreakAnimation;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Random;

public class v1_13_2 implements CompatibilityLayer {

    @Override
    public void sendBlockBreakPacket(Block target) {
        PacketPlayOutBlockBreakAnimation packet = new PacketPlayOutBlockBreakAnimation(new Random().nextInt(Integer.MAX_VALUE), new BlockPosition(target.getX(), target.getY(), target.getZ()), 9);
        for (Player player : Bukkit.getOnlinePlayers()) {
            CraftPlayer craftPlayer = (CraftPlayer) player;
            craftPlayer.getHandle().playerConnection.sendPacket(packet);
        }
    }
}
