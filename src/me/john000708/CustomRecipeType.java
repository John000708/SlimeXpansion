package me.john000708;

import org.bukkit.Material;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;

/**
 * Created by John on 17.04.2016.
 */
public class CustomRecipeType {
	
    public static final RecipeType RECYCLER;
    public static final RecipeType UU_FABRICATOR;
    public static final RecipeType DEEP_MINER;
    public static final RecipeType BEDROCK_BREAKER;

    static {
        RECYCLER = new RecipeType(new CustomItem(Material.IRON_BLOCK, "&cRecycler", "", "&a&oPut any materials into it and get Scrap out of it"));
        DEEP_MINER = new RecipeType(new CustomItem(Material.BEACON, "&aDeep Depth Miner", "", "&a&oMine it with the Deep Depth Miner"));
        BEDROCK_BREAKER = new RecipeType(new CustomItem(Material.IRON_BLOCK, "&aBedrock Breaker", "", "&a&oObtain it by mining bedrock using the Bedrock Breaker"));
        UU_FABRICATOR = new RecipeType(new CustomItem(Material.PURPLE_STAINED_GLASS, "&aMatter Fabricator", "", "&a&oProduct of a Matter Fabricator"));
    }
}
