package com.jazzkuh.mtwapens.compatibility.versions;

import com.jazzkuh.mtwapens.compatibility.CompatibilityLayer;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.PacketPlayOutBlockBreakAnimation;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Random;

public class v1_12_2 implements CompatibilityLayer {

    @Override
    public void sendBlockBreakPacket(Block target) {
        PacketPlayOutBlockBreakAnimation packet = new PacketPlayOutBlockBreakAnimation(new Random().nextInt(Integer.MAX_VALUE), new BlockPosition(target.getX(), target.getY(), target.getZ()), 9);
        for (Player player : Bukkit.getOnlinePlayers()) {
            CraftPlayer craftPlayer = (CraftPlayer) player;
            craftPlayer.getHandle().playerConnection.sendPacket(packet);
        }
    }
}
