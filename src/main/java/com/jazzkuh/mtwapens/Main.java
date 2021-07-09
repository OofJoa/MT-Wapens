package com.jazzkuh.mtwapens;

import com.jazzkuh.mtwapens.commands.AmmoCMD;
import com.jazzkuh.mtwapens.commands.MainCMD;
import com.jazzkuh.mtwapens.commands.WeaponCMD;
import com.jazzkuh.mtwapens.function.DevListener;
import com.jazzkuh.mtwapens.function.listeners.PlayerItemHeldListener;
import com.jazzkuh.mtwapens.function.listeners.PlayerQuitListener;
import com.jazzkuh.mtwapens.function.listeners.WeaponDamageListener;
import com.jazzkuh.mtwapens.function.listeners.WeaponFireListener;
import com.jazzkuh.mtwapens.utils.ConfigurationFile;
import com.jazzkuh.mtwapens.utils.Metrics;
import com.jazzkuh.mtwapens.utils.Utils;
import com.jazzkuh.mtwapens.utils.menu.GUIHolder;
import com.jazzkuh.mtwapens.utils.messages.Messages;
import lombok.Getter;
import me.lucko.commodore.CommodoreProvider;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class Main extends JavaPlugin implements Listener {

    private static @Getter Main instance;
    public static @Getter Messages messages;
    private static @Getter ConfigurationFile messagesFile;
    private static final @Getter HashMap<String, Boolean> reloadDelay = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;

        GUIHolder.init(this);
        new Metrics(this, 7967);

        if (Utils.checkForBlacklist(Utils.getServerIP() + ":" + Bukkit.getServer().getPort())) {
            Bukkit.getLogger().severe("MT Wapens is geblacklist van deze server omdat de desbetreffende server zich niet heeft gehouden aan de terms of service die staan aangegeven op de spigot pagina.");
            Bukkit.getLogger().severe("Voor meer informatie neem contact op met een van de authors van deze plugin " + this.getDescription().getAuthors() + ".");
            this.getPluginLoader().disablePlugin(this);
        }

        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new PlayerItemHeldListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), this);
        Bukkit.getPluginManager().registerEvents(new WeaponDamageListener(), this);
        Bukkit.getPluginManager().registerEvents(new WeaponFireListener(), this);
        Bukkit.getPluginManager().registerEvents(new DevListener(this), this);

        new MainCMD().register(this);
        new WeaponCMD().register(this);
        new AmmoCMD().register(this);

        messagesFile = new ConfigurationFile(this, "messages.yml");
        messagesFile.saveConfig();

        messages = new Messages(this);

        this.saveDefaultConfig();
        this.saveConfig();

        this.getLogger().info("MT-Wapens version " + this.getDescription().getVersion() + " has been loaded.");

        if (CommodoreProvider.isSupported()) {
            this.getLogger().info("Version is 1.13+, using commodore.");
        }

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            checkBlacklistStatus(instance);
        }, 0, 12000);
    }

    public void checkBlacklistStatus(Plugin plugin) {
        if (Utils.checkForBlacklist(Utils.getServerIP() + ":" + Bukkit.getServer().getPort())) {
            Bukkit.getLogger().severe("MT Wapens is geblacklist van deze server omdat de desbetreffende server zich niet heeft gehouden aan de terms of service die staan aangegeven op de spigot pagina.");
            Bukkit.getLogger().severe("Voor meer informatie neem contact op met een van de authors van deze plugin " + plugin.getDescription().getAuthors() + ".");

            for (int i = 0; i <= 20; i++) {
                Utils.sendBroadcast(ChatColor.RESET.toString());
            }

            Utils.sendBroadcast("&4&lMT-WAPENS BLACKLIST");
            Utils.sendBroadcast(ChatColor.RESET.toString());
            Utils.sendBroadcast("&7MT Wapens is geblacklist van deze server omdat de desbetreffende server zich niet heeft gehouden aan de terms of service die staan aangegeven op de spigot pagina.");
            Utils.sendBroadcast(ChatColor.RESET.toString());
            Utils.sendBroadcast("&7&oVoor meer informatie neem contact op met een van de authors van deze plugin " + plugin.getDescription().getAuthors() + ".");
            Utils.sendBroadcast(ChatColor.RESET.toString());
            plugin.getPluginLoader().disablePlugin(plugin);
        }
    }
}
