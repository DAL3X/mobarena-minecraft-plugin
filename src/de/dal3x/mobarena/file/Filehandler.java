package de.dal3x.mobarena.file;

import java.io.File;
import java.util.LinkedList;
import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

import de.dal3x.mobarena.classes.ClassController;
import de.dal3x.mobarena.classes.PlayerClass;
import de.dal3x.mobarena.config.Config;
import de.dal3x.mobarena.item.ItemBlueprint;
import de.dal3x.mobarena.mobs.MobBlueprint;
import de.dal3x.mobarena.output.ConsoleOutputs;
import de.dal3x.mobarena.skill.ILeftClickSkill;
import de.dal3x.mobarena.skill.IRightClickSkill;
import de.dal3x.mobarena.skill.SkillController;
import de.dal3x.mobarena.storage.BlueprintStorage;
import de.dal3x.mobarena.utility.EnchantmentMeta;

public class Filehandler {

	private BlueprintStorage storage;
	private ClassController classController;
	

	public Filehandler(BlueprintStorage storage, ClassController classController) {
		this.classController = classController;
		this.storage = storage;
	}

	public void loadMobBlueprints() {
		File mobFile = new File(Config.dirPath + Config.mobFileName);
		if (!mobFile.exists()) {
			System.out.println(ConsoleOutputs.consolePrefix + " (Mobs) " +ConsoleOutputs.fileNotFound);
			return;
		}
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(mobFile);
		Set<String> entrys = cfg.getKeys(false);
		int counter = 0;
		for (String entry : entrys) {
			MobBlueprint mob = new MobBlueprint();
			//Set the name
			mob.setName(entry);
			//Load and set EntityType
			EntityType type = EntityType.valueOf(cfg.getString(entry + ".type"));
			mob.setMobType(type);
			//Load and set Equipment
			String[] eq = new String[6];
			Set<String> itemEntrys = cfg.getConfigurationSection((entry + ".equip")).getKeys(false);
			if(itemEntrys != null) {
				for(String itemName : itemEntrys) {
					eq[cfg.getInt(entry + ".equip." + itemName)] = itemName;
				}
			}
			mob.setEquip(eq);
			this.storage.addMobBlueprint(mob);
			counter ++;
		}
		System.out.println(ConsoleOutputs.consolePrefix + counter + " Mobs " + ConsoleOutputs.successload);
	}

	public void loadItemBlueprints() {
		File itemFile = new File(Config.dirPath + Config.itemFileName);
		if (!itemFile.exists()) {
			System.out.println(ConsoleOutputs.consolePrefix + " (Items) " +ConsoleOutputs.fileNotFound);
			return;
		}
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(itemFile);
		Set<String> entrys = cfg.getKeys(false);
		int counter = 0;
		for (String entry : entrys) {
			ItemBlueprint item = new ItemBlueprint();
			// Set the name
			item.setName(entry);
			// Load and set Itemtypes
			String type = cfg.getString(entry + ".type");
			item.setItemType(type);
			// Load and set Itemlore
			String lore = cfg.getString(entry + ".lore");
			item.setLore(lore);
			// Load and set Enchantments
			Set<String> enchEntrys = cfg.getConfigurationSection((entry + ".enchantments")).getKeys(false);
			if (enchEntrys != null) {
				LinkedList<EnchantmentMeta> enchList = new LinkedList<EnchantmentMeta>();
				for (String enchName : enchEntrys) {
					int level = cfg.getInt(entry + ".enchantments." + enchName);
					enchList.add(new EnchantmentMeta(enchName, level));
				}
				item.setEnchantments(enchList);
			}
			this.storage.addItemBlueprint(item);
			counter ++;
		}
		System.out.println(ConsoleOutputs.consolePrefix + counter + " Items " + ConsoleOutputs.successload);
	}

	public void loadClasses() {
		File classFile = new File(Config.dirPath + Config.classFileName);
		if (!classFile.exists()) {
			System.out.println(ConsoleOutputs.consolePrefix + " (Klassen) " +ConsoleOutputs.fileNotFound);
			return;
		}
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(classFile);
		Set<String> entrys = cfg.getKeys(false);
		int counter = 0;
		for (String entry : entrys) {
			//Set the name
			PlayerClass playerClass = new PlayerClass(entry);
			//Load and set Skills
			String leftClickSkill = cfg.getString(entry + ".leftClickSkill");
			if(leftClickSkill != null) {
				ILeftClickSkill leftSkill = SkillController.getLeftClickSkill(leftClickSkill);
				playerClass.setLeftClickSkill(leftSkill);
			}
			String rightClickSkill = cfg.getString(entry + ".rightClickSkill");
			if(rightClickSkill != null) {
				IRightClickSkill rightSkill = SkillController.getRightClickSkill(rightClickSkill);
				playerClass.setRightClickSkill(rightSkill);
			}
			//Load and set Equipment
			String[] eq = new String[6];
			Set<String> itemEntrys = cfg.getConfigurationSection((entry + ".equip")).getKeys(false);
			if(itemEntrys != null) {
				for(String itemName : itemEntrys) {
					eq[cfg.getInt(entry + ".equip." + itemName)] = itemName;
				}
			}
			playerClass.setEquip(eq);
			this.classController.addClass(playerClass);
			counter ++;
		}
		System.out.println(ConsoleOutputs.consolePrefix + counter + " Klassen " + ConsoleOutputs.successload);
	}

	public void loadArenaAndMobewaves() {
		
	}

}
