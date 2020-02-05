package de.dal3x.mobarena.utility;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.entity.Player;

public class QueueController {
	
	LinkedList<Player> readyQueue;
	List<Player> joinedQueue;
	
	public QueueController() {
		this.readyQueue = new LinkedList<Player>();
		this.joinedQueue = new LinkedList<Player>();
	}
	
	public void putInReadyQueue(Player p) {
		readyQueue.add(p);
	}
	
	public void takeOutOfReadyQueue(Player p) {
		readyQueue.remove(p);
	}
	
	public void addToJoinedQueue(Player p) {
		joinedQueue.add(p);
	}
	
	public void takeOutOfJoinedQueue(Player p) {
		joinedQueue.remove(p);
	}
	
	
	public boolean allReady() {
		if(readyQueue.size() != joinedQueue.size()) {
			return false;
		}
		for(Player p : joinedQueue) {
			if(!readyQueue.contains(p)) {
				return false;
			}
		}
		return true;
	}

}
