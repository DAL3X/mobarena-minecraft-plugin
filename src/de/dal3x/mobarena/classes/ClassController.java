package de.dal3x.mobarena.classes;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.dal3x.mobarena.file.Filehandler;
import de.dal3x.mobarena.item.ItemStorage;

public class ClassController {

	private static ClassController instance;
	private List<PlayerClass> rawClasses;
	private HashMap<Player, PlayerClass> playerClasses;
	private ItemStorage itemStorage;

	private ClassController(ItemStorage itemStorage) {
		this.rawClasses = new LinkedList<PlayerClass>();
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

	public void addRawClass(PlayerClass playerClass) {
		this.rawClasses.add(playerClass);
	}

	public void addClassToPlayer(Player p, String klassenName) {
		this.playerClasses.put(p, this.getRawClassByName(klassenName).clone());
	}
	
	public PlayerClass getRawClassForPlayer(Player p) {
		String name = this.getClassForPlayer(p).getName();
		return this.getRawClassByName(name);
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

	public PlayerClass getRawClassByName(String name) {
		for (PlayerClass rawClass : this.rawClasses) {
			if (rawClass.getName().equalsIgnoreCase(name)) {
				return rawClass;
			}
		}
		return null;
	}

	private String[] getEquipPlayer(Player p) {
		PlayerClass rawClass = getRawClassForPlayer(p);
		return rawClass.getEquip();
	}

	public void equipPlayer(Player p) {
		String eq[] = getEquipPlayer(p);
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
	
	public boolean hasClass(Player p, String klassenName) {
		PlayerClass klasse = getRawClassByName(klassenName);
		if(klasse.getGlory() == 0) {
			return true;
		}
		List<String> classes = Filehandler.getInstance().getPlayersClasses(p);
		for(String c : classes) {
		if(c.equalsIgnoreCase(klassenName)) {
			return true;
		}
		}
		return false;
	}

}
