package de.dal3x.mobarena.arena;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import de.dal3x.mobarena.boss.BossStorage;
import de.dal3x.mobarena.classes.ClassController;
import de.dal3x.mobarena.config.Config;
import de.dal3x.mobarena.file.Filehandler;
import de.dal3x.mobarena.listener.ClassPickListener;
import de.dal3x.mobarena.listener.DamageListener;
import de.dal3x.mobarena.listener.DeathListener;
import de.dal3x.mobarena.listener.LeaveListener;
import de.dal3x.mobarena.listener.SkillListener;
import de.dal3x.mobarena.main.MobArenaPlugin;
import de.dal3x.mobarena.output.IngameOutput;
import de.dal3x.mobarena.utility.InventoryStorage;
import de.dal3x.mobarena.utility.QueueController;
import de.dal3x.mobarena.wave.Mobwave;
import de.dal3x.mobarena.wave.MobwaveController;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Arena {

	private Location lobby;
	private Location spectate;
	private Location spawnLocation;
	private Location bossLocation;
	private String name;
	private List<Location> mobspawns;
	private List<Location> playerspawn;
	private List<Mobwave> waves;
	private List<Player> participants;
	private List<Player> spectator;
	private HashMap<Player, Integer> arenaPoints;
	private boolean isFree;
	private int waveCounter;
	private int numberOfCurrentWave;
	private MobwaveController controller;
	private QueueController queue;
	private MobArenaPlugin plugin;
	private List<Mob> activeMobs;
	private final int bossWaveDivi = 10;
	private boolean running;
	private Mob activeBoss;

	public Arena(String name, Location lobby, Location spectate, Location spawn, Location bossLocation, List<Location> mobspawns,
			List<Location> playerspawn, List<Mobwave> waves) {
		this.name = name;
		this.spectate = spectate;
		this.lobby = lobby;
		this.spawnLocation = spawn;
		this.mobspawns = mobspawns;
		this.playerspawn = playerspawn;
		this.waves = waves;
		this.bossLocation = bossLocation;
		this.plugin = MobArenaPlugin.getInstance();
		this.controller = MobwaveController.getInstance();
		this.participants = new LinkedList<Player>();
		this.setActiveMobs(new LinkedList<Mob>());
		this.setQueue(new QueueController());
		this.spectator = new LinkedList<Player>();
		this.arenaPoints = new HashMap<Player, Integer>();
		this.waveCounter = 1;
		this.numberOfCurrentWave = 0;
		new SkillListener(this, plugin);
		isFree = true;
		running = false;
		registerListeners();
	}

	public void start() {
		this.running = true;
		this.isFree = false;
		for (Player p : this.queue.getReadyQueue()) {
			addParticipant(p);
			Location spawnLoc = this.playerspawn.get(new Random().nextInt(playerspawn.size()));
			p.teleport(spawnLoc);
		}
		this.queue.flushJoinedQueue();
		this.queue.flushReadyQueue();
		spawnNextWave();
	}

	private Mobwave getNextWave() {
		if (this.numberOfCurrentWave == 0) {
			return this.waves.get(new Random().nextInt(this.waves.size()));
		} else {
			List<Mobwave> possible = new LinkedList<Mobwave>();
			possible.addAll(this.waves);
			possible.remove(MobwaveController.getInstance().getMobwave(this.numberOfCurrentWave));
			return possible.get(new Random().nextInt(possible.size()));
		}
	}

	public void spawnNextWave() {
		// Bosswave
		if ((waveCounter % bossWaveDivi == 0) || this.waves.size() == 0) {
			spawnInBoss();
		}
		// Normale Wave
		else {
			spawnInNormalWave();
		}
	}

	private void spawnInNormalWave() {
		for (Player p : getParticipants()) {
			p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(IngameOutput.wave + getWaveCounter()));
		}
		final Mobwave wave = getNextWave();
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			public void run() {
				if (isRunning()) {
					numberOfCurrentWave = wave.getNumber();
					activeMobs = controller.spawnWave(wave, mobspawns, getRevisions());
					for (Mob mob : activeMobs) {
						setScaledMobHealth(mob);
					}
					waveCounter++;
				}
			}
		}, 140);
	}

	private void spawnInBoss() {
		for (Player p : getParticipants()) {
			p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(IngameOutput.boss));
		}
		final Arena instance = this;
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			@SuppressWarnings("deprecation")
			public void run() {
				if (isRunning()) {
					Mob boss = BossStorage.getInstance().spawnRandomBoss(bossLocation, instance);
					boss.setMaxHealth((Config.baseBossHealth
							* (1 + (getWaveCounter() * Config.healtAddMultiPerWave * (getParticipants().size() * Config.bossHealthMultiPerPlayer)))));
					boss.setHealth(boss.getMaxHealth());
					activeMobs.add(boss);
					setActiveBoss(boss);
					waveCounter++;
				}
			}
		}, 140);
	}

	@SuppressWarnings("deprecation")
	public void setScaledMobHealth(Mob mob) {
		mob.setMaxHealth((mob.getMaxHealth() * (1 + (getWaveCounter() * Config.healtAddMultiPerWave))));
		mob.setHealth(mob.getMaxHealth());
	}

	private void registerListeners() {
		plugin.getServer().getPluginManager().registerEvents(new ClassPickListener(this), plugin);
		plugin.getServer().getPluginManager().registerEvents(new DeathListener(this), plugin);
		plugin.getServer().getPluginManager().registerEvents(new LeaveListener(this), plugin);
		plugin.getServer().getPluginManager().registerEvents(new DamageListener(this), plugin);
	}

	public void addParticipant(Player p) {
		if (this.isFree) {
			this.isFree = false;
		}
		p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
		this.participants.add(p);
		setInventory(p);
		ClassController.getInstance().getClassForPlayer(p).activateSkills();
		this.arenaPoints.put(p, 0);
		if (ClassController.getInstance().getClassForPlayer(p).getPassiveSkill() != null) {
			ClassController.getInstance().getClassForPlayer(p).getPassiveSkill().apply(p, this);
		}
	}

	public void removeParticipant(Player p) {
		this.participants.remove(p);
		this.spectator.remove(p);
		p.teleport(this.spawnLocation);
		clearInventory(p);
		p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
		Filehandler.getInstance().addArenaPoints(p, this.arenaPoints.get(p));
		IngameOutput.sendGloryGainMessage(p, this.arenaPoints.get(p));
		this.arenaPoints.remove(p);
		if (ClassController.getInstance().getClassForPlayer(p).getPassiveSkill() != null) {
			ClassController.getInstance().getClassForPlayer(p).getPassiveSkill().disapply(p, this);
		}
		for (PotionEffect effect : p.getActivePotionEffects()) {
			p.removePotionEffect(effect.getType());
		}
		ClassController.getInstance().clearClassForPlayer(p);
		if (this.participants.size() == 0) {
			reset();
		}
	}

	private int getRevisions() {
		return this.participants.size() * Math.round(Config.multiSpawnForPlayer);
	}

	public List<Player> getParticipants() {
		return this.participants;
	}

	public List<Player> getAliveParticipants() {
		List<Player> alive = new LinkedList<Player>();
		for (Player p : this.participants) {
			if (!this.spectator.contains(p)) {
				alive.add(p);
			}
		}
		return alive;
	}

	public void sendMessageToAllParticipants(String message) {
		for (Player p : this.participants) {
			p.sendMessage(message);
		}
	}

	public void reset() {
		this.running = false;
		this.activeBoss = null;
		for (Mob mob : this.activeMobs) {
			mob.setHealth(0);
		}
		int count = this.waveCounter - 1;
		IngameOutput.sendDefeatMessage(count, this.participants);
		for (Player p : this.participants) {
			removeParticipant(p);
		}
		this.activeMobs.clear();
		this.spectator.clear();
		this.participants.clear();
		this.arenaPoints.clear();
		this.isFree = true;
		this.waveCounter = 1;
	}

	public boolean removeMobAndAskIfEmpty(Mob mob, Player killer) {
		this.activeMobs.remove(mob);
		if (this.activeBoss != null) {
			if (this.activeBoss.equals(mob)) {
				if (killer != null) {
					addBossPoints();
				}
				this.activeBoss = null;
				return true;
			}
			return false;
		}
		if (killer != null) {
			addMobPoints(killer);
		}
		if (this.activeMobs.size() == 0) {
			return true;
		} else {
			IngameOutput.sendRemainingMobs(this.activeMobs.size(), this.controller.getMobwave(this.numberOfCurrentWave).getMobs().size(),
					this.participants);
			return false;
		}
	}

	public void addToSpectator(Player p) {
		this.spectator.add(p);
		p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
		if (this.spectator.size() == this.participants.size()) {
			reset();
		} else {
			p.teleport(this.spectate);
		}
	}

	public void respawnAllSpectators() {
		for (Player p : this.spectator) {
			p.teleport(getPlayerspawn().get(new Random().nextInt(getPlayerspawn().size())));
			p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
			this.spectator.remove(p);
		}
	}

	private void setInventory(Player p) {
		InventoryStorage.getInstance().saveInventory(p);
		p.getInventory().clear();
		ClassController.getInstance().equipPlayer(p);
		p.updateInventory();
	}

	private void clearInventory(Player p) {
		p.getInventory().setContents(InventoryStorage.getInstance().retrieveInventory(p));
		p.updateInventory();
	}

	public boolean isRunning() {
		return this.running;
	}

	public Location getLobby() {
		return lobby;
	}

	public void setLobby(Location lobby) {
		this.lobby = lobby;
	}

	public void setLobbyCoords(World w, int x, int y, int z) {
		Location loc = new Location(w, x, y, z);
		this.lobby = loc;
	}

	public Location getSpectate() {
		return spectate;
	}

	public void setSpectate(Location spectate) {
		this.spectate = spectate;
	}

	public void setSpectateCoords(World w, int x, int y, int z) {
		Location loc = new Location(w, x, y, z);
		this.spectate = loc;
	}

	public List<Location> getMobspawns() {
		return mobspawns;
	}

	public void setMobspawns(List<Location> mobspawns) {
		this.mobspawns = mobspawns;
	}

	public List<Location> getPlayerspawn() {
		return playerspawn;
	}

	public void setPlayerspawn(List<Location> playerspawn) {
		this.playerspawn = playerspawn;
	}

	public boolean isParticipant(Player p) {
		return this.participants.contains(p);
	}

	public boolean isFree() {
		return this.isFree;
	}

	public int getCurrentWaveNumber() {
		return this.numberOfCurrentWave;
	}

	public int getWaveCounter() {
		return this.waveCounter;
	}

	public void addMobwave(Mobwave wave) {
		this.waves.add(wave);
	}

	public List<Mob> getActiveMobs() {
		return activeMobs;
	}

	public void setActiveMobs(List<Mob> activeMobs) {
		this.activeMobs = activeMobs;
	}

	public void addActiveMob(Mob mob) {
		this.activeMobs.add(mob);
	}

	public QueueController getQueue() {
		return queue;
	}

	public void setQueue(QueueController queue) {
		this.queue = queue;
	}

	public void addToJoinQueue(Player p) {
		this.queue.putInJoinedQueue(p);
	}

	public String getName() {
		return name;
	}

	public Location getSpawnLocation() {
		return spawnLocation;
	}

	public void setSpawnLocation(Location spawnLocation) {
		this.spawnLocation = spawnLocation;
	}

	public Mob getActiveBoss() {
		return activeBoss;
	}

	public boolean isActiveBoss(Mob mob) {
		if (mob.equals(this.activeBoss)) {
			return true;
		}
		return false;
	}

	public void setActiveBoss(Mob boss) {
		this.activeBoss = boss;
	}

	public void addMobPoints(Player p) {
		this.arenaPoints.put(p, this.arenaPoints.get(p) + Config.pointPerMobkill);
	}

	public void addBossPoints() {
		for (Player p : this.arenaPoints.keySet()) {
			this.arenaPoints.put(p, this.arenaPoints.get(p) + Config.pointPerBosskill);
		}
	}

	public void addWavePoints() {
		for (Player p : this.arenaPoints.keySet()) {
			this.arenaPoints.put(p, this.arenaPoints.get(p) + Config.pointPerWave);
		}
	}

}