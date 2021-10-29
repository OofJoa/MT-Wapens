package com.jazzkuh.mtwapens.compatibility;

import com.jazzkuh.mtwapens.compatibility.versions.*;
import lombok.Getter;
import org.bukkit.Bukkit;

public class CompatibilityManager {

    private final String bukkitVersion = Bukkit.getServer().getClass().getPackage().getName();
    private final @Getter String version = bukkitVersion.substring(bukkitVersion.lastIndexOf('.') + 1);

    public CompatibilityLayer registerCompatibilityLayer() {
        switch (this.version) {
            case "v1_12_R1": {
                return new v1_12_2();
            }
            case "v1_13_R2": {
                return new v1_13_2();
            }
            case "v1_14_R1": {
                return new v1_14_3();
            }
            case "v1_15_R1": {
                return new v1_15_2();
            }
            case "v1_16_R3": {
                return new v1_16_4();
            }
            default: {
                return null;
            }
        }
    }
}
