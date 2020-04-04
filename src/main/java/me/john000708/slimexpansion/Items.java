package me.john000708.slimexpansion;

import io.github.thebusybiscuit.slimefun4.core.attributes.MachineTier;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineType;
import io.github.thebusybiscuit.slimefun4.core.attributes.Radioactivity;
import io.github.thebusybiscuit.slimefun4.utils.LoreBuilder;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;


/**
 * Created by John on 14.04.2016.
 */

public final class Items {

    //Items
    public static final SlimefunItemStack UU_MATTER = new SlimefunItemStack("UU_MATTER", Material.PINK_DYE, "&dUU " +
        "Matter", "", "&7&o\"Matter from pure energy!\"");
    public static final SlimefunItemStack BEDROCK_DUST = new SlimefunItemStack("BEDROCK_DUST", Material.GUNPOWDER,
        "&8Bedrock Dust");
    public static final SlimefunItemStack LINKER = new SlimefunItemStack("LINKER", Material.CLOCK, "&6Linker", "",
        "&fA device which binds an Energy/Redstone", "&fTransmitter and Linker together.", "");
    public static final SlimefunItemStack MAG_THOR = new SlimefunItemStack("MAG_THOR", Material.IRON_INGOT, "&b&lMag" +
        "-Thor", "", "&7&o\"An extremely durable alloy used", "&7&oonly in the most advanced machines\"");

