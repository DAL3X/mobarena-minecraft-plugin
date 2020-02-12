package de.dal3x.mobarena.boss;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;

import de.dal3x.mobarena.arena.Arena;

public class TestBoss implements Boss{

	public Mob spawn(Location loc, Arena arena) {
		Mob m = (Mob) loc.getWorld().spawnEntity(loc, EntityType.SKELETON); 
		m.setCustomName("test");
		m.setCustomNameVisible(true);
		return m;
	}

	public String getBossName() {
		return "test";
	}

}
