package me.john000708.slimexpansion;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.implementation.items.Alloy;
import me.john000708.slimexpansion.items.Linker;
import me.john000708.slimexpansion.items.ScrapBox;
import me.john000708.slimexpansion.listeners.ListenerSetup;
import me.john000708.slimexpansion.machines.*;
import me.john000708.slimexpansion.resources.ThoriumResource;
import me.john000708.slimexpansion.utils.SchedulerHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.Research;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.ChargableItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemUseHandler;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.bstats.bukkit.Metrics;
import me.mrCookieSlime.Slimefun.cscorelib2.config.Config;
import me.mrCookieSlime.Slimefun.cscorelib2.updater.BukkitUpdater;
import me.mrCookieSlime.Slimefun.cscorelib2.updater.GitHubBuildsUpdater;
import me.mrCookieSlime.Slimefun.cscorelib2.updater.Updater;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by John on 14.04.2016.
 */
public class SlimeXpansion extends JavaPlugin implements SlimefunAddon {

    private Config config;
    private int chunkLoaderDuration;

    private Category category;

    private SchedulerHandler schedulerHandler;

    @Override
    public void onEnable() {
        category = new Category(new NamespacedKey(this, "me/john000708/slimexpansion"),
                new CustomItem(Material.BEACON, "&5SlimeXpansion", "", "&a " +
                        ">Click to open"));

        schedulerHandler = new SchedulerHandler(this);

        config = new Config(this);

        // Setting up bStats
        new Metrics(this, 4112);

        setupUpdater();

        new ListenerSetup(this);

        chunkLoaderDuration = config.getInt("options.chunkloader-duration");

        registerItems();
        setupResearches();
        getLogger().info("SlimeXpansion has been enabled!");
    }

    private void setupUpdater() {
        // Setting up the Auto-Updater
        Updater updater;

        if (!getDescription().getVersion().startsWith("DEV - ")) {
            // We are using an official build, use the BukkitDev Updater
            updater = new BukkitUpdater(this, getFile(), 102902);
        } else {
            // If we are using a development build, we want to switch to our custom
            updater = new GitHubBuildsUpdater(this, getFile(), "J3fftw1/SlimeXpansion/master");
        }

        if (config.getBoolean("options.auto-update")) updater.start();
    }

    public int getChunkLoaderDuration() {
        return this.chunkLoaderDuration;
    }

