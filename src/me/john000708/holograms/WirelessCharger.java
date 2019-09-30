package me.john000708.holograms;

import me.mrCookieSlime.CSCoreLibPlugin.general.World.ArmorStandFactory;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

/**
 * Created by John on 16.04.2016.
 */
public class WirelessCharger {

    public static ArmorStand getArmorStand(Block clay) {
        Location location = new Location(clay.getWorld(), clay.getX() + 0.5, clay.getY() - 1.2, clay.getZ() + 0.5);

        for (Entity n : location.getChunk().getEntities()) {
            if (n instanceof ArmorStand) {
                if (n.getCustomName() == null && location.distanceSquared(n.getLocation()) < 0.4D)
                    return (ArmorStand) n;
            }
        }

        ArmorStand hologram = ArmorStandFactory.createHidden(location);
        hologram.setCustomNameVisible(false);
        hologram.setCustomName(null);
        return hologram;
    }
}
