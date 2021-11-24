package me.oofjoa.oofarsenal.messages;

import me.oofjoa.oofarsenal.Main;
import me.oofjoa.oofarsenal.utils.Utils;

public enum Messages {
    NO_AMMO("Weapons.NoAmmo", "&cOut of ammo!"),
    USES("Weapons.Uses", "&4Uses: &c<Durability>"),
    AMMO_DURABILITY("Weapons.AmmoDurability", "&4Durability: &c<Durability>\n&4Ammo: &c<Ammo>&f/&c<MaxAmmo>"),
    RELOADING("Weapons.Reloading.Message", "&4Reloading..."),
    RELOADING_TITLE("Weapons.Reloading.Title", "&4Reloading..."),
    RELOADING_SUBTITLE("Weapons.Reloading.Subtitle", "&cClickerdy click."),
    MENU_DURABILITY_CRAFT("Weapons.Menu.Durability.Craft", "&4Craft <Type>: &c<Durability> &a<DurabilityPhrase>"),
    MENU_DURABILITY_TITLE("Weapons.Menu.Durability.Title", "Weapon Builder <WeaponType>"),
    MENU_WEAPON_TITLE("Weapons.Menu.Weapon.Title", "Weapon Menu"),
    MENU_WEAPON_BUTTON_CLOSE("Weapons.Menu.Weapon.Buttons.Close", "&cClose"),
    MENU_WEAPON_BUTTON_PAGE("Weapons.Menu.Weapon.Buttons.Page", "&aPage <Page>"),
    MENU_AMMO_TITLE("Weapons.Menu.Ammo.Title", "Ammo Menu"),
    MENU_SWITCHER("Weapons.Menu.Switcher", "&aSwitch to &2<Menu>"),
    MENU_GRENADE_TITLE("Weapons.Menu.Grenade.Title", "Grenade Menu"),
    WEAPON_CANT_SHOOT_WIHTOUT_SCOPE("Weapons.CantShootWithoutScope", "&cJe bent verplicht te scopen met dit geweer!");


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
