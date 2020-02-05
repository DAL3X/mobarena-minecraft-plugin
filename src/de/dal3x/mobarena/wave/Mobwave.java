package de.dal3x.mobarena.wave;

import java.util.List;

import de.dal3x.mobarena.mobs.MobBlueprint;

public class Mobwave {

	private int number;
	private List<MobBlueprint> mobs;

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public List<MobBlueprint> getMobs() {
		return mobs;
	}

	public void setMobs(List<MobBlueprint> mobs) {
		this.mobs = mobs;
	}

}
