package com.jazzkuh.mtwapens.commands;

import com.google.common.collect.ImmutableList;
import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.function.menu.WeaponMenu;
import com.jazzkuh.mtwapens.utils.Utils;
import com.jazzkuh.mtwapens.utils.command.AbstractCommand;
import com.jazzkuh.mtwapens.utils.command.Argument;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainCMD extends AbstractCommand {

    public MainCMD() {
        super("mtwapens",
                new Argument("reload", "Reload the plugin."),
                new Argument("menu", "Grab ammo and weapons from a menu."));
    }

    @Override
    public void execute(CommandSender sender, Command command, String label, String[] args) {
        if (!senderIsPlayer()) return;
        if (!hasPermission(getBasePermission() + ".reload", true)
                || !hasPermission(getBasePermission() + ".menu", true)) {
            sendDefaultMessage(sender);
            return;
        }

        Player player = (Player) sender;

        if (args.length > 0) {
            switch (args[0]) {
                case "reload": {
                    if (!hasPermission(getBasePermission() + ".reload", false)) return;

                    Main.getWeapons().reloadConfig();
                    Main.getAmmo().reloadConfig();
                    Main.getGrenades().reloadConfig();

                    Main.getMessages().reloadConfig();
                    Main.getInstance().reloadConfig();
                    Utils.sendMessage(player, "&aReloaded the configuration files, please check the console for any errors.");
                    break;
                }
                case "menu": {
                    if (!hasPermission(getBasePermission() + ".menu", false)) return;
                    new WeaponMenu(player, 0).open(player);
                    break;
                }
                default: {
                    sendNotEnoughArguments(this);
                    break;
                }
            }
        } else {
            sendNotEnoughArguments(this);
        }
    }

    private void sendDefaultMessage(CommandSender sender) {
        Utils.sendMessage(sender, "&8 ----------------------------------------------");
        Utils.sendMessage(sender, "&8| &2" + Main.getInstance().getDescription().getName() + " version: &a" + Main.getInstance().getDescription().getVersion());
        Utils.sendMessage(sender, "&8| &2Description: &a" + Main.getInstance().getDescription().getDescription());
        Utils.sendMessage(sender, "&8| &2Download: &a" + Main.getInstance().getDescription().getWebsite());
        Utils.sendMessage(sender, "&8| &2Authors: &aJazzkuh");
        Utils.sendMessage(sender, "&8 ----------------------------------------------");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(getBasePermission() + ".reload")
                || !sender.hasPermission(getBasePermission() + ".menu"))
            return Collections.emptyList();

        if (args.length == 1) {
            return getApplicableTabCompleters(args[0],
                    Arrays.asList("reload", "menu"));
        }

        return Collections.emptyList();
    }
}
