package de.dal3x.mobarena.file;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import de.dal3x.mobarena.arena.Arena;
import de.dal3x.mobarena.arena.ArenaStorage;
import de.dal3x.mobarena.classes.ClassController;
import de.dal3x.mobarena.classes.PlayerClass;
import de.dal3x.mobarena.config.Config;
import de.dal3x.mobarena.item.ItemBlueprint;
import de.dal3x.mobarena.item.ItemStorage;
import de.dal3x.mobarena.mobs.MobBlueprint;
import de.dal3x.mobarena.mobs.MobBlueprintStorage;
import de.dal3x.mobarena.output.ConsoleOutputs;
import de.dal3x.mobarena.skill.ILeftClickSkill;
import de.dal3x.mobarena.skill.IPassiveSkill;
import de.dal3x.mobarena.skill.IRightClickSkill;
import de.dal3x.mobarena.skill.SkillController;
import de.dal3x.mobarena.utility.EnchantmentMeta;
import de.dal3x.mobarena.wave.Mobwave;
import de.dal3x.mobarena.wave.MobwaveController;

public class Filehandler {

	private MobBlueprintStorage mobStorage;
	private ClassController classController;
	private ArenaStorage arenaStorage;
	private MobwaveController mobwaveController;
	private ItemStorage itemStorage;
	private static Filehandler instance;

	private Filehandler() {
		this.classController = ClassController.getInstance();
		this.mobStorage = MobBlueprintStorage.getInstance();
		this.arenaStorage = ArenaStorage.getInstance();
		this.mobwaveController = MobwaveController.getInstance();
		this.itemStorage = ItemStorage.getInstance();
	}

	public static Filehandler getInstance() {
		if (instance == null) {
			instance = new Filehandler();
		}
		return instance;
	}

	public static void clearInstance() {
		instance = null;
	}

	public void loadRessources() {
		// Festgelegte Reihenfolge, nicht aendern!
		loadItemBlueprints();
		loadMobBlueprints();
		loadClasses();
		loadWaves();
		loadArenas();
	}

	private void loadMobBlueprints() {
		File mobFile = new File(Config.dirPath + Config.mobFileName);
		if (!mobFile.exists()) {
			System.out.println(ConsoleOutputs.consolePrefix + " (" + ConsoleOutputs.mobs + ") " + ConsoleOutputs.fileNotFound);
			return;
		}
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(mobFile);
		Set<String> entrys = cfg.getKeys(false);
		int counter = 0;
		for (String entry : entrys) {
			MobBlueprint mob = new MobBlueprint();
			// Set the name
			mob.setName(entry);
			// Load and set EntityType
			EntityType type = EntityType.valueOf(cfg.getString(entry + ".type"));
			mob.setMobType(type);
			// Load and set Equipment
			if (cfg.getConfigurationSection(entry + ".equip") != null) {
				String[] eq = new String[6];
				Set<String> itemEntrys = cfg.getConfigurationSection((entry + ".equip")).getKeys(false);
				if (itemEntrys != null) {
					for (String itemName : itemEntrys) {
						eq[cfg.getInt(entry + ".equip." + itemName)] = itemName;
					}
				}
				mob.setEquip(eq);
			}
			// Register
			this.mobStorage.addMobBlueprint(mob);
			counter++;
		}
		System.out.println(ConsoleOutputs.consolePrefix + counter + " " + ConsoleOutputs.mobs + ConsoleOutputs.successload);
	}

	private void loadItemBlueprints() {
		File itemFile = new File(Config.dirPath + Config.itemFileName);
		if (!itemFile.exists()) {
			System.out.println(ConsoleOutputs.consolePrefix + " (" + ConsoleOutputs.items + ") " + ConsoleOutputs.fileNotFound);
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
			// Load colour of leather equip
			Material mat = Material.valueOf(type);
			if (mat != null && (mat.equals(Material.LEATHER_BOOTS) || mat.equals(Material.LEATHER_CHESTPLATE) || mat.equals(Material.LEATHER_HELMET)
					|| mat.equals(Material.LEATHER_LEGGINGS))) {
				if(cfg.contains(entry + ".color")) {
					item.setColor(cfg.getInt(entry + ".color"));
				}
			}
			// Load and set Enchantments
			if (cfg.getConfigurationSection(entry + ".enchantments") != null) {
				Set<String> enchEntrys = cfg.getConfigurationSection((entry + ".enchantments")).getKeys(false);
				if (enchEntrys != null) {
					LinkedList<EnchantmentMeta> enchList = new LinkedList<EnchantmentMeta>();
					for (String enchName : enchEntrys) {
						int level = cfg.getInt(entry + ".enchantments." + enchName);
						enchList.add(new EnchantmentMeta(enchName, level));
					}
					item.setEnchantments(enchList);
				}
			}
			// Register
			this.itemStorage.addItem(item);
			counter++;
		}
		System.out.println(ConsoleOutputs.consolePrefix + counter + " " + ConsoleOutputs.items + ConsoleOutputs.successload);
	}

