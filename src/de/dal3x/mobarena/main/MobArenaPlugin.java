package de.dal3x.mobarena.main;

import org.bukkit.plugin.java.JavaPlugin;

import de.dal3x.mobarena.classes.ClassController;
import de.dal3x.mobarena.file.Filehandler;
import de.dal3x.mobarena.storage.BlueprintStorage;

public class MobArenaPlugin extends JavaPlugin{
	
	public void onEnable(){
		BlueprintStorage storage = new BlueprintStorage();
		ClassController classController = new ClassController();
		Filehandler fileHandler = new Filehandler(storage, classController);
		fileHandler.loadItemBlueprints();
		fileHandler.loadMobBlueprints();
		fileHandler.loadClasses();
	}
	
	public void onDisable() {
		
	}
}
