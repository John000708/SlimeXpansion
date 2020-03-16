package me.john000708.slimexpansion.machines;

import io.github.thebusybiscuit.slimefun4.api.geo.GEOResource;
import me.john000708.slimexpansion.Items;
import me.john000708.slimexpansion.SlimeXpansion;
import me.john000708.slimexpansion.utils.SchedulerHandler;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import me.mrCookieSlime.Slimefun.cscorelib2.materials.MaterialCollection;
import me.mrCookieSlime.Slimefun.cscorelib2.materials.MaterialCollections;
import me.mrCookieSlime.Slimefun.cscorelib2.protection.ProtectableAction;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.Random;

/**
 * Created by John on 16.05.2016.
 */
public class DeepDepthMiner extends SimpleSlimefunItem<BlockTicker> {

    private static final int[] headBorder = {0, 1, 10, 18, 19};
    private static final int[] resultBorder = {7, 8, 16, 25, 26};
    private static final int[] toggleBorder = {27, 28, 36, 37, 45, 46, 34, 35, 43, 44, 52, 53};
    private static final int[] stoneBorder = {20, 21, 23, 24, 29, 30, 32, 33, 38, 39, 41, 42};
    private static final int[] bedrockBorder = {47, 48, 49, 50, 51};
    private static final int[] laserBar = {13, 22, 31, 40};

    private static final Random random = new Random();

    private final GEOResource thorium;

    private int time = 0;
    private int processTime = 3;
    private int laserPos = 0;

    private SchedulerHandler schedulerHandler;

