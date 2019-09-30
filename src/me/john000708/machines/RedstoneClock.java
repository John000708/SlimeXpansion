package me.john000708.machines;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.AnaloguePowerable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.john000708.SlimeXpansion;
import me.mrCookieSlime.CSCoreLibPlugin.CSCoreLib;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;

/**
 * Created by John on 13.11.2016.
 */
public class RedstoneClock extends SlimefunItem {
    private int boarder[] = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26};
    private int tickTime = 0;

    public RedstoneClock(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, name, recipeType, recipe);

        new BlockMenuPreset(name, "&4Redstone Clock") {
        	
            @Override
            public void init() {
                constructMenu(this);
            }

            @Override
            public void newInstance(final BlockMenu blockMenu, final Block block) {
                if (BlockStorage.getBlockInfo(block, "time") != null) {
                    blockMenu.replaceExistingItem(11, new CustomItem(new ItemStack(Material.REDSTONE_BLOCK), "&e+15 Seconds"));
                    blockMenu.addMenuClickHandler(11, new MenuClickHandler() {
                        @Override
                        public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction) {
                            if (checkTime(block, 15)) {
                                BlockStorage.addBlockInfo(block, "time", String.valueOf(Integer.valueOf(BlockStorage.getBlockInfo(block, "time")) + 15));
                                BlockStorage.addBlockInfo(block, "timeLeft", BlockStorage.getBlockInfo(block, "time"));
                            }
                            newInstance(blockMenu, block);
                            return false;
                        }
                    });

                    blockMenu.replaceExistingItem(12, new CustomItem(new ItemStack(Material.REDSTONE_BLOCK), "&e+1 Second"));
                    blockMenu.addMenuClickHandler(12, new MenuClickHandler() {
                        @Override
                        public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction) {
                            if (checkTime(block, 1)) {
                                BlockStorage.addBlockInfo(block, "time", String.valueOf(Integer.valueOf(BlockStorage.getBlockInfo(block, "time")) + 1));
                                BlockStorage.addBlockInfo(block, "timeLeft", BlockStorage.getBlockInfo(block, "time"));
                            }
                            newInstance(blockMenu, block);
                            return false;
                        }
                    });

                    blockMenu.replaceExistingItem(13, new CustomItem(Material.CLOCK, "&bTick Every &e" + BlockStorage.getBlockInfo(block, "time") + " &bSeconds"));

                    blockMenu.replaceExistingItem(14, new CustomItem(new ItemStack(Material.REDSTONE_BLOCK), "&e-1 Second"));
                    blockMenu.addMenuClickHandler(14, new MenuClickHandler() {
                        @Override
                        public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction) {
                            if (checkTime(block, -1)) {
                                BlockStorage.addBlockInfo(block, "time", String.valueOf(Integer.valueOf(BlockStorage.getBlockInfo(block, "time")) - 1));
                                BlockStorage.addBlockInfo(block, "timeLeft", BlockStorage.getBlockInfo(block, "time"));
                            }
                            newInstance(blockMenu, block);
                            return false;
                        }
                    });

                    blockMenu.replaceExistingItem(15, new CustomItem(new ItemStack(Material.REDSTONE_BLOCK), "&e-15 Seconds"));
                    blockMenu.addMenuClickHandler(15, new MenuClickHandler() {
                        @Override
                        public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction) {
                            if (checkTime(block, -15)) {
                                BlockStorage.addBlockInfo(block, "time", String.valueOf(Integer.valueOf(BlockStorage.getBlockInfo(block, "time")) - 15));
                                BlockStorage.addBlockInfo(block, "timeLeft", BlockStorage.getBlockInfo(block, "time"));
                            }
                            newInstance(blockMenu, block);
                            return false;
                        }
                    });
                } else {
                    BlockStorage.addBlockInfo(block, "time", "1");
                    BlockStorage.addBlockInfo(block, "timeLeft", "1");
                    newInstance(blockMenu, block);
                }
            }

            @Override
            public boolean canOpen(Block block, Player player) {
                return player.hasPermission("slimefun.inventory.bypass") || CSCoreLib.getLib().getProtectionManager().canAccessChest(player.getUniqueId(), block);
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow itemTransportFlow) {
                return new int[0];
            }
        };
    }


    @Override
    public void register(boolean slimefun) {
        addItemHandler(new BlockTicker() {
        	
            @Override
            public boolean isSynchronized() {
                return false;
            }

            @Override
            public void uniqueTick() {
                tickTime++;
            }

            @Override
            public void tick(final Block block, SlimefunItem slimefunItem, Config config) {
                if ((tickTime % 2) != 0) return;
                BlockStorage.getInventory(block).replaceExistingItem(13, new CustomItem(Material.CLOCK, "&bTick Every &e" + BlockStorage.getBlockInfo(block, "time") + " &bSeconds", "&7Time Left: " + BlockStorage.getBlockInfo(block, "timeLeft") + "s"));

                int timeLeft = Integer.valueOf(BlockStorage.getBlockInfo(block, "timeLeft"));

                if (timeLeft == 0) {
                    BlockStorage.addBlockInfo(block, "timeLeft", BlockStorage.getBlockInfo(block, "time"));
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            AnaloguePowerable powerable = (AnaloguePowerable) block.getBlockData();
                            powerable.setPower(powerable.getMaximumPower());
                            block.setBlockData(powerable);
                        }
                    }.runTaskLater(SlimeXpansion.plugin, 1);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            AnaloguePowerable powerable = (AnaloguePowerable) block.getBlockData();
                            powerable.setPower(0);
                            block.setBlockData(powerable);
                        }
                    }.runTaskLater(SlimeXpansion.plugin, 20);
                } else {
                    BlockStorage.addBlockInfo(block, "timeLeft", String.valueOf(timeLeft - 1));
                }
            }
        });
        super.register(slimefun);
    }


    private void constructMenu(BlockMenuPreset preset) {
        for (int i : boarder) {
            preset.addItem(i, new CustomItem(Material.CYAN_STAINED_GLASS_PANE, " "), new ChestMenu.MenuClickHandler() {
                @Override
                public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction) {
                    return false;
                }
            });
        }
        for (int i = 11; i <= 15; i++) {
            preset.addMenuClickHandler(i, new ChestMenu.MenuClickHandler() {
                @Override
                public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction) {
                    return false;
                }
            });
        }
    }

    private boolean checkTime(Block block, int time) {
        int timeAfter = Integer.valueOf(BlockStorage.getBlockInfo(block, "time")) + time;

        return timeAfter > 0 && timeAfter <= 3600;
    }
}
