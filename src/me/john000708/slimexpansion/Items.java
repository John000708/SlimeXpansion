package me.john000708.slimexpansion;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomArmor;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.utils.MachineTier;
import me.mrCookieSlime.Slimefun.utils.MachineType;


/**
 * Created by John on 14.04.2016.
 */

public class Items {
    //Items
    public static ItemStack UU_MATTER;
    public static ItemStack BEDROCK_DUST = new CustomItem(new ItemStack(Material.GUNPOWDER, 1), "&8Bedrock Dust");
    public static ItemStack LINKER = new CustomItem(new ItemStack(Material.CLOCK), "&6Linker", "", "&fA device which binds an Energy/Redstone", "&fTransmitter and Linker together.", "");
    public static ItemStack MAG_THOR = new CustomItem(new ItemStack(Material.IRON_INGOT), "&b&lMag-Thor", "", "&7&o\"An extremely durable alloy used", "&7&oonly in the most advanced machines\"");

    //Machines
    public static ItemStack REDSTONE_RECEIVER = new CustomItem(new ItemStack(Material.HEAVY_WEIGHTED_PRESSURE_PLATE), "&6Redstone Receiver", "", "&fEmits the redstone signal it's", "&frespective bound transmitter has");
    public static ItemStack REDSTONE_TRANSMITTER = new CustomItem(new ItemStack(Material.IRON_BLOCK), "&cRedstone Transmitter", "", "&fTransmits it's redstone signal to any bound receivers");
    public static ItemStack REDSTONE_CLOCK = new CustomItem(new ItemStack(Material.LIGHT_WEIGHTED_PRESSURE_PLATE), "&cRedstone Clock", "", "&fTransmits a strong redstone signal every seconds", "&fit is configured to do");
    public static ItemStack UU_FABRICATOR = new CustomItem(Material.PURPLE_STAINED_GLASS, "&5Matter Fabricator", "", "&fCreates UU Matter from Pure Energy", "", MachineTier.END_GAME.and(MachineType.MACHINE), "&8\u21E8 &e\u26A1 &74098 J Buffer", "&8\u21E8 &e\u26A1 &71024 J/s");
    public static ItemStack UU_TRANSMUTATOR = new CustomItem(Material.PINK_STAINED_GLASS, "&cUU Transmutator", "", "&fUses UU Matter to form Items", "", MachineTier.END_GAME.and(MachineType.MACHINE), "&8\u21E8 &e\u26A1 &74098 J Buffer", "&8\u21E8 &e\u26A1 &71024 J/s");
    public static ItemStack RECYCLER = new CustomItem(Material.IRON_BLOCK, "&6Recycler", "", "&fRecycles materials into &6Scrap Boxes", "", MachineTier.AVERAGE.and(MachineType.MACHINE), "&8\u21E8 &e\u26A1 &7512 J Buffer", "&8\u21E8 &e\u26A1 &732 J/s");
    public static ItemStack WIRELESS_CHARGER = new CustomItem(Material.CYAN_TERRACOTTA, "&bWireless Charger", "", "&fWirelessly Charges your items", "", MachineTier.ADVANCED.and(MachineType.MACHINE), "&8\u21E8 &e\u26A1 &7100 J/s", "&8\u21E8 &7Power Loss: &c98%");
    public static ItemStack RAIN_MAKER = new CustomItem(Material.BLUE_TERRACOTTA, "&3Rain Maker", "", "&fA machine which controls the weather", "", MachineTier.END_GAME.and(MachineType.MACHINE), "&8\u21E8 &e\u26A1 &7512 J/s");
    public static ItemStack BEDROCK_BREAKER = new CustomItem(Material.IRON_BLOCK, "&4Bedrock Breaker", "", "&fA machine which is so powerful that it can", "&fbreak &8Bedrock &fand into Bedrock Dust", "", MachineTier.END_GAME.and(MachineType.MACHINE), "&8\u21E8 &e\u26A1 &74096 J/s", "&8\u21E8 &7Requires a &cBedrock Drill &7to operate");
    public static ItemStack DEEP_DEPTH_MINER = new CustomItem(new ItemStack(Material.DIAMOND_BLOCK), "&4Deep Depth Miner", "", "&fThis miner extracts ores straight", "&fout of &8Bedrock. &fIt can also mine a rare radioactive", "&felement called &2Thorium", "", MachineTier.END_GAME.and(MachineType.MACHINE), "&8\u21E8 &e\u26A1 &71024 J/s", "&8\u21E8 &7Requires a &cLaser Charge &fto operate");
    public static ItemStack CHUNK_LOADER = new CustomItem(Material.ENCHANTING_TABLE, "&5Chunk Loader", "", "&fA special machine which can harness", "&fthe power of &2Thorium &fto keep a chunk loaded.");
    public static ItemStack ENERGY_TRANSMITTER = new CustomItem(Material.RED_TERRACOTTA, "&cEnergy Transmitter", "", "&fThis transmitter can send energy", "&fover to an &aEnergy Receiver", "", MachineTier.END_GAME.and(MachineType.MACHINE), "&8\u21E8 &7Transfer Rate: 200 J/s", "&8\u21E8 &c75% Efficiency");
    public static ItemStack ENERGY_RECEIVER = new CustomItem(Material.LIME_TERRACOTTA, "&aEnergy Receiver", "", "&fThis receiver accepts power from", "&fan &cEnergy transmitter", "", MachineTier.END_GAME.and(MachineType.MACHINE));
    public static ItemStack TERRAFORMER = new CustomItem(Material.GREEN_TERRACOTTA, "&2Terraformer", "", "&fA machine that allows you to change the biome", "&fof a 16x16 area", "", MachineTier.END_GAME.and(MachineType.MACHINE), "&8\u21E8 &7Uses Terraformer Modules");

