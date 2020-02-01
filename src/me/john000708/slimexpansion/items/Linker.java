package me.john000708.slimexpansion.items;

import me.john000708.slimexpansion.Items;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemInteractionHandler;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class Linker extends SimpleSlimefunItem<ItemInteractionHandler> {

    public Linker(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, id, recipeType, recipe);
    }

    @Override
    public ItemInteractionHandler getItemHandler() {
        return (e, player, itemStack) -> {
            Block clickedBlock = e.getClickedBlock();
            if (SlimefunManager.isItemSimiliar(itemStack, Items.LINKER, false) && clickedBlock != null && BlockStorage.check(clickedBlock) != null) {
                if (BlockStorage.check(clickedBlock, "REDSTONE_TRANSMITTER") || BlockStorage.check(clickedBlock,
                    "ENERGY_TRANSMITTER")) {
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    String lore[] = {"", itemMeta.getLore().get(1), itemMeta.getLore().get(2), "",
                        clickedBlock.getWorld().getName() + ";" + clickedBlock.getX() + ";" + clickedBlock.getY() +
                            ";" + clickedBlock.getZ()};
                    itemMeta.setLore(Arrays.asList(lore));
                    itemStack.setItemMeta(itemMeta);
                    player.sendMessage(ChatColor.GREEN + "Transmitter Location bound!");
                } else if (BlockStorage.check(clickedBlock, "REDSTONE_RECEIVER") || BlockStorage.check(clickedBlock,
                    "ENERGY_RECEIVER")) {
                    if (itemStack.getItemMeta().getLore().size() != 4 || !itemStack.getItemMeta().getLore().get(3).equals("")) {
                        BlockStorage.addBlockInfo(clickedBlock, "transmitterLoc",
                            itemStack.getItemMeta().getLore().get(4));
                        player.sendMessage(ChatColor.GREEN + "Transmitter Location set!");
                    } else {
                        player.sendMessage(ChatColor.RED + "No Bound Transmitter found!");
                    }
                }
            }
            return false;
        };
    }
}
