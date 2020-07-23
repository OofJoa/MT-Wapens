package com.jazzkuh.mtwapens.utility;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.data.WeaponData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Utils {

    Main plugin;

    static WeaponData weaponData = WeaponData.getInstance();

    public Utils(Main plugin) {
        this.plugin = plugin;
    }

    public static String color(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static ItemStack createItemStack(Material mat, String name, List<String> lore) {
        ItemStack is = new ItemStack(mat);
        ItemMeta ism = is.getItemMeta();
        if (name != null) {
            ism.setDisplayName(color(name));
        }
        ism.setLore(lore);
        is.setItemMeta(ism);
        return is;
    }

    @SuppressWarnings("deprecation")
    public static void handleToolDurability(Player p) {
        if ((short) (p.getInventory().getItemInMainHand().getDurability() + 2) >= p.getInventory().getItemInMainHand()
                .getType().getMaxDurability()) {
            p.getInventory().remove(p.getInventory().getItemInMainHand());
        } else {
            p.getInventory().getItemInMainHand()
                    .setDurability((short) (p.getInventory().getItemInMainHand().getDurability() + 2));
        }
        p.updateInventory();
    }

    public static boolean is113orUp() {
        String nmsver = Bukkit.getServer().getClass().getPackage().getName();
        nmsver = nmsver.substring(nmsver.lastIndexOf(".") + 1);
        return !nmsver.startsWith("v1_7_") && !nmsver.startsWith("v1_8_") && !nmsver.startsWith("v1_9_")
                && !nmsver.startsWith("v1_10_") && !nmsver.startsWith("v1_11_") && !nmsver.startsWith("v1_12_");
    }

    public static String formatMoneyUS(double money) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        return decimalFormat.format(money);
    }

    public static String TrimFromChar(String character, String string) {
        String string1 = string;
        String tempstring = string1.substring(string1.indexOf(character)+1);
        return tempstring.trim();
    }

    public static void noPermission(CommandSender sender, String s) {
        sender.sendMessage(Utils.color("&cJe hebt niet genoeg rechten om dit command uit te voeren. Je hebt de permission " + s + " nodig."));
    }

    public static HashMap<String, Date> cooldowns = new HashMap<String, Date>();

    public static boolean playerCooldown(final Long mil, final Player p) {
        if (cooldowns.containsKey(p.getName())) {
            if (cooldowns.get(p.getName()).getTime() > new Date().getTime()) {
                return false;
            } else {
                cooldowns.remove(p.getName());
                return true;
            }
        } else {
            cooldowns.put(p.getName() , new Date(new Date().getTime() + mil));
            return false;
        }
    }

    public static Integer randomValue(Integer max, Integer min) {
        Random random = new Random();
        int value = random.nextInt(max - min) + min;
        return value;
    }

    public static void createWeaponDataStringIfNotExists(int UUID, String string, String value) {
        if (weaponData.getWeaponData().getString(UUID + string) == null) {
            weaponData.getWeaponData().set(UUID + string, value);
        }
    }

    public static void createWeaponDataIntIfNotExists(int UUID, String string, Integer value) {
        if (weaponData.getWeaponData().getString(UUID + string) == null) {
            weaponData.getWeaponData().set(UUID + string, Integer.parseInt(String.valueOf(value)));
        }
    }

    public static void createWeaponDataIfNotExists(int UUID, String string, Boolean value) {
        if (weaponData.getWeaponData().getString(UUID + string) == null) {
            weaponData.getWeaponData().set(UUID + string, value);
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

