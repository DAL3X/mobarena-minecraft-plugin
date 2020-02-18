package de.dal3x.mobarena.boss;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;

public class TestBoss extends AbstractBoss {

	public Mob spawn(Location loc) {
		Mob m = (Mob) loc.getWorld().spawnEntity(loc, EntityType.SKELETON); 
		m.setCustomName("test");
		m.setCustomNameVisible(true);
		return m;
	}

	public String getBossName() {
		return "test";
	}

}
