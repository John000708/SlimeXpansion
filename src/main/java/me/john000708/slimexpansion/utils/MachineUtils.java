package me.john000708.slimexpansion.utils;

import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MachineUtils {

    //Private constructor to hide implicit public one
    private MachineUtils() {}

    public static Inventory inject(Block b, int[] outputSlots) {
        int size = BlockStorage.getInventory(b).toInventory().getSize();
        Inventory inv = Bukkit.createInventory(null, size);
        for (int i = 0; i < size; i++) {
            inv.setItem(i, new CustomItem(new ItemStack(Material.COMMAND_BLOCK), "&4ALL YOUR PLACEHOLDERS ARE BELONG TO US"));
        }
        for (int slot : outputSlots) {
            inv.setItem(slot, BlockStorage.getInventory(b).getItemInSlot(slot));
        }
        return inv;
    }
}
