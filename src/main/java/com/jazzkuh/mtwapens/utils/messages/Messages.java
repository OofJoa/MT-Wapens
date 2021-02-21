package com.jazzkuh.mtwapens.utils.messages;

import com.jazzkuh.mtwapens.configuration.FileManager;
import com.jazzkuh.mtwapens.utils.Utils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

public class Messages {
    private final HashMap<DefaultMessages, String> cache = new HashMap<>();
    static FileManager fileManager = FileManager.getInstance();

    public Messages(Plugin plugin) {
        FileConfiguration messagesConfig = fileManager.getMessages();
        for (DefaultMessages message : DefaultMessages.values()) {
            String text = messagesConfig.getString(message.getPath());
            if (text == null) {
                messagesConfig.set(message.getPath(), message.getMessage());
                text = message.getMessage();
            }

            cache.put(message, Utils.color(text));
        }

        FileManager.getInstance().saveMessages();
    }

    public String get(DefaultMessages message) {
        return cache.get(message);
    }
}
