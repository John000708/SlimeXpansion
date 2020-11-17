package me.john000708.slimexpansion.machines;

import me.john000708.slimexpansion.Items;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.energy.ItemEnergy;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by John on 16.04.2016.
 */
public class WirelessCharger extends SlimefunItem {

    public WirelessCharger(Category category) {
        super(category, Items.WIRELESS_CHARGER,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[] {null, SlimefunItems.POWER_CRYSTAL, null, SlimefunItems.GILDED_IRON,
                SlimefunItems.LARGE_CAPACITOR, SlimefunItems.GILDED_IRON, SlimefunItems.HEATING_COIL,
                SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.HEATING_COIL});
    }

    @Override
    public void preRegister() {
        addItemHandler(new BlockTicker() {

            @Override
            public boolean isSynchronized() {
                return true;
            }

            @Override
            public void tick(Block block, SlimefunItem slimefunItem, Config config) {
                WirelessCharger.this.tick(block);
            }
        });

        super.preRegister();
    }

    protected void tick(Block b) {
        b.getWorld().getNearbyEntities(b.getLocation(), 4, 4, 4, e -> e instanceof Player).forEach(e -> {
            Player p = (Player) e;
            for (ItemStack item : p.getInventory().getContents()) {
                if (ItemEnergy.getMaxEnergy(item) > 0) {
                    if (ItemEnergy.getStoredEnergy(item) < ItemEnergy.getMaxEnergy(item)) {
                        if (ChargableBlock.isChargable(b)) {
                            if (ChargableBlock.getCharge(b) < 50) return;
                            ChargableBlock.addCharge(b, -50);
                            ItemEnergy.chargeItem(item, 1);
                        }
                        break;
                    }
                }
            }
        });
    }
}
