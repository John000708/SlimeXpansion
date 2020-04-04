package me.john000708.slimexpansion.machines;

import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.john000708.slimexpansion.Items;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.cscorelib2.inventory.InvUtils;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by John on 16.04.2016.
 */
public class UUFabricator extends XpansionContainer {

    public UUFabricator(Category category) {
        super(category, Items.UU_FABRICATOR,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[] {SlimefunItems.REINFORCED_PLATE, SlimefunItems.POWER_CRYSTAL,
                SlimefunItems.REINFORCED_PLATE, SlimefunItems.BLISTERING_INGOT_3,
                SlimefunItems.CARBONADO_EDGED_CAPACITOR, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.PLUTONIUM,
                Items.SCRAP_BOX, SlimefunItems.PLUTONIUM});
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

    @Override
    public int getCapacity() {
        return 4098;
    }

    @Override
    public int getEnergyConsumption() {
        return 512;
    }

    @Override
    public int getSpeed() {
        return 1;
    }

    protected void tick(Block b) {
        if (isProcessing(b)) {
            int timeleft = progress.get(b);
            if (timeleft > 0) {
                ChestMenuUtils.updateProgressbar(BlockStorage.getInventory(b), 22, timeleft,
                    processing.get(b).getTicks(), getProgressBar());

                if (ChargableBlock.isChargable(b)) {
                    if (ChargableBlock.getCharge(b) < getEnergyConsumption()) return;
                    ChargableBlock.addCharge(b, -getEnergyConsumption());
                    for (int slot : getInputSlots()) {
                        if (InvUtils.removeItem(BlockStorage.getInventory(b).toInventory(), 1, false,
                            is -> SlimefunUtils.isItemSimilar(BlockStorage.getInventory(b).getItemInSlot(slot),
                                Items.SCRAP_BOX, false))
                        ) {
                            if (progress.get(b) < 6) progress.put(b, 0);
                            progress.put(b, timeleft - 6);
                            return;
                        }
                    }
                    progress.put(b, timeleft - 1);
                } else progress.put(b, timeleft - 1);
            } else {
                BlockStorage.getInventory(b).replaceExistingItem(22,
                    new CustomItem(Material.BLACK_STAINED_GLASS_PANE, " "));
                pushItems(b, processing.get(b).getOutput());

                progress.remove(b);
                processing.remove(b);
            }
        } else {
            MachineRecipe r = null;
            Map<Integer, Integer> found = new HashMap<>();
            for (MachineRecipe recipe : recipes) {
                for (ItemStack input : recipe.getInput()) {
                    for (int slot : getInputSlots()) {
                        if (SlimefunUtils.isItemSimilar(BlockStorage.getInventory(b).getItemInSlot(slot), input,
                            true)) {
                            found.put(slot, input.getAmount());
                            break;
                        }
                    }
                }
                if (found.size() == recipe.getInput().length) {
                    r = recipe;
                    break;
                } else found.clear();
            }

            if (r != null) {
                if (!fits(b, r.getOutput())) return;
                checkFoundRecipes(found, b, r);
            }
        }
    }
}
