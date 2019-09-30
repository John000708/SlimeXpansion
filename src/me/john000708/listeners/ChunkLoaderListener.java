package me.john000708.listeners;

import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

/**
 * Created by John on 22.05.2016.
 */
public class ChunkLoaderListener implements Listener {
    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent e) {
        if (BlockStorage.getChunkInfo(e.getChunk(), "loaded") != null && BlockStorage.getChunkInfo(e.getChunk(), "loaded").equals("true")) {

        }
    }
}
