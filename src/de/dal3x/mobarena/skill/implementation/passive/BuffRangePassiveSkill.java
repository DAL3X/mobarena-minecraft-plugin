package de.dal3x.mobarena.skill.implementation.passive;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.dal3x.mobarena.arena.Arena;
import de.dal3x.mobarena.main.MobArenaPlugin;
import de.dal3x.mobarena.skill.IPassiveSkill;

public class BuffRangePassiveSkill implements IPassiveSkill {

	private boolean running = false;
	private Arena arena;
	
	public void apply(Player p, Arena a) {
		arena = a;
		running = true;
		startBuffSequence(p);
	}
	
	private void startBuffSequence(final Player p) {
		Bukkit.getScheduler().runTaskLater(MobArenaPlugin.getInstance(), new Runnable() {
			public void run() {
				if(arena.isRunning() && arena.getAliveParticipants().contains(p) && running) {
					for(Player target : arena.getAliveParticipants()) {
						if(target.getLocation().distance(p.getLocation()) < 5 && !target.equals(p)) {
							target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 0), true);
							target.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 100, 0), true);
						}
					}
					startBuffSequence(p);
				}
			}
		}, 100);
	}

	public void disapply(Player p, Arena a) {
		running = false;
	}

}
