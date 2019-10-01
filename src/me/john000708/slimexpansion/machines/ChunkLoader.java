package me.john000708.slimexpansion.machines;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.john000708.slimexpansion.Items;
import me.john000708.slimexpansion.SlimeXpansion;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import me.mrCookieSlime.Slimefun.cscorelib2.protection.ProtectableAction;
import me.mrCookieSlime.Slimefun.utils.MachineHelper;

/**
 * Created by John on 22.05.2016.
 */
public class ChunkLoader extends SimpleSlimefunItem<BlockTicker> {

    private int time = 0;
    private int processTime;

    public ChunkLoader(Category category, ItemStack itemStack, String name, RecipeType recipeType, ItemStack[] recipe) {
        super(category, itemStack, name, recipeType, recipe);

        new BlockMenuPreset(name, "&dChunk Loader") {
        	
        	@Override
            public void init() {
                constructMenu(this);
            }

        	@Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                if (flow.equals(ItemTransportFlow.INSERT)) return new int[]{22};
                else return new int[]{22};
            }

        	@Override
            public boolean canOpen(Block b, Player p) {
                return p.hasPermission("slimefun.inventory.bypass") || SlimefunPlugin.getProtectionManager().hasPermission(p, b.getLocation(), ProtectableAction.ACCESS_INVENTORIES);
            }
        };
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
                ChunkLoader.this.tick(block);
            }
        };
    }

    protected void tick(Block block) {
        if (!(time % 2 == 0)) return;
        if (BlockStorage.getLocationInfo(block.getLocation(), "timeLeft") == null) BlockStorage.addBlockInfo(block, "timeLeft", "0");

        BlockMenu menu = BlockStorage.getInventory(block);
        processTime = Integer.valueOf(BlockStorage.getLocationInfo(block.getLocation(), "timeLeft"));

        if (processTime > 0) {
            processTime--;

            BlockStorage.addBlockInfo(block, "timeLeft", String.valueOf(Integer.valueOf(BlockStorage.getLocationInfo(block.getLocation(), "timeLeft")) - 1));
            menu.replaceExistingItem(4, new CustomItem(new ItemStack(Material.CLOCK), MachineHelper.getTimeLeft(processTime), MachineHelper.getProgress(processTime, SlimeXpansion.plugin.getChunkLoaderDuration())));
        } else {
            if (menu.getItemInSlot(13) == null || !SlimefunManager.isItemSimiliar(menu.getItemInSlot(13), Items.THORIUM, true)) {
                menu.replaceExistingItem(4, new CustomItem(Material.PURPLE_STAINED_GLASS_PANE, " "));
                block.getWorld().setChunkForceLoaded(block.getChunk().getX(), block.getChunk().getZ(), false);
                return;
            }

            BlockStorage.addBlockInfo(block, "timeLeft", String.valueOf(SlimeXpansion.plugin.getChunkLoaderDuration()));
            block.getWorld().setChunkForceLoaded(block.getChunk().getX(), block.getChunk().getZ(), true);
            menu.replaceExistingItem(13, InvUtils.decreaseItem(menu.getItemInSlot(13), 1));

            processTime = SlimeXpansion.plugin.getChunkLoaderDuration();
        }
    }
    
    private void constructMenu(BlockMenuPreset preset) {
        for (int i = 0; i <= 12; i++) {
            preset.addItem(i, new CustomItem(Material.PURPLE_STAINED_GLASS_PANE, " "), new ChestMenu.MenuClickHandler() {
                @Override
                public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction) {
                    return false;
                }
            });
        }

        for (int i = 14; i <= 26; i++) {
            preset.addItem(i, new CustomItem(Material.PURPLE_STAINED_GLASS_PANE, " "), new ChestMenu.MenuClickHandler() {
                @Override
                public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction) {
                    return false;
                }
            });
        }
//13 empty
    }
}
