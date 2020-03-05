package de.dal3x.mobarena.boss;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import de.dal3x.mobarena.arena.Arena;
import de.dal3x.mobarena.main.MobArenaPlugin;

public abstract class AbstractBoss implements IBoss, Listener {

	protected Mob bossInstance;
	protected String name;
	protected Arena arena;
	protected BossBar bar;

	protected AbstractBoss(String name, Arena arena) {
		this.name = name;
		this.arena = arena;
		MobArenaPlugin.getInstance().getServer().getPluginManager().registerEvents(this, MobArenaPlugin.getInstance());
	}

	public String getBossName() {
		return this.name;
	}

	public Mob getMobInstance() {
		return this.bossInstance;
	}

	@SuppressWarnings("deprecation")
	public void addBossBar() {
		this.bar = Bukkit.getServer().createBossBar(name, BarColor.RED, BarStyle.SEGMENTED_20, BarFlag.DARKEN_SKY);
		bar.setVisible(true);
		bar.setProgress((1 / bossInstance.getMaxHealth()) * bossInstance.getHealth());
		for (Player p : arena.getParticipants()) {
			bar.addPlayer(p);
		}
	}

	@SuppressWarnings("deprecation")
	public void updateBossBar(double damage) {
		this.bar.setProgress((1 / bossInstance.getMaxHealth()) * (bossInstance.getHealth() - damage));
	}

	public void removePlayerFromBossBar(Player p) {
		if (this.bar != null) {
			this.bar.removePlayer(p);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onBossDamage(EntityDamageEvent event) {
		if (event.getEntity().equals(this.bossInstance)) {
			if (this.bossInstance.getHealth() - event.getDamage() <= 0) {
				for (Player p : arena.getParticipants()) {
					removePlayerFromBossBar(p);
				}
			} else {
				updateBossBar(event.getDamage());
			}
		}
	}

	public void unregister() {
		HandlerList.unregisterAll(this);
	}
}
