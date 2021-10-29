package com.jazzkuh.mtwapens.commands;

import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.function.WeaponFactory;
import com.jazzkuh.mtwapens.function.objects.Melee;
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

public class MeleeCMD extends AbstractCommand {

    public MeleeCMD() {
        super("getmelee",
                new Argument("<meleeType> <durability> [player]", "Grab a melee weapon from the config files."));
    }

    @Override
    public void execute(CommandSender sender, Command command, String label, String[] args) {
        if (!hasPermission(getBasePermission(), false)) return;

        if (args.length > 1) {
            ArrayList<String> meleeTypes = new ArrayList<>(Main.getMelee().getConfig().getConfigurationSection("melee.").getKeys(false));

            if (Main.getMelee().getConfig().getString("melee." + args[0] + ".name") == null) {
                Utils.sendMessage(sender, "&cThe given melee weapon type is not a valid melee weapon. Please choose one of the following: "
                        + StringUtils.join(meleeTypes, ", "));
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
                    Melee melee = new Melee(type);
                    WeaponFactory weaponFactory = new WeaponFactory(player);
                    weaponFactory.buildMelee(melee, durability);
                    weaponFactory.addToInventory();
                    break;
                }
                case 3: {
                    if (Bukkit.getPlayer(args[2]) == null) {
                        Utils.sendMessage(sender, "&cJe hebt geen geldige speler opgegeven.");
                        return;
                    }

                    Player target = Bukkit.getPlayer(args[2]);

                    Melee melee = new Melee(type);
                    WeaponFactory weaponFactory = new WeaponFactory(target);
                    weaponFactory.buildMelee(melee, durability);
                    weaponFactory.addToInventory();
                    Utils.sendMessage(sender, "&aSuccesfully gave " + target.getName() + " a " + type + " with " + durability + " durability.");
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
                    Main.getMelee().getConfig().getConfigurationSection("melee.").getKeys(false));
        }

        if (args.length == 3) {
            return getApplicableTabCompleters(args[2], Utils.getPlayerNames());
        }

        return Collections.emptyList();
    }
}
