package me.john000708.slimexpansion.machines;

import me.john000708.slimexpansion.Items;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import me.mrCookieSlime.Slimefun.cscorelib2.protection.ProtectableAction;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Created by John on 16.04.2016.
 */
public class UUTransmutator extends SlimefunItem {

    private static final int[] uuBorder = {0, 1, 2, 9, 11, 18, 19, 20};
    private static final int[] itemBorder = {3, 8, 12, 17, 21, 22, 23, 24, 25, 26};
    private static final int[] resultBorder = {27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51
            , 52, 53};
    private static final int[] itemsSlots = {4, 5, 6, 7, 13, 14, 15, 16};

    public UUTransmutator(Category category, ItemStack item, String name, RecipeType recipeType,
                          final ItemStack[] recipe) {
        super(category, item, name, recipeType, recipe);

        new BlockMenuPreset(name, "&5UU Transmutator") {

            public void init() {
                constructMenu(this);
            }

            @Override
            public void newInstance(final BlockMenu menu, final Block b) {
                if (!BlockStorage.hasBlockInfo(b) || BlockStorage.getLocationInfo(b.getLocation(), "selected-item") == null) {
                    for (int i : itemsSlots) {
                        menu.addMenuClickHandler(i, (player, i12, itemStack, clickAction) -> {
                            BlockStorage.addBlockInfo(b, "selected-item", String.valueOf(i12));
                            ItemStack item1 = BlockStorage.getInventory(b).getItemInSlot(i12);
                            item1.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
                            BlockStorage.getInventory(b).replaceExistingItem(i12, item1);
                            newInstance(menu, b);
                            return false;
                        });
                    }
                } else {
                    for (int i : itemsSlots) {
                        if (Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(), "selected-item")) == i) {
                            ItemStack targetItem = BlockStorage.getInventory(b).getItemInSlot(i);
                            targetItem.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
                            BlockStorage.getInventory(b).replaceExistingItem(i, targetItem);
                        }
                        menu.addMenuClickHandler(i, (player, i1, itemStack, clickAction) -> {
                            ItemStack prevItem =
                                    BlockStorage.getInventory(b).getItemInSlot(Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(), "selected-item")));
                            prevItem.removeEnchantment(Enchantment.ARROW_INFINITE);
                            itemStack.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
                            BlockStorage.addBlockInfo(b, "selected-item", String.valueOf(i1));
                            newInstance(menu, b);
                            return false;
                        });
                    }
                }

            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {

                if (flow.equals(ItemTransportFlow.INSERT)) return getInputSlots();
                else return getOutputSlots();
            }

            @Override
            public boolean canOpen(Block b, Player p) {
                return p.hasPermission("slimefun.inventory.bypass") || SlimefunPlugin.getProtectionManager().hasPermission(p, b.getLocation(), ProtectableAction.ACCESS_INVENTORIES);
            }

        };
        registerBlockHandler(name, (p, b, sfItem, reason) -> {
            for (int slot : getInputSlots()) {
                if (BlockStorage.getInventory(b).getItemInSlot(slot) != null)
                    b.getWorld().dropItemNaturally(b.getLocation(), BlockStorage.getInventory(b).getItemInSlot(slot));
            }
            for (int slot : getOutputSlots()) {
                if (BlockStorage.getInventory(b).getItemInSlot(slot) != null)
                    b.getWorld().dropItemNaturally(b.getLocation(), BlockStorage.getInventory(b).getItemInSlot(slot));
            }
            return true;
        });
    }

    public int getEnergyConsumption() {
        return 512;
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
                if (BlockStorage.getLocationInfo(block.getLocation(), "selected-item") == null) return;
                if (ChargableBlock.getCharge(block) < getEnergyConsumption()) return;

                BlockMenu inv = BlockStorage.getInventory(block);

                if (inv.getItemInSlot(10) == null) return;

                ItemStack itemToOutput =
                        inv.getItemInSlot(Integer.parseInt(BlockStorage.getLocationInfo(block.getLocation(), "selected" +
                                "-item"))).clone();
                itemToOutput.removeEnchantment(Enchantment.ARROW_INFINITE);

                if (fits(block, new ItemStack[]{itemToOutput})) {
                    if (SlimefunManager.isItemSimilar(inv.getItemInSlot(10), Items.UU_MATTER, false)
                            && inv.getItemInSlot(10).getAmount() >= 1) {
                        inv.replaceExistingItem(10, InvUtils.decreaseItem(inv.getItemInSlot(10), 1));
                        ChargableBlock.addCharge(block, -getEnergyConsumption());
                        pushItems(block, new ItemStack[]{itemToOutput});
                    }
                }
            }
        });

        super.preRegister();
    }

    private void constructMenu(BlockMenuPreset preset) {
        for (int i : uuBorder) {
            preset.addItem(i, new CustomItem(Material.PINK_STAINED_GLASS_PANE, " "), (player, i13, itemStack,
                                                                                      clickAction) -> false);
        }

        for (int i : itemBorder) {
            preset.addItem(i, new CustomItem(Material.GRAY_STAINED_GLASS_PANE, " "), (player, i12, itemStack,
                                                                                      clickAction) -> false);
        }

        for (int i : resultBorder) {
            preset.addItem(i, new CustomItem(Material.PURPLE_STAINED_GLASS_PANE, " "), (player, i1, itemStack,
                                                                                        clickAction) -> false);
        }

        preset.addItem(4, new ItemStack(Material.OAK_LOG, 64));
        preset.addItem(5, new ItemStack(Material.COAL, 32));
        preset.addItem(6, new ItemStack(Material.IRON_INGOT, 8));
        preset.addItem(7, new ItemStack(Material.GOLD_INGOT, 8));
        preset.addItem(13, new ItemStack(Material.REDSTONE, 4));
        preset.addItem(14, new ItemStack(Material.LAPIS_LAZULI, 4));
        preset.addItem(15, new ItemStack(Material.DIAMOND));
        preset.addItem(16, new ItemStack(Material.EMERALD));
    }

    public int[] getInputSlots() {
        return new int[]{10};
    }

    public int[] getOutputSlots() {
        return new int[]{37, 38, 39, 40, 41, 42, 43};
    }

    private Inventory inject(Block b) {
        int size = BlockStorage.getInventory(b).toInventory().getSize();
        Inventory inv = Bukkit.createInventory(null, size);
        for (int i = 0; i < size; i++) {
            inv.setItem(i, new CustomItem(Material.COMMAND_BLOCK, "&4ALL YOUR PLACEHOLDERS ARE BELONG TO US"));
        }
        for (int slot : getOutputSlots()) {
            inv.setItem(slot, BlockStorage.getInventory(b).getItemInSlot(slot));
        }
        return inv;
    }

    protected boolean fits(Block b, ItemStack[] items) {
        return inject(b).addItem(items).isEmpty();
    }

    protected void pushItems(Block b, ItemStack[] items) {
        Inventory inv = inject(b);
        inv.addItem(items);

        for (int slot : getOutputSlots()) {
            BlockStorage.getInventory(b).replaceExistingItem(slot, inv.getItem(slot));
        }
    }
}
