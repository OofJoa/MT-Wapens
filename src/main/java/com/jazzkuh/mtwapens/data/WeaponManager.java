package com.jazzkuh.mtwapens.data;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.function.Function;

public class WeaponManager {
    HashMap<String, Weapon> weapons = new HashMap<>();
    FileConfiguration weaponData;
    File weaponDataFile;

    public WeaponManager(Plugin plugin) {
        for (String weapon : plugin.getConfig().getConfigurationSection("weapons").getKeys(false)) {
            Function<String, Object> get = value -> plugin.getConfig().get("weapons." + weapon + "." + value);
            weapons.put(ChatColor.stripColor((String) get.apply("name")), new Weapon(
                    (String) get.apply("name"), (String) get.apply("ammo-name"), (double) get.apply("damage"),
                    (double) get.apply("attackspeed"), (int) get.apply("max-ammo")));
        }

        this.weaponDataFile = new File(plugin.getDataFolder(), "weapondata.yml");

        try {
            weaponDataFile.createNewFile();
        } catch (IOException e) {
            Bukkit.getServer().getLogger().severe("There was an error while creating weapondata.yml");
        }

        loadWeaponData();
    }

    public FileConfiguration getWeaponData() {
        return weaponData;
    }

    public void saveWeaponData() {
        try {
            weaponData.save(weaponDataFile);
        } catch (IOException e) {
            Bukkit.getServer().getLogger().severe("There was an error while saving weapondata.yml!");
        }
    }

    public void loadWeaponData() {
        weaponData = YamlConfiguration.loadConfiguration(weaponDataFile);
    }
}
