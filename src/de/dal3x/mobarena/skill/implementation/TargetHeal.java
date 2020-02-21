package de.dal3x.mobarena.skill.implementation;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import de.dal3x.mobarena.skill.CooldownSkill;
import de.dal3x.mobarena.skill.ILeftClickSkill;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class TargetHeal extends CooldownSkill implements ILeftClickSkill {
	
	public TargetHeal() {
		setCooldown(8);
	}

	public void execute(Player p, LivingEntity target) {
		if(!(target instanceof Player)) {
			return;
		}
		if (this.isReady()) {
			p.setHealth(p.getHealth() + 8);
		}
		else {
			p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("�cSkill nicht bereit"));
		}
	}


}
