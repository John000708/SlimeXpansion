package me.john000708.slimexpansion.machines;

import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import me.john000708.slimexpansion.utils.XpansionLogger;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.GeneratorTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import java.util.logging.Level;


/**
 * Created by John on 05.09.2016.
 */
public class EnergyReceiver extends SimpleSlimefunItem<GeneratorTicker> implements EnergyNetComponent {

    public EnergyReceiver(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    private Location deserializeLoc(String locString) {
        String[] parts = locString.split(";");
        if (parts.length != 4) {
            XpansionLogger.log(Level.WARNING, "Invalid location string: " + locString);
            return null;
        }
        World world = Bukkit.getWorld(parts[0]);
        int x = Integer.parseInt(parts[1]);
        int y = Integer.parseInt(parts[2]);
        int z = Integer.parseInt(parts[3]);
        return new Location(world, x, y, z);
    }

    @Override
    public GeneratorTicker getItemHandler() {
        return new GeneratorTicker() {

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

        };
    }

    @Override
    public EnergyNetComponentType getEnergyComponentType() {
        return null;
    }

    @Override
    public int getCapacity() {
        return 0;
    }
}
