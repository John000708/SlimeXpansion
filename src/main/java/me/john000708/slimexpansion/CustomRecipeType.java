package me.john000708.slimexpansion;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

/**
 * Created by John on 17.04.2016.
 */
public final class CustomRecipeType {

    public static final RecipeType RECYCLER = new RecipeType(
        new NamespacedKey(SlimeXpansion.getInstance(), "recycler"),
        new CustomItem(Material.IRON_BLOCK, "&cRecycler",
            "", "&a&oPut any materials into it and get Scrap out of it")
    );
    public static final RecipeType UU_FABRICATOR = new RecipeType(
        new NamespacedKey(SlimeXpansion.getInstance(), "matter_fabricator"),
        new CustomItem(Material.PURPLE_STAINED_GLASS, "&aMatter Fabricator",
            "", "&a&oProduct of a Matter Fabricator")
    );
    public static final RecipeType BEDROCK_BREAKER = new RecipeType(
        new NamespacedKey(SlimeXpansion.getInstance(), "bedrock_breaker"),
        new CustomItem(Material.IRON_BLOCK, "&aBedrock Breaker",
            "", "&a&oObtain it by mining bedrock using the Bedrock Breaker")
    );
    public static final RecipeType DEEP_MINER = new RecipeType(
        new NamespacedKey(SlimeXpansion.getInstance(), "deep_miner"),
        new CustomItem(Material.BEACON, "&aDeep Depth Miner",
            "", "&a&0Mine it with the Deep Depth Miner")
    );

    private CustomRecipeType() {}
}
