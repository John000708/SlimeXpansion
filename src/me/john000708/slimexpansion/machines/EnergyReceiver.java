package me.john000708.slimexpansion.machines;

import me.john000708.slimexpansion.SlimeXpansion;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.energy.EnergyTicker;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;


/**
 * Created by John on 05.09.2016.
 */
public class EnergyReceiver extends SlimefunItem {

    public EnergyReceiver(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, name, recipeType, recipe);
    }

    @Override
    public void register(boolean slimefun) {
        addItemHandler(new EnergyTicker() {

            @Override
            public double generateEnergy(Location l, SlimefunItem slimefunItem, Config config) {
                if (BlockStorage.getLocationInfo(l, "transmitterLoc") == null) return 0;
                Location location = deserializeLoc(BlockStorage.getLocationInfo(l, "transmitterLoc"));
                if (location != null
                    && BlockStorage.check(location, "ENERGY_TRANSMITTER")
                    && Boolean.parseBoolean(BlockStorage.getLocationInfo(location, "enabled"))
                    && ChargableBlock.getCharge(location) >= 200
                    && (ChargableBlock.getMaxCharge(l) - ChargableBlock.getCharge(l) >= 200)
                ) {
                    ChargableBlock.addCharge(location, -200);
                    ChargableBlock.addCharge(l, 150);
                    return 150;
                }
                return 0;
            }

            @Override
            public boolean explode(Location location) {
                return false;
            }

        });
        super.register(SlimeXpansion.plugin);
    }

    private Location deserializeLoc(String locString) {
        String[] parts = locString.split(";");
        if (parts.length != 4) {
            SlimeXpansion.plugin.getLogger().warning("Invalid location string: " + locString);
            return null;
        }
        World world = Bukkit.getWorld(parts[0]);
        int x = Integer.parseInt(parts[1]);
        int y = Integer.parseInt(parts[2]);
        int z = Integer.parseInt(parts[3]);
        return new Location(world, x, y, z);
    }
}
