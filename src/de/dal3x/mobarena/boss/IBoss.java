package de.dal3x.mobarena.boss;

import org.bukkit.Location;
import org.bukkit.entity.Mob;

public interface IBoss {
	
	public Mob spawn(Location loc);
	public String getBossName();
	public Mob getMobInstance();
	public void unregister();
}
