package de.dal3x.mobarena.skill.implementation;

import org.bukkit.entity.Player;

import de.dal3x.mobarena.arena.Arena;
import de.dal3x.mobarena.output.IngameOutput;
import de.dal3x.mobarena.skill.CooldownSkill;
import de.dal3x.mobarena.skill.IRightClickSkill;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class SelfHealSkill extends CooldownSkill implements IRightClickSkill {

	public SelfHealSkill() {
		setCooldown(8);
	}

	@SuppressWarnings("deprecation")
	public void execute(Player p, Arena a) {
		if(!a.isParticipant(p)) {
			return;
		}
		if (this.isReady()) {
			putOnCooldown();
			if (p.getHealth() + 8 < p.getMaxHealth()) {
				p.setHealth(p.getHealth() + 8);
			} else {
				p.setHealth(p.getMaxHealth());
			}
		} else {
			p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(IngameOutput.SkillNotReady));
		}
	}
}
