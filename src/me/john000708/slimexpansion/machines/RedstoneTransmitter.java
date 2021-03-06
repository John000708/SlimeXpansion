package me.john000708.slimexpansion.machines;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.AnaloguePowerable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.john000708.slimexpansion.SlimeXpansion;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

public class RedstoneTransmitter extends SimpleSlimefunItem<BlockTicker> {

	public RedstoneTransmitter(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, id, recipeType, recipe);
	}

	@Override
	public BlockTicker getItemHandler() {
		return new BlockTicker() {

			@Override
			public boolean isSynchronized() {
				return false;
			}

			@Override
			public void tick(Block block, SlimefunItem slimefunItem, Config config) {
				if (BlockStorage.getLocationInfo(block.getLocation(), "transmitterLoc") != null) {
					String[] serializedLoc = BlockStorage.getLocationInfo(block.getLocation(), "transmitterLoc").split(";");
					World world = Bukkit.getWorld(serializedLoc[0]);
					int x = Integer.valueOf(serializedLoc[1]);
					int y = Integer.valueOf(serializedLoc[2]);
					int z = Integer.valueOf(serializedLoc[3]);

					Block transmitterBlock = world.getBlockAt(x, y, z);

					if (transmitterBlock.getType() == Material.AIR) {
						BlockStorage.addBlockInfo(block, "transmitterLoc", null);
						AnaloguePowerable powerable = (AnaloguePowerable) block.getBlockData();
						powerable.setPower(0);
						block.setBlockData(powerable);
						return;
					}

					int power = transmitterBlock.getBlockPower();
					new BukkitRunnable() {
						@Override
						public void run() {
							AnaloguePowerable powerable = (AnaloguePowerable) block.getBlockData();
							powerable.setPower(power);
							block.setBlockData(powerable); //TODO: make power dynamic
						}
					}.runTaskLater(SlimeXpansion.plugin, 1);
				}
			}
		};
	}

}
