package com.jazzkuh.mtwapens.commands;

import com.google.common.collect.ImmutableList;
import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.function.WeaponFactory;
import com.jazzkuh.mtwapens.function.objects.Ammo;
import com.jazzkuh.mtwapens.utils.Utils;
import com.jazzkuh.mtwapens.utils.command.AbstractCommand;
import com.jazzkuh.mtwapens.utils.command.Argument;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AmmoCMD extends AbstractCommand {

    public AmmoCMD() {
        super("getammo",
                new Argument("<ammoType> [player]", "Grab ammo from the config files."));
    }

    @Override
    public void execute(CommandSender sender, Command command, String label, String[] args) {
        if (!hasPermission(getBasePermission(), false)) return;

        if (args.length > 0) {
            ArrayList<String> ammoTypes = new ArrayList<>(Main.getAmmo().getConfig().getConfigurationSection("ammo.").getKeys(false));

            if (Main.getAmmo().getConfig().getString("ammo." + args[0] + ".name") == null) {
                Utils.sendMessage(sender, "&cThe given ammo type is not a valid ammo type. Please choose one of the following: "
                        + StringUtils.join(ammoTypes, ", "));
                return;
            }

            String type = args[0];

            switch (args.length) {
                case 1: {
                    if (!senderIsPlayer()) return;
                    Player player = (Player) sender;
                    Ammo ammo = new Ammo(type);
                    WeaponFactory weaponFactory = new WeaponFactory(player);
                    weaponFactory.buildAmmo(ammo);
                    weaponFactory.addToInventory();
                    break;
                }
                case 2: {
                    if (Bukkit.getPlayer(args[1]) == null) {
                        Utils.sendMessage(sender, "&cJe hebt geen geldige speler opgegeven.");
                        return;
                    }

                    Player target = Bukkit.getPlayer(args[1]);

                    Ammo ammo = new Ammo(type);
                    WeaponFactory weaponFactory = new WeaponFactory(target);
                    weaponFactory.buildAmmo(ammo);
                    weaponFactory.addToInventory();
                    Utils.sendMessage(sender, "&aSuccesfully gave " + target.getName() + " ammo type " + type + ".");
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
                    Main.getAmmo().getConfig().getConfigurationSection("ammo.").getKeys(false));
        }

        if (args.length == 2) {
            return getApplicableTabCompleters(args[1], Utils.getPlayerNames());
        }

        return Collections.emptyList();
    }
}