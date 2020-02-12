package de.dal3x.mobarena.boss;

import org.bukkit.Location;
import org.bukkit.entity.Mob;

import de.dal3x.mobarena.arena.Arena;

public interface Boss {
	
	public Mob spawn(Location loc, Arena arena);
	public String getBossName();
}
