/*
 *     MT-Wapens
 *     Copyright © 2021 Jazzkuh. All rights reserved.
 *
 *     “Commons Clause” License Condition v1.0
 *
 *    The Software is provided to you by the Licensor under the License, as defined below, subject to the following condition.
 *
 *     Without limiting other conditions in the License, the grant of rights under the License will not include, and the License does not grant to you, the right to Sell the Software.
 *
 *     For purposes of the foregoing, “Sell” means practicing any or all of the  rights granted to you under the License to provide to third parties, for a fee  or other consideration (including without limitation fees for hosting or  consulting/ support services related to the Software), a product or service  whose value derives, entirely or substantially, from the functionality of the  Software. Any license notice or attribution required by the License must also  include this Commons Clause License Condition notice.
 *
 *     Software: MT-Wapens
 *     License: GNU-LGPL v2.1 with Commons Clause
 *     Licensor: [Jazzkuh](https://github.com/Jazzkuh)
 *
 *     This library is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU Lesser General Public
 *     License as published by the Free Software Foundation; either
 *     version 2.1 of the License, or (at your option) any later version.
 *
 *     This library is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *     Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public
 *     License along with this library; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 *     USA
 */

package com.jazzkuh.mtwapens.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.function.WeaponFactory;
import com.jazzkuh.mtwapens.function.objects.Weapon;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

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

    public static void applyAttackSpeed(ItemStack itemStack, Double amount) {
        try {
            NBTEditor.NBTCompound compound = NBTEditor.getNBTCompound(itemStack);
            compound.set("generic.attackSpeed", "tag", "AttributeModifiers", null, "AttributeName");
            compound.set("AttackSpeed", "tag", "AttributeModifiers", 0, "Name");
            compound.set("mainhand", "tag", "AttributeModifiers", 0, "Slot");
            compound.set(0, "tag", "AttributeModifiers", 0, "Operation");
            compound.set(amount, "tag", "AttributeModifiers", 0, "Amount");
            compound.set(new int[]{0, 0, 0, 0}, "tag", "AttributeModifiers", 0, "UUID");
            compound.set(99L, "tag", "AttributeModifiers", 0, "UUIDMost");
            compound.set(77530600L, "tag", "AttributeModifiers", 0, "UUIDLeast");

            ItemStack is = NBTEditor.getItemFromTag(compound);
            ItemMeta itemMeta = is.getItemMeta();
            itemStack.setItemMeta(itemMeta);
        } catch (Exception exception) {
        }
    }

    /**
     * Returns a location with a specified distance away from the right side of
     * a location.
     *
     * @param location The origin location
     * @param distance The distance to the right
     * @return the location of the distance to the right
     */
    public static Location getRightSide(Location location, double distance) {
        float angle = location.getYaw() / 60;
        return location.clone().subtract(new Vector(Math.cos(angle), 0, Math.sin(angle)).normalize().multiply(distance));
    }

    public static String getServerIP() {
        JsonObject root = getJSON("https://verify.minetopiasdb.nl/reqip.php", "POST");
        return root == null ? "-1" : root.get("message").getAsString();
    }

    public static boolean checkForBlacklist(String string) {
        JsonObject root = getJSON("https://dash.mtwapens.nl/verify?check=" + string, "GET");
        return root != null && root.get("message").getAsBoolean();
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

    public static Block getTarget(Location from, int distance) {
        BlockIterator itr = new BlockIterator(from, 0, distance);
        while (itr.hasNext()) {
            Block block = itr.next();
            if (!block.getType().isOccluding()) {
                continue;
            }
            return block;
        }
        return null;
    }

    public static boolean canSee(LivingEntity from, Location to) {
        return getTarget(from.getEyeLocation(), (int) Math.ceil(from.getLocation().distance(to))) == null;
    }
}

