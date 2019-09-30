package me.john000708.slimexpansion;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.thebusybiscuit.cscorelib2.updater.BukkitUpdater;
import io.github.thebusybiscuit.cscorelib2.updater.GitHubBuildsUpdater;
import io.github.thebusybiscuit.cscorelib2.updater.Updater;
import me.john000708.slimexpansion.items.Linker;
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
import me.mrCookieSlime.CSCoreLibPlugin.events.ItemUseEvent;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.GEO.OreGenResource;
import me.mrCookieSlime.Slimefun.GEO.OreGenSystem;
import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Research;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.Alloy;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.ChargableItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.DamagableChargableItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemInteractionHandler;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.bstats.bukkit.Metrics;
import me.mrCookieSlime.Slimefun.cscorelib2.config.Config;

/**
 * Created by John on 14.04.2016.
 */
public class SlimeXpansion extends JavaPlugin {

    public static SlimeXpansion plugin;
    private final Random random = new Random();
    
    public Config config;
    private int chunkLoaderDuration;

    public void onEnable() {
        plugin = this;
        config = new Config(this);
        
        // Setting up bStats
        new Metrics(this);

		// Setting up the Auto-Updater
		Updater updater;

		if (!getDescription().getVersion().startsWith("DEV - ")) {
			// We are using an official build, use the BukkitDev Updater
			updater = new BukkitUpdater(this, getFile(), 102902);
		}
		else {
			// If we are using a development build, we want to switch to our custom 
			updater = new GitHubBuildsUpdater(this, getFile(), "John000708/SlimeXpansion/master");
		}

		if (config.getBoolean("options.auto-update")) updater.start();
        
        new ListenerSetup(this);
        
        chunkLoaderDuration = config.getInt("options.chunkloader-duration");
        
        registerItems();
        setupResearches();
        getLogger().info("SlimeXpansion has been enabled!");
    }

    public void onDisable() {
        for (World world : Bukkit.getWorlds()) {
            for (Chunk loadedChunk : world.getLoadedChunks()) {
                if (BlockStorage.hasChunkInfo(loadedChunk) && BlockStorage.getChunkInfo(loadedChunk, "loaded") != null && BlockStorage.getChunkInfo(loadedChunk, "loaded").equals("true")) {
                    BlockStorage.setChunkInfo(loadedChunk, "loaded", "false");
                }
            }
        }
        
        plugin = null;
    }

    public int getChunkLoaderDuration() {
        return this.chunkLoaderDuration;
    }

