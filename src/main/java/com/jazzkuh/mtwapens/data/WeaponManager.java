package com.jazzkuh.mtwapens.data;

import com.jazzkuh.mtwapens.utility.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.function.Function;

public class WeaponManager {
    HashMap<String, WeaponType> weaponTypes = new HashMap<>();
    HashMap<String, Weapon> weaponCache = new HashMap<>();
    FileConfiguration weaponData;
    File weaponDataFile;

    public WeaponManager(Plugin plugin) {
        for (String weapon : plugin.getConfig().getConfigurationSection("weapons").getKeys(false)) {
            Function<String, Object> get = value -> plugin.getConfig().get("weapons." + weapon + "." + value);
            weaponTypes.put(Utils.color((String) get.apply("name")), new WeaponType(weapon,
                    (String) get.apply("name"), (String) get.apply("ammo-name"), (double) get.apply("damage"),
                    (double) get.apply("attackspeed"), (int) get.apply("max-ammo")));
        }

        weaponDataFile = new File(plugin.getDataFolder(), "weapondata.yml");

        try {
            weaponDataFile.createNewFile();
        } catch (IOException e) {
            Bukkit.getServer().getLogger().severe("There was an error while creating weapondata.yml");
        }

        loadWeaponData();
    }

    public WeaponType getWeaponType(String displayName) {
        return weaponTypes.get(displayName);
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

    public Weapon getWeapon(String uuid) {
        return weaponCache.get(uuid);
    }

    public Weapon putWeapon(String uuid, Weapon weapon) {
        return weaponCache.put(uuid, weapon);
    }
}
