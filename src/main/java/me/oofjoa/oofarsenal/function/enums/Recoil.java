package me.oofjoa.oofarsenal.function.enums;

import lombok.Getter;

@SuppressWarnings("unused")
public enum Recoil {
    LOW(0.8D, 0.1D),
    MEDIUM(1.5D, 0.2D),
    HIGH(2D, 0.4D);

    public final @Getter double pitchIncrement;
    public final double pushBack;

    Recoil(double pitchIncrement, double pushBack) {
        this.pitchIncrement = pitchIncrement;
        this.pushBack = pushBack;
    }

    public double getPushBack() {
        return -pushBack;
    }
}
