package de.dal3x.mobarena.listener;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
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
		Mob mob = (Mob) event.getEntity();
		if (arena.getActiveMobs().contains(mob)) {
			event.setDroppedExp(0);
			event.getDrops().clear();
			if (!arena.isRunning()) {
				return;
			}
			event.getDrops().addAll(getDrops(event));
			Player killer = event.getEntity().getKiller();
			if (event.getEntity().getKiller() instanceof Projectile) {
				killer = (Player) ((Projectile) event.getEntity().getKiller()).getShooter();
			}
			boolean waveDone = arena.removeMobAndAskIfEmpty(mob, killer);
			if (waveDone) {
				arena.addWavePoints();
				arena.respawnAllSpectators();
				arena.spawnNextWave();
			}
		}
		if (arena.getActiveBoss() != null && mob.equals(arena.getActiveBoss().getMobInstance())) {
			event.setDroppedExp(0);
			event.getDrops().clear();
			return;
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
		if ((p.getHealth() - event.getDamage()) <= 0) {
			event.setDamage(0);
			event.setCancelled(true);
			addToSpec(p);
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
		if (!arena.isSpectator(p)) {
			if ((p.getHealth() - event.getDamage()) <= 0) {
				event.setDamage(0);
				event.setCancelled(true);
				addToSpec(p);
			}
		}
	}

	private List<ItemStack> getDrops(EntityDeathEvent event) {
		List<ItemStack> list = new LinkedList<ItemStack>();
		if ((new Random().nextInt(400) - arena.getWaveCounter()) <= 0) {
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
			list.add(potion);
		}
		return list;
	}

	private void addToSpec(final Player p) {
		arena.addToSpectator(p);
	}

}
