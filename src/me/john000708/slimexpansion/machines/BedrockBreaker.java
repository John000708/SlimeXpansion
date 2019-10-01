package me.john000708.slimexpansion.machines;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.john000708.slimexpansion.Items;
import me.john000708.slimexpansion.SlimeXpansion;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import me.mrCookieSlime.Slimefun.cscorelib2.protection.ProtectableAction;

/**
 * Created by John on 18.04.2016.
 */
public class BedrockBreaker extends SimpleSlimefunItem<BlockTicker> {

    private static final int headBorder[] = {0, 1, 2, 9, 11, 18, 19, 20};
    private static final int progressBorder[] = {3, 4, 5, 6, 7, 8, 12, 13, 16, 17, 21, 22, 23, 24, 25, 26};
    private static final int resultBorder[] = {27, 28, 29, 36, 38, 45, 46, 47};
    private static final int toggleBorder[] = {30, 31, 32, 33, 34, 35, 39, 40, 43, 44, 48, 49, 50, 51, 52, 53};
    private static final int toggleSlots[] = {41, 42};

    private int time = 0;
    private int timeLeft = 15;

    public BedrockBreaker(Category category, ItemStack item, String name, RecipeType recipeType, final ItemStack[] recipe) {
        super(category, item, name, recipeType, recipe);

        new BlockMenuPreset(name, "&4Bedrock Breaker") {
        	
        	@Override
            public void init() {
                constructMenu(this);
            }
        	
            @Override
            public void newInstance(final BlockMenu menu, final Block block) {
                if (BlockStorage.getLocationInfo(block.getLocation(), "enabled") != null) {
                    menu.replaceExistingItem(14, new CustomItem(new ItemStack(Material.DIAMOND_BLOCK), "&3Breaker Idle"));
                    menu.replaceExistingItem(15, new CustomItem(new ItemStack(Material.DIAMOND_BLOCK), "&3Breaker Idle"));
                    if (!BlockStorage.getLocationInfo(block.getLocation(), "enabled").equalsIgnoreCase("true")) {
                        for (int i : toggleSlots) {
                            menu.replaceExistingItem(i, new CustomItem(Material.RED_STAINED_GLASS_PANE, "&cDisabled"));
                            menu.addMenuClickHandler(i, new MenuClickHandler() {
                                @Override
                                public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction) {
                                    BlockStorage.addBlockInfo(block, "enabled", "true");
                                    newInstance(menu, block);
                                    return false;
                                }
                            });
                        }
                    } else {
                        for (int i : toggleSlots) {
                            menu.replaceExistingItem(i, new CustomItem(Material.GREEN_STAINED_GLASS_PANE, "&aEnabled"));
                            menu.addMenuClickHandler(i, new MenuClickHandler() {
                                @Override
                                public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction) {
                                    BlockStorage.addBlockInfo(block, "enabled", "false");
                                    newInstance(menu, block);
                                    return false;
                                }
                            });
                        }
                    }
                } else {
                    BlockStorage.addBlockInfo(block, "enabled", "false");
                    newInstance(menu, block);
                }
            }


            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                if (flow.equals(ItemTransportFlow.INSERT)) return new int[]{10};
                else return new int[]{37};
            }

            public boolean canOpen(Block b, Player p) {
                return p.hasPermission("slimefun.inventory.bypass") || SlimefunPlugin.getProtectionManager().hasPermission(p, b.getLocation(), ProtectableAction.ACCESS_INVENTORIES);
            }
        };
    }
    
    public int getEnergyConsumption() {
        return 4098;
    }

    @Override
    public BlockTicker getItemHandler() {
    	return new BlockTicker() {
        	
            @Override
            public boolean isSynchronized() {
                return false;
            }

            @Override
            public void uniqueTick() {
                time++;
            }

            @Override
            public void tick(Block block, SlimefunItem slimefunItem, Config config) {
                BedrockBreaker.this.tick(block);
            }
    	};
    }

    protected void tick(final Block block) {
        if (!(time % 2 == 0)) return;
        
        if (BlockStorage.getLocationInfo(block.getLocation(), "enabled") == null || BlockStorage.getLocationInfo(block.getLocation(), "enabled").equals("false"))
            return;
        
        if (getEnergyConsumption() > ChargableBlock.getCharge(block)) return;
        
        if (block.getRelative(BlockFace.DOWN).getType() != Material.BEDROCK) {
            updateStatus(block, new CustomItem(new ItemStack(Material.REDSTONE_BLOCK), "&4No Bedrock Found"));
            return;
        }
        
        if (!(BlockStorage.getInventory(block).getItemInSlot(10) != null && SlimefunManager.isItemSimiliar(BlockStorage.getInventory(block).getItemInSlot(10), Items.BEDROCK_DRILL, false))) {
            updateStatus(block, new CustomItem(new ItemStack(Material.REDSTONE_BLOCK), "&4No Drill Found"));
            return;
        }
        
        final Block bedrockBlock = block.getRelative(BlockFace.DOWN);
        ItemStack drillItem = BlockStorage.getInventory(block).getItemInSlot(10);
        ItemMeta meta = drillItem.getItemMeta();
        List<String> lore = meta.getLore();
        int durability = Integer.valueOf(ChatColor.stripColor(lore.get(3).replace("Durability: ", "").split("/")[0]));

        if (durability > 1) {
            lore.set(3, ChatColor.translateAlternateColorCodes('&', "&7Durability: " + String.valueOf(durability - 1) + "/1024"));
            meta.setLore(lore);
            drillItem.setItemMeta(meta);
            BlockStorage.getInventory(block).replaceExistingItem(10, drillItem);
        } else {
            BlockStorage.getInventory(block).replaceExistingItem(10, new ItemStack(Material.AIR));
        }

        if (BlockStorage.getLocationInfo(block.getLocation(), "durability") == null)
            BlockStorage.addBlockInfo(block, "durability", "10");

        ChargableBlock.addCharge(block, -getEnergyConsumption());

        if (timeLeft == 0) {
            if (Double.valueOf(BlockStorage.getLocationInfo(block.getLocation(), "durability")) == 0) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        bedrockBlock.setType(Material.AIR);
                        block.getWorld().playSound(block.getLocation(), Sound.BLOCK_STONE_BREAK, 1F, .75F);
                    }
                }.runTaskLater(SlimeXpansion.plugin, 1L);
                if (fits(block, new ItemStack[]{Items.BEDROCK_DUST})) {
                    pushItems(block, new ItemStack[]{Items.BEDROCK_DUST});
                    BlockStorage.addBlockInfo(block, "durability", "10");
                }
            } else {
                BlockStorage.addBlockInfo(block, "durability", String.valueOf(Double.valueOf(BlockStorage.getLocationInfo(block.getLocation(), "durability")) - .5));
            }
            timeLeft = 15;
        } else {

            block.getWorld().spawnParticle(Particle.BLOCK_CRACK, block.getLocation().add(.5, .5, .5), 50, Material.BEDROCK.createBlockData());
            updateStatus(block, new CustomItem(new ItemStack(Material.EMERALD_BLOCK), "&aBreaker Operational", "", "&7Bedrock Durability: " + BlockStorage.getLocationInfo(block.getLocation(), "durability")));
            timeLeft--;
        }
    }

    protected void pushItems(Block b, ItemStack[] items) {
        Inventory inv = inject(b);
        inv.addItem(items);

        for (int slot : getOutputSlots()) {
            BlockStorage.getInventory(b).replaceExistingItem(slot, inv.getItem(slot));
        }
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

    public int[] getOutputSlots() {
        return new int[]{37};
    }


    private void constructMenu(final BlockMenuPreset preset) {
        for (int i : headBorder) {
            preset.addItem(i, new CustomItem(Material.CYAN_STAINED_GLASS_PANE, " "), new ChestMenu.MenuClickHandler() {
                @Override
                public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction) {
                    return false;
                }
            });
        }

        for (int i : progressBorder) {
            preset.addItem(i, new CustomItem(Material.GRAY_STAINED_GLASS_PANE, " "), new ChestMenu.MenuClickHandler() {
                @Override
                public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction) {
                    return false;
                }
            });
        }
        for (int i : resultBorder) {
            preset.addItem(i, new CustomItem(Material.ORANGE_STAINED_GLASS_PANE, " "), new ChestMenu.MenuClickHandler() {
                @Override
                public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction) {
                    return false;
                }
            });
        }

        for (int i : toggleBorder) {
            preset.addItem(i, new CustomItem(Material.BLACK_STAINED_GLASS_PANE, " "), new ChestMenu.MenuClickHandler() {
                @Override
                public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction) {
                    return false;
                }
            });
        }

        preset.addMenuClickHandler(14, new ChestMenu.MenuClickHandler() {
            @Override
            public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction) {
                preset.addItem(14, new CustomItem(new ItemStack(Material.DIAMOND_BLOCK), "&3Breaker Idle"));
                return false;
            }
        });

        preset.addMenuClickHandler(15, new ChestMenu.MenuClickHandler() {
            @Override
            public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction) {
                preset.addItem(15, new CustomItem(new ItemStack(Material.DIAMOND_BLOCK), "&3Breaker Idle"));
                return false;
            }
        });
    }

    private void updateStatus(Block block, ItemStack statusItem) {
        BlockStorage.getInventory(block).replaceExistingItem(14, statusItem);
        BlockStorage.getInventory(block).replaceExistingItem(15, statusItem);
    }
}
