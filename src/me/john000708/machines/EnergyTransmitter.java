package me.john000708.machines;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import me.mrCookieSlime.Slimefun.cscorelib2.protection.ProtectableAction;

/**
 * Created by John on 02.09.2016.
 */
public class EnergyTransmitter extends SlimefunItem {

    public EnergyTransmitter(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, name, recipeType, recipe);

        new BlockMenuPreset(name, "&cEnergy Transmitter") {
            public void init() {
                constructMenu(this);
            }

            public void newInstance(final BlockMenu menu, final Block block) {
                if (BlockStorage.getLocationInfo(block.getLocation(), "enabled") != null) {
                    if (BlockStorage.getLocationInfo(block.getLocation(), "enabled").equalsIgnoreCase("true")) {
                        menu.replaceExistingItem(13, new CustomItem(Material.GREEN_STAINED_GLASS_PANE, "&aEnabled"));
                        menu.addMenuClickHandler(13, new MenuClickHandler() {
                            @Override
                            public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction) {
                                BlockStorage.addBlockInfo(block, "enabled", "false");
                                newInstance(menu, block);
                                return false;
                            }
                        });
                    } else {
                        menu.replaceExistingItem(13, new CustomItem(Material.RED_STAINED_GLASS_PANE, "&cDisabled"));
                        menu.addMenuClickHandler(13, new MenuClickHandler() {
                            @Override
                            public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction) {
                                BlockStorage.addBlockInfo(block, "enabled", "true");
                                newInstance(menu, block);
                                return false;
                            }
                        });
                    }
                } else {
                    BlockStorage.addBlockInfo(block, "enabled", "false");
                    newInstance(menu, block);
                }
            }

            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow itemTransportFlow) {
                return new int[0];
            }

            public boolean canOpen(Block b, Player p) {
                return p.hasPermission("slimefun.inventory.bypass") || SlimefunPlugin.getProtectionManager().hasPermission(p, b.getLocation(), ProtectableAction.ACCESS_INVENTORIES);
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
            public void tick(Block block, SlimefunItem slimefunItem, Config config) {
            }
            
        });
        super.register(slimefun);
    }

    private void constructMenu(BlockMenuPreset preset) {
        for (int i = 0; i <= 26; i++) {
            preset.addItem(i, new CustomItem(Material.GRAY_STAINED_GLASS_PANE, " "), new ChestMenu.MenuClickHandler() {
                @Override
                public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction) {
                    return false;
                }
            });
        }
    }

}
