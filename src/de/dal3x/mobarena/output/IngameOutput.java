package de.dal3x.mobarena.output;

import java.util.List;

import org.bukkit.entity.Player;

import de.dal3x.mobarena.config.Config;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class IngameOutput {

	public static final String prefix = "[Mobarena] ";

	public static final String noPermission = "Dir fehlen die Permissions fuer diesen Befehl";
	public static final String couldNotFindArena = "Arena konnte nicht gefunden werden. Nutze /mobarena " + Config.listCommand + " um alle Arenen zu sehen.";
	public static final String arenaTaken = "Diese Arena is zurzeit belegt. Nutze /mobarena " + Config.listCommand + " um alle Arenen zu sehen.";

	public static final String successfullReload = "Plugin wurde erfolgreich neu geladen.";
	public static final String listArenas = "Alle Arenen:";
	public static final String arenaListPrefix = "Arena: ";
	public static final String arenaFree = " -> frei";
	public static final String arenaNotFree = " -> belegt";
	
	public static final String wave = "Welle: ";
	public static final String boss = "Bosswelle!";
	
	
	public static void sendRemainingMobs(int alive, int all, List<Player> players) {
		for(Player p : players) {
			p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(alive + " von " + all + " Mobs verbleiben"));
		}
	}
	
	public static void sendDefeatMessage(int count, List<Player> players) {
		sendMessageToPlayers(prefix + " Alle Spieler wurden besiegt. Deine erreichte Welle ist: "+ count, players);
	}
	
	public static void sendReadyMessageToPlayers(Player p, List<Player> players) {
		String message = prefix + p.getName() + " ist jetzt bereit"; 
		sendMessageToPlayers(message, players);
	}
	
	public static void sendMessageToPlayers(String message, List<Player> players) {
		for(Player p : players) {
			p.sendMessage(message);
		}
	}

	public static void sendClassPickeMessage(Player p, String klasse) {
		p.sendMessage(prefix + "Du hast die Klasse " + klasse + " gewählt");
		p.sendMessage(prefix + "Rechtsklicke jetzt auf das '" + Config.signReadyIdentifier + "' Schild um dich bereit zu setzen");
	}

	public static void sendHelpMessage(Player p) {
		p.sendMessage("---- Mobarena ----");
		p.sendMessage("/mobarena list  - Liste aller Arenen");
		p.sendMessage("/mobarena join [Arena-Name] - Arena beitreten");
		p.sendMessage("/mobarena leave  - Arena verlassen");
	}

}
