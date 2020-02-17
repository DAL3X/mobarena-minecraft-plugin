package de.dal3x.mobarena.skill;

import org.bukkit.Bukkit;

import de.dal3x.mobarena.main.MobArenaPlugin;

public abstract class CooldownSkill {

	protected int cooldown;
	protected boolean ready = true;
	
	protected void setCooldown(int seconds) {
		this.cooldown = seconds * 20;
	}
	
	protected void putOnCooldown() {
		this.ready = false;
		Bukkit.getScheduler().runTaskLater(MobArenaPlugin.getInstance(), new Runnable() {
			public void run() {
				ready = true;
			}
		}, this.cooldown);
	}
	
	protected boolean isReady() {
		return this.ready;
	}
}
