package de.dal3x.mobarena.item;

import java.util.LinkedList;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import de.dal3x.mobarena.utility.EnchantmentMeta;

public class ItemGenerator {

	public static ItemStack generate(ItemBlueprint itemBP, int amount) {
		ItemStack item = new ItemStack(Material.valueOf(itemBP.getItemType()), amount);
		ItemMeta meta = item.getItemMeta();
		// Set name
		if (meta != null) {
			meta.setDisplayName(itemBP.getName());
			// Set enchantments
			if (itemBP.getEnchantments() != null) {
				for (EnchantmentMeta enchMeta : itemBP.getEnchantments()) {
					@SuppressWarnings("deprecation")
					Enchantment ench = Enchantment.getByName(enchMeta.getName());
					meta.addEnchant(ench, enchMeta.getLevel(), true);
				}
			}
			// Set lore
			LinkedList<String> lore = new LinkedList<String>();
			lore.add(itemBP.getLore());
			meta.setLore(lore);
			// Set unbreaking
			meta.setUnbreakable(true);
			// Set color if needed
			if(itemBP.isColored()) {
				LeatherArmorMeta lMeta = (LeatherArmorMeta) meta;
				lMeta.setColor(Color.fromRGB(itemBP.getColor()));
			}
			// Set the meta to item
			item.setItemMeta(meta);
		}
		return item;
	}
}
