package me.john000708.slimexpansion;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import me.john000708.slimexpansion.items.Linker;
import me.john000708.slimexpansion.items.NanoBlade;
import me.john000708.slimexpansion.items.ScrapBox;
import me.john000708.slimexpansion.listeners.ListenerSetup;
import me.john000708.slimexpansion.machines.BedrockBreaker;
import me.john000708.slimexpansion.machines.ChunkLoader;
import me.john000708.slimexpansion.machines.DeepDepthMiner;
import me.john000708.slimexpansion.machines.EnergyReceiver;
import me.john000708.slimexpansion.machines.EnergyTransmitter;
import me.john000708.slimexpansion.machines.RainMaker;
import me.john000708.slimexpansion.machines.Recycler;
import me.john000708.slimexpansion.machines.RedstoneClock;
import me.john000708.slimexpansion.machines.RedstoneReceiver;
import me.john000708.slimexpansion.machines.RedstoneTransmitter;
import me.john000708.slimexpansion.machines.UUFabricator;
import me.john000708.slimexpansion.machines.UUTransmutator;
import me.john000708.slimexpansion.machines.WirelessCharger;
import me.john000708.slimexpansion.resources.ThoriumResource;
import me.john000708.slimexpansion.utils.SchedulerHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.Research;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.ChargableItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.bstats.bukkit.Metrics;
import me.mrCookieSlime.Slimefun.cscorelib2.config.Config;
import me.mrCookieSlime.Slimefun.cscorelib2.updater.GitHubBuildsUpdater;
import me.mrCookieSlime.Slimefun.cscorelib2.updater.Updater;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

/**
 * Created by John on 14.04.2016.
 */
public class SlimeXpansion extends JavaPlugin implements SlimefunAddon {
    private Config config;
    private int chunkLoaderDuration;

    private Category category;

    private SchedulerHandler schedulerHandler;

    private static Logger logger;

    @Override
    public void onEnable() {
        category = new Category(new NamespacedKey(this, "slimexpansion"),
                new CustomItem(Material.BEACON, "&5SlimeXpansion"));

        setLogger(getLogger());

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

    private static void setLogger(Logger logger) {
        SlimeXpansion.logger = logger;
    }

    public static Logger getXpansionLogger() {
        return logger;
    }

    private void setupUpdater() {
        // Setting up the Auto-Updater
        Updater updater = null;

        if (getDescription().getVersion().startsWith("DEV - ")) {
            // If we are using a development build, we want to switch to our custom
            updater = new GitHubBuildsUpdater(this, getFile(), "J3fftw1/SlimeXpansion/master");
        }

        if (updater != null && config.getBoolean("options.auto-update")) updater.start();
    }

    public int getChunkLoaderDuration() {
        return this.chunkLoaderDuration;
    }

    private void registerItems() {
        // Resources
        new ThoriumResource(config.getBoolean("options.thorium-via-geo-miner")).register();

        // Other crap
        new ScrapBox(category, config).register(this);

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
        new Recycler(category).register(this);

        new UUFabricator(category).register(this);

        new UUTransmutator(category).register(this);

        new BedrockBreaker(category, schedulerHandler).register(this);

        new DeepDepthMiner(category, schedulerHandler).register(this);

        new WirelessCharger(category).register(this);

        new RainMaker(category).register(this);

        new ChunkLoader(category, getChunkLoaderDuration()).register(this);

        new RedstoneTransmitter(category, schedulerHandler).register(this);

        new RedstoneReceiver(category).register(this);

        new RedstoneClock(category, schedulerHandler).register(this);

        new Linker(category).register(this);

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

        new SlimefunItem(category, Items.MAG_THOR, RecipeType.SMELTERY,
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

        new NanoBlade(category).register(this);

        new ChargableItem(category, Items.FOOD_SYNTHESIZER,
                RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{SlimefunItems.PLASTIC_SHEET, new ItemStack(Material.COOKED_BEEF),
                        SlimefunItems.PLASTIC_SHEET, new ItemStack(Material.APPLE), SlimefunItems.COOLER,
                        new ItemStack(Material.APPLE), SlimefunItems.PLASTIC_SHEET, new ItemStack(Material.COOKED_BEEF),
                        SlimefunItems.PLASTIC_SHEET}).register(this);

        new EnergyTransmitter(category).register(this);

        new EnergyReceiver(category).register(this);
    }

    private void setupResearches() {
        Slimefun.registerResearch(new Research(new NamespacedKey(this, "going_green"), 500, "Going Green", 25),
                Items.RECYCLER);
        Slimefun.registerResearch(new Research(new NamespacedKey(this, "wireless_charging"), 501, "Wireless Charging", 40), Items.WIRELESS_CHARGER);
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
                Items.BEDROCK_BREAKER, Items.BEDROCK_DRILL, Items.BEDROCK_DUST);
        Slimefun.registerResearch(new Research(new NamespacedKey(this, "deep_depth_mining"), 506, "Deep Depth Mining", 55), Items.DEEP_DEPTH_MINER,
                Items.LASER_CHARGE, Items.THORIUM);
        Slimefun.registerResearch(new Research(new NamespacedKey(this, "superalloys"), 507, "Superalloys", 35),
                Items.MAG_THOR);
        Slimefun.registerResearch(new Research(new NamespacedKey(this, "chunk_loading"), 508, "Chunk Loading", 85),
                Items.CHUNK_LOADER, Items.CHUNK_LOADER);
        Slimefun.registerResearch(new Research(new NamespacedKey(this, "astronaut_food"), 509, "Astronaut Food", 45),
                Items.FOOD_SYNTHESIZER);
    }

    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Override
    public String getBugTrackerURL() {
        return "https://github.com/J3fftw1/SlimeXpansion/issues/";
    }
}
