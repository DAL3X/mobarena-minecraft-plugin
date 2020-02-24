package de.dal3x.mobarena.skill;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import de.dal3x.mobarena.main.MobArenaPlugin;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public abstract class CooldownSkill {

	protected int cooldown;
	protected boolean ready = true;
	protected String name;

	protected void setCooldown(int seconds) {
		this.cooldown = seconds * 20;
	}

	protected void putOnCooldown(final Player p) {
		this.ready = false;
		Bukkit.getScheduler().runTaskLater(MobArenaPlugin.getInstance(), new Runnable() {
			public void run() {
				p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(name + "§a ist wieder bereit"));
				p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.8F, 1);
				ready = true;
			}
		}, this.cooldown);
	}

	protected boolean isReady() {
		return this.ready;
	}

}
