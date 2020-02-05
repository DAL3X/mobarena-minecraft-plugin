package de.dal3x.mobarena.mobs;

import org.bukkit.entity.EntityType;

public class MobBlueprint {

	private EntityType mobType;
	private String name;
	private String[] equip;
	// [0] = head, [1] = chest, [2] = pants, [3] = boots, [4] = mainhand, [5] = offhand

	public EntityType getMobType() {
		return mobType;
	}

	public void setMobType(EntityType mobType) {
		this.mobType = mobType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getEquip() {
		return equip;
	}

	public void setEquip(String[] ausruestung) {
		this.equip = ausruestung;
	}
}
