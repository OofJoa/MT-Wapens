package com.jazzkuh.mtwapens;

import com.jazzkuh.mtwapens.commands.AmmoCMD;
import com.jazzkuh.mtwapens.commands.MainCMD;
import com.jazzkuh.mtwapens.commands.WeaponCMD;
import com.jazzkuh.mtwapens.function.DevListener;
import com.jazzkuh.mtwapens.function.WeaponListener;
import com.jazzkuh.mtwapens.utils.ConfigurationFile;
import com.jazzkuh.mtwapens.utils.Metrics;
import com.jazzkuh.mtwapens.utils.Utils;
import com.jazzkuh.mtwapens.utils.menu.GUIHolder;
import com.jazzkuh.mtwapens.utils.messages.Messages;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin implements Listener {

    private static @Getter Main instance;
    public static @Getter Messages messages;
    private static @Getter ConfigurationFile messagesFile;
    private static @Getter ConfigurationFile breakablesFile;

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
        Bukkit.getPluginManager().registerEvents(new WeaponListener(this), this);
        Bukkit.getPluginManager().registerEvents(new DevListener(this), this);

        new MainCMD().register(this);
        new WeaponCMD().register(this);
        new AmmoCMD().register(this);

        messagesFile = new ConfigurationFile(this, "messages.yml");
        messagesFile.saveConfig();

        breakablesFile = new ConfigurationFile(this, "breakables.yml");
        breakablesFile.saveConfig();

        messages = new Messages(this);

        this.saveDefaultConfig();
        this.saveConfig();

        this.getLogger().info("MT-Wapens was succesfully loaded.");

        new BukkitRunnable() {
            public void run() {
                checkBlacklistStatus(instance);
            }
        }.runTaskTimerAsynchronously(this, 0, 12000);
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
