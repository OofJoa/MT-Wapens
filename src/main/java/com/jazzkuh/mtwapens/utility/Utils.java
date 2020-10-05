package com.jazzkuh.mtwapens.utility;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.data.WeaponData;
import com.jazzkuh.mtwapens.utility.messages.Message;
import com.jazzkuh.mtwapens.utility.messages.Placeholder;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class Utils {

    Main plugin;

    static WeaponData weaponData = WeaponData.getInstance();

    public Utils(Main plugin) {
        this.plugin = plugin;
    }

    public static String color(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static void noPermission(CommandSender sender, String s) {
        sender.sendMessage(Main.getMessages().get(Message.NO_PERMISSION, Placeholder.of("permission", s)));
    }

    public static HashMap<String, Date> cooldowns = new HashMap<String, Date>();

    public static Integer randomValue(Integer max, Integer min) {
        Random random = new Random();
        int value = random.nextInt(max - min) + min;
        return value;
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
        boolean amIValid = false;
        try {
            Integer.parseInt(s);
            // s is a valid integer!
            amIValid = true;
        } catch (NumberFormatException e) {
            amIValid = false;
        }
        return amIValid;
    }

    public static String getServerIP() {
        try {
            URL url = new URL("https://verify.minetopiasdb.nl/reqip.php");
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("User-Agent", "MTWAPENS");
            httpURLConnection.connect();

            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(new InputStreamReader((InputStream)httpURLConnection.getContent()));
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            return jsonObject.get("message").getAsString();
        } catch (Exception e) {}
        return "-1";
    }

    public static boolean checkForBlacklist(String string) {
        try {
            URL url = new URL("https://mt-wapens.glitch.me/verify?check=" + string);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("User-Agent", "MTWAPENS");
            httpURLConnection.connect();

            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(new InputStreamReader((InputStream)httpURLConnection.getContent()));
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            return jsonObject.get("message").getAsBoolean();
        } catch (Exception exept) {}
        return false;
    }

    public static boolean authoriseUser(UUID string) {
        try {
            URL url = new URL("https://mt-wapens.glitch.me/authorize?check=" + string);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("User-Agent", "MTWAPENS");
            httpURLConnection.connect();

            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(new InputStreamReader((InputStream)httpURLConnection.getContent()));
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            return jsonObject.get("message").getAsBoolean();
        } catch (Exception exept) {}
        return false;
    }
}

