package de.dal3x.mobarena.mobs;

import java.util.HashMap;

public class MobBlueprintStorage {

	private static MobBlueprintStorage instance;
	private HashMap<String, MobBlueprint> mobBluprints;
	
	private MobBlueprintStorage() {
		this.mobBluprints = new HashMap<String, MobBlueprint>();
	}
	
	public static MobBlueprintStorage getInstance() {
		if(instance == null) {
			instance = new MobBlueprintStorage();
		}
		return instance;
	}
	
	public static void clearInstance() {
		instance = null;
	}

	public void addMobBlueprint(MobBlueprint blueprint) {
		mobBluprints.put(blueprint.getName(), blueprint);
	}

	public MobBlueprint getMobBluprint(String name) {
		return mobBluprints.get(name);
	}
}
