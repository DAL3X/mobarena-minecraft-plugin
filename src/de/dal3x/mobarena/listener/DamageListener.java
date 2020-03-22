package de.dal3x.mobarena.listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

import de.dal3x.mobarena.arena.Arena;
import de.dal3x.mobarena.config.Config;

public class DamageListener implements Listener {

	private Arena arena;

	public DamageListener(Arena arena) {
		this.arena = arena;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onArenaMobDamagesArenaMob(EntityDamageByEntityEvent event) {
		if (!this.arena.getActiveMobs().contains(event.getEntity())) {
			return;
		}
		// Getroffener Mob is in Arena
		if (this.arena.getActiveMobs().contains(event.getDamager())) {
			// Mob schlaegt Mob
			event.setDamage(0);
			event.setCancelled(true);
		}
		if (event.getDamager() instanceof Projectile) {
			Projectile p = (Projectile) event.getDamager();
			if (this.arena.getActiveMobs().contains(p.getShooter())) {
				// Mob schieﬂt auf Mob
				event.setDamage(0);
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onArenaMobDamagesPlayer(EntityDamageByEntityEvent event) {
		if (event.isCancelled()) {
			return;
		}
		if (!(event.getEntity() instanceof Player)) {
			return;
		}
		Player p = (Player) event.getEntity();
		if (!this.arena.isParticipant(p)) {
			return;
		}
		event.setDamage(event.getDamage() * (1 + Config.damageTakenMultiPerWave * arena.getWaveCounter()));
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerDamagesArena(EntityDamageByEntityEvent event) {
		if (event.isCancelled()) {
			return;
		}
		if (!(event.getDamager() instanceof Player) && !(event.getDamager() instanceof Projectile)) {
			return;
		}
		Player p = null;
		if (event.getDamager() instanceof Projectile) {
			ProjectileSource shooter = ((Projectile) event.getDamager()).getShooter();
			if (shooter instanceof Player) {
				p = (Player) shooter;
			} else {
				return;
			}
		} else {
			p = (Player) event.getDamager();
		}
		if (!this.arena.isParticipant(p)) {
			return;
		}
		if (!(event.getEntity() instanceof Mob)) {
			return;
		}
		Mob m = (Mob) event.getEntity();
		for (Entity ent : m.getNearbyEntities(5, 5, 5)) {
			if (ent instanceof Mob) {
				Mob near = (Mob) ent;
				if (near.getTarget() == null) {
					near.setTarget(p);
				}
			}
		}

	}

}
