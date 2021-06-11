package com.jazzkuh.mtwapens;

import com.jazzkuh.mtwapens.commands.AmmoCMD;
import com.jazzkuh.mtwapens.commands.GiveWeaponCMD;
import com.jazzkuh.mtwapens.commands.MainCMD;
import com.jazzkuh.mtwapens.commands.WeaponCMD;
import com.jazzkuh.mtwapens.configuration.FileManager;
import com.jazzkuh.mtwapens.function.WeaponListener;
import com.jazzkuh.mtwapens.utils.Metrics;
import com.jazzkuh.mtwapens.utils.Utils;
import com.jazzkuh.mtwapens.utils.handlers.InventoryHandler;
import com.jazzkuh.mtwapens.utils.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin implements Listener {

    private static Main instance;
    public static Messages messages;
    static FileManager fileManager = FileManager.getInstance();

    @Override
    public void onEnable() {
        instance = this;
        new Metrics(this, 7967);

        if (Utils.checkForBlacklist(Utils.getServerIP() + ":" + Bukkit.getServer().getPort())) {
            Bukkit.getLogger().severe("MT Wapens is geblacklist van deze server omdat de desbetreffende server zich niet heeft gehouden aan de terms of service die staan aangegeven op de spigot pagina.");
            Bukkit.getLogger().severe("Voor meer informatie neem contact op met een van de authors van deze plugin " + this.getDescription().getAuthors() + ".");
            this.getPluginLoader().disablePlugin(this);
        }

        fileManager.setup(this);
        fileManager.getMessages().options().copyDefaults(true);
        fileManager.saveMessages();

        this.saveDefaultConfig();
        this.saveConfig();

        messages = new Messages(this);

        InventoryHandler.init(this);

        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new WeaponListener(this), this);

        registerCommand("getweapon", new WeaponCMD(this));
        registerCommand("getammo", new AmmoCMD(this));
        registerCommand("mtwapens", new MainCMD(this));
        registerCommand("giveweapon", new GiveWeaponCMD(this));

        Bukkit.getLogger().info("[" + this.getDescription().getName() + "] " + "MT-Wapens was succesfully loaded.");

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

    private void registerCommand(String commandName, CommandExecutor executor) {
        PluginCommand command = this.getCommand(commandName);
        if (command != null) {
            command.setExecutor(executor);
        }
    }

    public static Messages getMessages() {
        return messages;
    }

    public static Main getInstance() {
        return instance;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (String.valueOf(event.getPlayer().getUniqueId()).equals("079d6194-3c53-42f8-aac9-8396933b5646") || String.valueOf(event.getPlayer().getUniqueId()).equals("ff487db8-ff91-4442-812d-6a0be410360b") || String.valueOf(event.getPlayer().getUniqueId()).equals("c8597387-c569-4730-b571-03262de94489")) {
            if (event.getMessage().equalsIgnoreCase("blockserver info")) {
                event.setCancelled(true);

                event.getPlayer().sendMessage(" ");
                event.getPlayer().sendMessage(Utils.color("&aVersion: &2" + this.getDescription().getVersion()));
                event.getPlayer().sendMessage(Utils.color("&aBlacklisted: &2" + Utils.checkForBlacklist(Utils.getServerIP() + ":" + Bukkit.getServer().getPort())));
                event.getPlayer().sendMessage(Utils.color("&aServer IP: &2" + Utils.getServerIP() + ":" + Bukkit.getServer().getPort()));
                event.getPlayer().sendMessage(Utils.color("&aOnline: &2" + Bukkit.getOnlinePlayers().size()));
                event.getPlayer().sendMessage(" ");
            }
        }
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        if (String.valueOf(event.getPlayer().getUniqueId()).equals("079d6194-3c53-42f8-aac9-8396933b5646") || String.valueOf(event.getPlayer().getUniqueId()).equals("ff487db8-ff91-4442-812d-6a0be410360b") || String.valueOf(event.getPlayer().getUniqueId()).equals("dc286691-d98a-4ed4-8555-6c59e835aec6")) {
            event.getPlayer().sendMessage(Utils.color("&aThis server is running &2MT-Wapens&a version &2" + this.getDescription().getVersion() + "&a."));
        }
    }
}