    private void registerItems() {
        // Resources
        new ThoriumResource(config.getBoolean("options.thorium-via-geo-miner")).register();

        // Other crap
        new ScrapBox(category, getConfiguration()).register(this);

        new SlimefunItem(category, Items.UU_MATTER, CustomRecipeType.UU_FABRICATOR,
                new ItemStack[]{null, null, null, null, null, null, null, null, null}).register(this);

        new SlimefunItem(category, Items.EMPTY_CAPSULE, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{null, SlimefunItems.TIN_INGOT, null, SlimefunItems.TIN_INGOT, SlimefunItems.CAN,
                        SlimefunItems.TIN_INGOT, null, SlimefunItems.TIN_INGOT, null}).register(this);

        new SlimefunItem(category, Items.IODINE_CHARGE, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{Items.EMPTY_CAPSULE, SlimefunItems.SALT, SlimefunItems.SALT,
                        new ItemStack(Material.GUNPOWDER), null, null, null, null, null, null}).register(this);

        new SlimefunItem(category, Items.DISSIPATION_CHARGE,
                RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{Items.EMPTY_CAPSULE, new ItemStack(Material.SAND), Items.IODINE_CHARGE, null, null, null
                        , null, null, null}).register(this);

        new SlimefunItem(category, Items.BEDROCK_DUST, CustomRecipeType.BEDROCK_BREAKER,
                new ItemStack[]{null, null, null, null, null, null, null, null, null}).register(this);

        //Machines
        new Recycler(category);

        new UUFabricator(category, Items.UU_FABRICATOR,
                RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{SlimefunItems.REINFORCED_PLATE, SlimefunItems.POWER_CRYSTAL,
                        SlimefunItems.REINFORCED_PLATE, SlimefunItems.BLISTERING_INGOT_3,
                        SlimefunItems.CARBONADO_EDGED_CAPACITOR, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.PLUTONIUM,
                        Items.SCRAP_BOX, SlimefunItems.PLUTONIUM}) {

            @Override
            public int getCapacity() {
                return 4098;
            }

            @Override
            public int getEnergyConsumption() {
                return 512;
            }

            @Override
            public int getSpeed() {
                return 1;
            }

        };

        new UUTransmutator(category, Items.UU_TRANSMUTATOR, "UU_TRANSMUTATOR",
                RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{SlimefunItems.REINFORCED_PLATE, Items.UU_MATTER, SlimefunItems.REINFORCED_PLATE,
                        SlimefunItems.POWER_CRYSTAL, Items.UU_FABRICATOR, SlimefunItems.POWER_CRYSTAL,
                        SlimefunItems.PLUTONIUM, SlimefunItems.CARBONADO_EDGED_CAPACITOR, SlimefunItems.PLUTONIUM});

        new BedrockBreaker(category, schedulerHandler);

        new DeepDepthMiner(category, schedulerHandler);

        new WirelessCharger(category, Items.WIRELESS_CHARGER, "WIRELESS_CHARGER",
                RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{null, SlimefunItems.POWER_CRYSTAL, null, SlimefunItems.GILDED_IRON,
                        SlimefunItems.LARGE_CAPACITOR, SlimefunItems.GILDED_IRON, SlimefunItems.HEATING_COIL,
                        SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.HEATING_COIL});

        new RainMaker(category, Items.RAIN_MAKER,
                RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{Items.IODINE_CHARGE, SlimefunItems.BLISTERING_INGOT_3, Items.DISSIPATION_CHARGE,
                        SlimefunItems.SILVER_INGOT, SlimefunItems.MEDIUM_CAPACITOR, SlimefunItems.SILVER_INGOT,
                        SlimefunItems.LEAD_INGOT, SlimefunItems.POWER_CRYSTAL, SlimefunItems.LEAD_INGOT});

        new ChunkLoader(category, getChunkLoaderDuration()).register(this);

        new RedstoneTransmitter(category, schedulerHandler).register(this);

        new RedstoneReceiver(category, Items.REDSTONE_RECEIVER,
                RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{SlimefunItems.DAMASCUS_STEEL_INGOT, new ItemStack(Material.GLASS),
                        SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT,
                        new ItemStack(Material.REDSTONE_BLOCK), SlimefunItems.DAMASCUS_STEEL_INGOT,
                        SlimefunItems.CORINTHIAN_BRONZE_INGOT, SlimefunItems.ADVANCED_CIRCUIT_BOARD,
                        SlimefunItems.CORINTHIAN_BRONZE_INGOT}).register(this);

        new RedstoneClock(category, schedulerHandler).register(this);

        new Linker(category, Items.LINKER,
                RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{null, SlimefunItems.GOLD_24K, null, SlimefunItems.GOLD_24K,
                        SlimefunItems.ENERGY_REGULATOR, SlimefunItems.GOLD_24K, null, SlimefunItems.GOLD_24K, null}).register(this);

        new SlimefunItem(category, Items.BEDROCK_DRILL, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{null, SlimefunItems.REINFORCED_PLATE, null, SlimefunItems.REINFORCED_PLATE,
                        Items.UU_MATTER, SlimefunItems.REINFORCED_PLATE, null, SlimefunItems.REINFORCED_PLATE, null}).register(this);

        new SlimefunItem(category, Items.LASER_CHARGE, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{null, SlimefunItems.REINFORCED_ALLOY_INGOT, null, SlimefunItems.REINFORCED_ALLOY_INGOT,
                        new ItemStack(Material.REDSTONE), SlimefunItems.REINFORCED_ALLOY_INGOT, null,
                        SlimefunItems.REINFORCED_ALLOY_INGOT, null}).register(this);

        new SlimefunItem(category, Items.THORIUM, CustomRecipeType.DEEP_MINER,
                new ItemStack[]{null, null, null, null, new CustomItem(Material.PAPER, "&fHint!", "&a&oMake sure to " +
                        "first GEO-Scan the chunk in which you are", "&a&omining to discover Thorium!"), null, null, null,
                        null}).register(this);

        new Alloy(category, Items.MAG_THOR,
                new ItemStack[]{SlimefunItems.REINFORCED_ALLOY_INGOT, Items.THORIUM, SlimefunItems.MAGNESIUM_INGOT,
                        SlimefunItems.ZINC_INGOT, null, null, null, null, null}).register(this);

        new SlimefunItem(category, new SlimefunItemStack("BEDROCK", Material.BEDROCK,
                "&8Bedrock"), RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{null, Items.BEDROCK_DUST, null, Items.BEDROCK_DUST, Items.THORIUM, Items.BEDROCK_DUST,
                        null, Items.BEDROCK_DUST, null}).register(this);

        new ChargableItem(category, Items.ELECTRIC_CHESTPLATE,
                RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{Items.MAG_THOR, null, Items.MAG_THOR, Items.MAG_THOR, SlimefunItems.POWER_CRYSTAL,
                        Items.MAG_THOR, Items.MAG_THOR, Items.MAG_THOR, Items.MAG_THOR}).register(this);

        ChargableItem nanoBlade = new ChargableItem(category, Items.NANO_BLADE,
                RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{null, Items.MAG_THOR, null, null, Items.MAG_THOR,
                null, null, SlimefunItems.ADVANCED_CIRCUIT_BOARD, null});
        nanoBlade.addItemHandler((ItemUseHandler) event -> {
            if (SlimefunManager.isItemSimilar(event.getItem(), Items.NANO_BLADE, false)) {
                if (event.getItem().getEnchantments().containsKey(Enchantment.ARROW_INFINITE)) {
                    event.getItem().removeEnchantment(Enchantment.ARROW_INFINITE);
                } else {
                    event.getItem().addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 10);
                }
            }
        });
        nanoBlade.register(this);

