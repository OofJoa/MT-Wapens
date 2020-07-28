package com.jazzkuh.mtwapens.commands;

import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.data.WeaponData;
import com.jazzkuh.mtwapens.listeners.WeaponMenuListener;
import com.jazzkuh.mtwapens.listeners.WeaponPartListener;
import com.jazzkuh.mtwapens.utility.ItemBuilder;
import com.jazzkuh.mtwapens.utility.Utils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class WeaponCommand implements CommandExecutor {

    private final Main plugin;

    static WeaponData weaponData = WeaponData.getInstance();

    public WeaponCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;

        if (sender.hasPermission(this.plugin.getName() + ".command." + command.getName())) {
            if (args.length > 1) {
                if (args[0].equalsIgnoreCase("getweapon")) {
                    if (sender.hasPermission(this.plugin.getName() + ".command." + command.getName() + "." + args[0])) {
                        if (args.length > 2) {
                            if (args[1].equalsIgnoreCase("deserteagle") || args[1].equalsIgnoreCase("magnum44") || args[1].equalsIgnoreCase("waltherp99") || args[1].equalsIgnoreCase("glock19") || args[1].equalsIgnoreCase("m16a4")) {
                                if (Utils.isInt(args[2]) && Integer.parseInt(args[2]) > 0 && args[2] != null) {
                                    getWeapon(player, Integer.parseInt(args[2]), args[1]);
                                } else {
                                    sender.sendMessage(Utils.color(plugin.getConfig().getString("messages.second-color") + "Kan argument 2 niet parsen als geldige integer. Gebruik een integer hoger dan 0."));
                                }
                            } else {
                                sender.sendMessage(Utils.color(plugin.getConfig().getString("messages.second-color") + "Dit type wapen bestaat niet. Kies uit: DesertEagle, Magnum44, WaltherP99, Glock19 of M16A4."));
                            }
                        } else {
                            sendHelp(command, sender);
                        }
                    } else {
                        Utils.noPermission(sender, this.plugin.getName() + ".command." + command.getName() + "." + args[0]);
                    }
                } else if (args[0].equalsIgnoreCase("getammo")) {
                    if (sender.hasPermission(this.plugin.getName() + ".command." + command.getName() + "." + args[0])) {
                        if (args[1].equalsIgnoreCase("deserteagle") || args[1].equalsIgnoreCase("magnum44") || args[1].equalsIgnoreCase("waltherp99") || args[1].equalsIgnoreCase("glock19") || args[1].equalsIgnoreCase("m16a4")) {
                            getAmmo(player, args[1]);
                        } else {
                            sender.sendMessage(Utils.color(plugin.getConfig().getString("messages.second-color") + "Dit type wapen bestaat niet. Kies uit: DesertEagle, Magnum44, WaltherP99, Glock19 of M16A4."));
                        }
                    } else {
                        Utils.noPermission(sender, this.plugin.getName() + ".command." + command.getName() + "." + args[0]);
                    }
                } if (args[0].equalsIgnoreCase("voucher")) {
                    if (sender.hasPermission(this.plugin.getName() + ".command." + command.getName() + "." + args[0])) {
                        if (args.length > 2) {
                            if (args[1].equalsIgnoreCase("deserteagle") || args[1].equalsIgnoreCase("magnum44") || args[1].equalsIgnoreCase("waltherp99") || args[1].equalsIgnoreCase("glock19") || args[1].equalsIgnoreCase("m16a4")) {
                                if (Utils.isInt(args[2]) && Integer.parseInt(args[2]) > 0 && args[2] != null) {
                                    getVoucher(player, Integer.parseInt(args[2]), args[1]);
                                } else {
                                    sender.sendMessage(Utils.color(plugin.getConfig().getString("messages.second-color") + "Kan argument 2 niet parsen als geldige integer. Gebruik een integer hoger dan 0."));
                                }
                            } else {
                                sender.sendMessage(Utils.color(plugin.getConfig().getString("messages.second-color") + "Dit type wapen bestaat niet. Kies uit: DesertEagle, Magnum44, WaltherP99, Glock19 of M16A4."));
                            }
                        } else {
                            sendHelp(command, sender);
                        }
                    } else {
                        Utils.noPermission(sender, this.plugin.getName() + ".command." + command.getName() + "." + args[0]);
                    }
                } else {
                    sendHelp(command, sender);
                }
            } else if (args.length > 0) {
                if (args[0].equalsIgnoreCase("reload")) {
                    if (sender.hasPermission(this.plugin.getName() + ".command." + command.getName() + "." + args[0])) {
                        sender.sendMessage(Utils.color(plugin.getConfig().getString("messages.first-color") + "Je hebt succesvol de " + plugin.getConfig().getString("messages.second-color") + "config files" + plugin.getConfig().getString("messages.first-color") + " van MT-Wapens herladen."));
                        plugin.reloadConfig();
                    } else {
                        Utils.noPermission(sender, this.plugin.getName() + ".command." + command.getName() + "." + args[0]);
                    }
                } else if (args[0].equalsIgnoreCase("menu")) {
                    if (sender.hasPermission(this.plugin.getName() + ".command." + command.getName() + "." + args[0])) {
                        WeaponMenuListener.weaponMenu(plugin, player);
                    } else {
                        Utils.noPermission(sender, this.plugin.getName() + ".command." + command.getName() + "." + args[0]);
                    }
                } else if (args[0].equalsIgnoreCase("parts")) {
                    if (sender.hasPermission(this.plugin.getName() + ".command." + command.getName() + "." + args[0])) {
                        WeaponPartListener.weaponPartMenu(plugin, player);
                    } else {
                        Utils.noPermission(sender, this.plugin.getName() + ".command." + command.getName() + "." + args[0]);
                    }
                } else {
                    sendHelp(command, sender);
                }
            } else {
                sendHelp(command, sender);
            }
        } else {
            sender.sendMessage(Utils.color("&f"));
            sender.sendMessage(Utils.color(plugin.getConfig().getString("messages.first-color") + "" + plugin.getName() + " version: " + plugin.getConfig().getString("messages.second-color") + "" + plugin.getDescription().getVersion() + plugin.getConfig().getString("messages.first-color") + "."));
            sender.sendMessage(Utils.color(plugin.getConfig().getString("messages.first-color") + "Description: " + plugin.getConfig().getString("messages.second-color") + "" + plugin.getDescription().getDescription() + plugin.getConfig().getString("messages.first-color") + "."));
            sender.sendMessage(Utils.color(plugin.getConfig().getString("messages.first-color") + "Download: " + plugin.getConfig().getString("messages.second-color") + "" + plugin.getDescription().getWebsite() + plugin.getConfig().getString("messages.first-color") + "."));
            sender.sendMessage(Utils.color(plugin.getConfig().getString("messages.first-color") + "By " + plugin.getConfig().getString("messages.second-color") + "[Jazzkuh]" + plugin.getConfig().getString("messages.first-color") + "."));
            sender.sendMessage(Utils.color("&f"));
        }
        return true;
    }

    private void sendHelp(Command command, CommandSender sender) {
        sender.sendMessage(Utils.color(plugin.getConfig().getString("messages.first-color") + "&m------------------&f " + plugin.getConfig().getString("messages.second-color") + "" + plugin.getName() + " " + plugin.getConfig().getString("messages.first-color") + "&m------------------&f"));
        sender.sendMessage(Utils.color(plugin.getConfig().getString("messages.first-color") + "Version: " + plugin.getConfig().getString("messages.second-color") + "" + plugin.getDescription().getVersion() + plugin.getConfig().getString("messages.first-color") + "."));
        sender.sendMessage(Utils.color(plugin.getConfig().getString("messages.first-color") + "By " + plugin.getConfig().getString("messages.second-color") + "" + plugin.getDescription().getAuthors() + plugin.getConfig().getString("messages.first-color") + "."));
        sender.sendMessage(Utils.color("&f"));
        sender.sendMessage(Utils.color(plugin.getConfig().getString("messages.first-color") + "/weapon " + plugin.getConfig().getString("messages.second-color") + "help " + plugin.getConfig().getString("messages.first-color") + "- " + plugin.getConfig().getString("messages.second-color") + "Laat alle commandos zien."));
        sender.sendMessage(Utils.color(plugin.getConfig().getString("messages.first-color") + "/weapon " + plugin.getConfig().getString("messages.second-color") + "reload " + plugin.getConfig().getString("messages.first-color") + "- " + plugin.getConfig().getString("messages.second-color") + "Herlaad de config files."));
        sender.sendMessage(Utils.color(plugin.getConfig().getString("messages.first-color") + "/weapon " + plugin.getConfig().getString("messages.second-color") + "menu " + plugin.getConfig().getString("messages.first-color") + "- " + plugin.getConfig().getString("messages.second-color") + "Open het MT Wapens menu."));
        sender.sendMessage(Utils.color(plugin.getConfig().getString("messages.first-color") + "/weapon " + plugin.getConfig().getString("messages.second-color") + "parts " + plugin.getConfig().getString("messages.first-color") + "- " + plugin.getConfig().getString("messages.second-color") + "Open het MT Wapen parts menu."));
        sender.sendMessage(Utils.color(plugin.getConfig().getString("messages.first-color") + "/weapon " + plugin.getConfig().getString("messages.second-color") + "voucher <weapon> <durability> " + plugin.getConfig().getString("messages.first-color") + "- " + plugin.getConfig().getString("messages.second-color") + "Krijg een voucher."));
        sender.sendMessage(Utils.color(plugin.getConfig().getString("messages.first-color") + "/weapon " + plugin.getConfig().getString("messages.second-color") + "getweapon <weapon> <durability> " + plugin.getConfig().getString("messages.first-color") + "- " + plugin.getConfig().getString("messages.second-color") + "Krijg een wapen."));
        sender.sendMessage(Utils.color(plugin.getConfig().getString("messages.first-color") + "/weapon " + plugin.getConfig().getString("messages.second-color") + "getammo <weapon> " + plugin.getConfig().getString("messages.first-color") + "- " + plugin.getConfig().getString("messages.second-color") + "Krijg ammo van een bepaald wapen."));
    }

    private void getWeapon(Player player, int dura, String string) {
        int UUID = Utils.randomValue(999999999, 10000);

        String type = string.toLowerCase();

        ArrayList<String> Lore = new ArrayList<String>();
        Lore.add(Utils.color("&f"));
        Lore.add(Utils.color(plugin.getConfig().getString("weapon-lore")));
        Lore.add(Utils.color("&f"));
        Lore.add(Utils.color("&fAmmo: &7" + plugin.getConfig().getInt("weapons." + type + ".max-ammo") + "&f/&7" + plugin.getConfig().getInt("weapons." + type + ".max-ammo")));
        Lore.add(Utils.color("&f"));

        ItemStack weapon = new ItemBuilder(Material.WOOD_HOE)
                .setName(Utils.color(plugin.getConfig().getString("weapons." + type + ".name")))
                .setNBT("mtcustom", type + "_fullmodel")
                .setNBT("WEAPON-UUID", String.valueOf(UUID))
                .setLore(Lore)
                .toItemStack();

        ItemMeta im = weapon.getItemMeta();
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        weapon.setItemMeta(im);

        Utils.createWeaponData(UUID, dura, plugin.getConfig().getInt("weapons." + type + ".max-ammo"));

        player.getInventory().addItem(weapon);
        player.sendMessage(Utils.color(plugin.getConfig().getString("messages.first-color") + "Je hebt succesvol het wapen " + plugin.getConfig().getString("messages.second-color") + "" + type + plugin.getConfig().getString("messages.first-color") + " (" + plugin.getConfig().getString("messages.second-color") + "" + dura + " Durability" + plugin.getConfig().getString("messages.first-color") + ") ontvangen."));
    }

    private void getVoucher(Player player, int dura, String string) {

        String type = string.toLowerCase();

        ArrayList<String> Lore = new ArrayList<String>();
        Lore.add(Utils.color("&8&m-----------------"));
        Lore.add(Utils.color("&7Gebruik " + plugin.getConfig().getString("messages.first-color") + "rechterklik&7 op deze voucher om"));
        Lore.add(Utils.color("&7hem te verzilveren!"));
        Lore.add(Utils.color("&f"));
        Lore.add(Utils.color("&7Deze " + plugin.getConfig().getString("messages.first-color") + "voucher&7 is houdbaar tot:"));
        Lore.add(Utils.color(plugin.getConfig().getString("messages.first-color") + "permanent"));
        Lore.add(Utils.color("&8&m-----------------"));


        ItemStack voucher = new ItemBuilder(Material.PAPER)
                .setName(Utils.color(plugin.getConfig().getString("messages.first-color") + "Voucher " + type + " " + dura + " Durability"))
                .setNBT("VOUCHER", "true")
                .setLore(Lore)
                .toItemStack();

        player.getInventory().addItem(voucher);
        player.sendMessage(Utils.color(plugin.getConfig().getString("messages.first-color") + "Je hebt succesvol een voucher voor het wapen " + plugin.getConfig().getString("messages.second-color") + "" + type + plugin.getConfig().getString("messages.first-color") + " (" + plugin.getConfig().getString("messages.second-color") + "" + dura + " Durability" + plugin.getConfig().getString("messages.first-color") + ") ontvangen."));
    }

    private void getAmmo(Player player, String string) {

        String type = string.toLowerCase();

        ItemStack bulletItem = new ItemBuilder(Material.IRON_INGOT)
                .setName(Utils.color(plugin.getConfig().getString("weapons." + type + ".ammo-name")))
                .setLore(Utils.color(plugin.getConfig().getString("ammo-lore")))
                .setNBT("mtcustom", "" + type + "_bullets")
                .toItemStack();

        player.getInventory().addItem(bulletItem);
        player.sendMessage(Utils.color(plugin.getConfig().getString("messages.first-color") + "Je hebt succesvol ammo voor het wapen " + plugin.getConfig().getString("messages.second-color") + "" + type + plugin.getConfig().getString("messages.first-color") + " ontvangen."));
    }
}
