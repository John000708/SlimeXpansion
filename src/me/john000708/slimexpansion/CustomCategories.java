package me.john000708.slimexpansion;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Objects.Category;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Created by John on 10.07.2016.
 */
public class CustomCategories {

    public static final Category SLIMEFUN_XPANSION;
    public static final Category TERRAFORMER_MODULES;

    static {
        SLIMEFUN_XPANSION = new Category(new CustomItem(new ItemStack(Material.BEACON), "&5SlimeXpansion", "", "&a " +
            ">Click to open"));
        TERRAFORMER_MODULES = new Category(new CustomItem(new ItemStack(Material.ITEM_FRAME), "&2Terraformer Modules"
            , "", "&a >Click to open"));
    }
}
