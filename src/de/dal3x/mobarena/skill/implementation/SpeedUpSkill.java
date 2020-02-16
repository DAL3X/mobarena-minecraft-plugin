package de.dal3x.mobarena.skill.implementation;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.dal3x.mobarena.arena.Arena;
import de.dal3x.mobarena.skill.IPassiveSkill;

public class SpeedUpSkill implements IPassiveSkill {

	public void apply(Player p, Arena a) {
		p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1), true);
		p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 1), true);
	}

	public void disapply(Player p, Arena a) {
		p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1, 1), true);
		p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 1, 1), true);
	}

}
