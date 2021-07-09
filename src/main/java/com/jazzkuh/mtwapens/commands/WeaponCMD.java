package com.jazzkuh.mtwapens.commands;

import com.google.common.collect.ImmutableList;
import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.function.WeaponFactory;
import com.jazzkuh.mtwapens.function.objects.Weapon;
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

public class WeaponCMD extends AbstractCommand {

    public WeaponCMD() {
        super("getweapon", ImmutableList.builder()
                .add(new Argument("<weaponType> <durability> [player]", "Grab a weapon from the config files."))
                .build());
    }

    @Override
    public void execute(CommandSender sender, Command command, String label, String[] args) {
        if (!hasPermission(getBasePermission(), false)) return;

        if (args.length > 1) {
            ArrayList<String> weaponTypes = new ArrayList<>(Main.getWeapons().getConfig().getConfigurationSection("weapons.").getKeys(false));

            if (Main.getWeapons().getConfig().getString("weapons." + args[0] + ".name") == null) {
                Utils.sendMessage(sender, "&cThe given weapon type is not a valid weapon. Please choose one of the following: "
                        + StringUtils.join(weaponTypes, ", "));
                return;
            }

            if (!Utils.isInt(args[1]) || Integer.parseInt(args[1]) < 0) {
                Utils.sendMessage(sender, "&cThe given durability is not a valid integer.");
                return;
            }

            String type = args[0];
            int durability = Integer.parseInt(args[1]);

            switch (args.length) {
                case 2: {
                    if (!senderIsPlayer()) return;
                    Player player = (Player) sender;
                    Weapon weapon = new Weapon(type);
                    new WeaponFactory(player).buildWeapon(weapon, durability);
                    break;
                }
                case 3: {
                    if (Bukkit.getPlayer(args[2]) == null) {
                        Utils.sendMessage(sender, "&cJe hebt geen geldige speler opgegeven.");
                        return;
                    }

                    Player target = Bukkit.getPlayer(args[2]);

                    Weapon weapon = new Weapon(type);
                    new WeaponFactory(target).buildWeapon(weapon, durability);
                    Utils.sendMessage(sender, "&aSuccesfully gave " + target.getName() + " an " + type + " of " + durability + " durability.");
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
                    Main.getWeapons().getConfig().getConfigurationSection("weapons.").getKeys(false));
        }

        if (args.length == 3) {
            return getApplicableTabCompleters(args[2], Utils.getPlayerNames());
        }

        return Collections.emptyList();
    }
}
