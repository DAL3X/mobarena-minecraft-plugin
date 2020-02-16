package de.dal3x.mobarena.mobs;

import org.bukkit.Location;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Rabbit;
import org.bukkit.inventory.ItemStack;

import de.dal3x.mobarena.item.ItemStorage;

public class MobGenerator {

	private static MobGenerator instance;
	private ItemStorage itemStorage;
	private MobBlueprintStorage mobStorage;

	private MobGenerator(ItemStorage itemStorage, MobBlueprintStorage mobStorage) {
		this.itemStorage = itemStorage;
		this.mobStorage = mobStorage;
	}

	public static MobGenerator getInstance() {
		if (instance == null) {
			instance = new MobGenerator(ItemStorage.getInstance(), MobBlueprintStorage.getInstance());
		}
		return instance;
	}

	public static void clearInstance() {
		instance = null;
	}

	public Mob spawnMob(MobBlueprint mobBP, Location loc) {
		// Spawn mob on location
		Mob mob = (Mob) loc.getWorld().spawnEntity(loc, mobBP.getMobType());
		mob = addCustomAttribute(mob);
		// Set name
		mob.setCustomName(mobBP.getName());
		mob.setCustomNameVisible(true);
		// Set equipment
		if (mobBP.getEquip() != null) {
			for (int i = 0; i < mobBP.getEquip().length; i++) {
				if (mobBP.getEquip()[i] != null) {
					String itemName = mobBP.getEquip()[i];
					ItemStack item = this.itemStorage.getItemByName(itemName);
					switch (i) {
					case 0:
						mob.getEquipment().setHelmet(item);
						break;
					case 1:
						mob.getEquipment().setChestplate(item);
						break;
					case 2:
						mob.getEquipment().setLeggings(item);
						break;
					case 3:
						mob.getEquipment().setBoots(item);
						break;
					case 4:
						mob.getEquipment().setItemInMainHand(item);
						break;
					case 5:
						mob.getEquipment().setItemInOffHand(item);
						break;
					}
				}
			}
		}
		return mob;
	}

	private Mob addCustomAttribute(Mob mob) {
		if (mob instanceof Bee) {
			Bee b = (Bee) mob;
			b.setAnger(Integer.MAX_VALUE);
			return b;
		} else if (mob instanceof Rabbit) {
			Rabbit r = (Rabbit) mob;
			r.setRabbitType(Rabbit.Type.THE_KILLER_BUNNY);
			return r;
		}
		return mob;
	}

	public Mob spawnMob(String name, Location loc) {
		return spawnMob(mobStorage.getMobBluprint(name), loc);
	}

}
