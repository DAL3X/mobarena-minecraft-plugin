package de.dal3x.mobarena.skill;

import org.bukkit.entity.Player;

import de.dal3x.mobarena.arena.Arena;

public interface IPassiveSkill extends IClonableSkill {

	public void apply(Player p, Arena a);
	
	public void disapply(Player p, Arena a);
}
