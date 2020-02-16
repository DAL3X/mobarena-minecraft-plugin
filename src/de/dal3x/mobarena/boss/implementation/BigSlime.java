package de.dal3x.mobarena.boss.implementation;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.SlimeSplitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import de.dal3x.mobarena.arena.Arena;
import de.dal3x.mobarena.boss.Boss;
import de.dal3x.mobarena.main.MobArenaPlugin;

public class BigSlime implements Boss, Listener {

	private Arena arena;
	private Mob mob;
	private List<Mob> minions;
	private final String name = "§6§lBig§c§lSlime";

	public BigSlime() {
		this.minions = new LinkedList<Mob>();
		MobArenaPlugin.getInstance().getServer().getPluginManager().registerEvents(this, MobArenaPlugin.getInstance());
	}

	public Mob spawn(Location loc, Arena arena) {
		this.arena = arena;
		Slime bigSlime = (Slime) loc.getWorld().spawnEntity(loc, EntityType.SLIME);
		bigSlime.setSize(7);
		bigSlime.setCustomName(this.name);
		bigSlime.setCustomNameVisible(true);
		this.mob = bigSlime;
		return bigSlime;
	}

	public String getBossName() {
		return this.name;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onBigSlimeDmg(EntityDamageByEntityEvent event) {
		if (event.getDamager().equals(this.mob)) {
			// Slime makes Dmg
			event.setDamage(event.getDamage() * 0.4);
			((LivingEntity) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 1), true);
			return;
		}
		if (event.getEntity().equals(this.mob)) {
			// Slime gets Dmg
			for (Player p : arena.getParticipants()) {
				if (p.getLocation().distance(mob.getLocation()) <= 5) {
					Vector vec = p.getLocation().toVector().subtract(mob.getLocation().toVector());
					p.setVelocity(vec.normalize().multiply(5 / vec.length()).multiply(0.3));
				}
			}
			// Spawn little slime
			Slime slime = (Slime) this.mob.getLocation().getWorld().spawnEntity(this.mob.getLocation(),
					EntityType.SLIME);
			slime.setSize(2);
			slime.setCustomName("§cSlime");
			slime.setCustomNameVisible(true);
			Vector vec = event.getDamager().getLocation().toVector().subtract(mob.getLocation().toVector()).normalize()
					.multiply(0.6);
			slime.setVelocity(vec);
			this.minions.add(slime);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onDeath(EntityDeathEvent event) {
		LivingEntity m = event.getEntity();
		if (m.equals(this.mob)) {
			// Big slime died
			event.setDroppedExp(0);
			event.getDrops().clear();
			List<Mob> toKill = new LinkedList<Mob>();
			for (Mob minion : this.minions) {
				toKill.add(minion);
			}
			for (Mob kill : toKill) {
				kill.setHealth(0);
			}
			((Slime) this.mob).setSize(0);
			return;
		}
		if (this.minions.contains(m)) {
			// Mini slime died
			event.setDroppedExp(0);
			event.getDrops().clear();
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onLitteSlimeDmg(EntityDamageByEntityEvent event) {
		if (this.minions.contains(event.getDamager())) {
			// Little slime made dmg
			if (event.getEntity() instanceof Player) {
				Player p = (Player) event.getEntity();
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 2), true);
				((LivingEntity) event.getDamager()).setHealth(0);
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onBigSlimeSplit(SlimeSplitEvent event) {
		if (event.getEntity().equals(this.mob)) {
			event.setCancelled(true);
			return;
		}
		if(this.minions.contains(event.getEntity())) {
			this.minions.remove(event.getEntity());
			event.setCancelled(true);
		}
	}

}