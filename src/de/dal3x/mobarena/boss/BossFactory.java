package de.dal3x.mobarena.boss;

import java.util.LinkedList;
import java.util.Random;

import de.dal3x.mobarena.arena.Arena;
import de.dal3x.mobarena.boss.implementation.BigSlime;
import de.dal3x.mobarena.boss.implementation.BroodMother;
import de.dal3x.mobarena.boss.implementation.LichLord;
import de.dal3x.mobarena.boss.implementation.Shuffler;

public class BossFactory {

	private static BossFactory instance;
	private LinkedList<String> names;

	private BossFactory() {
		names = new LinkedList<String>();
		names.add("BigSlime"); // 0
		names.add("BroodMother"); // 1
		names.add("Shuffler"); // 2
		names.add("LichLord"); // 3
	}

	private AbstractBoss getBossObject(String name, Arena arena) {
		if(name.equalsIgnoreCase(names.get(0))) {
			return new BigSlime(arena);
		}
		else if(name.equalsIgnoreCase(names.get(1))) {
			return new BroodMother(arena);
		}
		else if(name.equalsIgnoreCase(names.get(2))) {
			return new Shuffler(arena);
		}
		else if(name.equalsIgnoreCase(names.get(3))) {
			return new LichLord(arena);
		}
		else {
			return new TestBoss(arena);
		}
	}

	public static BossFactory getInstance() {
		if (instance == null) {
			instance = new BossFactory();
		}
		return instance;
	}

	public static void clearInstance() {
		instance = null;
	}

	public AbstractBoss getBoss(String name, Arena arena) {
		return getBossObject(name, arena);
	}

	public AbstractBoss getRandomBoss(Arena arena) {
		String name = this.names.get(new Random().nextInt(this.names.size()));
		return getBoss(name, arena);
	}

}