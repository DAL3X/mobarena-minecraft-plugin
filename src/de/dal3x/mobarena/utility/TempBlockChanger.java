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
		Location centered = loc.getWorld().getBlockAt(loc).getLocation();
		if(this.blocks.containsKey(centered)) {
			return;
		}
		this.blocks.put(centered, centered.getBlock().getType());
		this.data.put(centered, centered.getBlock().getBlockData());
		loc.getBlock().setType(material);
	}

	public void setMultipleBlocks(List<Location> locs, Material material) {
		for (Location loc : locs) {
			setSingleBlock(loc, material);
		}
	}
	
	public void resetSingleBlock(Location loc) {
		Location centered = loc.getWorld().getBlockAt(loc).getLocation();
		if(!this.blocks.containsKey(centered)) {
			return;
		}
		centered.getBlock().setType(this.blocks.get(centered));
		centered.getBlock().setBlockData(this.data.get(centered));
		this.blocks.remove(centered);
		this.data.remove(centered);
	}

	public void resetAllBlocks() {
		for (Location loc : this.blocks.keySet()) {
			loc.getBlock().setType(this.blocks.get(loc));
			loc.getBlock().setBlockData(this.data.get(loc));
		}
		this.blocks.clear();
		this.data.clear();
	}

}
