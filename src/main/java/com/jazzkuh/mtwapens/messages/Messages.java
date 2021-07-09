package com.jazzkuh.mtwapens.messages;

import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.utils.Utils;

public enum Messages {
    NO_AMMO("Weapons.NoAmmo", "&cOut of ammo!"),
    AMMO_DURABILITY("Weapons.AmmoDurability", "&9Durability: &a<Durability>\n&9Ammo: &a<Ammo>&f/&c<MaxAmmo>"),
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
    WEAPON_CANT_SHOOT_WIHTOUT_SCOPE("Weapons.CantShootWithoutScope", "&cSorry bro, no 360 noscope for you...");


    private final String path;
    private final String message;
    Messages(String path, String message) {
        this.path = path;
        this.message = message;
    }

    private String getMessage() {
        return message;
    }

    private String getPath() {
        return path;
    }

    public String get() {
        String msg = Main.getMessages().getConfig().getString(this.getPath());
        return Utils.color(msg);
    }

    public static void init() {
        for (Messages msg : Messages.values()) {
            if (Main.getMessages().getConfig().getString(msg.getPath()) == null) {
                Main.getMessages().getConfig().set(msg.getPath(), msg.getMessage());
            }
        }
    }
}
