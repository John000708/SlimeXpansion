package me.john000708.slimexpansion.machines;

import me.john000708.slimexpansion.Items;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class RedstoneReceiver extends SimpleSlimefunItem<BlockTicker> {

    public RedstoneReceiver(Category category) {
        super(category, Items.REDSTONE_RECEIVER,
                RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{SlimefunItems.DAMASCUS_STEEL_INGOT, new ItemStack(Material.GLASS),
                        SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT,
                        new ItemStack(Material.REDSTONE_BLOCK), SlimefunItems.DAMASCUS_STEEL_INGOT,
                        SlimefunItems.CORINTHIAN_BRONZE_INGOT, SlimefunItems.ADVANCED_CIRCUIT_BOARD,
                        SlimefunItems.CORINTHIAN_BRONZE_INGOT});
    }

    @Override
    public BlockTicker getItemHandler() {
        return new BlockTicker() {

            @Override
            public boolean isSynchronized() {
                return false;
            }

            @Override
            public void tick(Block block, SlimefunItem slimefunItem, Config config) {
                BlockStorage.addBlockInfo(block, "strength", String.valueOf(block.getBlockPower()));
            }
        };
    }

}
