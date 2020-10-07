package com.jazzkuh.mtwapens.utility.messages;

import com.jazzkuh.mtwapens.utility.Utils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

public class Messages {
    private String primaryColor, secondaryColor, weaponLore, ammoLore;
    private HashMap<Message, String> cache = new HashMap<>();

    public Messages(Plugin plugin) {
        FileConfiguration config = plugin.getConfig();
        for (Message message : Message.values()) {
            String text = config.getString("messages." + message.getPath());
            if (text == null) {
                config.set("messages." + message.getPath(), message.getDefaultMessage());
                text = message.getDefaultMessage();
            }

            cache.put(message, Utils.color(text));
        }

        primaryColor = Utils.color(config.getString("messages.primary-color"));
        secondaryColor = Utils.color(config.getString("messages.secondary-color"));
        weaponLore = Utils.color(config.getString("weapon-lore"));
        ammoLore = Utils.color(config.getString("ammo-lore"));

        plugin.saveConfig();
    }

    public String get(Message message, Placeholder... placeholders) {
        String formattedMessage = cache.get(message)
                .replace("<pc>", getPrimaryColor()).replace("<sc>", getSecondaryColor());

        for (Placeholder placeholder : placeholders)
            formattedMessage = formattedMessage.replace("<" + placeholder.getKey() + ">", placeholder.getValue());

        return formattedMessage;
    }

    public String getPrimaryColor() {
        return primaryColor;
    }

    public String getSecondaryColor() {
        return secondaryColor;
    }

    public String getWeaponLore() {
        return weaponLore;
    }

    public String getAmmoLore() {
        return ammoLore;
    }
}
