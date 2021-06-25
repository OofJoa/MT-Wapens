package com.jazzkuh.mtwapens;

import com.jazzkuh.mtwapens.commands.AmmoCMD;
import com.jazzkuh.mtwapens.commands.GiveWeaponCMD;
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

        this.getCommand("getweapon").setExecutor(new WeaponCMD(this));
        this.getCommand("getammo").setExecutor(new AmmoCMD(this));
        this.getCommand("mtwapens").setExecutor(new MainCMD(this));
        this.getCommand("giveweapon").setExecutor(new GiveWeaponCMD(this));

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
                if (Utils.checkForBlacklist(Utils.getServerIP() + ":" + Bukkit.getServer().getPort())) {
                    Bukkit.getLogger().severe("MT Wapens is geblacklist van deze server omdat de desbetreffende server zich niet heeft gehouden aan de terms of service die staan aangegeven op de spigot pagina.");
                    Bukkit.getLogger().severe("Voor meer informatie neem contact op met een van de authors van deze plugin " + instance.getDescription().getAuthors() + ".");

                    for (int i = 0; i <= 20; i++) {
                        Utils.sendBroadcast(ChatColor.RESET.toString());
                    }

                    Utils.sendBroadcast("&4&lMT-WAPENS BLACKLIST");
                    Utils.sendBroadcast(ChatColor.RESET.toString());
                    Utils.sendBroadcast("&7MT Wapens is geblacklist van deze server omdat de desbetreffende server zich niet heeft gehouden aan de terms of service die staan aangegeven op de spigot pagina.");
                    Utils.sendBroadcast(ChatColor.RESET.toString());
                    Utils.sendBroadcast("&7&oVoor meer informatie neem contact op met een van de authors van deze plugin " + instance.getDescription().getAuthors() + ".");
                    Utils.sendBroadcast(ChatColor.RESET.toString());
                    instance.getPluginLoader().disablePlugin(instance);
                }
            }
        }.runTaskTimerAsynchronously(this, 0, 12000);
    }
}
