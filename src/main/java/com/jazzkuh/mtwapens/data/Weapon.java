package com.jazzkuh.mtwapens.data;

public class Weapon {
    WeaponType weaponType;
    int durability, ammo;

    public Weapon(WeaponType weaponType, int durability, int ammo) {
        this.weaponType = weaponType;
        this.durability = durability;
        this.ammo = ammo;
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }

    public int getDurability() {
        return durability;
    }

    public int getAmmo() {
        return ammo;
    }

    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }
}
