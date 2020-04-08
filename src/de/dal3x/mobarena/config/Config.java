package de.dal3x.mobarena.config;

public class Config {
	
	public static final String dirPath = "plugins/MobArena/";
	public static final String itemFileName = "items.yml";
	public static final String mobFileName = "mobs.yml";
	public static final String classFileName = "classes.yml";
	public static final String waveFileName = "waves.yml";
	public static final String arenaFileName = "arenas.yml";
	public static final String highscoreFileName = "highscore.yml";
	public static final String pointFilePath = dirPath + "PlayerData/";
	public static final String points = "points";
	
	public static final String joinCommand = "join";
	public static final String leaveCommand = "leave";
	public static final String reloadCommand = "reload";
	public static final String listCommand = "list";
	public static final String gloryCommand = "glory";
	public static final String gloryAlias = "ruhm";
	public static final String highscoreCommand = "highscore";
	
	public static final String reloadPerm = "soulcraft.mobarena.admin";
	
	public static final String signClassIdentifier = "§8[§3Mob§bArena§8]";
	public static final String signReadyIdentifier = "Ready";
	
	public static final float multiSpawnForPlayer = 0.7f;
	
	public static final double healtAddMultiPerWave = 0.03;
	public static final double bossHealthMultiPerPlayer = 2;
	public static final double baseBossHealth = 50;
	
	public static final double damageTakenMultiPerWave = 0.015;
	
	public static final int pointPerMobkill = 1;
	public static final int pointPerBosskill = 50;
	public static final int pointPerWave = 8;
	
	public static final int LichLordSpawnCD = 30; // In seconds
	public static final int LichLordMinionPerPlayer = 3;
	public static final int LichLordDebuffCD = 25; // In seconds
	
	public static final int BroodMotherSpawnCD = 10; // In seconds
	public static final double BroodMotherWebCD = 5; // In seconds
	public static final int BroodMotherWebPerCycle = 70; //In reality less, bc some blocks are blocked 
	public static final int BroodMotherMinionPerPlayer = 5;
	
	public static final int ShufflerPlayerCD = 18;
	public static final int ShufflerCloneCD = 22;
	public static final int ShufflerClonePerPlayer = 2;
	
}
