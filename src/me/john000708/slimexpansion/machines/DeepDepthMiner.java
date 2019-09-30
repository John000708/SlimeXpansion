package me.john000708.slimexpansion.machines;

import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.cscorelib2.materials.MaterialCollections;
import me.john000708.slimexpansion.Items;
import me.john000708.slimexpansion.SlimeXpansion;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.GEO.OreGenSystem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
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
 * Created by John on 16.05.2016.
 */
public abstract class DeepDepthMiner extends SlimefunItem {
	
    private static final int[] headBorder = {0, 1, 10, 18, 19};
    private static final int[] resultBorder = {7, 8, 16, 25, 26};
    private static final int[] toggleBorder = {27, 28, 36, 37, 45, 46, 34, 35, 43, 44, 52, 53};
    private static final int[] stoneBorder = {20, 21, 23, 24, 29, 30, 32, 33, 38, 39, 41, 42};
    private static final int[] bedrockBorder = {47, 48, 49, 50, 51};
    private static final int[] laserBar = {13, 22, 31, 40};

    private static final Random random = new Random();

    private int time = 0;
    private int processTime = 3;
    private int laserPos = 0;

    public DeepDepthMiner(Category category, ItemStack item, String name, RecipeType recipeType, final ItemStack[] recipe) {
        super(category, item, name, recipeType, recipe);

        new BlockMenuPreset(name, getInventoryTitle()) {
        	
            public void init() {
                constructMenu(this);
            }

            @Override
            public void newInstance(final BlockMenu menu, final Block block) {
                if (BlockStorage.getLocationInfo(block.getLocation(), "enabled") != null) {
                    if (BlockStorage.getLocationInfo(block.getLocation(), "enabled").equals("true")) {
                        for (int i : toggleBorder) {
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
                    } else if (BlockStorage.getLocationInfo(block.getLocation(), "enabled").equals("false")) {
                        menu.replaceExistingItem(4, new CustomItem(Material.DIAMOND_BLOCK, "&3Laser Idle"));

                        for (int i : toggleBorder) {
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
                    }
                } else {
                    BlockStorage.addBlockInfo(block, "enabled", "false");
                    newInstance(menu, block);
                }
            }

            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                if (flow.equals(ItemTransportFlow.INSERT)) return new int[]{9};
                else return new int[]{17};
            }

            public boolean canOpen(Block b, Player p) {
                return p.hasPermission("slimefun.inventory.bypass") || SlimefunPlugin.getProtectionManager().hasPermission(p, b.getLocation(), ProtectableAction.ACCESS_INVENTORIES);
            }
        };
    }

    public String getInventoryTitle() {
        return "&6Deep Depth Miner";
    }

    public int getEnergyConsumption() {
        return 512;
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
                time++;
            }

            @Override
            public void tick(Block block, SlimefunItem slimefunItem, Config config) {
                DeepDepthMiner.this.tick(block);
            }
        });
        super.register(slimefun);
    }


    protected void tick(final Block block) {
        if (BlockStorage.getLocationInfo(block.getLocation(), "enabled") == null || BlockStorage.getLocationInfo(block.getLocation(), "enabled").equals("false"))
            return;
        if (!(time % 2 == 0)) return;

        boolean stop = false;

        for (int y = block.getY() - 1; y >= 1; y--) {
            Block targetBlock = block.getWorld().getBlockAt(block.getX(), y, block.getZ());
            if (targetBlock.getType() == Material.BEDROCK) break;
            else if (targetBlock.getType() != Material.AIR) {
                stop = true;
                break;
            }
        }

        if (stop) {
            BlockStorage.getInventory(block).replaceExistingItem(4, new CustomItem(new ItemStack(Material.REDSTONE_BLOCK), "&4No Bedrock Found"));
            return;
        }

        if (ChargableBlock.getCharge(block) < getEnergyConsumption()) return;
        ChargableBlock.addCharge(block, -getEnergyConsumption());

        if (!(BlockStorage.getInventory(block).getItemInSlot(9) != null && SlimefunManager.isItemSimiliar(BlockStorage.getInventory(block).getItemInSlot(9), Items.LASER_CHARGE, false))) {
            BlockStorage.getInventory(block).replaceExistingItem(4, new CustomItem(new ItemStack(Material.REDSTONE_BLOCK), "&4No Laser Charge Found"));
            return;
        }

        ItemStack laserCharge = BlockStorage.getInventory(block).getItemInSlot(9);
        ItemMeta meta = laserCharge.getItemMeta();
        int durability = Integer.valueOf(ChatColor.stripColor(laserCharge.getItemMeta().getLore().get(3).replace("Durability: ", "").split("/")[0]));
        List<String> lore = laserCharge.getItemMeta().getLore();

        if (durability > 1) {
            lore.set(3, ChatColor.translateAlternateColorCodes('&', "&7Durability: " + String.valueOf(durability - 1) + "/1024"));
            meta.setLore(lore);
            laserCharge.setItemMeta(meta);
            BlockStorage.getInventory(block).replaceExistingItem(9, laserCharge);
        } else {
            BlockStorage.getInventory(block).replaceExistingItem(9, new ItemStack(Material.AIR));
        }

        BlockStorage.getInventory(block).replaceExistingItem(4, new CustomItem(new ItemStack(Material.EMERALD_BLOCK), "&aLaser Operational"));

        if (laserPos == 3) {
            BlockStorage.getInventory(block).replaceExistingItem(laserBar[laserPos - 1], new CustomItem(Material.LIGHT_GRAY_STAINED_GLASS_PANE, " "));
            BlockStorage.getInventory(block).replaceExistingItem(laserBar[laserPos], new CustomItem(new ItemStack(Material.REDSTONE), " "));

            laserPos = 0;
        } else {
            if (laserPos == 0) {
                BlockStorage.getInventory(block).replaceExistingItem(laserBar[3], new CustomItem(Material.LIGHT_GRAY_STAINED_GLASS_PANE, " "));
                BlockStorage.getInventory(block).replaceExistingItem(laserBar[laserPos], new CustomItem(new ItemStack(Material.REDSTONE), " "));

                laserPos++;
                return;
            }
            BlockStorage.getInventory(block).replaceExistingItem(laserBar[laserPos], new CustomItem(new ItemStack(Material.REDSTONE), " "));
            BlockStorage.getInventory(block).replaceExistingItem(laserBar[laserPos - 1], new CustomItem(Material.LIGHT_GRAY_STAINED_GLASS_PANE, " "));

            laserPos++;
        }

        for (int i = block.getLocation().getBlockY(); i > 1; i--) {
            block.getWorld().spawnParticle(Particle.REDSTONE, new Location(block.getWorld(), block.getX() + 0.5, i, block.getZ() + 0.5), 1, new Particle.DustOptions(Color.RED, 2));
        }
        /*try {
            ParticleEffect.REDSTONE.drawLine(block.getLocation().add(0.5, 0.5, 0.5), new Location(block.getWorld(), block.getX() + 0.5, 0.5, block.getZ() + 0.5));
        } catch (Exception ex) {
            ex.printStackTrace();
        }*/

        if (processTime == 0) {
            processTime = 3;
            ItemStack outputItem = null;
            if (random.nextInt(100) <= 5) {
                if (OreGenSystem.wasResourceGenerated(OreGenSystem.getResource("Thorium"), block.getChunk())) {
                    if (OreGenSystem.getSupplies(OreGenSystem.getResource("Thorium"), block.getChunk(), false) > 0) {
                        OreGenSystem.setSupplies(OreGenSystem.getResource("Thorium"), block.getChunk(), OreGenSystem.getSupplies(OreGenSystem.getResource("Thorium"), block.getChunk(), false) - 1);
                        outputItem = Items.THORIUM;
                    }
                }
            } else {
            	Material[] ores = MaterialCollections.getAllOres();
                outputItem = new ItemStack(ores[random.nextInt(ores.length)]);
            }

            if (outputItem != null && fits(block, new ItemStack[]{outputItem})) {
                pushItems(block, new ItemStack[]{outputItem});
            } else {
                final ItemStack dropItem = outputItem;
                Bukkit.getScheduler().scheduleSyncDelayedTask(SlimeXpansion.plugin, new Runnable() {
                    @Override
                    public void run() {
                        if (dropItem != null) {
                            block.getWorld().dropItem(block.getLocation(), dropItem);
                        }
                    }
                });
            }
        } else {
            processTime--;
        }

    }


    private void constructMenu(BlockMenuPreset preset) {
        for (int i : headBorder) {
            preset.addItem(i, new CustomItem(Material.CYAN_STAINED_GLASS_PANE, " "), new ChestMenu.MenuClickHandler() {
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
            preset.addItem(i, new CustomItem(Material.GREEN_STAINED_GLASS_PANE, " "), new ChestMenu.MenuClickHandler() {
                @Override
                public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction) {
                    return false;
                }
            });
        }

        for (int i : stoneBorder) {
            preset.addItem(i, new CustomItem(Material.GRAY_STAINED_GLASS_PANE, " "), new ChestMenu.MenuClickHandler() {
                @Override
                public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction) {
                    return false;
                }
            });
        }

        for (int i : bedrockBorder) {
            preset.addItem(i, new CustomItem(Material.BLACK_STAINED_GLASS_PANE, " "), new ChestMenu.MenuClickHandler() {
                @Override
                public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction) {
                    return false;
                }
            });
        }

        for (int i = 11; i <= 15; i++) {
            preset.addItem(i, new CustomItem(Material.BROWN_STAINED_GLASS_PANE, " "), new ChestMenu.MenuClickHandler() {
                @Override
                public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction) {
                    return false;
                }
            });
        }

        for (int i = 2; i <= 6; i++) {
            preset.addItem(i, new CustomItem(Material.LIME_STAINED_GLASS_PANE, " "), new ChestMenu.MenuClickHandler() {
                @Override
                public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction) {
                    return false;
                }
            });
        }
        for (int i : laserBar) {
            preset.addItem(i, new CustomItem(Material.LIGHT_GRAY_STAINED_GLASS_PANE, " "), new ChestMenu.MenuClickHandler() {
                @Override
                public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction) {
                    return false;
                }
            });
        }

        preset.addItem(4, new CustomItem(Material.DIAMOND_BLOCK, "&3Laser Idle"));
    }

    private Inventory inject(Block b) {
        int size = BlockStorage.getInventory(b).toInventory().getSize();
        Inventory inv = Bukkit.createInventory(null, size);
        for (int i = 0; i < size; i++) {
            inv.setItem(i, new CustomItem(Material.COMMAND_BLOCK, "&4ALL YOUR PLACEHOLDERS ARE BELONG TO US"));
        }
        inv.setItem(17, BlockStorage.getInventory(b).getItemInSlot(17));
        return inv;
    }

    protected boolean fits(Block b, ItemStack[] items) {
        return inject(b).addItem(items).isEmpty();
    }

    protected void pushItems(Block b, ItemStack[] items) {
        Inventory inv = inject(b);
        inv.addItem(items);

        BlockStorage.getInventory(b).replaceExistingItem(17, inv.getItem(17));
    }
}
