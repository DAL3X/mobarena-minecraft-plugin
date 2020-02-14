package de.dal3x.mobarena.boss;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.Mob;

import de.dal3x.mobarena.arena.Arena;

public class BossStorage {

	private static BossStorage instance;
	private List<Boss> bosses;
	
	private BossStorage() {
		this.bosses = new LinkedList<Boss>();
		init();
	}
	
	private void init() {
		//Hier werden alle Bosse eingefügt
		this.bosses.add(new BigSlime());
	}
	
	public static BossStorage getInstance() {
		if (instance == null) {
			instance = new BossStorage();
		}
		return instance;
	}

	public static void clearInstance() {
		instance = null;
	}
	
	public Mob spawnBoss(String name, Location loc, Arena arena) {
		for(Boss b : this.bosses) {
			if(b.getBossName().equalsIgnoreCase(name)) {
				return b.spawn(loc, arena);
			}
		}
		return null;
	}
	
	public Mob spawnRandomBoss(Location loc, Arena arena) {
		String name = this.bosses.get(new Random().nextInt(this.bosses.size())).getBossName();
		return spawnBoss(name, loc, arena);
	}

}
