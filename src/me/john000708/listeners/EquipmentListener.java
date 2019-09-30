package me.john000708.listeners;

import me.john000708.Items;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.energy.ItemEnergy;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

/**
 * Created by John on 19.05.2016.
 */
public class EquipmentListener implements Listener {
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();

            if (player.getEquipment().getChestplate() != null) {
                if (SlimefunManager.isItemSimiliar(player.getEquipment().getChestplate(), Items.ELECTRIC_CHESTPLATE, false)) {

                    ItemStack chestPlate = player.getEquipment().getChestplate();

                    if (ItemEnergy.getStoredEnergy(chestPlate) >= 5) {
                        player.getEquipment().setChestplate(ItemEnergy.chargeItem(chestPlate, (float) (e.getDamage() / -1.75)));

                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDamageDeal(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            Player player = (Player) e.getDamager();

            if (SlimefunManager.isItemSimiliar(player.getInventory().getItemInMainHand(), Items.NANO_BLADE, false)) {
                ItemStack itemInhand = player.getInventory().getItemInMainHand();

                if (itemInhand.containsEnchantment(Enchantment.ARROW_INFINITE)) {
                    if (ItemEnergy.getStoredEnergy(itemInhand) >= 5) {
                        itemInhand.setDurability((short) 0);

                        e.setDamage(e.getDamage() * 2.5);
                        ItemEnergy.chargeItem(itemInhand, (float) -2.5);
                    }
                }
            }
        }
    }
}
