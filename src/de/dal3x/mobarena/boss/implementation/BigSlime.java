package de.dal3x.mobarena.boss.implementation;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.SlimeSplitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import de.dal3x.mobarena.arena.Arena;
import de.dal3x.mobarena.boss.MinionBoss;

public class BigSlime extends MinionBoss {

	public BigSlime(Arena arena) {
		super("§6§lBig§c§lSlime", arena);
	}

	@SuppressWarnings("deprecation")
	public Mob spawn(Location loc) {
		Slime bigSlime = (Slime) loc.getWorld().spawnEntity(loc, EntityType.SLIME);
		bigSlime.setSize(8);
		bigSlime.setCustomName(this.name);
		bigSlime.setCustomNameVisible(true);
		bigSlime.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 0), true);
		bigSlime.setPersistent(true);
		this.bossInstance = bigSlime;
		this.addBossBar();
		return bigSlime;
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.LOWEST)
	public void onBigSlimeDmg(EntityDamageByEntityEvent event) {
		if (event.getDamager().equals(this.bossInstance)) {
			// Slime makes Dmg
			event.setDamage(event.getDamage() * 0.8);
			return;
		}
		if (event.getEntity().equals(this.bossInstance)) {
			// Slime gets Dmg
			for (Player p : arena.getAliveParticipants()) {
				if (p.getLocation().distance(bossInstance.getLocation()) <= 10) {
					Vector vec = p.getLocation().toVector().subtract(bossInstance.getLocation().toVector());
					p.setVelocity(vec.normalize().multiply(5 / vec.length()).multiply(0.3));
				}
			}
			// Spawn little slime
			Slime slime = (Slime) this.bossInstance.getLocation().getWorld().spawnEntity(this.bossInstance.getLocation(), EntityType.SLIME);
			slime.setSize(2);
			slime.setCustomName("§cSlime");
			slime.setCustomNameVisible(true);
			slime.setPersistent(true);
			if (event.getDamager() instanceof Projectile) {
				Vector vec = event.getDamager().getLocation().toVector().subtract(bossInstance.getLocation().toVector()).multiply(0.45);
				slime.setVelocity(vec);
			} else {
				Vector vec = event.getDamager().getLocation().toVector().subtract(bossInstance.getLocation().toVector()).normalize();
				slime.setVelocity(vec);
			}
			addToMinions(slime, this.arena);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onLitteSlimeDmg(EntityDamageByEntityEvent event) {
		if (this.minions.contains(event.getDamager())) {
			// Little slime made dmg
			if (event.getEntity() instanceof Player) {
				Player p = (Player) event.getEntity();
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 3), true);
				p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 1), true);
				((LivingEntity) event.getDamager()).setHealth(0);
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onSlimeSplit(SlimeSplitEvent event) {
		if (event.getEntity().equals(this.bossInstance)) {
			event.setCancelled(true);
			return;
		}
		if (this.minions.contains(event.getEntity())) {
			removeMinion(event.getEntity());
			event.setCancelled(true);
		}
	}

}