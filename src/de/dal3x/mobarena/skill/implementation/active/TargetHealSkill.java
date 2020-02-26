package de.dal3x.mobarena.skill.implementation.active;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import de.dal3x.mobarena.arena.Arena;
import de.dal3x.mobarena.skill.CooldownSkill;
import de.dal3x.mobarena.skill.ILeftClickSkill;

public class TargetHealSkill extends CooldownSkill implements ILeftClickSkill {

	public TargetHealSkill() {
		this.name = "§eHeilung";
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
			putOnCooldown(p,a );
			if (targetPlayer.getHealth() + 8 < targetPlayer.getMaxHealth()) {
				targetPlayer.setHealth(targetPlayer.getHealth() + 8);
			} else {
				targetPlayer.setHealth(targetPlayer.getMaxHealth());
			}
		} else {
			sendSkillNotReady(p, a);
		}
	}

	public TargetHealSkill clone() {
		return new TargetHealSkill();
	}
}
