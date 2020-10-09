package com.jazzkuh.mtwapens.data;

public class Weapon {
    WeaponType weaponType;
    int uuid, durability, ammo;

    public Weapon(WeaponType weaponType, int uuid, int durability, int ammo) {
        this.weaponType = weaponType;
        this.uuid = uuid;
        this.durability = durability;
        this.ammo = ammo;
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }

    public int getUuid() {
        return uuid;
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

    public void setDurability(int durability) {
        this.durability = durability;
    }
}
