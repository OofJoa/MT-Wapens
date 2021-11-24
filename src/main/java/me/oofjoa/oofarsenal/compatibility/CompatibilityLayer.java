package me.oofjoa.oofarsenal.compatibility;

import org.bukkit.block.Block;

public interface CompatibilityLayer {
    void sendBlockBreakPacket(Block target);
}
