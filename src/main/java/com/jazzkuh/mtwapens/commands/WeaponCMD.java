package com.jazzkuh.mtwapens.commands;

import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.utils.ItemBuilder;
import com.jazzkuh.mtwapens.utils.Utils;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.StringUtil;

import java.util.*;

public class WeaponCMD implements TabExecutor {

    private final Main plugin;

    public WeaponCMD(Main plugin) {
        this.plugin = plugin;
        argumentHandler.put("<weaponType> <durability>", "Get a weapon item from the configuration files.");
    }

    public HashMap<String, String> argumentHandler = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage("This command is not usable by console.");
            return false;
        }

        Player player = (Player) sender;

        if (!Utils.checkPermission(sender, "mtwapens.command.getweapon")) return false;

        if (args.length > 1) {

            ArrayList<String> weaponTypes = new ArrayList<>(plugin.getConfig().getConfigurationSection("weapons.").getKeys(false));

            if (plugin.getConfig().getString("weapons." + args[0] + ".name") != null) {
                if (Utils.isInt(args[1]) && Integer.parseInt(args[1]) > 0) {
                    getWeapon(plugin, player, args[0], Integer.parseInt(args[1]));
                } else {
                    Utils.sendMessage(player, "&cThe given durability is not a valid integer.");
                }
            } else {
                Utils.sendMessage(player, "&cThe given weapon type is not a valid weapon. Please choose one of the following: " + weaponTypes.toString().replace("[", "").replace("]", ""));
            }
        } else {
            Utils.formatHelpMessage(argumentHandler, command, sender);
        }
        return true;
    }

    public static void getWeapon(Plugin plugin, Player player, String string, int durability) {
        String type = string.toLowerCase();

        ArrayList<String> lore = new ArrayList<>();
        for (String lorePart : plugin.getConfig().getStringList("weapons." + type + ".lore")) {
            String finalPart = lorePart
                    .replace("<Ammo>", String.valueOf(plugin.getConfig().getInt("weapons." + type + ".max-ammo")))
                    .replace("<MaxAmmo>", String.valueOf(plugin.getConfig().getInt("weapons." + type + ".max-ammo")))
                    .replace("<Damage>", String.valueOf(plugin.getConfig().getDouble("weapons." + type + ".damage")))
                    .replace("<Durability>", String.valueOf(durability));
            lore.add(Utils.color(finalPart));
        }

        ItemStack weapon = new ItemBuilder(Material.valueOf(plugin.getConfig().getString("weapons." + type + ".material")))
                .setName(Utils.color(plugin.getConfig().getString("weapons." + type + ".name")))
                .setNBT(plugin.getConfig().getString("weapons." + type + ".nbt"), plugin.getConfig().getString("weapons." + type + ".nbtvalue"))
                .setLore(lore)
                .toItemStack();

        ItemMeta im = weapon.getItemMeta();
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        weapon.setItemMeta(im);

        setNBT(weapon, "ammo", plugin.getConfig().getInt("weapons." + type + ".max-ammo"));
        setNBT(weapon, "durability", durability);

        player.getInventory().addItem(weapon);
    }

    private static void setNBT(ItemStack itemStack, String key, int value) {
        ItemStack itemStackMeta = NBTEditor.set(itemStack, value, key);
        ItemMeta itemMeta = itemStackMeta.getItemMeta();
        itemStack.setItemMeta(itemMeta);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("mtwapens.command.getweapon")) {
            return Collections.emptyList();
        }

        if (args.length == 1) {
            return getApplicableTabCompleters(args[0],
                    plugin.getConfig().getConfigurationSection("weapons.").getKeys(false));
        }

        return Collections.emptyList();
    }

    private List<String> getApplicableTabCompleters(String arg, Collection<String> completions) {
        return StringUtil.copyPartialMatches(arg, completions, new ArrayList<>(completions.size()));
    }
}
