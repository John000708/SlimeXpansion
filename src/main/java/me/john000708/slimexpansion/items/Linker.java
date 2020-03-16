package me.john000708.slimexpansion.items;

import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import me.john000708.slimexpansion.Items;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemUseHandler;
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

            String id = BlockStorage.checkID(clickedBlock);
            if (id != null) {
                if (id.equals("REDSTONE_TRANSMITTER") || id.equals("ENERGY_TRANSMITTER")) {
                    onTransmitterClick(event, clickedBlock);
                } else if (id.equals("REDSTONE_RECEIVER") || id.equals("ENERGY_RECEIVER")) {
                    onRecieverClick(event, clickedBlock);
                }
            }
        };
    }

    private void onRecieverClick(PlayerRightClickEvent event, Block clickedBlock) {
        ItemMeta itemMeta = event.getItem().getItemMeta();
        if (!itemMeta.getLore().isEmpty() || !itemMeta.getLore().get(3).equals("")) {
            BlockStorage.addBlockInfo(clickedBlock, "transmitterLoc",
                    itemMeta.getLore().get(4));
            event.getPlayer().sendMessage(ChatColor.GREEN + "Transmitter Location set!");
        } else {
            event.getPlayer().sendMessage(ChatColor.RED + "No Bound Transmitter found!");
        }
    }

    private void onTransmitterClick(PlayerRightClickEvent event, Block clickedBlock) {
        ItemMeta itemMeta = event.getItem().getItemMeta();
        itemMeta.setLore(Arrays.asList("", itemMeta.getLore().get(1), itemMeta.getLore().get(2), "",
                clickedBlock.getWorld().getName() + ";" + clickedBlock.getX() + ";" + clickedBlock.getY() +
                        ";" + clickedBlock.getZ()));
        event.getItem().setItemMeta(itemMeta);
        event.getPlayer().sendMessage(ChatColor.GREEN + "Transmitter Location bound!");
    }
}
