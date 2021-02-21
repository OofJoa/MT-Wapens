package com.jazzkuh.mtwapens.commands;

import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.utils.ItemBuilder;
import com.jazzkuh.mtwapens.utils.Utils;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.StringUtil;

import java.util.*;

public class GiveWeaponCMD implements TabExecutor {

    private final Main plugin;

    public GiveWeaponCMD(Main plugin) {
        this.plugin = plugin;
        argumentHandler.put("<weaponType> <durability> <player>", "Give a weapon to a player.");
    }

    public HashMap<String, String> argumentHandler = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!Utils.checkPermission(sender, "mtwapens.command.giveweapon")) return false;

        if (args.length > 2) {

            ArrayList<String> weaponTypes = new ArrayList<>(plugin.getConfig().getConfigurationSection("weapons.").getKeys(false));

            if (plugin.getConfig().getString("weapons." + args[0] + ".name") != null) {
                if (Utils.isInt(args[1]) && Integer.parseInt(args[1]) > 0) {
                    if (Bukkit.getPlayer(args[2]) != null) {
                        Player target = Bukkit.getPlayer(args[2]);

                        giveWeapon(target, sender, args[0], Integer.parseInt(args[1]));
                    } else {
                        Utils.sendMessage(sender, "&cThe given player is not a valid player.");
                    }
                } else {
                    Utils.sendMessage(sender, "&cThe given durability is not a valid integer.");
                }
            } else {
                Utils.sendMessage(sender, "&cThe given weapon type is not a valid weapon. Please choose one of the following: " + weaponTypes.toString().replace("[", "").replace("]", ""));
            }
        } else {
            Utils.formatHelpMessage(argumentHandler, command, sender);
        }
        return true;
    }

    private void giveWeapon(Player player, CommandSender sender, String string, int durability) {
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
        Utils.sendMessage(sender, "&aSuccesfully gave " + player.getName() + " an " + type + " of " + durability + " durability.");
    }

    private void setNBT(ItemStack itemStack, String key, int value) {
        ItemStack itemStackMeta = NBTEditor.set(itemStack, value, key);
        ItemMeta itemMeta = itemStackMeta.getItemMeta();
        itemStack.setItemMeta(itemMeta);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("mtwapens.command.giveweapon")) {
            return Collections.emptyList();
        }

        if (args.length == 1) {
            return getApplicableTabCompleters(args[0],
                    plugin.getConfig().getConfigurationSection("weapons.").getKeys(false));
        }

        if (args.length == 3) {
            ArrayList<String> onlinePlayers = new ArrayList<>();

            for (Player player : Bukkit.getOnlinePlayers()) {
                onlinePlayers.add(player.getName());
            }

            return getApplicableTabCompleters(args[2], onlinePlayers);
        }

        return Collections.emptyList();
    }

    private List<String> getApplicableTabCompleters(String arg, Collection<String> completions) {
        return StringUtil.copyPartialMatches(arg, completions, new ArrayList<>(completions.size()));
    }
}
