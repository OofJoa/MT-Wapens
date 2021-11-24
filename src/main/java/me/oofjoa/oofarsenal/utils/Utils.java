package me.oofjoa.oofarsenal.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.oofjoa.oofarsenal.Main;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static void sendMessage(Player player, String input) {
        if (input.length() >= 1) {
            if (input.startsWith("a:")) {
                sendActionbar(player, input.substring(2));
            } else {
                player.sendMessage(Utils.color(input));
            }
        }
    }
    public static void sendMessage(CommandSender sender, String input) {
        if (input.length() >= 1) {
            if (input.startsWith("a:") && sender instanceof Player) {
                sendActionbar((Player) sender, input.substring(2));
            } else {
                sender.sendMessage(Utils.color(input));
            }
        }
    }

    public static void sendActionbar(Player player, String input) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(color(input)));
    }

    public static void sendBroadcast(String input) {
        Bukkit.broadcastMessage(Utils.color(input));
    }

    public static final Pattern hexPattern = Pattern.compile("&#(\\w{5}[0-9a-f])");
    public static String color(String message) {
        if (Bukkit.getServer().getVersion().contains("1_16") || Bukkit.getServer().getVersion().contains("1_17")) {
            Matcher matcher = hexPattern.matcher(message);
            StringBuffer stringBuilder = new StringBuffer();

            while (matcher.find()) {
                matcher.appendReplacement(stringBuilder, net.md_5.bungee.api.ChatColor.of("#" + matcher.group(1)).toString());
            }

            return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', matcher.appendTail(stringBuilder).toString());
        } else {
            return org.bukkit.ChatColor.translateAlternateColorCodes('&', message);
        }
    }

    public static List<String> color(List<String> lore){
        List<String> clore = new ArrayList<>();
        for(String s : lore){
            clore.add(color(s));
        }
        return clore;
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

    public static void applyNBTTag(ItemStack itemStack, String key, Object value) {
        ItemStack is = NBTEditor.set(itemStack, value, key);
        ItemMeta itemMeta = is.getItemMeta();
        itemStack.setItemMeta(itemMeta);
    }


    private static JsonObject getJSON(String url, String method) {
        try {
            HttpURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod(method);
            connection.setRequestProperty("User-Agent", "MTWAPENS");
            connection.setRequestProperty("Version", Main.getInstance().getDescription().getVersion());
            connection.connect();

            return new JsonParser().parse(new InputStreamReader((InputStream) connection.getContent())).getAsJsonObject();
        } catch (IOException ignored) {
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

    public static ArrayList<Block> getBlocksAroundCenter(Location loc, int radius) {
        ArrayList<Block> blocks = new ArrayList<>();

        for (int x = (loc.getBlockX()-radius); x <= (loc.getBlockX()+radius); x++) {
            for (int y = (loc.getBlockY()-radius); y <= (loc.getBlockY()+radius); y++) {
                for (int z = (loc.getBlockZ()-radius); z <= (loc.getBlockZ()+radius); z++) {
                    Location l = new Location(loc.getWorld(), x, y, z);
                    if (l.distance(loc) <= radius) {
                        blocks.add(l.getBlock());
                    }
                }
            }
        }

        return blocks;
    }
}

