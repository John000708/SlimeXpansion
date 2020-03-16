package me.john000708.slimexpansion.machines;

import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public abstract class XpansionContainer extends AContainer {
    public XpansionContainer(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    public void updateEnergyConsumption(Block b) {
        int timeleft = progress.get(b);
        ChestMenuUtils.updateProgressbar(BlockStorage.getInventory(b), 22, timeleft,
                getProcessing(b).getTicks(), getProgressBar());

        if (ChargableBlock.isChargable(b)) {
            if (ChargableBlock.getCharge(b) < getEnergyConsumption()) return;
            ChargableBlock.addCharge(b, -getEnergyConsumption());
        }
        AContainer.progress.put(b, timeleft - 1);
    }

    @Nullable
    public MachineRecipe checkRecipes(Map<Integer, Integer> found, Block block) {
        for (MachineRecipe recipe : recipes) {
            for (ItemStack input : recipe.getInput()) {
                for (int slot : getInputSlots())
                    if (SlimefunManager.isItemSimilar(BlockStorage.getInventory(block).getItemInSlot(slot), input,
                            true)) {
                        found.put(slot, input.getAmount());
                        break;
                    }
            }
            if (found.size() == recipe.getInput().length) {
                return recipe;
            } else found.clear();
        }
        return null;
    }

    public void checkFoundRecipes(Map<Integer, Integer> found, Block block, MachineRecipe recipe) {
        for (Map.Entry<Integer, Integer> entry : found.entrySet()) {
            BlockStorage.getInventory(block).replaceExistingItem(entry.getKey(),
                    InvUtils.decreaseItem(BlockStorage.getInventory(block).getItemInSlot(entry.getKey()),
                            entry.getValue()));
        }
        processing.put(block, recipe);
        progress.put(block, recipe.getTicks());
    }
}
