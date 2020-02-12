package de.dal3x.mobarena.config;

public class Config {
	
	public static final String dirPath = "plugins/MobArena/";
	public static final String itemFileName = "items.yml";
	public static final String mobFileName = "mobs.yml";
	public static final String classFileName = "classes.yml";
	public static final String waveFileName = "waves.yml";
	public static final String arenaFileName = "arenas.yml";
	public static final String pointFilePath = dirPath + "Punkte/";
	public static final String points = "points";
	
	public static final String joinCommand = "join";
	public static final String leaveCommand = "leave";
	public static final String reloadCommand = "reload";
	public static final String listCommand = "list";
	public static final String forceStartCommand = "force";
	
	public static final String reloadPerm = "soulcraft.mobarena";
	
	public static final String signClassIdentifier = "�8[�3Mob�bArena�8]";
	public static final String signReadyIdentifier = "Bereit setzen";
	
	public static final float multiSpawnForPlayer = 0.8f;
	
	public static final double healtAddMultiPerWave = 0.05;
	public static final double bossHealthMultiPerPlayer = 3;
	
	public static final double damageTakenMultiPerWave = 0.03;
	
	public static final int pointPerMobkill = 1;
	public static final int pointPerBosskill = 50;
	public static final int pointPerWave = 8;
	
}