    private void registerItems() {
        new ScrapBox(Categories.MISC, Items.SCRAP_BOX, "SCRAP_BOX", CustomRecipeType.RECYCLER).register();

        new SlimefunItem(Categories.MISC, Items.UU_MATTER, "UU_MATTER", CustomRecipeType.UU_FABRICATOR, new ItemStack[]{null, null, null, null, null, null, null, null, null}).register();
        new SlimefunItem(Categories.MISC, Items.EMPTY_CAPSULE, "EMPTY_CHARGE", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{null, SlimefunItems.TIN_INGOT, null, SlimefunItems.TIN_INGOT, SlimefunItems.CAN, SlimefunItems.TIN_INGOT, null, SlimefunItems.TIN_INGOT, null}).register();
        new SlimefunItem(Categories.MISC, Items.IODINE_CHARGE, "IODINE_CHARGE", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{Items.EMPTY_CAPSULE, SlimefunItems.SALT, SlimefunItems.SALT, new ItemStack(Material.GUNPOWDER), null, null, null, null, null, null}).register();
        new SlimefunItem(Categories.MISC, Items.DISSIPATION_CHARGE, "DISSIPATION_CHARGE", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{Items.EMPTY_CAPSULE, new ItemStack(Material.SAND), Items.IODINE_CHARGE, null, null, null, null, null, null}).register();
        new SlimefunItem(Categories.MISC, Items.BEDROCK_DUST, "BEDROCK_DUST", CustomRecipeType.BEDROCK_BREAKER, new ItemStack[]{null}).register();

        //Machines
        new Recycler(CustomCategories.SLIMEFUN_XPANSION, Items.RECYCLER, "RECYCLER", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{SlimefunItems.ALUMINUM_INGOT, SlimefunItems.POWER_CRYSTAL, SlimefunItems.ALUMINUM_INGOT, SlimefunItems.CAN, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.CAN, SlimefunItems.LEAD_INGOT, SlimefunItems.MEDIUM_CAPACITOR, SlimefunItems.LEAD_INGOT}) {
            
            @Override
            public int getEnergyConsumption() {
                return 32;
            }

            @Override
            public int getSpeed() {
                return 1;
            }
            
        }.registerChargeableBlock(512);

        new UUFabricator(CustomCategories.SLIMEFUN_XPANSION, Items.UU_FABRICATOR, "UU_FABRICATOR", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{SlimefunItems.REINFORCED_PLATE, SlimefunItems.POWER_CRYSTAL, SlimefunItems.REINFORCED_PLATE, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.CARBONADO_EDGED_CAPACITOR, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.PLUTONIUM, Items.SCRAP_BOX, SlimefunItems.PLUTONIUM}) {
        	
            @Override
            public int getEnergyConsumption() {
                return 512;
            }

            @Override
            public int getSpeed() {
                return 1;
            }

        }.registerChargeableBlock(4098);

        new UUTransmutator(CustomCategories.SLIMEFUN_XPANSION, Items.UU_TRANSMUTATOR, "UU_TRANSMUTATOR", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{SlimefunItems.REINFORCED_PLATE, Items.UU_MATTER, SlimefunItems.REINFORCED_PLATE, SlimefunItems.POWER_CRYSTAL, Items.UU_FABRICATOR, SlimefunItems.POWER_CRYSTAL, SlimefunItems.PLUTONIUM, SlimefunItems.CARBONADO_EDGED_CAPACITOR, SlimefunItems.PLUTONIUM}) {
            
        	@Override
            public String getInventoryTitle() {
                return "&5UU Transmutator";
            }

            @Override
            public int getEnergyConsumption() {
                return 512;
            }
        }.registerChargeableBlock(4098);

        new BedrockBreaker(CustomCategories.SLIMEFUN_XPANSION, Items.BEDROCK_BREAKER, "BEDROCK_BREAKER", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{SlimefunItems.REINFORCED_PLATE, SlimefunItems.PLUTONIUM, SlimefunItems.REINFORCED_PLATE, SlimefunItems.SILVER_INGOT, SlimefunItems.POWER_CRYSTAL, SlimefunItems.SILVER_INGOT, SlimefunItems.BLISTERING_INGOT_3, Items.BEDROCK_DRILL, SlimefunItems.BLISTERING_INGOT_3}) {
            @Override
            public String getInventoryTitle() {
                return "§5Bedrock Breaker";
            }

            @Override
            public int getEnergyConsumption() {
                return 4098;
            }
        }.registerChargeableBlock(8192);

        new DeepDepthMiner(CustomCategories.SLIMEFUN_XPANSION, Items.DEEP_DEPTH_MINER, "DEEP_MINER", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{SlimefunItems.REINFORCED_PLATE, Items.BEDROCK_DUST, SlimefunItems.REINFORCED_PLATE, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.CARBONADO_EDGED_CAPACITOR, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.REINFORCED_ALLOY_INGOT, new ItemStack(Material.BEACON), SlimefunItems.REINFORCED_ALLOY_INGOT}) {

        }.registerChargeableBlock(4098);

        new WirelessCharger(CustomCategories.SLIMEFUN_XPANSION, Items.WIRELESS_CHARGER, "WIRELESS_CHARGER", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{null, SlimefunItems.POWER_CRYSTAL, null, SlimefunItems.GILDED_IRON, SlimefunItems.LARGE_CAPACITOR, SlimefunItems.GILDED_IRON, SlimefunItems.HEATING_COIL, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.HEATING_COIL}) {

        }.registerChargeableBlock(512);

        new RainMaker(CustomCategories.SLIMEFUN_XPANSION, Items.RAIN_MAKER, "RAIN_MAKER", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{Items.IODINE_CHARGE, SlimefunItems.BLISTERING_INGOT_3, Items.DISSIPATION_CHARGE, SlimefunItems.SILVER_INGOT, SlimefunItems.MEDIUM_CAPACITOR, SlimefunItems.SILVER_INGOT, SlimefunItems.LEAD_INGOT, SlimefunItems.POWER_CRYSTAL, SlimefunItems.LEAD_INGOT}) {
            @Override
            public ItemStack getProgressBar() {
                return new ItemStack(Material.CAULDRON);
            }

            @Override
            public String getInventoryTitle() {
                return "§3Rain Maker";
            }

            @Override
            public int getEnergyConsumption() {
                return 256;
            }

            @Override
            public int getSpeed() {
                return 1;
            }
        }.registerChargeableBlock(512);

        new ChunkLoader(CustomCategories.SLIMEFUN_XPANSION, Items.CHUNK_LOADER, "CHUNK_LOADER", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{SlimefunItems.GOLD_24K, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.GOLD_24K, Items.MAG_THOR, Items.THORIUM, Items.MAG_THOR, SlimefunItems.REINFORCED_PLATE, SlimefunItems.POWER_CRYSTAL, SlimefunItems.REINFORCED_PLATE}).register();

        new RedstoneTransmitter(CustomCategories.SLIMEFUN_XPANSION, Items.REDSTONE_TRANSMITTER, "REDSTONE_TRANSMITTER", RecipeType.ENHANCED_CRAFTING_TABLE, 
        new ItemStack[] {SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.ADVANCED_CIRCUIT_BOARD, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT, new ItemStack(Material.REDSTONE_BLOCK), SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.CORINTHIAN_BRONZE_INGOT, new ItemStack(Material.GLASS), SlimefunItems.CORINTHIAN_BRONZE_INGOT}).register();

        new RedstoneReceiver(CustomCategories.SLIMEFUN_XPANSION, Items.REDSTONE_RECEIVER, "REDSTONE_RECEIVER", RecipeType.ENHANCED_CRAFTING_TABLE, 
        new ItemStack[] {SlimefunItems.DAMASCUS_STEEL_INGOT, new ItemStack(Material.GLASS), SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.DAMASCUS_STEEL_INGOT, new ItemStack(Material.REDSTONE_BLOCK), SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.CORINTHIAN_BRONZE_INGOT, SlimefunItems.ADVANCED_CIRCUIT_BOARD, SlimefunItems.CORINTHIAN_BRONZE_INGOT}).register();

        new RedstoneClock(CustomCategories.SLIMEFUN_XPANSION, Items.REDSTONE_CLOCK, "REDSTONE_CLOCK", RecipeType.ENHANCED_CRAFTING_TABLE, 
        new ItemStack[] {SlimefunItems.LEAD_INGOT, new ItemStack(Material.REDSTONE_BLOCK), SlimefunItems.LEAD_INGOT, new ItemStack(Material.REDSTONE_BLOCK), new ItemStack(Material.CLOCK), new ItemStack(Material.REDSTONE_BLOCK), SlimefunItems.LEAD_INGOT, new ItemStack(Material.REDSTONE_BLOCK), SlimefunItems.LEAD_INGOT}).register();

        new Linker(Categories.TECH, Items.LINKER, "LINKER", RecipeType.ENHANCED_CRAFTING_TABLE, 
        new ItemStack[] {null, SlimefunItems.GOLD_24K, null, SlimefunItems.GOLD_24K, SlimefunItems.ENERGY_REGULATOR, SlimefunItems.GOLD_24K, null, SlimefunItems.GOLD_24K, null}).register();

        new SlimefunItem(Categories.MISC, Items.BEDROCK_DRILL, "BEDROCK_DRILL", RecipeType.ENHANCED_CRAFTING_TABLE, 
        new ItemStack[] {null, SlimefunItems.REINFORCED_PLATE, null, SlimefunItems.REINFORCED_PLATE, Items.UU_MATTER, SlimefunItems.REINFORCED_PLATE, null, SlimefunItems.REINFORCED_PLATE, null}).register();

        new SlimefunItem(Categories.MISC, Items.LASER_CHARGE, "LASER_CHARGE", RecipeType.ENHANCED_CRAFTING_TABLE, 
        new ItemStack[] {null, SlimefunItems.REINFORCED_ALLOY_INGOT, null, SlimefunItems.REINFORCED_ALLOY_INGOT, new ItemStack(Material.REDSTONE), SlimefunItems.REINFORCED_ALLOY_INGOT, null, SlimefunItems.REINFORCED_ALLOY_INGOT, null}).register();

        new SlimefunItem(Categories.RESOURCES, Items.THORIUM, "THORIUM", CustomRecipeType.DEEP_MINER, 
        new ItemStack[] {null, null, null, null, new CustomItem(Material.PAPER, "&fHint!", "&a&oMake sure to first GEO-Scan the chunk in which you are", "&a&omining to discover Thorium!"), null, null, null, null}).register();

        new Alloy(Categories.RESOURCES, Items.MAG_THOR, "MAG_THOR", new ItemStack[]{SlimefunItems.REINFORCED_ALLOY_INGOT, Items.THORIUM, SlimefunItems.MAGNESIUM_INGOT, SlimefunItems.ZINC_INGOT, null, null, null, null, null}).register();

        new SlimefunItem(CustomCategories.SLIMEFUN_XPANSION, new CustomItem(new ItemStack(Material.BEDROCK), "&8Bedrock"), "BEDROCK", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{null, Items.BEDROCK_DUST, null, Items.BEDROCK_DUST, Items.THORIUM, Items.BEDROCK_DUST, null, Items.BEDROCK_DUST, null}).register();

        new DamagableChargableItem(Categories.TECH, Items.ELECTRIC_CHESTPLATE, "ELECTRIC_CHESTPLATE", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{Items.MAG_THOR, null, Items.MAG_THOR, Items.MAG_THOR, SlimefunItems.POWER_CRYSTAL, Items.MAG_THOR, Items.MAG_THOR, Items.MAG_THOR, Items.MAG_THOR}, "Armor").register();

        new DamagableChargableItem(Categories.TECH, Items.NANO_BLADE, "NANO_BLADE", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{null, Items.MAG_THOR, null, null, Items.MAG_THOR, null, null, SlimefunItems.ADVANCED_CIRCUIT_BOARD, null}, "Weapon").register(new ItemInteractionHandler() {
            @Override
            public boolean onRightClick(ItemUseEvent itemUseEvent, Player player, ItemStack itemStack) {
                if (SlimefunManager.isItemSimiliar(itemStack, Items.NANO_BLADE, false)) {
                    if (itemStack.getEnchantments().containsKey(Enchantment.ARROW_INFINITE) && itemStack.getEnchantments().get(Enchantment.ARROW_INFINITE) == 10) {
                        itemStack.removeEnchantment(Enchantment.ARROW_INFINITE);
                    } else {
                        itemStack.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 10);
                    }
                }
                return false;
            }
        });

