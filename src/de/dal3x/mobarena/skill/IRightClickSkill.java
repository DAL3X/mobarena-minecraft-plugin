package de.dal3x.mobarena.skill;

import org.bukkit.entity.Player;

import de.dal3x.mobarena.arena.Arena;

public interface IRightClickSkill extends IClonableSkill {

	public void execute(Player p, Arena a);
}
