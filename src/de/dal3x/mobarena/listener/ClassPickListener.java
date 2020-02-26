package de.dal3x.mobarena.listener;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import de.dal3x.mobarena.arena.Arena;
import de.dal3x.mobarena.classes.ClassController;
import de.dal3x.mobarena.classes.PlayerClass;
import de.dal3x.mobarena.config.Config;
import de.dal3x.mobarena.file.Filehandler;
import de.dal3x.mobarena.output.IngameOutput;

public class ClassPickListener implements Listener {

	private Arena arena;

	public ClassPickListener(Arena arena) {
		this.arena = arena;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onClassPick(PlayerInteractEvent event) {
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}
		Block block = event.getClickedBlock();
		if (!(block.getState() instanceof Sign)) {
			return;
		}
		Sign s = (Sign) block.getState();
		if (!s.getLine(0).equalsIgnoreCase(Config.signClassIdentifier)) {
			return;
		}
		if (arena.getQueue().isJoined(event.getPlayer())) {
			// Ready sign
			if (s.getLine(1).equalsIgnoreCase(Config.signReadyIdentifier)) {
				//Player wasnt ready yet and has picked class
				if ((!arena.getQueue().isReady(event.getPlayer())) && (ClassController.getInstance().getClassForPlayer(event.getPlayer()) != null)) {
					arena.getQueue().putInReadyQueue(event.getPlayer());
					IngameOutput.sendReadyMessageToPlayers(event.getPlayer(), arena.getQueue().getJoinedQueue());
				}
				if (arena.getQueue().allReady()) {
					arena.start();
				}
			}
			// Class sign
			else {
				String klassenName = s.getLine(1);
				if(s.getLine(1).contains("&") || s.getLine(1).contains("§")) {
					klassenName = s.getLine(1).substring(2);
				}
				//Class unlocked
				if(ClassController.getInstance().hasClass(event.getPlayer(), klassenName)) {
					ClassController.getInstance().addClassToPlayer(event.getPlayer(), klassenName);
					IngameOutput.sendClassPickMessage(event.getPlayer(), klassenName);
					return;
				}
				//Class locked
				else {
					PlayerClass rawClass = ClassController.getInstance().getRawClassByName(klassenName);
					if(Filehandler.getInstance().getArenaPoints(event.getPlayer()) >= rawClass.getGlory()) {
						Filehandler.getInstance().addArenaPoints(event.getPlayer(), -rawClass.getGlory());
						Filehandler.getInstance().unlockPlayerClass(event.getPlayer(), rawClass);
						IngameOutput.sendClassEnabled(event.getPlayer(), rawClass);
						return;
					}
					else{
						IngameOutput.sendClassNotUnlocked(event.getPlayer(), rawClass);
					}
				}
			}
		}
	}
}
