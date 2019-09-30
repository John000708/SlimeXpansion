package me.john000708.machines;

import java.util.Iterator;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.energy.ItemEnergy;

/**
 * Created by John on 16.04.2016.
 */
public abstract class WirelessCharger extends SlimefunItem {
    public WirelessCharger(Category category, ItemStack item, String name, RecipeType recipeType, final ItemStack[] recipe) {
        super(category, item, name, recipeType, recipe);

        registerBlockHandler(name, new SlimefunBlockHandler() {
            @Override
            public void onPlace(Player player, Block block, SlimefunItem slimefunItem) {

            }

            @Override
            public boolean onBreak(Player player, Block block, SlimefunItem slimefunItem, UnregisterReason unregisterReason) {
                me.john000708.holograms.WirelessCharger.getArmorStand(block).remove();
                return true;
            }
        });
    }

    public int getEnergyConsumption() {
        return 50;
    }


    @Override
    public void register(boolean slimefun) {
        addItemHandler(new BlockTicker() {
            @Override
            public boolean isSynchronized() {
                return true;
            }

            @Override
            public void uniqueTick() {

            }

            @Override
            public void tick(Block block, SlimefunItem slimefunItem, Config config) {
                WirelessCharger.this.tick(block);
            }
        });

        super.register(slimefun);
    }

    protected void tick(Block b) {
        Iterator<Entity> iterator = me.john000708.holograms.WirelessCharger.getArmorStand(b).getNearbyEntities(4D, 4D, 4D).iterator();
        while (iterator.hasNext()) {
            Entity entity = iterator.next();
            if (entity instanceof Player) {
                Player player = (Player) entity;
                for (ItemStack itemStack : player.getInventory().getContents()) {
                    if (ItemEnergy.getMaxEnergy(itemStack) > 0) {
                        if (ItemEnergy.getStoredEnergy(itemStack) < ItemEnergy.getMaxEnergy(itemStack)) {
                            if (ChargableBlock.isChargable(b)) {
                                if (ChargableBlock.getCharge(b) < 50) return;
                                ChargableBlock.addCharge(b, -50);
                                ItemEnergy.chargeItem(itemStack, 1);
                            }
                            break;
                        }
                    }
                }
            }
        }
    }
}
