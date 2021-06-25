package com.jazzkuh.mtwapens.commands;

import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.function.WeaponMenu;
import com.jazzkuh.mtwapens.utils.Utils;
import com.jazzkuh.mtwapens.utils.messages.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.*;

public class MainCMD implements TabExecutor {

    private final Main plugin;

    public MainCMD(Main plugin) {
        this.plugin = plugin;
        argumentHandler.put("reload", "Reload the plugin.");
        argumentHandler.put("menu", "Open the weapon menu.");
        argumentHandler.put("debug <weaponType>", "Check if a weapon is setup properly.");
    }

    public HashMap<String, String> argumentHandler = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage("This command is not usable by console.");
            return false;
        }

        Player player = (Player) sender;

        if (!sender.hasPermission("mtwapens.command.reload") || !sender.hasPermission("mtwapens.command.menu")  || !sender.hasPermission("mtwapens.command.debug")) {
            getPluginMessage(player);
            return false;
        }

        if (args.length > 0) {
            if (args[0].equals("reload")) {
                if (!Utils.checkPermission(sender, "mtwapens.command.reload")) return false;

                Main.getMessagesFile().reloadConfig();
                Main.messages = new Messages(plugin);
                plugin.reloadConfig();
                Utils.sendMessage(player, "&aSuccessfully reloaded the configuration files.");
            } else if (args[0].equalsIgnoreCase("menu")) {
                if (!Utils.checkPermission(sender, "mtwapens.command.menu")) return false;

                new WeaponMenu(plugin, 0).open(player);
            } else if (args[0].equalsIgnoreCase("debug")) {
                if (!Utils.checkPermission(sender, "mtwapens.command.debug")) return false;

                if (args.length > 1) {
                    Utils.debugWeapon(player, args[1]);
                } else {
                    Utils.formatHelpMessage(argumentHandler, command, sender);
                }
            } else {
                Utils.formatHelpMessage(argumentHandler, command, sender);
            }
        } else {
            Utils.formatHelpMessage(argumentHandler, command, sender);
        }

        return true;
    }

    private void getPluginMessage(Player player) {
        Utils.sendMessage(player, "&f");
        Utils.sendMessage(player, "&a" + plugin.getName() + " version: &2" + plugin.getDescription().getVersion() + "&a.");
        Utils.sendMessage(player, "&aDescription: &2" + plugin.getDescription().getDescription() + "&a.");
        Utils.sendMessage(player, "&aDownload: &2" + plugin.getDescription().getWebsite() + "&a.");
        Utils.sendMessage(player, "&aBy &2[Jazzkuh]&a.");
        Utils.sendMessage(player, "&f");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("mtwapens.command.reload") || !sender.hasPermission("mtwapens.command.menu") || !sender.hasPermission("mtwapens.command.debug")) {
            return Collections.emptyList();
        }

        if (args.length == 1) {
            return getApplicableTabCompleters(args[0],
                    Arrays.asList("reload", "menu", "debug"));
        }

        if (args[0].equalsIgnoreCase("debug") && args.length == 2) {
            return getApplicableTabCompleters(args[1],
                    plugin.getConfig().getConfigurationSection("weapons.").getKeys(false));
        }

        return Collections.emptyList();
    }

    private List<String> getApplicableTabCompleters(String arg, Collection<String> completions) {
        return StringUtil.copyPartialMatches(arg, completions, new ArrayList<>(completions.size()));
    }
}
