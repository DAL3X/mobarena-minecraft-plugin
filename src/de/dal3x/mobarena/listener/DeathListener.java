package de.dal3x.mobarena.listener;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;

import de.dal3x.mobarena.arena.Arena;
import de.dal3x.mobarena.main.MobArenaPlugin;

public class DeathListener implements Listener {

	private Arena arena;

	public DeathListener(Arena arena) {
		this.arena = arena;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onArenaMobDeath(EntityDeathEvent event) {
		if (!(event.getEntity() instanceof Mob) || event.getEntity() instanceof Player) {
			return;
		}
		final Mob mob = (Mob) event.getEntity();
		if (arena.getActiveMobs().contains(mob) && mob instanceof Slime && ((Slime) mob).getSize() > 1 && arena.getActiveBoss() == null) {
			event.setDroppedExp(0);
			event.getDrops().clear();
			((Slime) mob).setSize(1);
			mob.setHealth(0);
		}
		if (arena.getActiveMobs().contains(mob)) {
			event.setDroppedExp(0);
			event.getDrops().clear();
			if (!arena.isRunning()) {
				return;
			}
			final Player killer;
			if (event.getEntity().getKiller() instanceof Projectile) {
				killer = (Player) ((Projectile) event.getEntity().getKiller()).getShooter();
			} else {
				killer = event.getEntity().getKiller();
			}
			Bukkit.getScheduler().runTask(MobArenaPlugin.getInstance(), new Runnable() {
				public void run() {
					ItemStack item = getPotion();
					if (item != null && killer != null) {
						ItemStack potion = getPotion();
						if (potion != null) {
							killer.getInventory().addItem(potion);
						}
					}
					boolean waveDone = arena.removeMobAndAskIfEmpty(mob, killer);
					if (waveDone) {
						arena.addWavePoints();
						arena.respawnAllSpectators();
						arena.spawnNextWave();
					}
				}
			});
		}
	}

	// Backup Event, if mobs are removed for whatever reason
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onArenaMobRemove(EntityRemoveFromWorldEvent event) {
		if (!(event.getEntity() instanceof Mob) || event.getEntity() instanceof Player) {
			return;
		}
		Mob mob = (Mob) event.getEntity();
		if (arena.getActiveMobs().contains(mob)) {
			if (!arena.isRunning()) {
				return;
			}
			boolean waveDone = arena.removeMobAndAskIfEmpty(mob, arena.getAliveParticipants().get(0));
			if (waveDone) {
				arena.addWavePoints();
				arena.respawnAllSpectators();
				arena.spawnNextWave();
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onArenaPlayerDeath(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player)) {
			return;
		}
		Player p = (Player) event.getEntity();
		if (!arena.isParticipant(p)) {
			return;
		}
		if (event.getDamager() instanceof Player) {
			return;
		}
		if ((p.getHealth() - event.getFinalDamage()) <= 0) {
			event.setDamage(0);
			event.setCancelled(true);
			arena.addToSpectator(p);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onArenaPlayerNaturalDeath(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player)) {
			return;
		}
		Player p = (Player) event.getEntity();
		if (!arena.isParticipant(p)) {
			return;
		}
		if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK
				|| event.getCause() == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) {
			return;
		}
		if (!arena.isSpectator(p)) {
			if ((p.getHealth() - event.getFinalDamage()) <= 0) {
				event.setDamage(0);
				event.setCancelled(true);
				arena.addToSpectator(p);
			}
		} else {
			event.setCancelled(true);
		}
	}

	private ItemStack getPotion() {
		if ((new Random().nextInt(160) - arena.getWaveCounter()) <= 0) {
			ItemStack potion = new ItemStack(Material.POTION);
			PotionMeta meta = (PotionMeta) potion.getItemMeta();
			int rand = new Random().nextInt(3);
			switch (rand) {
			case 0:
				meta.addCustomEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1), true);
				break;
			case 1:
				meta.addCustomEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 400, 2), true);
				break;
			case 2:
				meta.addCustomEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 300, 1), true);
				break;
			}
			potion.setItemMeta(meta);
			return potion;
		}
		return null;
	}

}
