package de.dal3x.mobarena.listener;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import de.dal3x.mobarena.arena.Arena;
import de.dal3x.mobarena.main.MobArenaPlugin;

public class SpectatorTeleportListener implements Listener {

	private Arena arena;

	public SpectatorTeleportListener(Arena arena, MobArenaPlugin p) {
		p.getServer().getPluginManager().registerEvents(this, p);
		this.arena = arena;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onSpectatorTeleport(PlayerTeleportEvent event) {
		Player p = event.getPlayer();
		if (event.getCause() != PlayerTeleportEvent.TeleportCause.SPECTATE) {
			return;
		}
		if (!arena.isSpectator(p)) {
			return;
		}
		Player spectated = (Player) p.getSpectatorTarget();
		if(spectated == null) {
			p.setSpectatorTarget(arena.getAliveParticipants().get(0));
			return;
		}
		Player next = getNextTarget(spectated);
		p.setSpectatorTarget(next);
	}

	public Player getNextTarget(Player spectated) {
		List<Player> alive = arena.getAliveParticipants();
		int pos = 0;
		for (int i = 0; i < alive.size(); i++) {
			if (alive.get(i).equals(spectated)) {
				pos = i;
			}
		}
		pos++;
		if (pos >= alive.size()) {
			pos = 0;
		}
		return alive.get(pos);
	}

}
