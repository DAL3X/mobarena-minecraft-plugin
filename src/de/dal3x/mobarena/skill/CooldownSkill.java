package de.dal3x.mobarena.skill;


import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import de.dal3x.mobarena.arena.Arena;
import de.dal3x.mobarena.main.MobArenaPlugin;
import de.dal3x.mobarena.output.IngameOutput;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public abstract class CooldownSkill {

	protected int cooldown;
	protected boolean ready = true;
	protected String name;
	protected boolean isOnActionCooldown = false;

	protected void setCooldown(int seconds) {
		this.cooldown = seconds * 20;
	}

	protected void putOnCooldown(final Player p, final Arena a) {
		this.ready = false;
		this.isOnActionCooldown = true;
		Bukkit.getScheduler().runTaskLater(MobArenaPlugin.getInstance(), new Runnable() {
			public void run() {
				isOnActionCooldown = false;
			}
		}, 20);
		Bukkit.getScheduler().runTaskLater(MobArenaPlugin.getInstance(), new Runnable() {
			public void run() {
				if (a.isParticipant(p)) {
					p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(name + "§a ist wieder bereit"));
					p.playSound(p.getLocation(), Sound.BLOCK_GRASS_BREAK, 0.7F, 1);
					ready = true;
				}
			}
		}, this.cooldown);
	}
	
	public void activateSkill() {
		this.ready = false;
		this.isOnActionCooldown = true;
		Bukkit.getScheduler().runTaskLater(MobArenaPlugin.getInstance(), new Runnable() {
			public void run() {
				isOnActionCooldown = false;
				ready = true;
			}
		}, 40);
	}

	protected void sendSkillNotReady(Player p, Arena a) {
		if (!this.isOnActionCooldown && a.isParticipant(p)) {
			p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(name + IngameOutput.SkillNotReady));
		}
	}

	protected boolean isReady() {
		return this.ready;
	}

}
