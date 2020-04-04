package me.john000708.slimexpansion.machines;

import me.john000708.slimexpansion.Items;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

/**
 * Created by John on 14.04.2016.
 */
public class Recycler extends XpansionContainer {

    public Recycler(Category category) {
        super(category, Items.RECYCLER,
            RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {SlimefunItems.ALUMINUM_INGOT,
                SlimefunItems.POWER_CRYSTAL, SlimefunItems.ALUMINUM_INGOT, SlimefunItems.CAN,
                SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.CAN, SlimefunItems.LEAD_INGOT,
                SlimefunItems.MEDIUM_CAPACITOR
                , SlimefunItems.LEAD_INGOT});
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
    protected void tick(Block block) {
        if (isProcessing(block)) {
            int timeleft = progress.get(block);
            if (timeleft > 0) {
                updateEnergyConsumption(block);
            } else {
                BlockStorage.getInventory(block).replaceExistingItem(22,
                    new CustomItem(Material.BLACK_STAINED_GLASS_PANE
                        , " "));
                pushItems(block, processing.get(block).getOutput());

                progress.remove(block);
                processing.remove(block);
            }
        } else {
            for (int slot : getInputSlots()) {
                if (BlockStorage.getInventory(block).getItemInSlot(slot) != null) {
                    MachineRecipe r = new MachineRecipe(4, new ItemStack[0], new ItemStack[] {Items.SCRAP_BOX});
                    if (!fits(block, r.getOutput())) return;
                    BlockStorage.getInventory(block).replaceExistingItem(slot,
                        InvUtils.decreaseItem(BlockStorage.getInventory(block).getItemInSlot(slot), 1));
                    processing.put(block, r);
                    progress.put(block, r.getTicks());
                    break;
                }
            }
        }
    }

    @Override
    public int getCapacity() {
        return 512;
    }

    @Override
    public int getEnergyConsumption() {
        return 32;
    }

    @Override
    public int getSpeed() {
        return 1;
    }
}