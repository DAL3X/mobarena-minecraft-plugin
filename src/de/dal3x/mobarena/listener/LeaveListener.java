package de.dal3x.mobarena.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import de.dal3x.mobarena.arena.Arena;
import de.dal3x.mobarena.utility.QueueController;

public class LeaveListener  implements Listener {

	private Arena arena;

	public LeaveListener(Arena arena) {
		this.arena = arena;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerLeave(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		QueueController queue = arena.getQueue();
		if(queue.isJoined(p)) {
			queue.takeOutOfJoinedQueue(p);
		}
		if(queue.isReady(p)) {
			queue.takeOutOfReadyQueue(p);
		}
		if(arena.isParticipant(p)) {
			arena.removeParticipant(p);
		}
	}
}
