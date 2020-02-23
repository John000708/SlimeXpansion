package me.john000708.slimexpansion.machines;

import me.john000708.slimexpansion.Items;
import me.john000708.slimexpansion.SlimeXpansion;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import me.mrCookieSlime.Slimefun.cscorelib2.protection.ProtectableAction;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by John on 22.05.2016.
 */
public class ChunkLoader extends SimpleSlimefunItem<BlockTicker> {

    private int time = 0;

    public ChunkLoader(Category category, SlimefunItemStack itemStack, String name, RecipeType recipeType,
                       ItemStack[] recipe) {
        super(category, itemStack, recipeType, recipe);

        new BlockMenuPreset(name, "&dChunk Loader") {

            @Override
            public void init() {
                constructMenu(this);
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                if (flow.equals(ItemTransportFlow.INSERT)) return new int[] {22};
                else return new int[] {22};
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
        if (BlockStorage.getLocationInfo(block.getLocation(), "timeLeft") == null)
            BlockStorage.addBlockInfo(block, "timeLeft", "0");

        BlockMenu menu = BlockStorage.getInventory(block);
        int processTime = Integer.parseInt(BlockStorage.getLocationInfo(block.getLocation(), "timeLeft"));

        if (processTime > 0) {
            processTime--;

            BlockStorage.addBlockInfo(block, "timeLeft",
                String.valueOf(Integer.parseInt(BlockStorage.getLocationInfo(block.getLocation(), "timeLeft")) - 1));
            menu.replaceExistingItem(4, new CustomItem(new ItemStack(Material.CLOCK),
                getTimeLeft(processTime), getProgress(processTime,
                SlimeXpansion.plugin.getChunkLoaderDuration())));
        } else {
            if (menu.getItemInSlot(13) == null || !SlimefunManager.isItemSimilar(menu.getItemInSlot(13),
                Items.THORIUM, true)) {
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
            preset.addItem(i, new CustomItem(Material.PURPLE_STAINED_GLASS_PANE, " "), (player, i1, itemStack,
                                                                                        clickAction) -> false);
        }

        for (int i = 14; i <= 26; i++) {
            preset.addItem(i, new CustomItem(Material.PURPLE_STAINED_GLASS_PANE, " "), (player, i12, itemStack,
                                                                                        clickAction) -> false);
        }
    }

    private String getTimeLeft(int seconds) {
        String timeleft = "";

        int minutes = (int) (seconds / 60L);
        if (minutes > 0) {
            timeleft = timeleft + minutes + "m ";
        }

        seconds -= minutes * 60;
        timeleft = timeleft + seconds + "s";
        return ChatColor.translateAlternateColorCodes('&', "&7" + timeleft + " left");
    }

    private String getProgress(int time, int total) {
        StringBuilder progress = new StringBuilder();
        float percentage = Math.round(((((total - time) * 100.0F) / total) * 100.0F) / 100.0F);

        if (percentage < 16.0F) progress.append("&4");
        else if (percentage < 32.0F) progress.append("&c");
        else if (percentage < 48.0F) progress.append("&6");
        else if (percentage < 64.0F) progress.append("&e");
        else if (percentage < 80.0F) progress.append("&2");
        else progress.append("&a");

        int rest = 20;
        for (int i = (int) percentage; i >= 5; i = i - 5) {
            progress.append(":");
            rest--;
        }

        progress.append("&7");

        for (int i = 0; i < rest; i++) {
            progress.append(":");
        }

        progress.append(" - ").append(percentage).append("%");
        return ChatColor.translateAlternateColorCodes('&', progress.toString());
    }
}