    //Machines
    public static final SlimefunItemStack REDSTONE_RECEIVER = new SlimefunItemStack("REDSTONE_RECEIVER",
        Material.HEAVY_WEIGHTED_PRESSURE_PLATE, "&6Redstone Receiver", "", "&fEmits the redstone signal it's",
        "&frespective bound transmitter has");
    public static final SlimefunItemStack REDSTONE_TRANSMITTER = new SlimefunItemStack("REDSTONE_TRANSMITTER",
        Material.IRON_BLOCK, "&cRedstone Transmitter", "", "&fTransmits it's redstone signal to any bound receivers");
    public static final SlimefunItemStack REDSTONE_CLOCK = new SlimefunItemStack("REDSTONE_CLOCK",
        Material.LIGHT_WEIGHTED_PRESSURE_PLATE, "&cRedstone Clock", "", "&fTransmits a strong redstone signal every " +
        "seconds", "&fit is configured to do");
    public static final SlimefunItemStack UU_FABRICATOR = new SlimefunItemStack("UU_FABRICATOR",
        Material.PURPLE_STAINED_GLASS, "&5Matter Fabricator", "", "&fCreates UU Matter from Pure Energy", "",
        LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &e\u26A1 &74098 J Buffer", "&8" +
        "\u21E8 &e\u26A1 &71024 J/s");
    public static final SlimefunItemStack UU_TRANSMUTATOR = new SlimefunItemStack("UU_TRANSMUTATOR",
        Material.PINK_STAINED_GLASS, "&cUU Transmutator", "", "&fUses UU Matter to form Items", "",
        LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &e\u26A1 &74098 J Buffer", "&8" +
        "\u21E8 &e\u26A1 &71024 J/s");
    public static final SlimefunItemStack RECYCLER = new SlimefunItemStack("RECYCLER", Material.IRON_BLOCK,
        "&6Recycler", "", "&fRecycles materials into &6Scrap Boxes", "", LoreBuilder.machine(MachineTier.AVERAGE,
        MachineType.MACHINE), "&8\u21E8 &e\u26A1 &7512 J Buffer", "&8\u21E8 &e\u26A1 &732 J/s");
    public static final SlimefunItemStack WIRELESS_CHARGER = new SlimefunItemStack("WIRELESS_CHARGER",
        Material.CYAN_TERRACOTTA, "&bWireless Charger", "", "&fWirelessly Charges your items", "",
        LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE), "&8\u21E8 &e\u26A1 &7100 J/s", "&8\u21E8 " +
        "&7Power Loss: &c98%");
    public static final SlimefunItemStack RAIN_MAKER = new SlimefunItemStack("RAIN_MAKER", Material.BLUE_TERRACOTTA,
        "&3Rain Maker", "", "&fA machine which controls the weather", "", LoreBuilder.machine(MachineTier.END_GAME,
        MachineType.MACHINE), "&8\u21E8 &e\u26A1 &7512 J/s");
    public static final SlimefunItemStack BEDROCK_BREAKER = new SlimefunItemStack("BEDROCK_BREAKER",
        Material.IRON_BLOCK, "&4Bedrock Breaker", "", "&fA machine which is so powerful that it can", "&fbreak " +
        "&8Bedrock &fand into Bedrock Dust", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8" +
        "\u21E8 &e\u26A1 &74096 J/s", "&8\u21E8 &7Requires a &cBedrock Drill &7to operate");
    public static final SlimefunItemStack DEEP_DEPTH_MINER = new SlimefunItemStack("DEEP_DEPTH_MINER",
        Material.DIAMOND_BLOCK, "&4Deep Depth Miner", "", "&fThis miner extracts ores straight", "&fout of &8Bedrock." +
        " &fIt can also mine a rare radioactive", "&felement called &2Thorium", "",
        LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &e\u26A1 &71024 J/s", "&8\u21E8 " +
        "&7Requires a &cLaser Charge &fto operate");
    public static final SlimefunItemStack CHUNK_LOADER = new SlimefunItemStack("CHUNK_LOADER",
        Material.ENCHANTING_TABLE, "&5Chunk Loader", "", "&fA special machine which can harness", "&fthe power of " +
        "&2Thorium &fto keep a chunk loaded.");
    public static final SlimefunItemStack ENERGY_TRANSMITTER = new SlimefunItemStack("ENERGY_TRANSMITTER",
        Material.RED_TERRACOTTA, "&cEnergy Transmitter", "", "&fThis transmitter can send energy", "&fover to an " +
        "&aEnergy Receiver", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7Transfer" +
        " Rate: 200 J/s", "&8\u21E8 &c75% Efficiency");
    public static final SlimefunItemStack ENERGY_RECEIVER = new SlimefunItemStack("ENERGY_RECEIVER",
        Material.LIME_TERRACOTTA, "&aEnergy Receiver", "", "&fThis receiver accepts power from", "&fan &cEnergy " +
        "transmitter", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE));

    //Weapons
    public static final SlimefunItemStack NANO_BLADE = new SlimefunItemStack("NANO_BLADE", Material.DIAMOND_SWORD,
        "&2Nano Blade", "", "&fAn advanced piece of technology which can", "&fcut through organic tissue with ease.",
        "", "&fActivate: &aShift-Right Click", "", "&8\u21E8 &7Consumes &e2.5J &7per hit", "", "&c&o&8\u21E8 &e\u26A1" +
        " &70 / 500 J");

    //Armor
    public static final SlimefunItemStack ELECTRIC_CHESTPLATE = new SlimefunItemStack("ELECTRIC_CHESTPLATE",
        Material.LEATHER_CHESTPLATE, Color.TEAL, "&9Electric Chestplate", "",
        "&8\u21E8 &7Negates all the damage dealt to player.", "", "&c&o&8\u21E8 &e\u26A1 &70 / 250 J");

    //Custom Items
    public static final SlimefunItemStack SCRAP_BOX = new SlimefunItemStack("SCRAP_BOX",
        "f34da0cf7eb776ddf29b3e3d7fbd1e32738885229b1a34766e66823c34705552", "&6Scrap Box");
    public static final SlimefunItemStack EMPTY_CAPSULE = new SlimefunItemStack("EMPTY_CAPSULE",
        "8af2fc9b53a31eb1d574e1759438ad972f1533fb457f1de32bbaea1e7c70", "&3Empty Capsule");
    public static final SlimefunItemStack IODINE_CHARGE = new SlimefunItemStack("IODINE_CHARGE",
        "ce395bdaf632dcae3951dac1ed473c2541ca27fb1c36183a8dabf7fca58e9e0", "&4Iodine Charge");
    public static final SlimefunItemStack DISSIPATION_CHARGE = new SlimefunItemStack("DISSIPATION_CHARGE",
        "8a50dfd09aef49406f3d953e418dc94841f9d0465aec8d7c858069fc31b4385", "&6Dissipation Charge");
    public static final SlimefunItemStack BEDROCK_DRILL = new SlimefunItemStack("BEDROCK_DRILL",
        "4921f88b35bf1e531ea4d53be83a1ba5dca883743d16d9b1efc69446b62894d4", "&cDrill Head", "&fA special drill which " +
        "is used in a &4Bedrock Breaker", "&fto break bedrock into dust.", "", "&7Durability: 1024/1024");
    public static final SlimefunItemStack LASER_CHARGE = new SlimefunItemStack("LASER_CHARGE",
        "279f57c68b7a90e0d8850e794ade5b8e810d338e2459eeef9babd6832ca169a", "&cLaser Charge", "&fThis item is " +
        "necessary for a", "&4Deep Depth Miner &fto mine Ores.", "", "&7Durability: 1024/1024");
    public static final SlimefunItemStack THORIUM = new SlimefunItemStack("THORIUM",
        "427d1a6184c62d4c4a67f862b8e19ec001abe4c7d889f23349e8dafe6d033", "&8Thorium", "",
        LoreBuilder.radioactive(Radioactivity.HIGH), LoreBuilder.HAZMAT_SUIT_REQUIRED);
    public static final SlimefunItemStack FOOD_SYNTHESIZER = new SlimefunItemStack("FOOD_SYNTHESIZER",
        "a11a2df7d37af40ed5ce442fd2d78cd8ebcdcdc029d2ae691a2b64395cdf", "&dFood Synthesizer", "", "&fKeeps you fed " +
        "with artificial food.", "&fComes in two flavors!", "", "&c&o&8\u21E8 &e\u26A1 &70 / 100 J");

    static {
        ItemMeta nanoBladeMeta = NANO_BLADE.getItemMeta();
        nanoBladeMeta.setUnbreakable(true);
        NANO_BLADE.setItemMeta(nanoBladeMeta);
    }

    private Items() {}
}


