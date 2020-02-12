package de.dal3x.mobarena.classes;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.dal3x.mobarena.item.ItemStorage;

public class ClassController {

	private static ClassController instance;
	private List<PlayerClass> classes;
	private HashMap<Player, PlayerClass> playerClasses;
	private ItemStorage itemStorage;

	private ClassController(ItemStorage itemStorage) {
		this.classes = new LinkedList<PlayerClass>();
		this.playerClasses = new HashMap<Player, PlayerClass>();
		this.itemStorage = itemStorage;
	}

	public static ClassController getInstance() {
		if (instance == null) {
			instance = new ClassController(ItemStorage.getInstance());
		}
		return instance;
	}

	public static void clearInstance() {
		instance = null;
	}

	public void addClass(PlayerClass playerClass) {
		this.classes.add(playerClass);
	}

	public void addClassToPlayer(Player p, PlayerClass playerClass) {
		this.playerClasses.put(p, playerClass);
	}

	public PlayerClass getClassForPlayer(Player p) {
		return this.playerClasses.get(p);
	}

	public void clearClassForPlayer(Player p) {
		if (this.playerClasses.containsKey(p)) {
			this.playerClasses.remove(p);
		}
	}
	
	public void clearClassForPlayers(List<Player> list) {
		for(Player p : list){
			clearClassForPlayer(p);
		}
	}

	public void clearClassesForAllPlayers() {
		this.playerClasses = new HashMap<Player, PlayerClass>();
	}

	public PlayerClass getClassByName(String name) {
		for (PlayerClass pClass : this.classes) {
			if (pClass.getName().equalsIgnoreCase(name)) {
				return pClass;
			}
		}
		return null;
	}

	public String[] getEquipForClass(PlayerClass playerClass) {
		for (PlayerClass pClass : this.classes) {
			if (pClass.equals(playerClass)) {
				return pClass.getEquip();
			}
		}
		return null;
	}

	public void equipPlayer(Player p) {
		String[] eq = getEquipForClass(getClassForPlayer(p));
		if (eq == null) {
			return;
		}
		for (int i = 0; i < eq.length; i++) {
			if (eq[i] != null) {
				String itemName = eq[i];
				ItemStack item = this.itemStorage.getItemByName(itemName);
				switch (i) {
				case 0:
					p.getEquipment().setHelmet(item);
					break;
				case 1:
					p.getEquipment().setChestplate(item);
					break;
				case 2:
					p.getEquipment().setLeggings(item);
					break;
				case 3:
					p.getEquipment().setBoots(item);
					break;
				case 4:
					p.getEquipment().setItemInMainHand(item);
					break;
				case 5:
					p.getEquipment().setItemInOffHand(item);
					break;
				}
			}
		}
	}

}
