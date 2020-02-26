package de.dal3x.mobarena.skill.implementation.active;

import org.bukkit.Particle;
import org.bukkit.entity.Player;

import de.dal3x.mobarena.arena.Arena;
import de.dal3x.mobarena.skill.CooldownSkill;
import de.dal3x.mobarena.skill.IRightClickSkill;
import de.dal3x.mobarena.utility.EffectSpawner;

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
			putOnCooldown(p, a);
			if (p.getHealth() + 8 < p.getMaxHealth()) {
				p.setHealth(p.getHealth() + 8);
			} else {
				p.setHealth(p.getMaxHealth());
			}
			EffectSpawner.spawnParticleCloud(p.getLocation().add(0, 1.6, 0), Particle.HEART, 4, 0.3);
		} else {
			sendSkillNotReady(p, a);
		}
	}
	
	public SelfHealSkill clone() {
		return new SelfHealSkill();
	}
}
