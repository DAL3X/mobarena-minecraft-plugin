package de.dal3x.mobarena.arena;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.entity.Player;

public final class ArenaStorage {

	private static ArenaStorage instance;
	private List<Arena> arenas;

	private ArenaStorage() {
		this.arenas = new LinkedList<Arena>();
	}

	public static ArenaStorage getInstance() {
		if (instance == null) {
			instance = new ArenaStorage();
		}
		return instance;
	}

	public static void clearInstance() {
		for (Arena a : getInstance().getArenas()) {
			a.reset();
		}
		instance = null;
	}

	public List<Arena> getArenas() {
		return arenas;
	}

	public void setArenas(List<Arena> arenas) {
		this.arenas = arenas;
	}

	public void addArena(Arena a) {
		arenas.add(a);
	}

	public Arena getArena(String name) {
		for (Arena a : this.arenas) {
			if (a.getName().equalsIgnoreCase(name)) {
				return a;
			}
		}
		return null;
	}

	public boolean isFree(String name) {
		if (getArena(name).isFree()) {
			return true;
		}
		return false;
	}

	public boolean isInAnArena(Player p) {
		for (Arena a : this.arenas) {
			if (a.isParticipant(p)) {
				return true;
			}
		}
		return false;
	}
}
