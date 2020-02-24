package de.dal3x.mobarena.output;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.entity.Player;

import de.dal3x.mobarena.arena.Arena;
import de.dal3x.mobarena.classes.PlayerClass;
import de.dal3x.mobarena.config.Config;
import de.dal3x.mobarena.file.Filehandler;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;

public class IngameOutput {

	public static final String prefix = "§8[§3Mob§bArena§8] ";

	public static final String noPermission = "§cDir fehlen die Permissions fuer diesen Befehl";
	public static final String couldNotFindArena = "§cArena konnte nicht gefunden werden. Nutze §6/mobarena " + Config.listCommand + " §cum alle Arenen zu sehen.";
	public static final String arenaTaken = "§cDiese Arena is zurzeit belegt. Nutze §6/mobarena " + Config.listCommand + " §cum alle Arenen zu sehen.";

	public static final String successfullReload = "§aPlugin wurde erfolgreich neu geladen.";
	
	public static final String wave = "Welle ";
	public static final String boss = "§cBosswelle";
	
	public static final String SkillNotReady = "§c ist nicht bereit";
	
	
	public static void sendArenaList(Player p, List<Arena> arenaList) {
		List<Arena> freeArenas = new LinkedList<Arena>();
		List<Arena> takenArenas = new LinkedList<Arena>();
		for (Arena a : arenaList) {
			if (a.isFree()) {
				freeArenas.add(a);
			} else {
				takenArenas.add(a);
			}
		}
		p.sendMessage("§8--------------§8[§3Mob§bArena§8]--------------");
		p.sendMessage("§8Klicke auf eine Arena um beizutreten");
		for(Arena a : freeArenas) {
			TextComponent tc = new TextComponent();
			tc.setText("§a■  §e" + a.getName());
			tc.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/mobarena join " + a.getName()));
			tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aBeitreten").create()));
			p.spigot().sendMessage(tc);
		}
		for(Arena a: takenArenas) {
			TextComponent tc = new TextComponent();
			tc.setText("§c■  §e" + a.getName());
			tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cArena belegt").create()));
			p.spigot().sendMessage(tc);
		}
		p.sendMessage("§8----------------------------");
	}
	
	public static void sendClassEnabled(Player p, PlayerClass klasse) {
		p.sendMessage(prefix + "§aDu hast die Klasse §b" + klasse.getName() + " §afür §b" + klasse.getGlory() + " §aRuhm freigeschaltet");
	}
	
	public static void sendClassNotUnlocked(Player p, PlayerClass klasse) {
		p.sendMessage(prefix + "§cUm die Klasse §6" + klasse.getName() + " §cspielen zu können, musst du §6" + klasse.getGlory() + " §cRuhm besitzen");
	}
	
	public static void sendRemainingMobs(int alive, int all, List<Player> players) {
		for(Player p : players) {
			p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(alive + " Mobs verbleiben"));
		}
	}
	
	public static void sendDefeatMessage(int count, List<Player> players) {
		sendMessageToPlayers(prefix + "§7Alle Spieler wurden besiegt. Du hast es bis Welle §e"+ count + " §7geschafft", players);
	}
	
	public static void sendReadyMessageToPlayers(Player p, List<Player> players) {
		String message = prefix +"§b" + p.getName() + " §aist jetzt bereit"; 
		sendMessageToPlayers(message, players);
	}
	
	public static void sendMessageToPlayers(String message, List<Player> players) {
		for(Player p : players) {
			p.sendMessage(message);
		}
	}
	
	public static void sendGloryMessage(Player p) {
		int glory = Filehandler.getInstance().getArenaPoints(p);
		p.sendMessage(prefix + "§7Du besitzt §e" + glory + " §7Ruhm");
	}
	
	public static void sendGloryGainMessage(Player p, int glory) {
		p.sendMessage(prefix + "§7Du hast §e" + glory + " §7Ruhm erhalten");
	}

	public static void sendClassPickMessage(Player p, String klasse) {
		p.sendMessage(prefix + "§7Du hast die Klasse §e" + klasse + " §7gewählt");
		p.sendMessage(prefix + "§7Klicke auf das §e" + Config.signReadyIdentifier + " §7Schild um dich bereit zu setzen");
	}

	public static void sendHelpMessage(Player p) {
		p.sendMessage("§8--------------§8[§3Mob§bArena§8]--------------");
		TextComponent tcJ = new TextComponent();
		tcJ.setText("§e§o/mobarena join §8- §7Arena beitreten");
		tcJ.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/mobarena join"));
		tcJ.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§ejoin").create()));
		p.spigot().sendMessage(tcJ);
		TextComponent tcL = new TextComponent();
		tcL = new TextComponent();
		tcL.setText("§e§o/mobarena leave  §8- §7Arena verlassen");
		tcL.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/mobarena leave"));
		tcL.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§eleave").create()));
		p.spigot().sendMessage(tcL);
		TextComponent tcG = new TextComponent();
		tcG = new TextComponent();
		tcG.setText("§e§o/mobarena glory  §8- §7Ruhm anzeigen");
		tcG.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/mobarena glory"));
		tcG.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§eglory").create()));
		p.spigot().sendMessage(tcG);
		if(p.hasPermission(Config.reloadPerm)) {
			p.sendMessage("§c§o/mobarena reload  §8- §7Plugin neu laden");
		}
		p.sendMessage("§8----------------------------");
	}

}
