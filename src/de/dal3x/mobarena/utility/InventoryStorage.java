package de.dal3x.mobarena.utility;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryStorage {
	
	private HashMap<Player, ItemStack[]> inventorys;
	private static InventoryStorage instance;
	
	private InventoryStorage() {
		this.inventorys = new HashMap<Player, ItemStack[]>();
	}
	
	public static InventoryStorage getInstance() {
		if(instance == null) {
			instance = new InventoryStorage();
		}
		return instance;
	}
	
	public static void clearInstance() {
		instance = null;
	}
	
	public void saveInventory(Player p) {
		ItemStack[] items = p.getInventory().getContents();
		this.inventorys.put(p, items);
	}
	
	public ItemStack[] retrieveInventory(Player p) {
		ItemStack[] inv = this.inventorys.get(p);
		this.inventorys.remove(p);
		return inv;
	}
}
