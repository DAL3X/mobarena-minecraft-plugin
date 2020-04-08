package de.dal3x.mobarena.boss.implementation;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Evoker;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Vex;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.dal3x.mobarena.arena.Arena;
import de.dal3x.mobarena.boss.MinionBoss;
import de.dal3x.mobarena.config.Config;
import de.dal3x.mobarena.main.MobArenaPlugin;

public class Shuffler extends MinionBoss implements Listener {

	public Shuffler(Arena arena) {
		super("§a§lShuf§e§lfler", arena);
	}

	@SuppressWarnings("deprecation")
	public Mob spawn(Location loc) {
		Evoker shuffler = (Evoker) loc.getWorld().spawnEntity(loc, EntityType.EVOKER);
		shuffler.setPatrolLeader(false);
		shuffler.getEquipment().setHelmet(null);
		shuffler.setCustomName(this.name);
		shuffler.setCustomNameVisible(true);
		shuffler.setPersistent(true);
		shuffler.setRemoveWhenFarAway(false);
		this.bossInstance = shuffler;
		this.addBossBar();
		shufflePlayers();
		startPlayerShuffleSequence();
		startCloneSequence();
		return shuffler;
	}

	private void startPlayerShuffleSequence() {
		Bukkit.getScheduler().runTaskLater(MobArenaPlugin.getInstance(), new Runnable() {
			public void run() {
				if (bossInstance.getHealth() > 0 && arena.getActiveBoss().getMobInstance().equals(bossInstance)) {
					shufflePlayers();
					startPlayerShuffleSequence();
				}
			}
		}, Config.ShufflerPlayerCD * 20);
	}

	private void startCloneSequence() {
		Bukkit.getScheduler().runTaskLater(MobArenaPlugin.getInstance(), new Runnable() {
			public void run() {
				if (bossInstance.getHealth() > 0 && arena.getActiveBoss().getMobInstance().equals(bossInstance)) {
					spawnClonesAndShuffle();
					startCloneSequence();
				}
			}
		}, Config.ShufflerCloneCD * 20);
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onVexSpawn(CreatureSpawnEvent event) {
		if (!event.getEntityType().equals(EntityType.VEX)) {
			return;
		}
		Vex vex = (Vex) event.getEntity();
		if (this.bossInstance != null) {
			if (this.bossInstance.getHealth() > 0 && this.arena.getActiveBoss().getMobInstance().equals(this.bossInstance)) {
				if (vex.getLocation().getWorld().equals(this.arena.getLobby().getWorld())) {
					if (vex.getLocation().distance(this.bossInstance.getLocation()) < 3) {
						int minionSize = this.minions.size();
						if (minionSize > (arena.getParticipants().size() * 3)) {
							vex.setHealth(0);
							event.setCancelled(true);
							return;
						} else {
							vex.setPersistent(true);
							vex.setRemoveWhenFarAway(false);
							addToMinions(vex, arena);
							return;
						}
					}
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCloneDamage(EntityDamageByEntityEvent event) {
		Entity e = event.getEntity();
		if (this.minions.contains(e) && e.getType().equals(EntityType.EVOKER)) {
			Mob m = (Mob) e;
			m.setHealth(0);
			Player target = null;
			if (event.getDamager() instanceof Player) {
				target = (Player) event.getDamager();
			} else if (event.getDamager() instanceof Projectile) {
				if (((Projectile) event.getDamager()).getShooter() instanceof Player) {
					target = (Player) ((Projectile) event.getDamager()).getShooter();
				}
			}
			if (target != null) {
				target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 80, 0), true);
			}
		}
	}

	@SuppressWarnings("deprecation")
	private void spawnClonesAndShuffle() {
		List<Location> spawnlocs = new LinkedList<Location>();
		for (Location loc : arena.getMobspawns()) {
			spawnlocs.add(loc);
		}
		Collections.shuffle(spawnlocs);
		World w = bossInstance.getWorld();
		bossInstance.teleport(spawnlocs.get(new Random().nextInt(spawnlocs.size())));
		for (int i = 1; i < (arena.getParticipants().size() * Config.ShufflerClonePerPlayer); i++) {
			Mob clone = (Mob) w.spawnEntity(spawnlocs.get(i % spawnlocs.size()), EntityType.EVOKER);
			((Evoker) clone).setPatrolLeader(false);
			((Evoker) clone).getEquipment().setHelmet(null);
			clone.setMaxHealth(bossInstance.getMaxHealth());
			clone.setHealth(bossInstance.getMaxHealth());
			clone.setFireTicks(bossInstance.getFireTicks());
			clone.addPotionEffects(bossInstance.getActivePotionEffects());
			clone.setCustomName(this.name);
			clone.setCustomNameVisible(true);
			clone.setPersistent(true);
			clone.setRemoveWhenFarAway(false);
			addToMinions(clone, this.arena);
		}
	}

	private void shufflePlayers() {
		List<Player> shuffle = new LinkedList<Player>();
		for (Player p : arena.getAliveParticipants()) {
			shuffle.add(p);
		}
		Collections.shuffle(shuffle);
		List<Location> shuffleLocations = new LinkedList<Location>();
		for (Player p : shuffle) {
			shuffleLocations.add(p.getLocation().clone());
		}
		int i = 0;
		while ((i + 1) < shuffle.size()) {
			shuffle.get(i).teleport(shuffleLocations.get(i + 1));
			i++;
		}
		shuffle.get(i).teleport(shuffleLocations.get(0));
	}

}
