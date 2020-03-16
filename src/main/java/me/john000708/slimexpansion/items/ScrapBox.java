package me.john000708.slimexpansion.items;

import me.john000708.slimexpansion.CustomRecipeType;
import me.john000708.slimexpansion.Items;
import me.john000708.slimexpansion.SlimeXpansion;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemUseHandler;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.cscorelib2.config.Config;
import me.mrCookieSlime.Slimefun.cscorelib2.inventory.ItemUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

public class ScrapBox extends SimpleSlimefunItem<ItemUseHandler> {

    private final boolean openScrapbox;
    private final Random random = new Random();
    private final List<ItemStack> scrapBoxLoot = new ArrayList<>();

    public ScrapBox(Category category, Config config) {
        super(category, Items.SCRAP_BOX, CustomRecipeType.RECYCLER, new ItemStack[]{null, null, null, null, null, null, null, null, null});

        openScrapbox = config.getBoolean("options.lootable-scrapbox");
        parseScrapboxDrops(config);
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return event -> {
            if (!event.getPlayer().isSneaking() &&
                    openScrapbox && random.nextInt(100) <= 25) {
                event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_HORSE_SADDLE, 0.5F, 1F);
                if (event.getPlayer().getGameMode() != GameMode.CREATIVE)
                    ItemUtils.consumeItem(event.getPlayer().getInventory().getItemInMainHand(), false);
                event.getPlayer().getWorld().dropItem(event.getPlayer().getLocation().add(0, 1, 0),
                        scrapBoxLoot.get(random.nextInt(scrapBoxLoot.size())));
                event.cancel();
            }
        };
    }

    private void parseScrapboxDrops(Config config) {
        if (!openScrapbox) return;

        for (String lootName : config.getStringList("scrapbox-items")) {
            boolean success;
            Material material = Material.getMaterial(lootName);
            if (material != null) {
                scrapBoxLoot.add(new ItemStack(material));
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
                SlimeXpansion.getXpansionLogger().log(Level.INFO, "There is no such item with name " + lootName);
            }
        }
    }
}
