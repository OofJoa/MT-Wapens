package com.jazzkuh.mtwapens.function.listeners;

import com.jazzkuh.mtwapens.Main;
import com.jazzkuh.mtwapens.function.enums.ShowDurability;
import com.jazzkuh.mtwapens.function.objects.Melee;
import com.jazzkuh.mtwapens.function.objects.Weapon;
import com.jazzkuh.mtwapens.messages.Messages;
import com.jazzkuh.mtwapens.utils.Utils;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MeleeDamageListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getEntity() instanceof LivingEntity) || !(event.getDamager() instanceof Player)) return;

        Player player = (Player) event.getDamager();

        if (player.getInventory().getItemInMainHand().getType() == Material.AIR) return;
        if (player.getInventory().getItemInMainHand().getItemMeta() == null) return;
        if (!player.getInventory().getItemInMainHand().getItemMeta().hasDisplayName()) return;

        ItemStack itemStack = player.getInventory().getItemInMainHand();

        if (!NBTEditor.contains(itemStack, "mtwapens_melee")) return;
        String meleeType = NBTEditor.getString(itemStack, "mtwapens_melee");

        LivingEntity entity = (LivingEntity) event.getEntity();
        event.setCancelled(true);
        event.setDamage(0D);

        if (Main.getMelee().getConfig().getString("melee." + meleeType + ".name") == null) {
            player.getInventory().removeItem(player.getInventory().getItemInMainHand());
            Utils.sendMessage(player, "&cYour melee weapon has been removed from the config files and has therefore been destroyed.");
            return;
        }

        if (NBTEditor.getInt(player.getInventory().getItemInMainHand(), "durability") <= 0) {
            player.getInventory().removeItem(player.getInventory().getItemInMainHand());
            return;
        }

        Melee melee = new Melee(meleeType);
        Double damage = (Double) melee.getParameter(Melee.MeleeParameters.DAMAGE);

        if (!(boolean) melee.getParameter(Melee.MeleeParameters.DISABLEDURABILITY)) {
            Utils.applyNBTTag(itemStack, "durability", NBTEditor.getInt(itemStack, "durability") - 1);
        }

        String showDurability = Main.getInstance().getConfig().getString("showDurability");
        if (ShowDurability.getInstance().isDurabilityShown(showDurability) == ShowDurability.Options.SHOOT || ShowDurability.getInstance().isDurabilityShown(showDurability) == ShowDurability.Options.BOTH) {
            Utils.sendMessage(player, Messages.DURABILITY.get()
                    .replace("<Durability>", String.valueOf(NBTEditor.getInt(itemStack, "durability"))));
        }
        this.updateMeleeLore(itemStack, melee);

        if (entity.getLocation().getWorld() != null) {
            entity.getLocation().getWorld().playEffect(entity.getLocation().add(0.0D, 1.0D, 0.0D), Effect.STEP_SOUND, 152);
        }

        if (damage > entity.getHealth()) {
            entity.setHealth(0D);
        } else {
            entity.setHealth(entity.getHealth() - damage);
        }

        if (NBTEditor.getInt(player.getInventory().getItemInMainHand(), "durability") <= 0) {
            player.getInventory().removeItem(player.getInventory().getItemInMainHand());
        }
    }

    @SuppressWarnings("unchecked")
    private void updateMeleeLore(ItemStack itemStack, Melee melee) {
        ItemMeta im = itemStack.getItemMeta();
        ArrayList<String> weaponLore = new ArrayList<>();
        for (String string : (List<String>) melee.getParameter(Melee.MeleeParameters.LORE)) {
            string = string
                    .replace("<Durability>", String.valueOf(NBTEditor.getInt(itemStack, "durability")));

            weaponLore.add(string);
        }
        im.setLore(weaponLore);
        itemStack.setItemMeta(im);
    }
}
