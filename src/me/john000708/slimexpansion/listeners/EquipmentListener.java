package me.john000708.slimexpansion.listeners;

import io.github.thebusybiscuit.slimefun4.api.events.AutoDisenchantEvent;
import me.john000708.slimexpansion.Items;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.energy.ItemEnergy;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

/**
 * Created by John on 19.05.2016.
 */
public class EquipmentListener implements Listener {

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();

            if (Objects.requireNonNull(player.getEquipment()).getChestplate() != null) {
                if (SlimefunManager.isItemSimilar(player.getEquipment().getChestplate(), Items.ELECTRIC_CHESTPLATE,
                    false)) {

                    ItemStack chestPlate = player.getEquipment().getChestplate();

                    if (ItemEnergy.getStoredEnergy(chestPlate) >= 5) {
                        player.getEquipment().setChestplate(ItemEnergy.chargeItem(chestPlate,
                            (float) (e.getDamage() / -1.75)));

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
            ItemStack itemInhand = player.getInventory().getItemInMainHand();

            if (SlimefunManager.isItemSimilar(itemInhand, Items.NANO_BLADE, false)) {
                if (itemInhand.containsEnchantment(Enchantment.ARROW_INFINITE)) {
                    if (ItemEnergy.getStoredEnergy(itemInhand) >= 5) {
                        e.setDamage(e.getDamage() * 2.5);
                        ItemEnergy.chargeItem(itemInhand, (float) -2.5);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onNanoBladeDisenchant(AutoDisenchantEvent e) {
        ItemStack item = e.getItem();
        if (SlimefunManager.isItemSimilar(item, Items.NANO_BLADE, false)) {
            if (item.containsEnchantment(Enchantment.ARROW_INFINITE)) {
                e.setCancelled(true);
                item.removeEnchantment(Enchantment.ARROW_INFINITE);
            }
        }
    }
}
