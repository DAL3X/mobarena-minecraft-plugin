package de.dal3x.mobarena.skill.implementation.active;

import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.dal3x.mobarena.arena.Arena;
import de.dal3x.mobarena.skill.CooldownSkill;
import de.dal3x.mobarena.skill.IRightClickSkill;

public class SlowRangeSkill extends CooldownSkill implements IRightClickSkill {

	public SlowRangeSkill() {
		this.name = "§dFurcht";
		setCooldown(13);
	}
	
	public void execute(Player p, Arena a) {
		if(!a.isParticipant(p)) {
			return;
		}
		if (this.isReady()) {
			putOnCooldown(p);
			for(Mob m : a.getActiveMobs()) {
				if(p.getLocation().distance(m.getLocation()) < 15){
					m.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 140, 1), true);
				}
			}
		}
		else {
			sendSkillNotReady(p);
		}
	}

}
