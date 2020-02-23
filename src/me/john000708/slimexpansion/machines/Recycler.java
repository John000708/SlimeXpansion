package me.john000708.slimexpansion.machines;

import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.john000708.slimexpansion.Items;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

/**
 * Created by John on 14.04.2016.
 */
public abstract class Recycler extends AContainer {

    public Recycler(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public void registerDefaultRecipes() {
        registerRecipe(8, new ItemStack[] {}, new ItemStack[] {Items.SCRAP_BOX});
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.WOODEN_HOE);
    }

    @Override
    public String getInventoryTitle() {
        return "&4Recycler";
    }

    @Override
    public String getMachineIdentifier() {
        return "RECYCLER";
    }

    @Override
    protected void tick(Block b) {
        if (isProcessing(b)) {
            int timeleft = progress.get(b);
            if (timeleft > 0) {
                ChestMenuUtils.updateProgressbar(BlockStorage.getInventory(b), 22, timeleft,
                    processing.get(b).getTicks(), getProgressBar());

                if (ChargableBlock.isChargable(b)) {
                    if (ChargableBlock.getCharge(b) < getEnergyConsumption()) return;
                    ChargableBlock.addCharge(b, -getEnergyConsumption());
                    progress.put(b, timeleft - 1);
                } else progress.put(b, timeleft - 1);
            } else {
                BlockStorage.getInventory(b).replaceExistingItem(22, new CustomItem(Material.BLACK_STAINED_GLASS_PANE
                    , " "));
                pushItems(b, processing.get(b).getOutput());

                progress.remove(b);
                processing.remove(b);
            }
        } else {
            for (int slot : getInputSlots()) {
                if (BlockStorage.getInventory(b).getItemInSlot(slot) != null) {
                    MachineRecipe r = new MachineRecipe(4, new ItemStack[0], new ItemStack[] {Items.SCRAP_BOX});
                    if (!fits(b, r.getOutput())) return;
                    BlockStorage.getInventory(b).replaceExistingItem(slot,
                        InvUtils.decreaseItem(BlockStorage.getInventory(b).getItemInSlot(slot), 1));
                    processing.put(b, r);
                    progress.put(b, r.getTicks());
                    break;
                }
            }
        }
    }
}