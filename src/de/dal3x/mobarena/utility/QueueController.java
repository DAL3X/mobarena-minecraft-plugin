package de.dal3x.mobarena.utility;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.entity.Player;

public class QueueController {
	
	List<Player> readyQueue;
	List<Player> joinedQueue;
	
	public QueueController() {
		this.readyQueue = new LinkedList<Player>();
		this.joinedQueue = new LinkedList<Player>();
	}
	
	public List<Player> getJoinedQueue() {
		return this.joinedQueue;
	}
	
	public List<Player> getReadyQueue() {
		return this.readyQueue;
	}
	
	public void flushJoinedQueue() {
		this.joinedQueue = new LinkedList<Player>();
	}
	
	public void flushReadyQueue() {
		this.readyQueue = new LinkedList<Player>();
	}
	
	public void putInReadyQueue(Player p) {
		readyQueue.add(p);
	}
	
	public void takeOutOfReadyQueue(Player p) {
		readyQueue.remove(p);
	}
	
	public void putInJoinedQueue(Player p) {
		joinedQueue.add(p);
	}
	
	public void takeOutOfJoinedQueue(Player p) {
		joinedQueue.remove(p);
	}
	
	public boolean isJoined(Player p) {
		return this.joinedQueue.contains(p);
	}
	
	public boolean isReady(Player p) {
		return this.readyQueue.contains(p);
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
