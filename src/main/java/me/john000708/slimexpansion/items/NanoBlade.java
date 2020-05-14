package me.john000708.slimexpansion.items;

import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.john000708.slimexpansion.Items;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.ChargableItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemUseHandler;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class NanoBlade extends ChargableItem {

    public NanoBlade(Category category) {
        super(category, Items.NANO_BLADE,
            RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {null, Items.MAG_THOR, null, null, Items.MAG_THOR,
                null, null, SlimefunItems.ADVANCED_CIRCUIT_BOARD, null});

        addItemHandler((ItemUseHandler) event -> {
            if (SlimefunUtils.isItemSimilar(event.getItem(), Items.NANO_BLADE, false)) {
                if (event.getItem().getEnchantments().containsKey(Enchantment.ARROW_INFINITE)) {
                    event.getItem().removeEnchantment(Enchantment.ARROW_INFINITE);
                } else {
                    event.getItem().addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 10);
                }
            }
        });
    }
}
