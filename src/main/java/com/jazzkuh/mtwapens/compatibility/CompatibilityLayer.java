package com.jazzkuh.mtwapens.compatibility;

import org.bukkit.block.Block;

public interface CompatibilityLayer {
    void sendBlockBreakPacket(Block target);
}
