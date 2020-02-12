package de.dal3x.mobarena.wave;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import de.dal3x.mobarena.mobs.MobBlueprint;

public class Mobwave {

	private int number;
	private List<MobBlueprint> mobs;
	private HashMap<MobBlueprint, Integer> amounts;
	private String name;
	
	public Mobwave() {
		this.amounts = new HashMap<MobBlueprint, Integer>();
		this.mobs = new LinkedList<MobBlueprint>();
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public List<MobBlueprint> getMobs() {
		List<MobBlueprint> allBlueprints = new LinkedList<MobBlueprint>();
		for(MobBlueprint bp : this.mobs) {
			for(int i = 0; i < this.amounts.get(bp);i++) {
				allBlueprints.add(bp);
			}
		}
		return allBlueprints;
	}
	
	public void setMobs(List<MobBlueprint> mobs) {
		this.mobs = mobs;
	}
	
	public void addMob(MobBlueprint bp, int amount) {
		this.mobs.add(bp);
		this.amounts.put(bp, amount);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
