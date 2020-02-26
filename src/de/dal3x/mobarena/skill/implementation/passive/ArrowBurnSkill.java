package de.dal3x.mobarena.skill.implementation.passive;

import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

import de.dal3x.mobarena.arena.Arena;
import de.dal3x.mobarena.main.MobArenaPlugin;
import de.dal3x.mobarena.skill.IPassiveSkill;
import de.dal3x.mobarena.utility.EffectSpawner;

public class ArrowBurnSkill implements IPassiveSkill, Listener {

	boolean active = false;
	Player p;
	Arena a;

	public void apply(Player p, Arena a) {
		MobArenaPlugin.getInstance().getServer().getPluginManager().registerEvents(this, MobArenaPlugin.getInstance());
		this.p = p;
		this.a = a;
		active = true;
	}

	public void disapply(Player p, Arena a) {
		active = false;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onArrowShot(EntityShootBowEvent event) {
		if (event.getEntity().equals(p)) {
			if (active) {
				if (a.isParticipant(p)) {
					if (p.isSneaking()) {
						event.getProjectile().setFireTicks(200);
						EffectSpawner.spawnParticleCloud(p.getLocation().add(0, 1.1, 0), Particle.LAVA, 4, 0.3);
					}
				}
			}
		}
	}
	
	public ArrowBurnSkill clone() {
		return new ArrowBurnSkill();
	}

}
