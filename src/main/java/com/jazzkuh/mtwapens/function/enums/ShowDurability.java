package com.jazzkuh.mtwapens.function.enums;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;

public class ShowDurability {

    private static @Getter @Setter ShowDurability instance;

    public ShowDurability() {
        setInstance(this);
    }

    public ShowDurability.Options isDurabilityShown(@Nullable String option) {
        if (option == null) return Options.BOTH;

        switch (option.toUpperCase()) {
            case "SHOOT":
                return Options.SHOOT;
            case "SWITCH":
                return Options.SWITCH;
            case "NONE":
                return Options.NONE;
            case "BOTH":
            default:
                return Options.BOTH;
        }
    }

    public enum Options {
        SHOOT, SWITCH, BOTH, NONE;
    }
}
