package me.john000708.slimexpansion.machines;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import org.bukkit.block.Block;

import java.util.function.Consumer;

public final class XpansionBlockTicker extends BlockTicker {
    private Consumer<Block> onTick;
    private int time = 0;

    public XpansionBlockTicker(Consumer<Block> onTick) {
        this.onTick = onTick;
    }

    @Override
    public boolean isSynchronized() {
        return false;
    }

    @Override
    public void uniqueTick() {
        time++;
    }

    @Override
    public void tick(Block block, SlimefunItem slimefunItem, Config config) {
        if (time % 2 != 0) return;
        onTick.accept(block);
    }
}
