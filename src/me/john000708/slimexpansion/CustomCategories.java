package me.john000708.slimexpansion;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Objects.Category;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

/**
 * Created by John on 10.07.2016.
 */
public class CustomCategories {

    public static final Category SLIMEFUN_XPANSION;

    static {
        SLIMEFUN_XPANSION = new Category(new NamespacedKey(SlimeXpansion.plugin, "slimexpansion"),
            new CustomItem(new ItemStack(Material.BEACON), "&5SlimeXpansion", "", "&a " +
            ">Click to open"));
    }
}
