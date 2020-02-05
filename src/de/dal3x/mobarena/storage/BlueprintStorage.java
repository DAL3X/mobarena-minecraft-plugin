package de.dal3x.mobarena.storage;

import java.util.HashMap;

import de.dal3x.mobarena.item.ItemBlueprint;
import de.dal3x.mobarena.mobs.MobBlueprint;

public class BlueprintStorage {

	private HashMap<String, ItemBlueprint> itemBluprints;
	private HashMap<String, MobBlueprint> mobBluprints;

	public BlueprintStorage() {
		this.itemBluprints = new HashMap<String, ItemBlueprint>();
		this.mobBluprints = new HashMap<String, MobBlueprint>();
	}

	public void addItemBlueprint(ItemBlueprint blueprint) {
		itemBluprints.put(blueprint.getName(), blueprint);
	}

	public void addMobBlueprint(MobBlueprint blueprint) {
		mobBluprints.put(blueprint.getName(), blueprint);
	}

	public void getItemBluprint(String name) {
		itemBluprints.get(name);
	}

	public void getMobBluprint(String name) {
		mobBluprints.get(name);
	}

	public int getAmountOfItems() {
		return this.itemBluprints.size();
	}

	public int getAmountOfMobs() {
		return this.mobBluprints.size();
	}
}
