package de.dal3x.mobarena.boss;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.Mob;

import de.dal3x.mobarena.arena.Arena;
import de.dal3x.mobarena.arena.ArenaStorage;
import de.dal3x.mobarena.boss.implementation.BigSlime;
import de.dal3x.mobarena.boss.implementation.BroodMother;
import de.dal3x.mobarena.boss.implementation.LichLord;
import de.dal3x.mobarena.boss.implementation.Shuffler;

public class BossStorage {

	private static BossStorage instance;
	private HashMap<Arena, List<IBoss>> bosses;

	private BossStorage() {
		this.bosses = new HashMap<Arena, List<IBoss>>();
		init();
	}

	private void init() {
		// Hier werden alle Bosse eingefügt
		for (Arena a : ArenaStorage.getInstance().getArenas()) {
			List<IBoss> bossList = new LinkedList<IBoss>();
			// -----------------------------
			//bossList.add(new BigSlime(a));
			//bossList.add(new LichLord(a));
			//bossList.add(new BroodMother(a));
			bossList.add(new Shuffler(a));
			// -----------------------------
			this.bosses.put(a, bossList);
		}
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
		for (IBoss b : this.bosses.get(arena)) {
			if (b.getBossName().equalsIgnoreCase(name)) {
				return b.spawn(loc);
			}
		}
		return null;
	}

	public Mob spawnRandomBoss(Location loc, Arena arena) {
		String name = this.bosses.get(arena).get(new Random().nextInt(this.bosses.get(arena).size())).getBossName();
		return spawnBoss(name, loc, arena);
	}

}
