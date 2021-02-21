package com.jazzkuh.mtwapens.configuration;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class FileManager {
    static FileManager instance = new FileManager();

    Plugin p;

    FileConfiguration messagedata;

    File file;

    public static FileManager getInstance() {
        return instance;
    }

    public void setup(Plugin p) {
        this.file = new File(p.getDataFolder(), "messages.yml");
        if (!this.file.exists())
            try {
                this.file.createNewFile();
            } catch (IOException e) {
                Bukkit.getLogger().severe("There was an error while creating messages.yml");
            }
        this.messagedata = YamlConfiguration.loadConfiguration(this.file);
    }

    public FileConfiguration getMessages() {
        return this.messagedata;
    }

    public void saveMessages() {
        try {
            this.messagedata.save(this.file);
        } catch (IOException e) {
        }
    }

    public void reloadMessages() {
        this.messagedata = YamlConfiguration.loadConfiguration(this.file);
    }
}