        new ChargableItem(Categories.TECH, Items.FOOD_SYNTHESIZER, "FOOD_SYNTHESIZER", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{SlimefunItems.PLASTIC_SHEET, new ItemStack(Material.COOKED_BEEF), SlimefunItems.PLASTIC_SHEET, new ItemStack(Material.APPLE), SlimefunItems.COOLER, new ItemStack(Material.APPLE), SlimefunItems.PLASTIC_SHEET, new ItemStack(Material.COOKED_BEEF), SlimefunItems.PLASTIC_SHEET}, "Utility").register();
        new EnergyTransmitter(CustomCategories.SLIMEFUN_XPANSION, Items.ENERGY_TRANSMITTER, "ENERGY_TRANSMITTER", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{null, SlimefunItems.GOLD_24K, null, SlimefunItems.GOLD_24K, Items.LINKER, SlimefunItems.GOLD_24K, null, SlimefunItems.GOLD_24K, null}).registerChargeableBlock(false, 12000);

        new EnergyReceiver(CustomCategories.SLIMEFUN_XPANSION, Items.ENERGY_RECEIVER, "ENERGY_RECEIVER", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{null, SlimefunItems.BLISTERING_INGOT_3, null, SlimefunItems.BLISTERING_INGOT_3, Items.ENERGY_TRANSMITTER, SlimefunItems.BLISTERING_INGOT_3, null, SlimefunItems.BLISTERING_INGOT_3, null}).registerChargeableBlock(false, 12000);

