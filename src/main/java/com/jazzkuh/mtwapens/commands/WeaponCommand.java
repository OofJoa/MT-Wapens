package com.jazzkuh.mtwapens.commands;

import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.data.WeaponData;
import com.jazzkuh.mtwapens.listeners.WeaponMenuListener;
import com.jazzkuh.mtwapens.listeners.WeaponPartListener;
import com.jazzkuh.mtwapens.utility.ItemBuilder;
import com.jazzkuh.mtwapens.utility.Utils;
import com.jazzkuh.mtwapens.utility.messages.Message;
import com.jazzkuh.mtwapens.utility.messages.Messages;
import com.jazzkuh.mtwapens.utility.messages.Placeholder;
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

public class WeaponCommand implements TabExecutor {
    private final Main plugin;

    public WeaponCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        Messages messages = Main.getMessages();

        if (!sender.hasPermission(this.plugin.getName() + ".command." + command.getName())) {
            String primaryColor = messages.getPrimaryColor();
            String secondaryColor = messages.getSecondaryColor();

            sender.sendMessage(Utils.color("&f"));
            sender.sendMessage(primaryColor + plugin.getName()
                    + " version: " + secondaryColor + plugin.getDescription().getVersion() + primaryColor + ".");
            sender.sendMessage(primaryColor + "Description: " + secondaryColor +
                    plugin.getDescription().getDescription() + primaryColor + ".");
            sender.sendMessage(primaryColor + "Download: " + secondaryColor + plugin.getDescription().getWebsite()
                    + primaryColor + ".");
            sender.sendMessage(primaryColor + "By " + secondaryColor + "[Jazzkuh]" + primaryColor + ".");
            sender.sendMessage(Utils.color("&f"));
        } else {
            if (args.length == 3 && args[0].equalsIgnoreCase("getweapon")) {
                if (!sender.hasPermission(this.plugin.getName() + ".command." + command.getName() + "." + args[0])) {
                    Utils.noPermission(sender, this.plugin.getName() + ".command." + command.getName() + "." + args[0]);
                    return true;
                }

                if (args[1].equalsIgnoreCase("deserteagle") || args[1].equalsIgnoreCase("magnum44")
                        || args[1].equalsIgnoreCase("waltherp99") || args[1].equalsIgnoreCase("glock19")
                        || args[1].equalsIgnoreCase("m16a4")) {
                    if (Utils.isInt(args[2]) && Integer.parseInt(args[2]) > 0 && args[2] != null) {
                        getWeapon(player, Integer.parseInt(args[2]), args[1]);
                    } else {
                        sender.sendMessage(messages.get(Message.INVALID_DURABILITY, Placeholder.of("argument", 2)));
                    }
                } else {
                    sender.sendMessage(messages.get(Message.INVALID_WEAPON,
                            Placeholder.of("weapons", "DesertEagle, Magnum44, WaltherP99, Glock19 of M16A4")));
                }
            } else if (args.length == 2 && args[0].equalsIgnoreCase("getammo")) {
                if (!sender.hasPermission(this.plugin.getName() + ".command." + command.getName() + "." + args[0])) {
                    Utils.noPermission(sender, this.plugin.getName() + ".command." + command.getName() + "." + args[0]);
                    return true;
                }

                if (args[1].equalsIgnoreCase("deserteagle") || args[1].equalsIgnoreCase("magnum44")
                        || args[1].equalsIgnoreCase("waltherp99") || args[1].equalsIgnoreCase("glock19")
                        || args[1].equalsIgnoreCase("m16a4")) {
                    getAmmo(player, args[1]);
                } else {
                    sender.sendMessage(messages.get(Message.INVALID_WEAPON,
                            Placeholder.of("weapons", "DesertEagle, Magnum44, WaltherP99, Glock19 of M16A4")));
                }
            } else if (args.length == 3 && args[0].equalsIgnoreCase("voucher")) {
                if (!sender.hasPermission(this.plugin.getName() + ".command." + command.getName() + "." + args[0])) {
                    Utils.noPermission(sender, this.plugin.getName() + ".command." + command.getName() + "." + args[0]);
                    return true;
                }

                if (args[1].equalsIgnoreCase("deserteagle") || args[1].equalsIgnoreCase("magnum44")
                        || args[1].equalsIgnoreCase("waltherp99") || args[1].equalsIgnoreCase("glock19")
                        || args[1].equalsIgnoreCase("m16a4")) {
                    if (Utils.isInt(args[2]) && Integer.parseInt(args[2]) > 0 && args[2] != null) {
                        getVoucher(player, Integer.parseInt(args[2]), args[1]);
                    } else {
                        sender.sendMessage(messages.get(Message.INVALID_DURABILITY, Placeholder.of("argument", 2)));
                    }
                } else {
                    sender.sendMessage(messages.get(Message.INVALID_WEAPON,
                            Placeholder.of("weapons", "DesertEagle, Magnum44, WaltherP99, Glock19 of M16A4")));
                }
            } else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission(this.plugin.getName() + ".command." + command.getName() + "." + args[0])) {
                    Utils.noPermission(sender, this.plugin.getName() + ".command." + command.getName() + "." + args[0]);
                    return true;
                }

