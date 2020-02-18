package de.dal3x.mobarena.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.dal3x.mobarena.arena.Arena;
import de.dal3x.mobarena.arena.ArenaStorage;
import de.dal3x.mobarena.config.Config;
import de.dal3x.mobarena.main.MobArenaPlugin;
import de.dal3x.mobarena.output.ConsoleOutputs;
import de.dal3x.mobarena.output.IngameOutput;

public class MobarenaCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ConsoleOutputs.consolePrefix + ConsoleOutputs.notAPlayer);
			return true;
		}
		Player p = (Player) sender;
		// Join
		if (args.length == 2 && args[0].equalsIgnoreCase(Config.joinCommand)) {
			Arena a = ArenaStorage.getInstance().getArena(args[1]);
			if (a == null) {
				p.sendMessage(IngameOutput.prefix + IngameOutput.couldNotFindArena);
				return true;
			}
			if (!a.isFree()) {
				p.sendMessage(IngameOutput.prefix + IngameOutput.arenaTaken);
				return true;
			}
			for (Arena checkArena : ArenaStorage.getInstance().getArenas()) {
				if (checkArena.getQueue().isJoined(p) || checkArena.isParticipant(p)) {
					return true;
				}
			}
			a.addToJoinQueue(p);
			p.teleport(a.getLobby());
			return true;
		}
		// Leave
		else if (args.length == 1 && args[0].equalsIgnoreCase(Config.leaveCommand)) {
			for (Arena a : ArenaStorage.getInstance().getArenas()) {
				if (a.isParticipant(p)) {
					a.removeParticipant(p);
					return true;
				}
				if(a.getQueue().isJoined(p)) {
					a.getQueue().takeOutOfJoinedQueue(p);
					a.getQueue().takeOutOfReadyQueue(p);
					p.teleport(a.getSpawnLocation());
					return true;
				}
			}
			return true;
		}
		// Reload
		else if (args.length == 1 && args[0].equalsIgnoreCase(Config.reloadCommand)) {
			reload(p);
			return true;
		}
		// List
		else if (args.length == 1 && args[0].equalsIgnoreCase(Config.joinCommand)) {
			sendArenaList(p, ArenaStorage.getInstance().getArenas());
			return true;
		}
		// Glory
		else if (args.length == 1 && args[0].equalsIgnoreCase(Config.gloryCommand)) {
			IngameOutput.sendGloryMessage(p);
			return true;
		}
		// Help
		else {
			IngameOutput.sendHelpMessage(p);
			return true;
		}
	}

	private void sendArenaList(Player p, List<Arena> arenaList) {
		IngameOutput.sendArenaList(p, arenaList);
	}

	private void reload(Player p) {
		if (p.hasPermission(Config.reloadPerm)) {
			MobArenaPlugin.getInstance().reload();
			p.sendMessage(IngameOutput.prefix + IngameOutput.successfullReload);
		} else {
			p.sendMessage(IngameOutput.prefix + IngameOutput.noPermission);
		}
	}
}
