package de.dal3x.mobarena.skill.implementation.active;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.dal3x.mobarena.arena.Arena;
import de.dal3x.mobarena.skill.CooldownSkill;
import de.dal3x.mobarena.skill.IRightClickSkill;

public class BerserkerSkill extends CooldownSkill implements IRightClickSkill {

	public BerserkerSkill() {
		this.name = "§4Berserker";
		setCooldown(20);
	}

	public void execute(Player p, Arena a) {
		if (!a.isParticipant(p)) {
			return;
		}
		if (this.isReady()) {
			putOnCooldown(p, a);
			p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 2), true);
			p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 200, 1), true);
		} else {
			sendSkillNotReady(p, a);
		}
	}

	public BerserkerSkill clone() {
		return new BerserkerSkill();
	}
}
