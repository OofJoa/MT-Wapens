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

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static void sendMessage(Player player, String input) {
        if (input.length() >= 1) {
            player.sendMessage(Utils.color(input));
        }
    }
    public static void sendMessage(CommandSender sender, String input) {
        if (input.length() >= 1) {
            sender.sendMessage(Utils.color(input));
        }
    }
    public static void sendBroadcast(String input) {
        Bukkit.broadcastMessage(Utils.color(input));
    }

    public static final Pattern hexPattern = Pattern.compile("&#(\\w{5}[0-9a-f])");
    public static String color(String message) {
        if (Bukkit.getServer().getVersion().contains("1_16") || Bukkit.getServer().getVersion().contains("1_17")) {
            Matcher matcher = hexPattern.matcher(message);
            StringBuilder stringBuilder = new StringBuilder();

            while (matcher.find()) {
                matcher.appendReplacement(stringBuilder, net.md_5.bungee.api.ChatColor.of("#" + matcher.group(1)).toString());
            }

            return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', matcher.appendTail(stringBuilder).toString());
        } else {
            return org.bukkit.ChatColor.translateAlternateColorCodes('&', message);
        }
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
            HttpURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod(method);
            connection.setRequestProperty("User-Agent", "MTWAPENS");
            connection.connect();

            return new JsonParser().parse(new InputStreamReader((InputStream) connection.getContent())).getAsJsonObject();
        } catch (IOException e) {
            Bukkit.getLogger().severe("Error performing HTTPS request");
            e.printStackTrace();
        }

        return null;
    }

    public static Collection<String> getPlayerNames() {
        List<String> playerNames = new ArrayList<>();
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            playerNames.add(player.getName());
        }
        return playerNames;
    }

}