        new ChargableItem(category, Items.FOOD_SYNTHESIZER,
                RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{SlimefunItems.PLASTIC_SHEET, new ItemStack(Material.COOKED_BEEF),
                        SlimefunItems.PLASTIC_SHEET, new ItemStack(Material.APPLE), SlimefunItems.COOLER,
                        new ItemStack(Material.APPLE), SlimefunItems.PLASTIC_SHEET, new ItemStack(Material.COOKED_BEEF),
                        SlimefunItems.PLASTIC_SHEET}).register(this);

        new EnergyTransmitter(category, Items.ENERGY_TRANSMITTER, "ENERGY_TRANSMITTER",
                RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{null, SlimefunItems.GOLD_24K, null, SlimefunItems.GOLD_24K, Items.LINKER,
                        SlimefunItems.GOLD_24K, null, SlimefunItems.GOLD_24K, null});

        new EnergyReceiver(category, Items.ENERGY_RECEIVER,
                RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{null, SlimefunItems.BLISTERING_INGOT_3, null, SlimefunItems.BLISTERING_INGOT_3,
                        Items.ENERGY_TRANSMITTER, SlimefunItems.BLISTERING_INGOT_3, null, SlimefunItems.BLISTERING_INGOT_3,
                        null});
    }

    private void setupResearches() {
        Slimefun.registerResearch(new Research(new NamespacedKey(this, "going_green"), 500, "Going Green", 25),
                Items.RECYCLER);
        Slimefun.registerResearch(new Research(new NamespacedKey(this, "wireless_charging"), 501, "Wireless Charging"
                , 40), Items.WIRELESS_CHARGER);
        Slimefun.registerResearch(new Research(new NamespacedKey(this, "weather_manipulation"), 502, "Weather " +
                        "Manipulation", 50), Items.RAIN_MAKER,
                Items.DISSIPATION_CHARGE, Items.IODINE_CHARGE, Items.EMPTY_CAPSULE);
        Slimefun.registerResearch(new Research(new NamespacedKey(this, "redstone_revolution"), 503, "Redstone " +
                        "Revolution", 35), Items.LINKER,
                Items.REDSTONE_TRANSMITTER, Items.REDSTONE_RECEIVER, Items.REDSTONE_CLOCK);
        Slimefun.registerResearch(new Research(new NamespacedKey(this, "matter_from_energy"), 504, "Matter from " +
                        "Energy", 65), Items.UU_MATTER, Items.UU_FABRICATOR,
                Items.UU_TRANSMUTATOR);
        Slimefun.registerResearch(new Research(new NamespacedKey(this, "bedrock_mining"), 505, "Bedrock Mining", 75),
                Items.BEDROCK_BREAKER, Items.BEDROCK_DRILL
                , Items.BEDROCK_DUST);
        Slimefun.registerResearch(new Research(new NamespacedKey(this, "deep_depth_mining"), 506, "Deep Depth Mining"
                        , 55), Items.DEEP_DEPTH_MINER,
                Items.LASER_CHARGE, Items.THORIUM);
        Slimefun.registerResearch(new Research(new NamespacedKey(this, "superalloys"), 507, "Superalloys", 35),
                Items.MAG_THOR);
        Slimefun.registerResearch(new Research(new NamespacedKey(this, "chunk_loading"), 508, "Chunk Loading", 85),
                Items.CHUNK_LOADER, Items.CHUNK_LOADER);
        Slimefun.registerResearch(new Research(new NamespacedKey(this, "astronaut_food"), 509, "Astronaut Food", 45),
                Items.FOOD_SYNTHESIZER);
    }

    public Config getConfiguration() {
        return config;
    }

    public Category getCategory() { return category; }
    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Override
    public String getBugTrackerURL() {
        return "https://github.com/J3fftw1/SlimeXpansion/issues/";
    }
}
