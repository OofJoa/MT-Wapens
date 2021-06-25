package com.jazzkuh.mtwapens.commands;

import com.cryptomorin.xseries.XMaterial;
import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.utils.ItemBuilder;
import com.jazzkuh.mtwapens.utils.Utils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

import java.util.*;

public class AmmoCMD implements TabExecutor {

    private final Main plugin;

    public AmmoCMD(Main plugin) {
        this.plugin = plugin;
        argumentHandler.put("<ammoType>", "Get an ammo item from the configuration files.");
    }

    public HashMap<String, String> argumentHandler = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage("This command is not usable by console.");
            return false;
        }

        Player player = (Player) sender;

        if (!Utils.checkPermission(sender, "mtwapens.command.getammo")) return false;

        if (args.length > 0) {

            ArrayList<String> weaponTypes = new ArrayList<>(plugin.getConfig().getConfigurationSection("ammo.").getKeys(false));

            if (plugin.getConfig().getString("ammo." + args[0] + ".name") != null) {
                getAmmo(player, args[0]);
            } else {
                Utils.sendMessage(player, "&cThe given ammo type is not a valid ammo type. Please choose one of the following: " + weaponTypes.toString().replace("[", "").replace("]", ""));
            }
        } else {
            Utils.formatHelpMessage(argumentHandler, command, sender);
        }
        return true;
    }

    public static void getAmmo(Player player, String string) {

        String type = string.toLowerCase();

        ArrayList<String> lore = new ArrayList<>();
        for (String lorePart : Main.getInstance().getConfig().getStringList("ammo." + type + ".lore")) {
            lore.add(Utils.color(lorePart));
        }

        ItemStack bulletItem = new ItemBuilder(XMaterial.matchXMaterial( Main.getInstance().getConfig().getString("ammo." + type + ".material")).get().parseMaterial())
                .setName(Utils.color( Main.getInstance().getConfig().getString("ammo." + type + ".name")))
                .setNBT(Main.getInstance().getConfig().getString("ammo." + type + ".nbt"),  Main.getInstance().getConfig().getString("ammo." + type + ".nbtvalue"))
                .setLore(lore)
                .toItemStack();

        player.getInventory().addItem(bulletItem);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("mtwapens.command.getammo")) {
            return Collections.emptyList();
        }

        if (args.length == 1) {
            return getApplicableTabCompleters(args[0],
                    plugin.getConfig().getConfigurationSection("ammo.").getKeys(false));
        }

        return Collections.emptyList();
    }

    private List<String> getApplicableTabCompleters(String arg, Collection<String> completions) {
        return StringUtil.copyPartialMatches(arg, completions, new ArrayList<>(completions.size()));
    }
}