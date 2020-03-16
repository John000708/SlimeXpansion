package me.john000708.slimexpansion.items;

import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import me.john000708.slimexpansion.Items;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemUseHandler;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Optional;

public class Linker extends SimpleSlimefunItem<ItemUseHandler> {

    public Linker(Category category) {
        super(category, Items.LINKER,
                RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{null, SlimefunItems.GOLD_24K, null, SlimefunItems.GOLD_24K,
                        SlimefunItems.ENERGY_REGULATOR, SlimefunItems.GOLD_24K, null, SlimefunItems.GOLD_24K, null});
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return event -> {
            Optional<Block> optClickedBlock = event.getClickedBlock();
            if (!optClickedBlock.isPresent())
                return;

            final Block clickedBlock = optClickedBlock.get();

            if (SlimefunManager.isItemSimilar(event.getItem(), Items.LINKER, false) && BlockStorage.check(clickedBlock) != null) {
                if (BlockStorage.check(clickedBlock, "REDSTONE_TRANSMITTER") || BlockStorage.check(clickedBlock,
                        "ENERGY_TRANSMITTER")) {
                    onTransmitterClick(event, clickedBlock);
                } else if (BlockStorage.check(clickedBlock, "REDSTONE_RECEIVER") || BlockStorage.check(clickedBlock,
                        "ENERGY_RECEIVER")) {
                    onRecieverClick(event, clickedBlock);
                }
            }
        };
    }

    private void onRecieverClick(PlayerRightClickEvent event, Block clickedBlock) {
        ItemMeta itemMeta = event.getItem().getItemMeta();
        assert itemMeta != null && itemMeta.getLore() != null;
        if (itemMeta.getLore().size() != 4 || !itemMeta.getLore().get(3).equals("")) {
            BlockStorage.addBlockInfo(clickedBlock, "transmitterLoc",
                    itemMeta.getLore().get(4));
            event.getPlayer().sendMessage(ChatColor.GREEN + "Transmitter Location set!");
        } else {
            event.getPlayer().sendMessage(ChatColor.RED + "No Bound Transmitter found!");
        }
    }

    private void onTransmitterClick(PlayerRightClickEvent event, Block clickedBlock) {
        ItemMeta itemMeta = event.getItem().getItemMeta();
        assert itemMeta != null && itemMeta.getLore() != null;
        String[] lore = {"", itemMeta.getLore().get(1), itemMeta.getLore().get(2), "",
                clickedBlock.getWorld().getName() + ";" + clickedBlock.getX() + ";" + clickedBlock.getY() +
                        ";" + clickedBlock.getZ()};
        itemMeta.setLore(Arrays.asList(lore));
        event.getItem().setItemMeta(itemMeta);
        event.getPlayer().sendMessage(ChatColor.GREEN + "Transmitter Location bound!");
    }
}
