package com.jazzkuh.mtwapens.data;

import java.io.File;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class WeaponData implements Listener {
    static WeaponData instance = new WeaponData();

    Plugin p;

    FileConfiguration weapondata;

    File wfile;

    public static WeaponData getInstance() {
        return instance;
    }

    public void setup(Plugin p) {
        this.wfile = new File(p.getDataFolder(), "weapondata.yml");
        if (!this.wfile.exists())
            try {
                this.wfile.createNewFile();
            } catch (IOException e) {
                Bukkit.getServer().getLogger().severe("There was an error while creating weapondata!");
            }
        this.weapondata = (FileConfiguration)YamlConfiguration.loadConfiguration(this.wfile);
    }

    public FileConfiguration getWeaponData() {
        return this.weapondata;
    }

    public void saveWeaponData() {
        try {
            this.weapondata.save(this.wfile);
        } catch (IOException e) {
            Bukkit.getServer().getLogger().severe("There was an error while saving weapondata!");
        }
    }

    public void reloadWeaponData() {
        this.weapondata = (FileConfiguration)YamlConfiguration.loadConfiguration(this.wfile);
    }
}
