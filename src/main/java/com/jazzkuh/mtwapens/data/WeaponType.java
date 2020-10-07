package com.jazzkuh.mtwapens.data;

import com.jazzkuh.mtwapens.utility.Utils;

public class WeaponType {
    String type, displayName, ammoName;
    double damage, attackSpeed;
    int maxAmmo;

    public WeaponType(String type, String displayName, String ammoName, double damage, double attackSpeed, int maxAmmo) {
        this.type = type;
        this.displayName = Utils.color(displayName);
        this.ammoName = Utils.color(ammoName);
        this.damage = damage;
        this.attackSpeed = attackSpeed;
        this.maxAmmo = maxAmmo;
    }

    public String getType() {
        return type;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getAmmoName() {
        return ammoName;
    }

    public double getDamage() {
        return damage;
    }

    public double getAttackSpeed() {
        return attackSpeed;
    }

    public int getMaxAmmo() {
        return maxAmmo;
    }
}
