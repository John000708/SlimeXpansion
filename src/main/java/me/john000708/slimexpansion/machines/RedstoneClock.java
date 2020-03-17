package me.john000708.slimexpansion.machines;

import me.john000708.slimexpansion.Items;
import me.john000708.slimexpansion.utils.SchedulerHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import me.mrCookieSlime.Slimefun.cscorelib2.protection.ProtectableAction;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.AnaloguePowerable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by John on 13.11.2016.
 */
public class RedstoneClock extends SimpleSlimefunItem<XpansionBlockTicker> {

    private final int[] border = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26};

    private final SchedulerHandler schedulerHandler;

    public RedstoneClock(Category category, SchedulerHandler schedulerHandler) {
        super(category, Items.REDSTONE_CLOCK,
                RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{SlimefunItems.LEAD_INGOT, new ItemStack(Material.REDSTONE_BLOCK),
                        SlimefunItems.LEAD_INGOT, new ItemStack(Material.REDSTONE_BLOCK), new ItemStack(Material.CLOCK),
                        new ItemStack(Material.REDSTONE_BLOCK), SlimefunItems.LEAD_INGOT,
                        new ItemStack(Material.REDSTONE_BLOCK), SlimefunItems.LEAD_INGOT});

        this.schedulerHandler = schedulerHandler;

        new BlockMenuPreset("REDSTONE_CLOCK", "&4Redstone Clock") {

            @Override
            public void init() {
                constructMenu(this);
            }

            @Override
            public void newInstance(final BlockMenu blockMenu, final Block block) {
                if (BlockStorage.getLocationInfo(block.getLocation(), "time") != null) {
                    blockMenu.replaceExistingItem(11, new CustomItem(Material.REDSTONE_BLOCK, "&e+15 " +
                            "Seconds"));
                    blockMenu.addMenuClickHandler(11, (player, i, itemStack, clickAction) -> {
                        if (checkTime(block, 15)) {
                            BlockStorage.addBlockInfo(block, "time",
                                    String.valueOf(Integer.parseInt(BlockStorage.getLocationInfo(block.getLocation(),
                                            "time")) + 15));
                            BlockStorage.addBlockInfo(block, "timeLeft",
                                    BlockStorage.getLocationInfo(block.getLocation(), "time"));
                        }
                        newInstance(blockMenu, block);
                        return false;
                    });

                    blockMenu.replaceExistingItem(12, new CustomItem(new ItemStack(Material.REDSTONE_BLOCK), "&e+1 " +
                            "Second"));
                    blockMenu.addMenuClickHandler(12, (player, i, itemStack, clickAction) -> {
                        if (checkTime(block, 1)) {
                            BlockStorage.addBlockInfo(block, "time",
                                    String.valueOf(Integer.parseInt(BlockStorage.getLocationInfo(block.getLocation(),
                                            "time")) + 1));
                            BlockStorage.addBlockInfo(block, "timeLeft",
                                    BlockStorage.getLocationInfo(block.getLocation(), "time"));
                        }
                        newInstance(blockMenu, block);
                        return false;
                    });

                    blockMenu.replaceExistingItem(13, new CustomItem(Material.CLOCK,
                            "&bTick Every &e" + BlockStorage.getLocationInfo(block.getLocation(), "time") + " &bSeconds"));

                    blockMenu.replaceExistingItem(14, new CustomItem(new ItemStack(Material.REDSTONE_BLOCK), "&e-1 " +
                            "Second"));
                    blockMenu.addMenuClickHandler(14, (player, i, itemStack, clickAction) -> {
                        if (checkTime(block, -1)) {
                            BlockStorage.addBlockInfo(block, "time",
                                    String.valueOf(Integer.parseInt(BlockStorage.getLocationInfo(block.getLocation(),
                                            "time")) - 1));
                            BlockStorage.addBlockInfo(block, "timeLeft",
                                    BlockStorage.getLocationInfo(block.getLocation(), "time"));
                        }
                        newInstance(blockMenu, block);
                        return false;
                    });

                    blockMenu.replaceExistingItem(15, new CustomItem(new ItemStack(Material.REDSTONE_BLOCK), "&e-15 " +
                            "Seconds"));
                    blockMenu.addMenuClickHandler(15, (player, i, itemStack, clickAction) -> {
                        if (checkTime(block, -15)) {
                            BlockStorage.addBlockInfo(block, "time",
                                    String.valueOf(Integer.parseInt(BlockStorage.getLocationInfo(block.getLocation(),
                                            "time")) - 15));
                            BlockStorage.addBlockInfo(block, "timeLeft",
                                    BlockStorage.getLocationInfo(block.getLocation(), "time"));
                        }
                        newInstance(blockMenu, block);
                        return false;
                    });
                } else {
                    BlockStorage.addBlockInfo(block, "time", "1");
                    BlockStorage.addBlockInfo(block, "timeLeft", "1");
                    newInstance(blockMenu, block);
                }
            }

            @Override
            public boolean canOpen(Block block, Player player) {
                return player.hasPermission("slimefun.inventory.bypass") || SlimefunPlugin.getProtectionManager().hasPermission(player, block.getLocation(), ProtectableAction.ACCESS_INVENTORIES);
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow itemTransportFlow) {
                return new int[0];
            }
        };
    }

    private void constructMenu(BlockMenuPreset preset) {
        for (int i : border) {
            preset.addItem(i, new CustomItem(Material.CYAN_STAINED_GLASS_PANE, " "), (player, i1, itemStack,
                                                                                      clickAction) -> false);
        }
        for (int i = 11; i <= 15; i++) {
            preset.addMenuClickHandler(i, (player, i12, itemStack, clickAction) -> false);
        }
    }

    private boolean checkTime(Block block, int time) {
        int timeAfter = Integer.parseInt(BlockStorage.getLocationInfo(block.getLocation(), "time")) + time;

        return timeAfter > 0 && timeAfter <= 3600;
    }

    private void tick(Block block) {
        BlockStorage.getInventory(block).replaceExistingItem(13, new CustomItem(Material.CLOCK, "&bTick Every" +
                " &e" + BlockStorage.getLocationInfo(block.getLocation(), "time") + " &bSeconds",
                "&7Time Left: " + BlockStorage.getLocationInfo(block.getLocation(), "timeLeft") + "s"));

        int timeLeft = Integer.parseInt(BlockStorage.getLocationInfo(block.getLocation(), "timeLeft"));

        if (timeLeft == 0) {
            BlockStorage.addBlockInfo(block, "timeLeft", BlockStorage.getLocationInfo(block.getLocation(),
                    "time"));
            schedulerHandler.runTaskLater(() -> {
                AnaloguePowerable powerable = (AnaloguePowerable) block.getBlockData();
                powerable.setPower(powerable.getMaximumPower());
                block.setBlockData(powerable);
            }, 1L);

            schedulerHandler.runTaskLater(() -> {
                AnaloguePowerable powerable = (AnaloguePowerable) block.getBlockData();
                powerable.setPower(0);
                block.setBlockData(powerable);
            }, 20L);
        } else {
            BlockStorage.addBlockInfo(block, "timeLeft", String.valueOf(timeLeft - 1));
        }
    }

    @Override
    public XpansionBlockTicker getItemHandler() {
        return new XpansionBlockTicker(this::tick);
    }
}
