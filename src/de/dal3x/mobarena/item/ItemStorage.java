package de.dal3x.mobarena.item;

import java.util.HashMap;

import org.bukkit.inventory.ItemStack;

public class ItemStorage {
	
	private static ItemStorage instance;
	private HashMap <String, ItemStack> items;
	
	private ItemStorage() {
		instance = this;
		this.items = new HashMap<String, ItemStack>();
	}
	
	public static ItemStorage getInstance() {
		if(instance == null) {
			instance = new ItemStorage();
		}
		return instance;
	}
	
	public static void clearInstance() {
		instance = null;
	}

	public HashMap <String, ItemStack> getItems() {
		return items;
	}

	public void setItems(HashMap <String, ItemStack> items) {
		this.items = items;
	}
	
	public ItemStack getItemByName(String name) {
		return this.items.get(name);
	}
	
	public void addItem(String name, ItemStack item) {
		this.items.put(name, item);
	}
	
	public void addItem(ItemBlueprint bp) {
		this.items.put(bp.getName(), ItemGenerator.generate(bp, 1));
	}
	
	public ItemStack getItemByName(String name, int amount) {
		ItemStack item = getItemByName(name);
		item.setAmount(amount);
		return item;
	}

}