                plugin.reloadConfig();
                WeaponData.getInstance().reloadWeaponData();
                sender.sendMessage(messages.get(Message.FILES_RELOADED));
            } else if (args.length == 1 && args[0].equalsIgnoreCase("menu")) {
                if (!sender.hasPermission(this.plugin.getName() + ".command." + command.getName() + "." + args[0])) {
                    Utils.noPermission(sender, this.plugin.getName() + ".command." + command.getName() + "." + args[0]);
                    return true;
                }

                WeaponMenuListener.weaponMenu(plugin, player);
            } else if (args.length == 1 && args[0].equalsIgnoreCase("parts")) {
                if (!sender.hasPermission(this.plugin.getName() + ".command." + command.getName() + "." + args[0])) {
                    Utils.noPermission(sender, this.plugin.getName() + ".command." + command.getName() + "." + args[0]);
                    return true;
                }

                WeaponPartListener.weaponPartMenu(player);
            } else {
                sendHelp(sender);
            }
        }
        return true;

    }

    private void sendHelp(CommandSender sender) {
        String primaryColor = Main.getMessages().getPrimaryColor();
        String secondaryColor = Main.getMessages().getSecondaryColor();

        sender.sendMessage(Utils.color(primaryColor + "&m------------------&f " + secondaryColor + "" + plugin.getName()
                + " " + primaryColor + "&m------------------&f"));
        sender.sendMessage(Utils.color(primaryColor + "Version: " + secondaryColor + ""
                + plugin.getDescription().getVersion() + primaryColor
                + " by " + secondaryColor + plugin.getDescription().getAuthors() + "."));
        sender.sendMessage(Utils.color("&f"));
        sender.sendMessage(Utils.color(primaryColor + "/weapon " + secondaryColor + "help " + primaryColor + "- "
                + secondaryColor + "Laat alle commandos zien."));
        sender.sendMessage(Utils.color(primaryColor + "/weapon " + secondaryColor + "reload " + primaryColor + "- "
                + secondaryColor + "Herlaad de config files."));
        sender.sendMessage(Utils.color(primaryColor + "/weapon " + secondaryColor + "menu " + primaryColor + "- "
                + secondaryColor + "Open het MT Wapens menu."));
        sender.sendMessage(Utils.color(primaryColor + "/weapon " + secondaryColor + "parts " + primaryColor + "- "
                + secondaryColor + "Open het MT Wapen parts menu."));
        sender.sendMessage(Utils.color(primaryColor + "/weapon " + secondaryColor + "voucher <weapon> <durability> "
                + primaryColor + "- " + secondaryColor + "Krijg een voucher."));
        sender.sendMessage(Utils.color(primaryColor + "/weapon " + secondaryColor + "getweapon <weapon> <durability> "
                + primaryColor + "- " + secondaryColor + "Krijg een wapen."));
        sender.sendMessage(Utils.color(primaryColor + "/weapon " + secondaryColor + "getammo <weapon> " + primaryColor
                + "- " + secondaryColor + "Krijg ammo van een bepaald wapen."));
    }

    private void getWeapon(Player player, int dura, String string) {
        int UUID = Utils.randomValue(999999999, 10000);

        String type = string.toLowerCase();

        ArrayList<String> lore = new ArrayList<>();
        lore.add(Utils.color("&f"));
        lore.add(Utils.color(plugin.getConfig().getString("weapon-lore")));
        lore.add(Utils.color("&f"));
        lore.add(Utils.color("&fAmmo: &7" + plugin.getConfig().getInt("weapons." + type + ".max-ammo") + "&f/&7"
                + plugin.getConfig().getInt("weapons." + type + ".max-ammo")));
        lore.add(Utils.color("&f"));

        ItemStack weapon = new ItemBuilder(Material.WOOD_HOE)
                .setName(Utils.color(plugin.getConfig().getString("weapons." + type + ".name")))
                .setNBT("mtcustom", type + "_fullmodel").setNBT("WEAPON-UUID", String.valueOf(UUID)).setLore(lore)
                .toItemStack();

        ItemMeta im = weapon.getItemMeta();
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        weapon.setItemMeta(im);

        Utils.createWeaponData(UUID, dura, plugin.getConfig().getInt("weapons." + type + ".max-ammo"));

        player.getInventory().addItem(weapon);
        player.sendMessage(Main.getMessages().get(Message.WEAPON_RECEIVED,
                Placeholder.of("weapontype", type), Placeholder.of("durability", dura)));
    }

    private void getVoucher(Player player, int dura, String string) {
        Messages messages = Main.getMessages();

        String type = string.toLowerCase();

        ArrayList<String> lore = new ArrayList<>();
        lore.add(Utils.color("&8&m-----------------"));
        lore.add(Utils.color("&7Gebruik " +  messages.getPrimaryColor() + "rechterklik&7 op deze voucher om"));
        lore.add(Utils.color("&7hem te verzilveren!"));
        lore.add(Utils.color("&f"));
        lore.add(Utils.color(
                "&7Deze " + messages.getPrimaryColor() + "voucher&7 is houdbaar tot:"));
        lore.add(Utils.color(messages.getPrimaryColor() + "permanent"));
        lore.add(Utils.color("&8&m-----------------"));

        ItemStack voucher = new ItemBuilder(Material.PAPER).setName(Utils.color(
                messages.getPrimaryColor() + "Voucher " + type + " " + dura + " Durability"))
                .setNBT("VOUCHER", "true").setLore(lore).toItemStack();

        player.getInventory().addItem(voucher);
        player.sendMessage(Main.getMessages().get(Message.VOUCHER_RECEIVED,
                Placeholder.of("weapontype", type), Placeholder.of("durability", dura)));
    }

    private void getAmmo(Player player, String string) {
        String type = string.toLowerCase();

        ItemStack bulletItem = new ItemBuilder(Material.IRON_INGOT)
                .setName(Utils.color(plugin.getConfig().getString("weapons." + type + ".ammo-name")))
                .setLore(Utils.color(plugin.getConfig().getString("ammo-lore")))
                .setNBT("mtcustom", "" + type + "_bullets").toItemStack();

        player.getInventory().addItem(bulletItem);
        player.sendMessage(Main.getMessages().get(Message.AMMO_RECEIVED, Placeholder.of("weapontype", type)));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!command.testPermissionSilent(sender)) {
            return Collections.emptyList();
        }

        if (args.length == 1) {
            return getApplicableTabCompletes(args[0],
                    Arrays.asList("help", "reload", "menu", "parts", "voucher", "getweapon", "getammo"));
        } else if (args.length == 2) {
            return getApplicableTabCompletes(args[1],
                    plugin.getConfig().getConfigurationSection("weapons.").getKeys(false));
        }

        return Collections.emptyList();
    }

    private List<String> getApplicableTabCompletes(String arg, Collection<String> completions) {
        return StringUtil.copyPartialMatches(arg, completions, new ArrayList<>(completions.size()));
    }
}