    public DeepDepthMiner(Category category, SchedulerHandler schedulerHandler) {
        super(category, Items.DEEP_DEPTH_MINER, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{SlimefunItems.REINFORCED_PLATE, Items.BEDROCK_DUST, SlimefunItems.REINFORCED_PLATE,
                        SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.CARBONADO_EDGED_CAPACITOR,
                        SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.REINFORCED_ALLOY_INGOT,
                        new ItemStack(Material.BEACON), SlimefunItems.REINFORCED_ALLOY_INGOT});

        this.schedulerHandler = schedulerHandler;

        new BlockMenuPreset("DEEP_MINER", "&6Deep Depth Miner") {

            public void init() {
                constructMenu(this);
            }

            @Override
            public void newInstance(final BlockMenu menu, final Block block) {
                if (BlockStorage.getLocationInfo(block.getLocation(), "enabled") != null) {
                    if (BlockStorage.getLocationInfo(block.getLocation(), "enabled").equals("true")) {
                        for (int i : toggleBorder) {
                            menu.replaceExistingItem(i, new CustomItem(Material.GREEN_STAINED_GLASS_PANE, "&aEnabled"));
                            menu.addMenuClickHandler(i, (player, i12, itemStack, clickAction) -> {
                                BlockStorage.addBlockInfo(block, "enabled", "false");
                                newInstance(menu, block);
                                return false;
                            });
                        }
                    } else if (BlockStorage.getLocationInfo(block.getLocation(), "enabled").equals("false")) {
                        menu.replaceExistingItem(4, new CustomItem(Material.DIAMOND_BLOCK, "&3Laser Idle"));

                        for (int i : toggleBorder) {
                            menu.replaceExistingItem(i, new CustomItem(Material.RED_STAINED_GLASS_PANE, "&cDisabled"));
                            menu.addMenuClickHandler(i, (player, i1, itemStack, clickAction) -> {
                                BlockStorage.addBlockInfo(block, "enabled", "true");
                                newInstance(menu, block);
                                return false;
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

        thorium = SlimefunPlugin.getRegistry().getGEOResources()
                .get(new NamespacedKey(SlimefunPlugin.instance, "thoruim")).orElse(null);
    }

    public int getEnergyConsumption() {
        return 512;
    }

    @Override
    public BlockTicker getItemHandler() {
        return new XpansionBlockTicker(this::tick);
    }

    protected void tick(final Block block) {
        if (BlockStorage.getLocationInfo(block.getLocation(), "enabled") == null
                || BlockStorage.getLocationInfo(block.getLocation(), "enabled").equals("false"))
            return;

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
            BlockStorage.getInventory(block).replaceExistingItem(4,
                    new CustomItem(new ItemStack(Material.REDSTONE_BLOCK), "&4No Bedrock Found"));
            return;
        }

        if (ChargableBlock.getCharge(block) < getEnergyConsumption()) return;
        ChargableBlock.addCharge(block, -getEnergyConsumption());

        if (!(BlockStorage.getInventory(block).getItemInSlot(9) != null
                && SlimefunManager.isItemSimilar(BlockStorage.getInventory(block).getItemInSlot(9), Items.LASER_CHARGE,
                false))) {
            BlockStorage.getInventory(block).replaceExistingItem(4,
                    new CustomItem(new ItemStack(Material.REDSTONE_BLOCK), "&4No Laser Charge Found"));
            return;
        }

        ItemStack laserCharge = BlockStorage.getInventory(block).getItemInSlot(9);
        ItemMeta meta = laserCharge.getItemMeta();
        int durability =
                Integer.parseInt(ChatColor.stripColor(Objects.requireNonNull(laserCharge.getItemMeta()).getLore().get(3).replace("Durability: ", "").split("/")[0]));
        List<String> lore = laserCharge.getItemMeta().getLore();

        if (durability > 1) {
            lore.set(3, ChatColor.translateAlternateColorCodes('&',
                    "&7Durability: " + (durability - 1) + "/1024"));
            meta.setLore(lore);
            laserCharge.setItemMeta(meta);
            BlockStorage.getInventory(block).replaceExistingItem(9, laserCharge);
        } else {
            BlockStorage.getInventory(block).replaceExistingItem(9, new ItemStack(Material.AIR));
        }

        BlockStorage.getInventory(block).replaceExistingItem(4, new CustomItem(new ItemStack(Material.EMERALD_BLOCK),
                "&aLaser Operational"));

        if (laserPos == 3) {
            BlockStorage.getInventory(block).replaceExistingItem(laserBar[laserPos - 1],
                    new CustomItem(Material.LIGHT_GRAY_STAINED_GLASS_PANE, " "));
            BlockStorage.getInventory(block).replaceExistingItem(laserBar[laserPos],
                    new CustomItem(new ItemStack(Material.REDSTONE), " "));

            laserPos = 0;
        } else {
            if (laserPos == 0) {
                BlockStorage.getInventory(block).replaceExistingItem(laserBar[3],
                        new CustomItem(Material.LIGHT_GRAY_STAINED_GLASS_PANE, " "));
                BlockStorage.getInventory(block).replaceExistingItem(laserBar[laserPos],
                        new CustomItem(new ItemStack(Material.REDSTONE), " "));

                laserPos++;
                return;
            }
            BlockStorage.getInventory(block).replaceExistingItem(laserBar[laserPos],
                    new CustomItem(Material.REDSTONE, " "));
            BlockStorage.getInventory(block).replaceExistingItem(laserBar[laserPos - 1],
                    new CustomItem(Material.LIGHT_GRAY_STAINED_GLASS_PANE, " "));

            laserPos++;
        }

        for (int i = block.getLocation().getBlockY(); i > 1; i--) {
            block.getWorld().spawnParticle(Particle.REDSTONE, new Location(block.getWorld(), block.getX() + 0.5, i,
                    block.getZ() + 0.5), 1, new Particle.DustOptions(Color.RED, 2));
        }

        if (processTime == 0) {
            processTime = 3;
            ItemStack outputItem = null;

            if (random.nextInt(100) <= 5) {
                final OptionalInt optSupplies = SlimefunPlugin.getGPSNetwork().getResourceManager()
                        .getSupplies(thorium, block.getWorld(), block.getX() >> 4, block.getZ() >> 4);

                if (optSupplies.isPresent() && optSupplies.getAsInt() > 0) {
                    SlimefunPlugin.getGPSNetwork().getResourceManager()
                            .setSupplies(thorium, block.getWorld(), block.getX() >> 4, block.getZ() >> 4,
                                    optSupplies.getAsInt());
                    outputItem = Items.THORIUM;
                }
            } else {
                MaterialCollection ores = MaterialCollections.getAllOres();
                outputItem = new ItemStack(ores.get(random.nextInt(ores.size())));
            }

            if (outputItem != null && fits(block, new ItemStack[]{outputItem})) {
                pushItems(block, new ItemStack[]{outputItem});
            } else {
                final ItemStack dropItem = outputItem;
                schedulerHandler.runTaskLater(() -> {
                    if (dropItem != null) {
                        block.getWorld().dropItem(block.getLocation(), dropItem);
                    }
                }, 0L);
            }
        } else {
            processTime--;
        }

    }

    private void constructMenu(BlockMenuPreset preset) {
        for (int i : headBorder) {
            preset.addItem(i, new CustomItem(Material.CYAN_STAINED_GLASS_PANE, " "), (player, i1, itemStack,
                                                                                      clickAction) -> false);
        }

        for (int i : resultBorder) {
            preset.addItem(i, new CustomItem(Material.ORANGE_STAINED_GLASS_PANE, " "), (player, i18, itemStack,
                                                                                        clickAction) -> false);
        }

        for (int i : toggleBorder) {
            preset.addItem(i, new CustomItem(Material.GREEN_STAINED_GLASS_PANE, " "), (player, i17, itemStack,
                                                                                       clickAction) -> false);
        }

        for (int i : stoneBorder) {
            preset.addItem(i, new CustomItem(Material.GRAY_STAINED_GLASS_PANE, " "), (player, i16, itemStack,
                                                                                      clickAction) -> false);
        }

        for (int i : bedrockBorder) {
            preset.addItem(i, new CustomItem(Material.BLACK_STAINED_GLASS_PANE, " "), (player, i15, itemStack,
                                                                                       clickAction) -> false);
        }

        for (int i = 11; i <= 15; i++) {
            preset.addItem(i, new CustomItem(Material.BROWN_STAINED_GLASS_PANE, " "), (player, i14, itemStack,
                                                                                       clickAction) -> false);
        }

        for (int i = 2; i <= 6; i++) {
            preset.addItem(i, new CustomItem(Material.LIME_STAINED_GLASS_PANE, " "), (player, i13, itemStack,
                                                                                      clickAction) -> false);
        }
        for (int i : laserBar) {
            preset.addItem(i, new CustomItem(Material.LIGHT_GRAY_STAINED_GLASS_PANE, " "), (player, i12, itemStack,
                                                                                            clickAction) -> false);
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
