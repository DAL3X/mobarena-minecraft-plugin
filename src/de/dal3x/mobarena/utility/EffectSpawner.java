package de.dal3x.mobarena.utility;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class EffectSpawner {

	public static void spawnPlayerCylinder(Player p, Location loc, Particle particle, double radius, double height, int perCircle, double yIncrease) {
		double increment = (2 * Math.PI) / perCircle;
		for (double y = loc.getBlockY(); y < loc.getBlockY() + height; y = y + yIncrease) {
			for (int i = 0; i < 10; i++) {
				double angle = i * increment;
				double x = loc.getX() + (radius * Math.cos(angle));
				double z = loc.getZ() + (radius * Math.sin(angle));
				Location pLoc = new Location(loc.getWorld(), x, y, z);
				p.spawnParticle(particle, pLoc, 1);
			}
		}
	}

	public static void spawnPlayerSpiral(Player p, Location loc, Particle particle, double radius, double height, int perCircle, double yIncrease) {
		double increment = (2 * Math.PI) / perCircle;
		for (int i = 0; i < 10; i++) {
			for (double y = loc.getBlockY(); y < loc.getBlockY() + height; y = y + yIncrease) {
				double angle = i * increment;
				double x = loc.getX() + (radius * Math.cos(angle));
				double z = loc.getZ() + (radius * Math.sin(angle));
				Location pLoc = new Location(loc.getWorld(), x, y, z);
				p.spawnParticle(particle, pLoc, 1);
			}
		}
	}

	public static void spawnPlayerCircle(Player p, Location loc, Particle particle, double radius, double heightToPlayer, int perCircle) {
		double y = loc.getY() + heightToPlayer;
		double increment = (2 * Math.PI) / perCircle;
		for (int i = 0; i < 10; i++) {
			double angle = i * increment;
			double x = loc.getX() + (radius * Math.cos(angle));
			double z = loc.getZ() + (radius * Math.sin(angle));
			Location pLoc = new Location(loc.getWorld(), x, y, z);
			p.spawnParticle(particle, pLoc, 1);
		}
	}

	public static void spawnPlayerParticle(Player p, Location loc, Particle particle, int amount) {
		p.spawnParticle(particle, loc, amount);
	}

	public static void spawnPlayerParticleCloud(Player p, Location loc, Particle particle, int amount, double range) {
		for (int i = 0; i < amount; i++) {
			Random rand = new Random();
			double xPlus = rand.nextDouble() * range;
			double yPlus = rand.nextDouble() * range;
			double zPlus = rand.nextDouble() * range;
			loc.add(xPlus, yPlus, zPlus);
			p.spawnParticle(particle, loc, 1);
		}
	}

	public static void spawnCylinder(Location loc, Particle particle, double radius, double height, int perCircle, double yIncrease) {
		double increment = (2 * Math.PI) / perCircle;
		for (double y = loc.getBlockY(); y < loc.getBlockY() + height; y = y + yIncrease) {
			for (int i = 0; i < 10; i++) {
				double angle = i * increment;
				double x = loc.getX() + (radius * Math.cos(angle));
				double z = loc.getZ() + (radius * Math.sin(angle));
				Location pLoc = new Location(loc.getWorld(), x, y, z);
				loc.getWorld().spawnParticle(particle, pLoc, 1);
			}
		}
	}

	public static void spawnSpiral(Location loc, Particle particle, double radius, double height, int perCircle, double yIncrease) {
		double increment = (2 * Math.PI) / perCircle;
		for (int i = 0; i < 10; i++) {
			for (double y = loc.getBlockY(); y < loc.getBlockY() + height; y = y + yIncrease) {
				double angle = i * increment;
				double x = loc.getX() + (radius * Math.cos(angle));
				double z = loc.getZ() + (radius * Math.sin(angle));
				Location pLoc = new Location(loc.getWorld(), x, y, z);
				loc.getWorld().spawnParticle(particle, pLoc, 1);
			}
		}
	}

	public static void spawnCircle(Location loc, Particle particle, double radius, double heightToLocation, int perCircle) {
		double y = loc.getY() + heightToLocation;
		double increment = (2 * Math.PI) / perCircle;
		for (int i = 0; i < 10; i++) {
			double angle = i * increment;
			double x = loc.getX() + (radius * Math.cos(angle));
			double z = loc.getZ() + (radius * Math.sin(angle));
			Location pLoc = new Location(loc.getWorld(), x, y, z);
			loc.getWorld().spawnParticle(particle, pLoc, 1);
		}
	}

	public static void spawnParticle(Location loc, Particle particle, int amount) {
		loc.getWorld().spawnParticle(particle, loc, amount);
	}

	public static void spawnParticleCloud(Location loc, Particle particle, int amount, double range) {
		for (int i = 0; i < amount; i++) {
			Random rand = new Random();
			double xPlus = rand.nextDouble() * range;
			double yPlus = rand.nextDouble() * range;
			double zPlus = rand.nextDouble() * range;
			loc.add(xPlus, yPlus, zPlus);
			loc.getWorld().spawnParticle(particle, loc, 1);
		}
	}
}