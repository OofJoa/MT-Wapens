package com.jazzkuh.mtwapens.utility.messages;

public enum Message {
    NO_PERMISSION("error.no-permission", "&cJe hebt niet genoeg rechten om dit command uit te voeren. Je hebt de permission <permission> nodig."),
    INVALID_DURABILITY("error.invalid-durability", "&cDe durability kan niet minder zijn dan 0"),
    WEAPON_RECEIVED("weapon-received", "<pc>Je hebt succesvol het wapen <sc><weapontype><pc> (<sc><durability> Durability<pc>) ontvangen."),
    VOUCHER_RECEIVED("voucher-received", "<pc>Je hebt succesvol een voucher voor het wapen <sc><weapontype><pc> (<sc><durability> Durability<pc>) ontvangen."),
    AMMO_RECEIVED("ammo-received", "<pc>Je hebt succesvol ammo voor het wapen <sc><weapontype><pc> ontvangen."),
    INVALID_WEAPON("error.invalid-number", "<sc>Dit type wapen bestaat niet. Kies uit: <weapons>."),
    FILES_RELOADED("files-reloaded", "<pc>Je hebt succesvol de <sc>config files<pc> van MT-Wapens herladen."),
    SHOT_INFO_DURABILITY("shot.info.durability", "&9Durability: &a<durability>"),
    SHOT_INFO_AMMO("shot.info.ammo", "&9Ammo: &a<ammo>&f/&c<max-ammo>"),
    SHOT_HIT_OTHER("shot.hit.other", "&cJe hebt &4<player> &cgeraakt met jouw schot!"),
    SHOT_HIT_YOU("shot.hit.you", "&cJe bent beschoten door &4<player>&c."),
    RELOADING("reload.message", "&eReloading..."),
    RELOADING_TITLE("reload.title", "&eReloading..."),
    RELOADING_SUBTITLE("reload.subtitle", "&7Clickerdy click."),
    NO_AMMO("no-ammo", "&cOut of ammo!");

    private String path, defaultMessage;
    Message(String path, String defaultMessage) {
        this.path = path;
        this.defaultMessage = defaultMessage;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public String getPath() {
        return path;
    }
}
