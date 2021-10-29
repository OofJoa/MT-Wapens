package com.jazzkuh.mtwapens.compatibility;

import com.jazzkuh.mtwapens.utils.ItemBuilder;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public interface CompatibilityLayer {
    void sendBlockBreakPacket(Block target);
}
