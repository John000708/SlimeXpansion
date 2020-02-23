package me.john000708.slimexpansion.listeners;

import me.john000708.slimexpansion.Items;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.energy.ItemEnergy;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by John on 30.10.2016.
 */
public class SynthesizerListener implements Listener {

    @EventHandler
    public void onHunger(FoodLevelChangeEvent e) {
        if (e.getFoodLevel() < ((Player) e.getEntity()).getFoodLevel()) {
            Player p = (Player) e.getEntity();
            for (ItemStack item : p.getInventory().getContents()) {
                if (SlimefunManager.isItemSimilar(item, Items.FOOD_SYNTHESIZER, false)) {
                    if (ItemEnergy.getStoredEnergy(item) >= 3) {
                        ItemEnergy.chargeItem(item, -3F);
                        p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EAT, 1.5F, 1F);
                        e.setFoodLevel(20);
                        p.setSaturation(5);
                        break;
                    }
                }
            }
        }
    }
}