    //Weapons
    public static ItemStack NANO_BLADE = new CustomItem(new ItemStack(Material.DIAMOND_SWORD), "&2Nano Blade", "", "&fAn advanced piece of technology which can", "&fcut through organic tissue with ease.", "", "&fActivate: &aShift-Right Click", "", "&8\u21E8 &7Consumes &e2.5J &7per hit", "", "&c&o&8\u21E8 &e\u26A1 &70 / 500 J");

    //Armor
    public static ItemStack ELECTRIC_CHESTPLATE = new CustomArmor(new CustomItem(Material.LEATHER_CHESTPLATE, "&9Electric Chestplate", "", "&8\u21E8 &7Negates all the damage dealt to player.", "", "&c&o&8\u21E8 &e\u26A1 &70 / 250 J"), Color.TEAL);

    //Custom Items
    public static ItemStack SCRAP_BOX;
    public static ItemStack EMPTY_CAPSULE;
    public static ItemStack IODINE_CHARGE;
    public static ItemStack DISSIPATION_CHARGE;
    public static ItemStack BEDROCK_DRILL;
    public static ItemStack LASER_CHARGE;
    public static ItemStack THORIUM;
    public static ItemStack FOOD_SYNTHESIZER;

    static {
        try {
            SCRAP_BOX = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjM0ZGEwY2Y3ZWI3NzZkZGYyOWIzZTNkN2ZiZDFlMzI3Mzg4ODUyMjliMWEzNDc2NmU2NjgyM2MzNDcwNTU1MiJ9fX0="), "&6Scrap Box");
            IODINE_CHARGE = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2UzOTViZGFmNjMyZGNhZTM5NTFkYWMxZWQ0NzNjMjU0MWNhMjdmYjFjMzYxODNhOGRhYmY3ZmNhNThlOWUwIn19fQ=="), "&4Iodine Charge");
            DISSIPATION_CHARGE = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGE1MGRmZDA5YWVmNDk0MDZmM2Q5NTNlNDE4ZGM5NDg0MWY5ZDA0NjVhZWM4ZDdjODU4MDY5ZmMzMWI0Mzg1In19fQ=="), "&6Dissipation Charge");
            EMPTY_CAPSULE = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGFmMmZjOWI1M2EzMWViMWQ1NzRlMTc1OTQzOGFkOTcyZjE1MzNmYjQ1N2YxZGUzMmJiYWVhMWU3YzcwIn19fQ=="), "&3Empty Capsule");
            UU_MATTER = new CustomItem(Material.PINK_DYE, "&dUU Matter", "", "&7&o\"Matter from pure energy!\"");
            BEDROCK_DRILL = new CustomItem(CustomSkull.getItem("eyJ0aW1lc3RhbXAiOjE0NjMzNDA3NTkyNDksInByb2ZpbGVJZCI6ImQ2MmI1MjJkMTVjZjQyNWE4NTFlNmNjNDRkOGJlMDg5IiwicHJvZmlsZU5hbWUiOiJKb2huMDAwNzA4IiwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzQ5MjFmODhiMzViZjFlNTMxZWE0ZDUzYmU4M2ExYmE1ZGNhODgzNzQzZDE2ZDliMWVmYzY5NDQ2YjYyODk0ZDQifX19"), "&cDrill Head", "&fA special drill which is used in a &4Bedrock Breaker", "&fto break bedrock into dust.", "", "&7Durability: 1024/1024");
            LASER_CHARGE = new CustomItem(CustomSkull.getItem("eyJ0aW1lc3RhbXAiOjE0NjM1ODgyNjYwMTgsInByb2ZpbGVJZCI6ImQ2MmI1MjJkMTVjZjQyNWE4NTFlNmNjNDRkOGJlMDg5IiwicHJvZmlsZU5hbWUiOiJKb2huMDAwNzA4IiwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzI3OWY1N2M2OGI3YTkwZTBkODg1MGU3OTRhZGU1YjhlODEwZDMzOGUyNDU5ZWVlZjliYWJkNjgzMmNhMTY5YSJ9fX0="), "&cLaser Charge", "&fThis item is necessary for a", "&4Deep Depth Miner &fto mine Ores.", "", "&7Durability: 1024/1024");
            THORIUM = new CustomItem(CustomSkull.getItem("eyJ0aW1lc3RhbXAiOjE0NjM1OTgzMTg2NDgsInByb2ZpbGVJZCI6ImQ2MmI1MjJkMTVjZjQyNWE4NTFlNmNjNDRkOGJlMDg5IiwicHJvZmlsZU5hbWUiOiJKb2huMDAwNzA4IiwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzQyN2QxYTYxODRjNjJkNGM0YTY3Zjg2MmI4ZTE5ZWMwMDFhYmU0YzdkODg5ZjIzMzQ5ZThkYWZlNmQwMzMifX19"), "&8Thorium", "", "&2Radiation Level: HIGH", "&4&oHazmat Suit required");
            FOOD_SYNTHESIZER = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTExYTJkZjdkMzdhZjQwZWQ1Y2U0NDJmZDJkNzhjZDhlYmNkY2RjMDI5ZDJhZTY5MWEyYjY0Mzk1Y2RmIn19fQ=="), "&dFood Synthesizer", "", "&fKeeps you fed with artificial food.", "&fComes in two flavors!", "", "&c&o&8\u21E8 &e\u26A1 &70 / 100 J");
            
            SlimefunItem.setRadioactive(THORIUM);
            
            ItemMeta nanoBladeMeta = NANO_BLADE.getItemMeta();
            nanoBladeMeta.setUnbreakable(true);
            NANO_BLADE.setItemMeta(nanoBladeMeta);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}


