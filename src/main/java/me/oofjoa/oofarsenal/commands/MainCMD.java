package me.oofjoa.oofarsenal.commands;

import me.oofjoa.oofarsenal.Main;
import me.oofjoa.oofarsenal.function.menu.WeaponMenu;
import me.oofjoa.oofarsenal.utils.Utils;
import me.oofjoa.oofarsenal.utils.command.AbstractCommand;
import me.oofjoa.oofarsenal.utils.command.Argument;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainCMD extends AbstractCommand {

    public MainCMD() {
        super("oofarsenal",
                new Argument("reload", "Reload the plugin."),
                new Argument("menu", "Grab ammo and weapons from a menu."));
    }

    @Override
    public void execute(CommandSender sender, Command command, String label, String[] args) {
        if (!senderIsPlayer()) return;
        if (!hasPermission(getBasePermission() + ".reload", true)
                || !hasPermission(getBasePermission() + ".menu", true)) {
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