	private void loadClasses() {
		File classFile = new File(Config.dirPath + Config.classFileName);
		if (!classFile.exists()) {
			System.out.println(ConsoleOutputs.consolePrefix + " (" + ConsoleOutputs.classes + ") " + ConsoleOutputs.fileNotFound);
			return;
		}
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(classFile);
		Set<String> entrys = cfg.getKeys(false);
		int counter = 0;
		for (String entry : entrys) {
			// Set the name
			PlayerClass playerClass = new PlayerClass(entry);
			// Load and set Skills
			String leftClickSkill = cfg.getString(entry + ".leftClickSkill");
			if (leftClickSkill != null) {
				ILeftClickSkill leftSkill = SkillController.getLeftClickSkill(leftClickSkill);
				playerClass.setLeftClickSkill(leftSkill);
			}
			String rightClickSkill = cfg.getString(entry + ".rightClickSkill");
			if (rightClickSkill != null) {
				IRightClickSkill rightSkill = SkillController.getRightClickSkill(rightClickSkill);
				playerClass.setRightClickSkill(rightSkill);
			}
			String passiveSkill = cfg.getString(entry + ".passiveSkill");
			if (passiveSkill != null) {
				IPassiveSkill passive = SkillController.getPassiveSkill(passiveSkill);
				playerClass.setPassiveSkill(passive);
			}
			// Load and set Glory
			int glory = cfg.getInt(entry + ".glory");
			playerClass.setGlory(glory);
			// Load and set Equipment
			String[] eq = new String[6];
			Set<String> itemEntrys = cfg.getConfigurationSection(entry + ".equip").getKeys(false);
			if (itemEntrys != null) {
				for (String itemName : itemEntrys) {
					eq[cfg.getInt(entry + ".equip." + itemName)] = itemName;
				}
			}
			playerClass.setEquip(eq);
			// Register
			this.classController.addRawClass(playerClass);
			counter++;
		}
		System.out.println(ConsoleOutputs.consolePrefix + counter + " " + ConsoleOutputs.classes + ConsoleOutputs.successload);
	}

	private void loadArenas() {
		File arenaFile = new File(Config.dirPath + Config.arenaFileName);
		if (!arenaFile.exists()) {
			System.out.println(ConsoleOutputs.consolePrefix + " (" + ConsoleOutputs.arenas + ") " + ConsoleOutputs.fileNotFound);
			return;
		}
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(arenaFile);
		Set<String> entrys = cfg.getKeys(false);
		int counter = 0;
		for (String entry : entrys) {
			// Load world
			World world = Bukkit.getServer().getWorld(cfg.getString(entry + ".world"));
			// Load Lobbylocation
			Location lobby = createLocation(cfg, entry + ".lobbyPunkt", world);
			// Load Spectatorlocation
			Location spectator = createLocation(cfg, entry + ".spectatorPunkt", world);
			// Load BossSpawnlocation
			Location bossLocation = createLocation(cfg, entry + ".bossSpawn", world);
			// Load spawnLocation
			World spawnWorld = Bukkit.getServer().getWorld(cfg.getString(entry + ".spawn.world"));
			Location spawnLocation = createLocation(cfg, entry + ".spawn", spawnWorld);
			// Load playerSpawnpoints
			List<Location> playerspawnpoints = new LinkedList<Location>();
			Set<String> playerSpawnpointEntrys = cfg.getConfigurationSection((entry + ".spielerSpawnPunkte")).getKeys(false);
			for (String playerSpawnPunkt : playerSpawnpointEntrys) {
				playerspawnpoints.add(createLocation(cfg, entry + ".spielerSpawnPunkte." + playerSpawnPunkt, world));
			}
			// Load mobSpawnpoints
			List<Location> mobspawnpoints = new LinkedList<Location>();
			Set<String> mobSpawnpointEntrys = cfg.getConfigurationSection((entry + ".monsterSpawnPunkte")).getKeys(false);
			for (String mobSpawnPunkt : mobSpawnpointEntrys) {
				mobspawnpoints.add(createLocation(cfg, entry + ".monsterSpawnPunkte." + mobSpawnPunkt, world));
			}
			// Load and set wavenumbers
			@SuppressWarnings("unchecked")
			List<Integer> wavenumbers = (List<Integer>) cfg.getList(entry + ".wellen");
			LinkedList<Mobwave> mobwaves = new LinkedList<Mobwave>();
			if (wavenumbers != null) {
				for (int number : wavenumbers) {
					if (mobwaveController.getMobwave(number) != null) {
						mobwaves.add(mobwaveController.getMobwave(number));
					}
				}
			}
			// Register
			Arena arena = new Arena(entry, lobby, spectator, spawnLocation, bossLocation, mobspawnpoints, playerspawnpoints, mobwaves);
			this.arenaStorage.addArena(arena);
			counter++;
		}
		System.out.println(ConsoleOutputs.consolePrefix + counter + " " + ConsoleOutputs.arenas + ConsoleOutputs.successload);
	}

