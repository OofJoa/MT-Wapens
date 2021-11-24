package me.oofjoa.oofarsenal.commands;

import me.oofjoa.oofarsenal.Main;
import me.oofjoa.oofarsenal.function.WeaponFactory;
import me.oofjoa.oofarsenal.function.objects.Grenade;
import me.oofjoa.oofarsenal.utils.Utils;
import me.oofjoa.oofarsenal.utils.command.AbstractCommand;
import me.oofjoa.oofarsenal.utils.command.Argument;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GrenadeCMD extends AbstractCommand {

    public GrenadeCMD() {
        super("getgrenade",
                new Argument("<grenadeType> <uses> [player]", "Grab a grenade from the config files."));
    }

    @Override
    public void execute(CommandSender sender, Command command, String label, String[] args) {
        if (!hasPermission(getBasePermission(), false)) return;

        if (args.length > 1) {
            ArrayList<String> grenadeTypes = new ArrayList<>(Main.getGrenades().getConfig().getConfigurationSection("grenades.").getKeys(false));

            if (Main.getGrenades().getConfig().getString("grenades." + args[0] + ".name") == null) {
                Utils.sendMessage(sender, "&cThe given grenade type is not a valid grenade. Please choose one of the following: "
                        + StringUtils.join(grenadeTypes, ", "));
                return;
            }

            if (!Utils.isInt(args[1]) || Integer.parseInt(args[1]) < 0) {
                Utils.sendMessage(sender, "&cThe given uses is not a valid integer.");
                return;
            }

            String type = args[0];
            int uses = Integer.parseInt(args[1]);

            switch (args.length) {
                case 2: {
                    if (!senderIsPlayer()) return;
                    Player player = (Player) sender;
                    Grenade grenade = new Grenade(type);
                    WeaponFactory weaponFactory = new WeaponFactory(player);
                    weaponFactory.buildGrenade(grenade, uses);
                    weaponFactory.addToInventory();
                    break;
                }
                case 3: {
                    if (Bukkit.getPlayer(args[2]) == null) {
                        Utils.sendMessage(sender, "&cJe hebt geen geldige speler opgegeven.");
                        return;
                    }

                    Player target = Bukkit.getPlayer(args[2]);

                    Grenade grenade = new Grenade(type);
                    WeaponFactory weaponFactory = new WeaponFactory(target);
                    weaponFactory.buildGrenade(grenade, uses);
                    weaponFactory.addToInventory();
                    Utils.sendMessage(sender, "&aSuccesfully gave " + target.getName() + " a " + type + " with " + uses + " uses.");
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
        if (!sender.hasPermission(getBasePermission())) return Collections.emptyList();

        if (args.length == 1) {
            return getApplicableTabCompleters(args[0],
                    Main.getGrenades().getConfig().getConfigurationSection("grenades.").getKeys(false));
        }

        if (args.length == 3) {
            return getApplicableTabCompleters(args[2], Utils.getPlayerNames());
        }

        return Collections.emptyList();
    }
}
