package me.john000708.slimexpansion.utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Wrapper class for BukkitScheduler class.
 */
public final class SchedulerHandler {

    private JavaPlugin plugin;

    public SchedulerHandler(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void runTaskLater(Runnable runnable, long delay) {
        Bukkit.getScheduler().runTaskLater(plugin, runnable, delay);
    }
}
