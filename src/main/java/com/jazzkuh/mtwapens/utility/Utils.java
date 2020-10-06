package com.jazzkuh.mtwapens.utility;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.data.WeaponData;
import com.jazzkuh.mtwapens.utility.messages.Message;
import com.jazzkuh.mtwapens.utility.messages.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.UUID;

public class Utils {
    static WeaponData weaponData = WeaponData.getInstance();

    public static String color(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static void noPermission(CommandSender sender, String s) {
        sender.sendMessage(Main.getMessages().get(Message.NO_PERMISSION, Placeholder.of("permission", s)));
    }

    public static Integer randomValue(Integer max, Integer min) {
        return new Random().nextInt(max - min) + min;
    }

    public static void createWeaponDataIntIfNotExists(int UUID, String string, Integer value) {
        if (weaponData.getWeaponData().getString(UUID + string) == null) {
            weaponData.getWeaponData().set(UUID + string, Integer.parseInt(String.valueOf(value)));
        }
    }

    public static void createWeaponData(int UUID, Integer durability, Integer ammo) {
        Utils.createWeaponDataIntIfNotExists(UUID, ".durability", durability);
        Utils.createWeaponDataIntIfNotExists(UUID, ".ammo", ammo);
    }

    public static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String getServerIP() {
        JsonObject root = getJSON("https://verify.minetopiasdb.nl/reqip.php", "POST");
        return root == null ? "-1" : root.get("message").getAsString();
    }

    public static boolean checkForBlacklist(String string) {
        JsonObject root = getJSON("https://mt-wapens.glitch.me/verify?check=" + string, "GET");
        return root != null && root.get("message").getAsBoolean();
    }

    public static boolean authoriseUser(UUID string) {
        JsonObject root = getJSON("https://mt-wapens.glitch.me/authorize?check=" + string, "GET");
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
}

