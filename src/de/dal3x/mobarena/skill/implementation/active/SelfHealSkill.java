package de.dal3x.mobarena.skill.implementation.active;

import org.bukkit.entity.Player;

import de.dal3x.mobarena.arena.Arena;
import de.dal3x.mobarena.skill.CooldownSkill;
import de.dal3x.mobarena.skill.IRightClickSkill;

public class SelfHealSkill extends CooldownSkill implements IRightClickSkill {

	public SelfHealSkill() {
		this.name = "§bSelbstheilung";
		setCooldown(8);
	}

	@SuppressWarnings("deprecation")
	public void execute(Player p, Arena a) {
		if(!a.isParticipant(p)) {
			return;
		}
		if (this.isReady()) {
			putOnCooldown(p);
			if (p.getHealth() + 8 < p.getMaxHealth()) {
				p.setHealth(p.getHealth() + 8);
			} else {
				p.setHealth(p.getMaxHealth());
			}
		} else {
			sendSkillNotReady(p);
		}
	}
}
