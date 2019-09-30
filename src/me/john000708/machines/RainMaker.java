package me.john000708.machines;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import me.john000708.Items;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.utils.MachineHelper;

/**
 * Created by John on 17.04.2016.
 */
public abstract class RainMaker extends AContainer {
    public RainMaker(Category category, ItemStack item, String name, RecipeType recipeType, final ItemStack[] recipe) {
        super(category, item, name, recipeType, recipe);
    }

    @Override
    public String getInventoryTitle() {
        return "&3Rain Maker";
    }

    @Override
    public String getMachineIdentifier() {
        return "&3Rain Maker";
    }

    public void registerDefaultRecipes() {
        registerRecipe(30, new ItemStack[] {Items.IODINE_CHARGE}, new ItemStack[]{Items.EMPTY_CAPSULE});
        registerRecipe(30, new ItemStack[] {Items.DISSIPATION_CHARGE}, new ItemStack[]{Items.EMPTY_CAPSULE});
    }

    @Override
    public void register(boolean slimefun) {
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

        super.register(slimefun);
    }


    protected void tick(Block b) {
        if (isProcessing(b)) {
            int timeleft = progress.get(b);
            if (timeleft > 0) {
            	MachineHelper.updateProgressbar(BlockStorage.getInventory(b), 22, timeleft, processing.get(b).getTicks(), getProgressBar());

                if (ChargableBlock.isChargable(b)) {
                    if (ChargableBlock.getCharge(b) < getEnergyConsumption()) return;
                    ChargableBlock.addCharge(b, -getEnergyConsumption());
                    progress.put(b, timeleft - 1);
                } else progress.put(b, timeleft - 1);
            } else {
                ItemStack input = processing.get(b).getInput()[0];
                if (SlimefunManager.isItemSimiliar(input, Items.IODINE_CHARGE, false)) b.getWorld().setStorm(true);
                else if (SlimefunManager.isItemSimiliar(input, Items.DISSIPATION_CHARGE, false)) b.getWorld().setStorm(false);
                BlockStorage.getInventory(b).replaceExistingItem(22, new CustomItem(Material.BLACK_STAINED_GLASS_PANE, " "));
                pushItems(b, processing.get(b).getOutput());

                progress.remove(b);
                processing.remove(b);
            }
        } else {
            MachineRecipe r = null;
            Map<Integer, Integer> found = new HashMap<>();
            outer:
            for (MachineRecipe recipe : recipes) {
                for (ItemStack input : recipe.getInput()) {
                    slots:
                    for (int slot : getInputSlots()) {
                        if (SlimefunManager.isItemSimiliar(BlockStorage.getInventory(b).getItemInSlot(slot), input, true)) {
                            found.put(slot, input.getAmount());
                            break slots;
                        }
                    }
                }
                if (found.size() == recipe.getInput().length) {
                    r = recipe;
                    break outer;
                } else found.clear();
            }

            if (r != null) {
                if (!fits(b, r.getOutput())) return;
                ItemStack input = r.getInput()[0];
                if (SlimefunManager.isItemSimiliar(input, Items.IODINE_CHARGE, false)) {
                    if (b.getWorld().hasStorm()) return;
                } else if (SlimefunManager.isItemSimiliar(input, Items.DISSIPATION_CHARGE, false)) {
                    if (!b.getWorld().hasStorm()) return;
                }
                for (Map.Entry<Integer, Integer> entry : found.entrySet()) {
                    BlockStorage.getInventory(b).replaceExistingItem(entry.getKey(), InvUtils.decreaseItem(BlockStorage.getInventory(b).getItemInSlot(entry.getKey()), entry.getValue()));
                }
                processing.put(b, r);
                progress.put(b, r.getTicks());
            }
        }
    }
}
