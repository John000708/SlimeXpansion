package me.john000708.slimexpansion.machines;

import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.john000708.slimexpansion.Items;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.cscorelib2.inventory.InvUtils;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by John on 17.04.2016.
 */
public class RainMaker extends XpansionContainer {

    public RainMaker(Category category) {
        super(category, Items.RAIN_MAKER,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[] {Items.IODINE_CHARGE, SlimefunItems.BLISTERING_INGOT_3, Items.DISSIPATION_CHARGE,
                SlimefunItems.SILVER_INGOT, SlimefunItems.MEDIUM_CAPACITOR, SlimefunItems.SILVER_INGOT,
                SlimefunItems.LEAD_INGOT, SlimefunItems.POWER_CRYSTAL, SlimefunItems.LEAD_INGOT});
    }

    @Override
    public String getInventoryTitle() {
        return "&3Rain Maker";
    }

    @Override
    public String getMachineIdentifier() {
        return "RAIN_MAKER";
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.GOLDEN_HOE);
    }

    @Override
    public int getEnergyConsumption() {
        return 256;
    }

    @Override
    public int getSpeed() {
        return 1;
    }

    public void registerDefaultRecipes() {
        registerRecipe(30, new ItemStack[] {Items.IODINE_CHARGE}, new ItemStack[] {Items.EMPTY_CAPSULE});
        registerRecipe(30, new ItemStack[] {Items.DISSIPATION_CHARGE}, new ItemStack[] {Items.EMPTY_CAPSULE});
    }

    @Override
    public void preRegister() {
        super.preRegister();

        addItemHandler(new BlockTicker() {

            @Override
            public boolean isSynchronized() {
                return true;
            }

            @Override
            public void tick(Block block, SlimefunItem slimefunItem, Config config) {
                RainMaker.this.tick(block);
            }
        });
    }

    protected void tick(Block block) {
        if (isProcessing(block)) {
            int timeleft = progress.get(block);
            if (timeleft > 0) {
                updateEnergyConsumption(block);
            } else {
                ItemStack input = processing.get(block).getInput()[0];
                if (SlimefunUtils.isItemSimilar(input, Items.IODINE_CHARGE, false)) block.getWorld().setStorm(true);
                else if (SlimefunUtils.isItemSimilar(input, Items.DISSIPATION_CHARGE, false))
                    block.getWorld().setStorm(false);
                BlockStorage.getInventory(block).replaceExistingItem(22,
                    new CustomItem(Material.BLACK_STAINED_GLASS_PANE, " "));
                //pushItems(block, processing.get(block).getOutput());

                progress.remove(block);
                processing.remove(block);
            }
        } else {
            Map<Integer, Integer> found = new HashMap<>();
            MachineRecipe recipe = checkRecipes(found, block);

            if (recipe != null) {
                if (!InvUtils.fitAll(BlockStorage.getInventory(block).toInventory(),
                    recipe.getOutput(), this.getOutputSlots()))
                    return;
                ItemStack input = recipe.getInput()[0];
                if (SlimefunUtils.isItemSimilar(input, Items.IODINE_CHARGE, false)
                    && block.getWorld().hasStorm()
                    || SlimefunUtils.isItemSimilar(input, Items.DISSIPATION_CHARGE, false)
                    && !block.getWorld().hasStorm()) {
                    return;
                }
                checkFoundRecipes(found, block, recipe);
            }
        }
    }


    @Override
    public int getCapacity() {
        return 512;
    }
}