        OreGenSystem.registerResource(new OreGenResource() {
        	
            @Override
            public int getDefaultSupply(Biome biome) {
                switch (biome) {
                    case MOUNTAINS:
                        return random.nextInt(8) + 5;
                    case GRAVELLY_MOUNTAINS:
                        return random.nextInt(12) + 10;
                    case WOODED_MOUNTAINS:
                        return random.nextInt(4) + 3;
                    case SNOWY_MOUNTAINS:
                        return random.nextInt(10) + 14;
                    case MODIFIED_GRAVELLY_MOUNTAINS:
                        return random.nextInt(15) + 16;
                    default:
                        return 4;
                }
            }

            @Override
            public String getName() {
                return "Thorium";
            }

            @Override
            public ItemStack getIcon() {
                return Items.THORIUM;
            }

            @Override
            public ItemStack getItem() {
                return Items.THORIUM;
            }

            @Override
            public String getMeasurementUnit() {
                return "Units";
            }

            @Override
            public boolean isLiquid() {
                return config.getBoolean("options.thorium-via-geo-miner");
            }
        });
    }

    private void setupResearches() {
        Slimefun.registerResearch(new Research(500, "Going Green", 25), Items.RECYCLER);
        Slimefun.registerResearch(new Research(501, "Wireless Charging", 40), Items.WIRELESS_CHARGER);
        Slimefun.registerResearch(new Research(502, "Weather Manipulation", 50), Items.RAIN_MAKER, Items.DISSIPATION_CHARGE, Items.IODINE_CHARGE, Items.EMPTY_CAPSULE);
        Slimefun.registerResearch(new Research(503, "Redstone Revolution", 35), Items.LINKER, Items.REDSTONE_TRANSMITTER, Items.REDSTONE_RECEIVER, Items.REDSTONE_CLOCK);
        Slimefun.registerResearch(new Research(504, "Matter from Energy", 65), Items.UU_MATTER, Items.UU_FABRICATOR, Items.UU_TRANSMUTATOR);
        Slimefun.registerResearch(new Research(505, "Bedrock Mining", 75), Items.BEDROCK_BREAKER, Items.BEDROCK_DRILL, Items.BEDROCK_DUST);
        Slimefun.registerResearch(new Research(506, "Deep Depth Mining", 55), Items.DEEP_DEPTH_MINER, Items.LASER_CHARGE, Items.THORIUM);
        Slimefun.registerResearch(new Research(507, "Superalloys", 35), Items.MAG_THOR);
        Slimefun.registerResearch(new Research(508, "Chunk Loading", 85), Items.CHUNK_LOADER, Items.CHUNK_LOADER);
        Slimefun.registerResearch(new Research(509, "Astronaut Food?", 45), Items.FOOD_SYNTHESIZER);
    }
}
