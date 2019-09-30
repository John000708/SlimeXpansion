package me.john000708.machines;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import me.john000708.Items;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.utils.MachineHelper;

/**
 * Created by John on 16.04.2016.
 */
public abstract class UUFabricator extends AContainer {
    public UUFabricator(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, name, recipeType, recipe);
    }

    @Override
    public void registerDefaultRecipes() {
        registerRecipe(480, new ItemStack[0], new ItemStack[] {Items.UU_MATTER});
    }

    @Override
    public String getMachineIdentifier() {
        return "UU_FABRICATOR";
    }
    
    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.DIAMOND_AXE);
    }

    @Override
    public String getInventoryTitle() {
        return "&5UU Fabricator";
    }

    protected void tick(Block b) {
        if (isProcessing(b)) {
            int timeleft = progress.get(b);
            if (timeleft > 0) {
                MachineHelper.updateProgressbar(BlockStorage.getInventory(b), 22, timeleft, processing.get(b).getTicks(), getProgressBar());

                if (ChargableBlock.isChargable(b)) {
                    if (ChargableBlock.getCharge(b) < getEnergyConsumption()) return;
                    ChargableBlock.addCharge(b, -getEnergyConsumption());
                    for (int slot : getInputSlots()) {
                        if (SlimefunManager.isItemSimiliar(BlockStorage.getInventory(b).getItemInSlot(slot), Items.SCRAP_BOX, false)) {
                            InvUtils.removeItem(BlockStorage.getInventory(b).toInventory(), BlockStorage.getInventory(b).getItemInSlot(slot), 1);
                            if (progress.get(b) < 6) progress.put(b, 0);
                            progress.put(b, timeleft - 6);
                            return;
                        }
                    }
                    progress.put(b, timeleft - 1);
                } else progress.put(b, timeleft - 1);
            } else {
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
                for (Map.Entry<Integer, Integer> entry : found.entrySet()) {
                    BlockStorage.getInventory(b).replaceExistingItem(entry.getKey(), InvUtils.decreaseItem(BlockStorage.getInventory(b).getItemInSlot(entry.getKey()), entry.getValue()));
                }
                processing.put(b, r);
                progress.put(b, r.getTicks());
            }
        }
    }
}
