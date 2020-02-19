package de.dal3x.mobarena.boss;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;

import de.dal3x.mobarena.arena.Arena;

public class TestBoss extends AbstractBoss {

	protected TestBoss(Arena a) {
		super("test", a);
	}

	public Mob spawn(Location loc) {
		Mob m = (Mob) loc.getWorld().spawnEntity(loc, EntityType.SKELETON); 
		m.setCustomName("test");
		m.setCustomNameVisible(true);
		return m;
	}
}
