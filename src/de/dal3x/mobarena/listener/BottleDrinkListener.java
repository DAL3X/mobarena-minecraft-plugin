package de.dal3x.mobarena.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import de.dal3x.mobarena.arena.Arena;
import de.dal3x.mobarena.main.MobArenaPlugin;

public class BottleDrinkListener implements Listener {

	private Arena arena;

	public BottleDrinkListener(Arena arena, MobArenaPlugin p) {
		p.getServer().getPluginManager().registerEvents(this, p);
		this.arena = arena;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPotionDrink(PlayerItemConsumeEvent event) {
		final Player p = event.getPlayer();
		if (arena.isParticipant(p)) {
			if (event.getItem().getType() == Material.POTION) {
				MobArenaPlugin.getInstance().getServer().getScheduler().runTaskLater(MobArenaPlugin.getInstance(), new Runnable() {
					public void run() {
						p.getInventory().remove(Material.GLASS_BOTTLE);
					}
				}, 10);
			}
		}
	}
}
