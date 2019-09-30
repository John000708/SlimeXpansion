package me.john000708.slimexpansion.machines;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.energy.EnergyTicker;


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
                if (BlockStorage.check(location, "ENERGY_TRANSMITTER") && Boolean.valueOf(BlockStorage.getLocationInfo(location, "enabled"))) {
                    if (ChargableBlock.getCharge(location) >= 200 && (ChargableBlock.getMaxCharge(l) - ChargableBlock.getCharge(l) >= 200)) {
                        ChargableBlock.addCharge(location, -200);
                        ChargableBlock.addCharge(l, 150);
                        return 150;
                    }
                }
                return 0;
            }

            @Override
            public boolean explode(Location location) {
                return false;
            }
            
        });
        super.register(slimefun);
    }

    private Location deserializeLoc(String locString) {
        String[] parts = locString.split(";");
        World world = Bukkit.getWorld(parts[0]);
        int x = Integer.valueOf(parts[1]);
        int y = Integer.valueOf(parts[2]);
        int z = Integer.valueOf(parts[3]);
        return new Location(world, x, y, z);
    }
}
