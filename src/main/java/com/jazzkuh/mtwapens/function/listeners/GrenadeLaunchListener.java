package com.jazzkuh.mtwapens.function.listeners;

import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.api.PlayerLaunchGrenadeEvent;
import com.jazzkuh.mtwapens.function.objects.Grenade;
import com.jazzkuh.mtwapens.utils.Utils;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.*;

public class GrenadeLaunchListener implements Listener {
    private final HashMap<UUID, Date> grenadeCooldown = new HashMap<>();

    private boolean grenadeCooldown(UUID uuid) {
        if (grenadeCooldown.containsKey(uuid)) {
            if (grenadeCooldown.get(uuid).getTime() > new Date().getTime()) return false;

            grenadeCooldown.remove(uuid);
        }
        return true;
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getHand() == EquipmentSlot.OFF_HAND) return;

        if (player.getInventory().getItemInMainHand().getType() == Material.AIR) return;
        if (player.getInventory().getItemInMainHand().getItemMeta() == null) return;
        if (!player.getInventory().getItemInMainHand().getItemMeta().hasDisplayName()) return;

        ItemStack itemStack = player.getInventory().getItemInMainHand();

        if (!NBTEditor.contains(itemStack, "mtwapens_grenade")) return;
        if (!NBTEditor.contains(itemStack, "durability")) return;
        String grenadeType = NBTEditor.getString(itemStack, "mtwapens_grenade");
        Grenade grenade = new Grenade(grenadeType);

        switch (event.getAction()) {
            case RIGHT_CLICK_BLOCK:
            case RIGHT_CLICK_AIR: {
                event.setCancelled(true);

                PlayerLaunchGrenadeEvent playerLaunchGrenadeEvent = new PlayerLaunchGrenadeEvent(player, grenade);
                Bukkit.getServer().getPluginManager().callEvent(playerLaunchGrenadeEvent);
                if (playerLaunchGrenadeEvent.isCancelled()) return;

                if (!grenadeCooldown(player.getUniqueId())) return;
                grenadeCooldown.put(player.getUniqueId(), new Date(new Date().getTime() + (long) ((double) grenade.getParameter(Grenade.GrenadeParameters.COOLDOWN))));

                if (NBTEditor.getInt(player.getInventory().getItemInMainHand(), "durability") <= 0) {
                    player.getInventory().removeItem(player.getInventory().getItemInMainHand());
                    return;
                }

                Utils.applyNBTTag(itemStack, "durability", NBTEditor.getInt(itemStack, "durability") - 1);
                updateGrenadeLore(itemStack, grenade);

                Egg grenadeItem = player.launchProjectile(Egg.class);
                grenadeItem.setCustomName(grenadeType);
                grenadeItem.setShooter(player);
                grenadeItem.setVelocity(grenadeItem.getVelocity().multiply(0.3D));
                grenadeItem.setMetadata("mtwapens_grenade", new FixedMetadataValue(Main.getInstance(), true));
                break;
            }
            default:
                break;
        }
    }

    private void updateGrenadeLore(ItemStack itemStack, Grenade grenade) {
        ItemMeta im = itemStack.getItemMeta();
        ArrayList<String> weaponLore = new ArrayList<>();
        for (String string : (List<String>) grenade.getParameter(Grenade.GrenadeParameters.LORE)) {
            string = string.replace("<Ranged-Damage>", grenade.getParameter(Grenade.GrenadeParameters.RANGED_DAMAGE).toString())
                    .replace("<Durability>", String.valueOf(NBTEditor.getInt(itemStack, "durability")));
            weaponLore.add(string);
        }
        im.setLore(weaponLore);
        itemStack.setItemMeta(im);
    }
}
