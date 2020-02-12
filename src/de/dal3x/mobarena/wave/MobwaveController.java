package de.dal3x.mobarena.wave;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Mob;

import de.dal3x.mobarena.mobs.MobBlueprint;
import de.dal3x.mobarena.mobs.MobGenerator;

public class MobwaveController {

	private static MobwaveController instance;
	private HashMap<Integer, Mobwave> mobWaves;
	private HashMap<String, Mobwave> BossWaves;
	private MobGenerator gen;

	private MobwaveController(MobGenerator gen) {
		this.mobWaves = new HashMap<Integer, Mobwave>();
		this.BossWaves = new HashMap<String, Mobwave>();
		this.gen = gen;
	}

	public static MobwaveController getInstance() {
		if (instance == null) {
			instance = new MobwaveController(MobGenerator.getInstance());
		}
		return instance;
	}

	public static void clearInstance() {
		instance = null;
	}

	public void addWave(int number, Mobwave wave) {
		this.mobWaves.put(number, wave);
	}

	public Mobwave getMobwave(int number) {
		return this.mobWaves.get(number);
	}

	public void addBossWave(String boss, Mobwave wave) {
		this.BossWaves.put(boss, wave);
	}

	public Mobwave getBossWave(String boss) {
		return this.BossWaves.get(boss);
	}

	public List<Mob> spawnWave(int number, List<Location> locs, int revisions) {
		int j = 0;
		LinkedList<Mob> mobs = new LinkedList<Mob>();
		Collections.shuffle(locs);
		for (int i = 0; i < revisions; i++) {
			for (MobBlueprint mobBP : getMobwave(number).getMobs()) {
				Location loc = locs.get(j % locs.size());
				mobs.add(this.gen.spawnMob(mobBP, loc));
				j++;
			}
		}
		return mobs;
	}

	public List<Mob> spawnWave(Mobwave wave, List<Location> locs, int revisions) {
		int j = 0;
		LinkedList<Mob> mobs = new LinkedList<Mob>();
		Collections.shuffle(locs);
		for (int i = 0; i < revisions; i++) {
			for (MobBlueprint mobBP : wave.getMobs()) {
				Location loc = locs.get(j % locs.size());
				mobs.add(this.gen.spawnMob(mobBP, loc));
				j++;
			}
		}
		return mobs;
	}

}
