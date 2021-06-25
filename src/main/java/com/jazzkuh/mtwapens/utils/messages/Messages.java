package com.jazzkuh.mtwapens.utils.messages;

import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.utils.Utils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

public class Messages {
    private final HashMap<DefaultMessages, String> cache = new HashMap<>();

    public Messages(Plugin plugin) {
        FileConfiguration messagesConfig = Main.getMessagesFile().getConfig();
        for (DefaultMessages message : DefaultMessages.values()) {
            String text = messagesConfig.getString(message.getPath());
            if (text == null) {
                messagesConfig.set(message.getPath(), message.getMessage());
                text = message.getMessage();
            }

            cache.put(message, Utils.color(text));
        }

        Main.getMessagesFile().saveConfig();
    }

    public String get(DefaultMessages message) {
        return cache.get(message);
    }
}
