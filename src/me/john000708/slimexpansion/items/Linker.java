package me.john000708.slimexpansion.items;

import me.john000708.slimexpansion.Items;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemUseHandler;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Optional;

public class Linker extends SimpleSlimefunItem<ItemUseHandler> {

    public Linker(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return (e) -> {
            Optional<Block> optClickedBlock = e.getClickedBlock();
            if (!optClickedBlock.isPresent())
                return;

            final Block clickedBlock = optClickedBlock.get();

            if (SlimefunManager.isItemSimilar(e.getItem(), Items.LINKER, false) && BlockStorage.check(clickedBlock) != null) {
                if (BlockStorage.check(clickedBlock, "REDSTONE_TRANSMITTER") || BlockStorage.check(clickedBlock,
                    "ENERGY_TRANSMITTER")) {
                    ItemMeta itemMeta = e.getItem().getItemMeta();
                    String lore[] = {"", itemMeta.getLore().get(1), itemMeta.getLore().get(2), "",
                        clickedBlock.getWorld().getName() + ";" + clickedBlock.getX() + ";" + clickedBlock.getY() +
                            ";" + clickedBlock.getZ()};
                    itemMeta.setLore(Arrays.asList(lore));
                    e.getItem().setItemMeta(itemMeta);
                    e.getPlayer().sendMessage(ChatColor.GREEN + "Transmitter Location bound!");
                } else if (BlockStorage.check(clickedBlock, "REDSTONE_RECEIVER") || BlockStorage.check(clickedBlock,
                    "ENERGY_RECEIVER")) {
                    if (e.getItem().getItemMeta().getLore().size() != 4 || !e.getItem().getItemMeta().getLore().get(3).equals("")) {
                        BlockStorage.addBlockInfo(clickedBlock, "transmitterLoc",
                            e.getItem().getItemMeta().getLore().get(4));
                        e.getPlayer().sendMessage(ChatColor.GREEN + "Transmitter Location set!");
                    } else {
                        e.getPlayer().sendMessage(ChatColor.RED + "No Bound Transmitter found!");
                    }
                }
            }
        };
    }
}
