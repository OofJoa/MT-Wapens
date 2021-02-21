package com.jazzkuh.mtwapens.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jazzkuh.mtwapens.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.UUID;

public class Utils {

    public static void sendMessage(Player player, String input) {
        player.sendMessage(Utils.color(input));
    }
    public static void sendMessage(CommandSender sender, String input) {
        sender.sendMessage(Utils.color(input));
    }
    public static void sendBroadcast(String input) {
        Bukkit.broadcastMessage(Utils.color(input));
    }

    public static String color(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static boolean isInt(String s) {
        boolean amIValid;
        try {
            Integer.parseInt(s);
            amIValid = true;
        } catch (NumberFormatException e) {
            amIValid = false;
        }
        return amIValid;
    }

    public static boolean isDouble(String s) {
        boolean amIValid;
        try {
            Double.parseDouble(s);
            amIValid = true;
        } catch (NumberFormatException e) {
            amIValid = false;
        }
        return amIValid;
    }

    public static boolean checkPermission(CommandSender sender, String permission) {
        if (sender.hasPermission(permission))
            return true;

        sender.sendMessage(Utils.color("&cTo use this command, you need permission " + permission + "."));
        return false;
    }

    public static String getServerIP() {
        JsonObject root = getJSON("https://verify.minetopiasdb.nl/reqip.php", "POST");
        return root == null ? "-1" : root.get("message").getAsString();
    }

    public static boolean checkForBlacklist(String string) {
        JsonObject root = getJSON("https://mt-wapens.glitch.me/verify?check=" + string, "GET");
        return root != null && root.get("message").getAsBoolean();
    }

    private static JsonObject getJSON(String url, String method) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod(method);
            connection.setRequestProperty("User-Agent", "MTWAPENS");
            connection.connect();

            return new JsonParser().parse(new InputStreamReader((InputStream) connection.getContent())).getAsJsonObject();
        } catch (IOException e) {
            Bukkit.getLogger().severe("Error performing HTTP request");
            e.printStackTrace();
        }

        return null;
    }

    public static void formatHelpMessage(HashMap<String, String> argumentHandler, Command command, CommandSender sender) {
        for (String argument : argumentHandler.keySet()) {
            sender.sendMessage(Utils.color("&a/" + command.getName() + " " + argument + " - " + argumentHandler.get(argument)));
        }
    }

    private static void debugMessage(Player player, String type, String debugString, Boolean configured) {
        if (!configured) {
            Utils.sendMessage(player, "&7 - " + debugString + ": " + "&4INCORRECT");
        } else {
            Utils.sendMessage(player, "&7 - " + debugString + ": " + "&2CORRECT");
        }
    }

    public static void debugWeapon(Player player, String type) {
        if (Main.getInstance().getConfig().getConfigurationSection("weapons.").getKeys(false).contains(type)) {
            sendMessage(player, "&aDebug report for weapon " + type + ":");

            debugString(player, type, "name");
            debugString(player, type, "lore");

            debugMessage(player, type, "material", Main.getInstance().getConfig().getString("weapons." + type + ".material") != null && isMaterial(Main.getInstance().getConfig().getString("weapons." + type + ".material")));

            debugString(player, type, "nbt");
            debugString(player, type, "nbtvalue");

            debugMessage(player, type, "damage", Main.getInstance().getConfig().getString("weapons." + type + ".damage") != null && isDouble(Main.getInstance().getConfig().getString("weapons." + type + ".damage")));

            debugMessage(player, type, "max-ammo", Main.getInstance().getConfig().getString("weapons." + type + ".max-ammo") != null && isInt(Main.getInstance().getConfig().getString("weapons." + type + ".max-ammo")));

            debugMessage(player, type, "attackspeed", Main.getInstance().getConfig().getString("weapons." + type + ".attackspeed") != null && isDouble(Main.getInstance().getConfig().getString("weapons." + type + ".attackspeed")));

            debugString(player, type, "ammo-type");
        } else {
            Utils.sendMessage(player, "&cThe weapon you are trying to debug has not been found in the configuration files.");
        }
    }

    private static boolean isMaterial(String material) {
        boolean amIValid;
        try {
            Material.valueOf(material);
            amIValid = true;
        } catch (Exception e) {
            amIValid = false;
        }
        return amIValid;
    }

    private static void debugString(Player player, String type, String debugString) {
        if (Main.getInstance().getConfig().getString("weapons." + type + "." + debugString) == null) {
            debugMessage(player, type, debugString, false);
        } else {
            debugMessage(player, type, debugString, true);
        }
    }
}

