package de.dal3x.mobarena.boss.implementation;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Spider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import de.dal3x.mobarena.arena.Arena;
import de.dal3x.mobarena.boss.MinionBoss;
import de.dal3x.mobarena.config.Config;
import de.dal3x.mobarena.main.MobArenaPlugin;
import de.dal3x.mobarena.utility.TempBlockChanger;

public class BroodMother extends MinionBoss {

	private TempBlockChanger blockChanger;

	public BroodMother(Arena arena) {
		super("§1§lBrut§3§lMutter", arena);
		this.blockChanger = new TempBlockChanger();
	}

	public Mob spawn(Location loc) {
		Spider mother = (Spider) loc.getWorld().spawnEntity(loc, EntityType.SPIDER);
		mother.setGlowing(true);
		mother.setTarget(getNearestPlayer(loc));
		startWebSequence(mother.getLocation());
		startSpawnSequence();
		mother.setCustomName(name);
		mother.setCustomNameVisible(true);
		bossInstance = mother;
		return mother;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onMotherDeath(EntityDeathEvent event) {
		if (event.getEntity().equals(bossInstance)) {
			blockChanger.resetAllBlocks();
		}
	}

	@Override
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onMinionDeath(EntityDeathEvent event) {
		if (this.minions.contains(event.getEntity())) {
			if (bossInstance.getHealth() > 0) {
				blockChanger.setSingleBlock(event.getEntity().getLocation(), Material.COBWEB);
				minions.remove(event.getEntity());
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onCobWebClickedEvent(PlayerInteractEvent event) {
		if (arena.getParticipants().contains(event.getPlayer())) {
			if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if (event.getClickedBlock().getType().equals(Material.COBWEB)) {
					if (bossInstance.getHealth() > 0) {
						blockChanger.resetSingleBlock(event.getClickedBlock().getLocation());
					}
				}
			}
		}
	}

	private void startSpawnSequence() {
		Bukkit.getScheduler().runTaskLater(MobArenaPlugin.getInstance(), new Runnable() {
			public void run() {
				if (bossInstance.getHealth() > 0 && arena.getActiveBoss().equals(bossInstance)) {
					clearMinions();
					spawnMinions(bossInstance.getLocation());
					bossInstance.setTarget(getNearestPlayer(bossInstance.getLocation()));
					startSpawnSequence();
				}
			}
		}, Config.BroodMotherSpawnCD * 20);
	}

	private void startWebSequence(Location loc) {
		final Location old = loc;
		Bukkit.getScheduler().runTaskLater(MobArenaPlugin.getInstance(), new Runnable() {
			public void run() {
				if (bossInstance.getHealth() > 0 && arena.getActiveBoss().equals(bossInstance)) {
					for (int i = 0; i < Config.BroodMotherWebPerCycle; i++) {
						blockChanger.setSingleBlock(getWebPosition(old), Material.COBWEB);
					}
					startWebSequence(bossInstance.getLocation());
				}
			}
		}, (long) (Config.BroodMotherWebCD * 20));
	}

	private void spawnMinions(Location loc) {
		List<Mob> newMinions = new LinkedList<Mob>();
		int missingSpawns = Config.BroodMotherMinionPerPlayer * arena.getParticipants().size() - minions.size();
		for (int i = 0; i < missingSpawns; i++) {
			Mob m = (Mob) loc.getWorld().spawnEntity(getMinionSpawnPosition(i, loc), EntityType.CAVE_SPIDER);
			m.setCustomName("§2Gift§3Spinne");
			m.setCustomNameVisible(true);
			addToMinions(m, arena);
			newMinions.add(m);
		}
		setTargetsToAllPlayer(newMinions);
	}

	private Location getMinionSpawnPosition(int counter, Location loc) {
		int mod = counter % 4;
		switch (mod) {
		case 0:
			return loc.add(1, 1, 0);
		case 1:
			return loc.add(-1, 1, 0);
		case 2:
			return loc.add(0, 1, 1);
		case 3:
			return loc.add(0, 1, -1);
		}
		return loc;
	}

	private Location getWebPosition(Location loc) {
		Random rand = new Random();
		return loc.add(rand.nextInt(7) - 3, rand.nextInt(3) - 1, rand.nextInt(7) - 3);
	}

	private void setTargetsToAllPlayer(List<Mob> mobs) {
		Collections.shuffle(mobs);
		for (int i = 0; i < mobs.size(); i++) {
			mobs.get(i).setTarget(arena.getParticipants().get(i % arena.getParticipants().size()));
		}
	}

	private Player getNearestPlayer(Location loc) {
		Player nearest = null;
		for (Player p : arena.getParticipants()) {
			if (nearest == null) {
				nearest = p;
			}
			if (nearest.getLocation().distance(loc) > p.getLocation().distance(loc)) {
				nearest = p;
			}
		}
		return nearest;
	}

}
