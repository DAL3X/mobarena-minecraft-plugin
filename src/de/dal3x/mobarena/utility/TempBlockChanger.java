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
		this.blocks.put(loc.clone(), loc.clone().getBlock().getType());
		this.data.put(loc.clone(), loc.clone().getBlock().getBlockData());
		loc.getBlock().setType(material);
	}

	public void setMultipleBlocks(List<Location> locs, Material material) {
		for (Location loc : locs) {
			setSingleBlock(loc, material);
		}
	}

	public void resetAllBlocks() {
		for (Location loc : this.blocks.keySet()) {
			loc.getWorld().getBlockAt(loc).setType(this.blocks.get(loc));
			loc.getWorld().getBlockAt(loc).setBlockData(this.data.get(loc));
		}
		this.blocks.clear();
		this.data.clear();
	}

}
