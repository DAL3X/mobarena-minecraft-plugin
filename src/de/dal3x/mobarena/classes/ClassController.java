package de.dal3x.mobarena.classes;

import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;


public class ClassController {
	
	private List<PlayerClass> classes;
	private HashMap<Player, PlayerClass> playerClasses;
	
	public void addClass(PlayerClass playerClass) {
		this.classes.add(playerClass);
	}
	
	public void addClassToPlayer(Player p, PlayerClass playerClass) {
		this.playerClasses.put(p, playerClass);
	}
	
	public void clearClassesForAllPlayers() {
		this.playerClasses = new HashMap<Player, PlayerClass>();
	}
	
	public PlayerClass getClassByName(String name) {
		for(PlayerClass pClass : this.classes) {
			if(pClass.getName().equalsIgnoreCase(name)) {
				return pClass;
			}
		}
		return null;
	}
	
	public String[] getEquipForClass(PlayerClass playerClass) {
		for(PlayerClass pClass : this.classes) {
			if(pClass.equals(playerClass)) {
				return pClass.getEquip();
			}
		}
		return null;
	}

}
