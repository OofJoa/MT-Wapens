package com.jazzkuh.mtwapens.commands;

import com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.ImmutableList;
import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.function.WeaponFactory;
import com.jazzkuh.mtwapens.utils.ItemBuilder;
import com.jazzkuh.mtwapens.utils.Utils;
import com.jazzkuh.mtwapens.utils.command.AbstractCommand;
import com.jazzkuh.mtwapens.utils.command.Argument;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

import java.util.*;

public class AmmoCMD extends AbstractCommand {

    public AmmoCMD() {
        super("getammo", ImmutableList.builder()
                .add(new Argument("<ammoType> [player]", "Grab ammo from the config files."))
                .build());
    }

    @Override
    public void execute(CommandSender sender, Command command, String label, String[] args) {
        if (!hasPermission(getBasePermission(), false)) return;

        if (args.length > 0) {
            ArrayList<String> ammoTypes = new ArrayList<>(Main.getInstance().getConfig().getConfigurationSection("ammo.").getKeys(false));

            if (Main.getInstance().getConfig().getString("weapons." + args[0] + ".name") == null) {
                Utils.sendMessage(sender, "&cThe given ammo type is not a valid ammo type. Please choose one of the following: "
                        + StringUtils.join(ammoTypes, ", "));
                return;
            }

            String type = args[0];

            switch (args.length) {
                case 1: {
                    if (!senderIsPlayer()) return;
                    Player player = (Player) sender;
                    new WeaponFactory(player).buildAmmo(type);
                    break;
                }
                case 2: {
                    if (Bukkit.getPlayer(args[1]) == null) {
                        Utils.sendMessage(sender, "&cJe hebt geen geldige speler opgegeven.");
                        return;
                    }

                    Player target = Bukkit.getPlayer(args[1]);

                    new WeaponFactory(target).buildAmmo(type);
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
                    Main.getInstance().getConfig().getConfigurationSection("ammo.").getKeys(false));
        }

        if (args.length == 2) {
            return getApplicableTabCompleters(args[1], Utils.getPlayerNames());
        }

        return Collections.emptyList();
    }
}