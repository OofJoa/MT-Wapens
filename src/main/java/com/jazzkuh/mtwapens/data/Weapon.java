package com.jazzkuh.mtwapens.data;

public class Weapon {
    String displayName, ammoName;
    double damage, attackSpeed;
    int maxAmmo;

    public Weapon(String displayName, String ammoName, double damage, double attackSpeed, int maxAmmo) {
        this.displayName = displayName;
        this.ammoName = ammoName;
        this.damage = damage;
        this.attackSpeed = attackSpeed;
        this.maxAmmo = maxAmmo;
    }
}
