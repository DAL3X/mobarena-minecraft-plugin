package de.dal3x.mobarena.skill;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import de.dal3x.mobarena.arena.Arena;

public interface ILeftClickSkill {

	public void execute(Player p, LivingEntity target, Arena a);
}