	private void loadWaves() {
		File waveFile = new File(Config.dirPath + Config.waveFileName);
		if (!waveFile.exists()) {
			System.out.println(ConsoleOutputs.consolePrefix + " (" + ConsoleOutputs.waves + ") " + ConsoleOutputs.fileNotFound);
			return;
		}
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(waveFile);
		Set<String> entrys = cfg.getKeys(false);
		int counter = 0;
		for (String entry : entrys) {
			Mobwave mobwave = new Mobwave();
			// Load and set name
			mobwave.setName(entry);
			// Load and set number
			int number = cfg.getInt(entry + ".nummer");
			mobwave.setNumber(number);
			// Load and set mobs
			Set<String> mobEntrys = cfg.getConfigurationSection((entry + ".mobs")).getKeys(false);
			for (String mob : mobEntrys) {
				MobBlueprint mobBP = this.mobStorage.getMobBluprint(mob);
				int amount = cfg.getInt(entry + ".mobs." + mob);
				mobwave.addMob(mobBP, amount);
			}
			// Register
			this.mobwaveController.addWave(number, mobwave);
			counter++;
		}
		System.out.println(ConsoleOutputs.consolePrefix + counter + " " + ConsoleOutputs.waves + ConsoleOutputs.successload);
	}

	private Location createLocation(FileConfiguration cfg, String path, World w) {
		double x = cfg.getDouble(path + ".x");
		double y = cfg.getDouble(path + ".y");
		double z = cfg.getDouble(path + ".z");
		float yaw = (float) cfg.getDouble(path + ".yaw");
		float pitch = (float) cfg.getDouble(path + ".pitch");
		return new Location(w, x, y, z, yaw, pitch);
	}

	public void addArenaPoints(Player p, int points) {
		File playerfile = new File(Config.pointFilePath + p.getUniqueId() + ".yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(playerfile);
		if (!playerfile.exists()) {
			cfg.set(Config.points, points);
		} else {
			cfg.set(Config.points, getArenaPoints(p) + points);
		}
		try {
			cfg.save(playerfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getArenaPoints(Player p) {
		File playerfile = new File(Config.pointFilePath + p.getUniqueId() + ".yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(playerfile);
		if (!playerfile.exists()) {
			return 0;
		}
		return cfg.getInt(Config.points);
	}

	public List<String> getPlayersClasses(Player p) {
		List<String> classes = new LinkedList<String>();
		File playerfile = new File(Config.pointFilePath + p.getUniqueId() + ".yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(playerfile);
		if (!playerfile.exists()) {
			return classes;
		}
		for (String s : cfg.getStringList("classes")) {
			classes.add(s);
		}
		return classes;
	}

	public void unlockPlayerClass(Player p, PlayerClass klasse) {
		File playerfile = new File(Config.pointFilePath + p.getUniqueId() + ".yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(playerfile);
		List<String> names = cfg.getStringList("classes");
		names.add(klasse.getName());
		cfg.set("classes", names);
		try {
			cfg.save(playerfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}