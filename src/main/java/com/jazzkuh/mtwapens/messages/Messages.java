package com.jazzkuh.mtwapens.messages;

import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.utils.Utils;

public enum Messages {
    NO_AMMO("Weapons.NoAmmo", "&cOut of ammo!"),
    USES("Weapons.Uses", "&9Uses: &a<Durability>"),
    AMMO_DURABILITY("Weapons.AmmoDurability", "&9Durability: &a<Durability>\n&9Ammo: &a<Ammo>&f/&c<MaxAmmo>"),
    RELOADING("Weapons.Reloading.Message", "&eReloading..."),
    RELOADING_TITLE("Weapons.Reloading.Title", "&eReloading..."),
    RELOADING_SUBTITLE("Weapons.Reloading.Subtitle", "&7Clickerdy click."),
    MENU_DURABILITY_CRAFT("Weapons.Menu.Durability.Craft", "&aCraft <Type>: &2<Durability> &a<DurabilityPhrase>"),
    MENU_DURABILITY_TITLE("Weapons.Menu.Durability.Title", "Weapon Builder <WeaponType>"),
    MENU_WEAPON_TITLE("Weapons.Menu.Weapon.Title", "Weapon Menu"),
    MENU_WEAPON_BUTTON_CLOSE("Weapons.Menu.Weapon.Buttons.Close", "&cClose"),
    MENU_WEAPON_BUTTON_PAGE("Weapons.Menu.Weapon.Buttons.Page", "&aPage <Page>"),
    MENU_AMMO_TITLE("Weapons.Menu.Ammo.Title", "Ammo Menu"),
    MENU_SWITCHER("Weapons.Menu.Switcher", "&aSwitch to &2<Menu>"),
    MENU_GRENADE_TITLE("Weapons.Menu.Grenade.Title", "Grenade Menu"),
    WEAPON_CANT_SHOOT_WIHTOUT_SCOPE("Weapons.CantShootWithoutScope", "&cSorry bro, no 360 noscope for you..."),
    WEAPON_CANT_SHOOT_IN_REGION("Weapons.CantShootInRegion", "&cThe use of weapons has been disabled in this region.");


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
