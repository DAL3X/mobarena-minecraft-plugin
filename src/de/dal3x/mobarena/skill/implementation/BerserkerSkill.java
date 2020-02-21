package de.dal3x.mobarena.skill.implementation;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.dal3x.mobarena.skill.CooldownSkill;
import de.dal3x.mobarena.skill.IRightClickSkill;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class BerserkerSkill extends CooldownSkill implements IRightClickSkill {

	public BerserkerSkill() {
		setCooldown(20);
	}

	public void execute(Player p) {
		if (this.isReady()) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1), true);
			p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 200, 1), true);
		}
		else {
			p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§cSkill nicht bereit"));
		}
	}

}
