package com.jazzkuh.mtwapens.utils.messages;

public enum DefaultMessages {
    NO_AMMO("Weapons.NoAmmo", "&cOut of ammo!"),
    DURABILITY("Weapons.Durability", "&9Durability: &a<Durability>"),
    AMMO("Weapons.Ammo", "&9Ammo: &a<Ammo>&f/&c<MaxAmmo>"),
    RELOADING("Weapons.Reloading.Message", "&eReloading..."),
    RELOADING_TITLE("Weapons.Reloading.Title", "&eReloading..."),
    RELOADING_SUBTITLE("Weapons.Reloading.Subtitle", "&7Clickerdy click."),
    MENU_DURABILITY_REMOVE("Weapons.Menu.Durability.Remove", "&c<Identifier><Durability> Durability"),
    MENU_DURABILITY_ADD("Weapons.Menu.Durability.Add", "&a<Identifier><Durability> Durability"),
    MENU_DURABILITY_CRAFT("Weapons.Menu.Durability.Craft", "&aCraft Weapon: &2<Durability> &aDurability"),
    MENU_DURABILITY_TITLE("Weapons.Menu.Durability.Title", "Weapon Builder <WeaponType>"),
    MENU_WEAPON_TITLE("Weapons.Menu.Weapon.Title", "Weapon Menu"),
    MENU_WEAPON_BUTTON_CLOSE("Weapons.Menu.Weapon.Buttons.Close", "&cClose"),
    MENU_WEAPON_BUTTON_PAGE("Weapons.Menu.Weapon.Buttons.Page", "&aPage <Page>"),
    MENU_AMMO_TITLE("Weapons.Menu.Ammo.Title", "Ammo Menu"),
    MENU_SWITCHER("Weapons.Menu.Switcher", "&aSwitch to &2<Menu>"),
    WEAPON_CANT_SHOOT_WIHTOUT_SCOPE("Weapons.CanShootWithoutScope", "&cSorry bro, no 360 noscope for you...");

    private final String path;
    private final String message;
    DefaultMessages(String path, String message) {
        this.path = path;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }
}
