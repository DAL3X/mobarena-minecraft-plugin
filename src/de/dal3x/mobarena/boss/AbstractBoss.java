package de.dal3x.mobarena.boss;

import org.bukkit.entity.Mob;

import de.dal3x.mobarena.arena.Arena;

public abstract class AbstractBoss implements IBoss {

	protected Mob bossInstance;
	protected String name;
	protected Arena arena;
	
	protected AbstractBoss(String name, Arena arena) {
		this.name = name;
		this.arena = arena;
	}
	
	public String getBossName() {
		return this.name;
	}
}
