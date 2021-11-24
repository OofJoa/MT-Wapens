package me.oofjoa.oofarsenal;

import me.oofjoa.oofarsenal.commands.AmmoCMD;
import me.oofjoa.oofarsenal.commands.GrenadeCMD;
import me.oofjoa.oofarsenal.commands.MainCMD;
import me.oofjoa.oofarsenal.commands.WeaponCMD;
import me.oofjoa.oofarsenal.compatibility.CompatibilityLayer;
import me.oofjoa.oofarsenal.compatibility.CompatibilityManager;
import me.oofjoa.oofarsenal.function.listeners.*;
import me.oofjoa.oofarsenal.messages.Messages;
import me.oofjoa.oofarsenal.utils.ConfigurationFile;
import me.oofjoa.oofarsenal.utils.Metrics;
import me.oofjoa.oofarsenal.utils.Utils;
import me.oofjoa.oofarsenal.utils.menu.GUIHolder;
import de.slikey.effectlib.EffectManager;
import lombok.Getter;
import me.lucko.commodore.CommodoreProvider;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class Main extends JavaPlugin implements Listener {

    private static @Getter Main instance;
    private static @Getter
    ConfigurationFile weapons;
    private static @Getter ConfigurationFile ammo;
    private static @Getter ConfigurationFile grenades;
    private static @Getter ConfigurationFile messages;
    private static final @Getter HashMap<UUID, Boolean> reloadDelay = new HashMap<>();
    private static @Getter EffectManager effectManager;
    private static @Getter CompatibilityLayer compatibilityLayer;

    @Override
    public void onEnable() {
        instance = this;

        GUIHolder.init(this);
        effectManager = new EffectManager(this);
        new Metrics(this, 7967);

        compatibilityLayer = new CompatibilityManager().registerCompatibilityLayer();
        if (compatibilityLayer == null) {
            this.getLogger().warning("This server is using an unsupported server version. NMS functions are disabled.");
        } else {
            this.getLogger().info("This server is running " + Bukkit.getServer().getVersion() + ". Now using CompatbilityLayer version " + new CompatibilityManager().getVersion());
            Bukkit.getPluginManager().registerEvents(new ProjectileHitListener(), this);
        }


        Bukkit.getPluginManager().registerEvents(new PlayerItemHeldListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), this);
        Bukkit.getPluginManager().registerEvents(new WeaponDamageListener(), this);
        Bukkit.getPluginManager().registerEvents(new WeaponFireListener(), this);
        Bukkit.getPluginManager().registerEvents(new GrenadeLaunchListener(), this);
        Bukkit.getPluginManager().registerEvents(new GrenadeHitListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new EntityDamageListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerSneakListener(), this);
        Bukkit.getPluginManager().registerEvents(new WeaponRestoreModifiedListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerSwapHandListener(), this);

        new MainCMD().register(this);
        new WeaponCMD().register(this);
        new AmmoCMD().register(this);
        new GrenadeCMD().register(this);

        weapons = new ConfigurationFile(this, "weapons.yml", true);
        weapons.saveConfig();

        ammo = new ConfigurationFile(this, "ammo.yml", true);
        ammo.saveConfig();

        grenades = new ConfigurationFile(this, "grenades.yml", true);
        grenades.saveConfig();

        messages = new ConfigurationFile(this, "messages.yml", false);
        Messages.init();
        messages.saveConfig();

        this.saveDefaultConfig();
        this.saveConfig();

        this.getLogger().info("OofArsenal version " + this.getDescription().getVersion() + " has been loaded.");

        if (CommodoreProvider.isSupported()) {
            this.getLogger().info("Version is 1.13+, using commodore.");
        }
    }
}
