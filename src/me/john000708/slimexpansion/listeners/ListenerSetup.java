package me.john000708.slimexpansion.listeners;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by John on 28.04.2016.
 */
public class ListenerSetup {

    public ListenerSetup(JavaPlugin plugin) {
        Bukkit.getServer().getPluginManager().registerEvents(new EquipmentListener(), plugin);
        //Bukkit.getServer().getPluginManager().registerEvents(new ChunkLoaderListener(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new SynthesizerListener(), plugin);
    }
}
