package me.john000708.slimexpansion.items;

import me.john000708.slimexpansion.Items;
import me.john000708.slimexpansion.SlimeXpansion;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemUseHandler;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.config.Config;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ScrapBox extends SimpleSlimefunItem<ItemUseHandler> {

    private final boolean openScrapbox;
    private final Random random = new Random();
    private final List<ItemStack> scrapBoxLoot = new ArrayList<>();

    public ScrapBox(Category category, SlimefunItemStack item, RecipeType recipeType) {
        super(category, item, recipeType, new ItemStack[] {null, null, null, null, null, null, null, null, null});

        openScrapbox = SlimeXpansion.plugin.config.getBoolean("options.lootable-scrapbox");
        parseScrapboxDrops(SlimeXpansion.plugin.config);
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return (e) -> {
            if (!e.getPlayer().isSneaking() && SlimefunManager.isItemSimilar(e.getItem(), Items.SCRAP_BOX, true)) {
                if (openScrapbox && random.nextInt(100) <= 25) {
                    e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_HORSE_SADDLE, 0.5F, 1F);
                    if (e.getPlayer().getGameMode() != GameMode.CREATIVE)
                        e.getPlayer().getInventory().getItemInMainHand().setAmount(e.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
                    e.getPlayer().getWorld().dropItem(e.getPlayer().getLocation().add(0, 1, 0),
                        scrapBoxLoot.get(random.nextInt(scrapBoxLoot.size())));
                    e.cancel();
                }
            }
        };
    }

    private void parseScrapboxDrops(Config config) {
        if (!openScrapbox) return;

        for (String lootName : config.getStringList("scrapbox-items")) {
            boolean success;
            if (Material.getMaterial(lootName) != null) {
                scrapBoxLoot.add(new ItemStack(Material.getMaterial(lootName)));
                success = true;
            } else {
                success = false;
            }

            if (success) continue;

            if (SlimefunItem.getItem(lootName) != null) {
                scrapBoxLoot.add(SlimefunItem.getItem(lootName));
                success = true;
            } else {
                success = false;
            }

            if (!success) {
                SlimeXpansion.plugin.getLogger().info("There is no such item with name " + lootName);
            }
        }
    }

}
