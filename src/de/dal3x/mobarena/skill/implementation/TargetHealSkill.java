package de.dal3x.mobarena.skill.implementation;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import de.dal3x.mobarena.arena.Arena;
import de.dal3x.mobarena.output.IngameOutput;
import de.dal3x.mobarena.skill.CooldownSkill;
import de.dal3x.mobarena.skill.ILeftClickSkill;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class TargetHealSkill extends CooldownSkill implements ILeftClickSkill {

	public TargetHealSkill() {
		setCooldown(6);
	}

	@SuppressWarnings("deprecation")
	public void execute(Player p, LivingEntity target, Arena a) {
		if (!(target instanceof Player)) {
			return;
		}
		Player targetPlayer = (Player) target;
		if(!a.isParticipant(targetPlayer)) {
			return;
		}
		if (this.isReady()) {
			putOnCooldown();
			if (targetPlayer.getHealth() + 8 < targetPlayer.getMaxHealth()) {
				targetPlayer.setHealth(targetPlayer.getHealth() + 8);
			} else {
				targetPlayer.setHealth(targetPlayer.getMaxHealth());
			}
		} else {
			p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(IngameOutput.SkillNotReady));
		}
	}

}
