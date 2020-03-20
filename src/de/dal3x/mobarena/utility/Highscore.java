package de.dal3x.mobarena.utility;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

import de.dal3x.mobarena.file.Filehandler;

public class Highscore {
	
	private static List<UUID> ids;
	private static int maxWave;
	
	public static void init(List<UUID> IDs, int welle) {
		setIDs(IDs);
		setWelle(welle);
	}
	
	public static void newHighScore(List<Player> players, int waveNumber) {
		if(waveNumber > maxWave) {
			List<UUID> playerIDs = new LinkedList<UUID>();
			for(Player p: players) {
				playerIDs.add(p.getUniqueId());
			}
			setIDs(playerIDs);
			setWelle(waveNumber);
			Filehandler.getInstance().saveHighscore();
		}
	}
	
	public static List<UUID> getIDs() {
		return ids;
	}
	public static void setIDs(List<UUID> IDs) {
		Highscore.ids = IDs;
	}
	public static int getWelle() {
		return maxWave;
	}
	public static void setWelle(int welle) {
		Highscore.maxWave = welle;
	}

}
