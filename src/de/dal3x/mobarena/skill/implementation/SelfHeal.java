package de.dal3x.mobarena.skill.implementation;

import org.bukkit.entity.Player;

import de.dal3x.mobarena.output.IngameOutput;
import de.dal3x.mobarena.skill.CooldownSkill;
import de.dal3x.mobarena.skill.IRightClickSkill;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class SelfHeal extends CooldownSkill implements IRightClickSkill{

	public SelfHeal() {
		setCooldown(8);
	}
	
	public void execute(Player p) {
		if (this.isReady()) {
			p.setHealth(p.getHealth() + 8);
		}
		else {
			p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(IngameOutput.SkillNotReady));
		}
	}

}
