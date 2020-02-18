package de.dal3x.mobarena.boss;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import de.dal3x.mobarena.arena.Arena;
import de.dal3x.mobarena.main.MobArenaPlugin;

public abstract class MinionBoss extends AbstractBoss implements Listener {

	protected List<Mob> minions;
	
	protected MinionBoss() {
		this.minions = new LinkedList<Mob>();
		MobArenaPlugin.getInstance().getServer().getPluginManager().registerEvents(this, MobArenaPlugin.getInstance());
	}
	
	protected void clearMinions() {
		List<Mob> toKill = new LinkedList<Mob>();
		for (Mob minion : this.minions) {
			toKill.add(minion);
		}
		for (Mob kill : toKill) {
			kill.setHealth(0);
		}
	}

	protected void addToMinions(Mob minion, Arena a) {
		a.addActiveMob(minion);
		this.minions.add(minion);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onMinionDeath(EntityDeathEvent event) {
		LivingEntity m = event.getEntity();
		if (this.minions.contains(m)) {
			if (!(m instanceof Slime)) {
				this.minions.remove(m);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onBossDeath(EntityDeathEvent event) {
		if (event.getEntity().equals(this.bossInstance)) {
			event.setDroppedExp(0);
			event.getDrops().clear();
			clearMinions();
			return;
		}
	}

	protected void removeMinion(Mob minion) {
		this.minions.remove(minion);
	}
}
