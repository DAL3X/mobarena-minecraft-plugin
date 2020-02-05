package de.dal3x.mobarena.item;

import java.util.List;

import de.dal3x.mobarena.utility.EnchantmentMeta;

public class ItemBlueprint {

	private String itemType;
	private String name;
	private String lore;
	private List<EnchantmentMeta> enchantments;

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLore() {
		return lore;
	}

	public void setLore(String lore) {
		this.lore = lore;
	}

	public List<EnchantmentMeta> getEnchantments() {
		return enchantments;
	}

	public void setEnchantments(List<EnchantmentMeta> enchantments) {
		this.enchantments = enchantments;
	}

}
