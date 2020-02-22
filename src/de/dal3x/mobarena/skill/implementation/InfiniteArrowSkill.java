package de.dal3x.mobarena.skill.implementation;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import de.dal3x.mobarena.arena.Arena;
import de.dal3x.mobarena.main.MobArenaPlugin;
import de.dal3x.mobarena.skill.IPassiveSkill;

public class InfiniteArrowSkill implements IPassiveSkill, Listener {

	private boolean running = false;
	private Arena arena;

	public void apply(Player p, Arena a) {
		MobArenaPlugin.getInstance().getServer().getPluginManager().registerEvents(this, MobArenaPlugin.getInstance());
		running = true;
		arena = a;
	}

	public void disapply(Player p, Arena a) {
		running = false;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onBowShot(EntityShootBowEvent event) {
		if (running) {
			if (arena.isRunning() && running && arena.getParticipants().contains(event.getEntity())) {
				Player p = (Player) event.getEntity();
				if (!p.getInventory().contains(Material.ARROW)) {
					p.getInventory().addItem(new ItemStack(Material.ARROW));
				}
			}
		}
	}

}
