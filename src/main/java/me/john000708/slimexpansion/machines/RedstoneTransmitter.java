package me.john000708.slimexpansion.machines;

import me.john000708.slimexpansion.Items;
import me.john000708.slimexpansion.utils.SchedulerHandler;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.AnaloguePowerable;
import org.bukkit.inventory.ItemStack;

public class RedstoneTransmitter extends SimpleSlimefunItem<BlockTicker> {

    private final SchedulerHandler schedulerHandler;
    
    private static final String TRANSMITTERLOCATION = "transmitterLoc";

    public RedstoneTransmitter(Category category, SchedulerHandler schedulerHandler) {
        super(category, Items.REDSTONE_TRANSMITTER,
                RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.ADVANCED_CIRCUIT_BOARD,
                        SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT,
                        new ItemStack(Material.REDSTONE_BLOCK), SlimefunItems.DAMASCUS_STEEL_INGOT,
                        SlimefunItems.CORINTHIAN_BRONZE_INGOT, new ItemStack(Material.GLASS),
                        SlimefunItems.CORINTHIAN_BRONZE_INGOT});

        this.schedulerHandler = schedulerHandler;
    }

    @Override
    public BlockTicker getItemHandler() {
        return new BlockTicker() {
            @Override
            public boolean isSynchronized() {
                return false;
            }

            @Override
            public void tick(Block block, SlimefunItem slimefunItem, Config config) {
                if (BlockStorage.getLocationInfo(block.getLocation(), TRANSMITTERLOCATION) != null) {
                    String[] serializedLoc =
                            BlockStorage.getLocationInfo(block.getLocation(), TRANSMITTERLOCATION).split(";");
                    World world = Bukkit.getWorld(serializedLoc[0]);
                    int x = Integer.parseInt(serializedLoc[1]);
                    int y = Integer.parseInt(serializedLoc[2]);
                    int z = Integer.parseInt(serializedLoc[3]);

                    assert world != null;
                    Block transmitterBlock = world.getBlockAt(x, y, z);

                    if (transmitterBlock.getType() == Material.AIR) {
                        BlockStorage.addBlockInfo(block, TRANSMITTERLOCATION, null);
                        AnaloguePowerable powerable = (AnaloguePowerable) block.getBlockData();
                        powerable.setPower(0);
                        block.setBlockData(powerable);
                        return;
                    }

                    int power = transmitterBlock.getBlockPower();
                    schedulerHandler.runTaskLater(() -> {
                        AnaloguePowerable powerable = (AnaloguePowerable) block.getBlockData();
                        powerable.setPower(power);
                        block.setBlockData(powerable); //TODO: make power dynamic
                    }, 1L);
                }
            }
        };
    }

}
