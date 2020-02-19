package de.dal3x.mobarena.utility;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

public class TempBlockChanger {

	private HashMap<Location, Material> blocks;
	private HashMap<Location, BlockData> data;

	public TempBlockChanger() {
		this.blocks = new HashMap<Location, Material>();
		this.data = new HashMap<Location, BlockData>();
	}

	public void setSingleBlock(Location loc, Material material) {
		this.blocks.put(loc, loc.getBlock().getType());
		this.data.put(loc, loc.getBlock().getBlockData());
		loc.getBlock().setType(material);
	}

	public void setMultipleBlocks(List<Location> locs, Material material) {
		for (Location loc : locs) {
			setSingleBlock(loc, material);
		}
	}

	public void resetSingleBlock(Location loc) {
		loc.getBlock().setType(this.blocks.get(loc));
		loc.getBlock().setBlockData(this.data.get(loc));
		this.blocks.remove(loc);
		this.data.remove(loc);
	}

	public void resetMultipleBlocks(List<Location> locs) {
		for (Location loc : locs) {
			resetSingleBlock(loc);
		}
	}

	public void resetAllBlocks() {
		for (Location loc : this.data.keySet()) {
			resetSingleBlock(loc);
		}
	}

}
