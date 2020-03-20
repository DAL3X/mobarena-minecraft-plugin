package de.dal3x.mobarena.boss.implementation;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.dal3x.mobarena.arena.Arena;
import de.dal3x.mobarena.boss.MinionBoss;
import de.dal3x.mobarena.config.Config;
import de.dal3x.mobarena.main.MobArenaPlugin;

public class LichLord extends MinionBoss implements Listener {

	public LichLord(Arena arena) {
		super("§0§lLich§f§lLord", arena);
	}

	@SuppressWarnings("deprecation")
	public Mob spawn(Location loc) {
		WitherSkeleton lich = (WitherSkeleton) loc.getWorld().spawnEntity(loc, EntityType.WITHER_SKELETON);
		lich.setGlowing(true);
		lich.setCustomName(this.name);
		lich.setCustomNameVisible(true);
		lich.setPersistent(true);
		// Lich Crown
		ItemStack helmet = new ItemStack(Material.GOLDEN_HELMET, 1);
		ItemMeta helmetMeta = helmet.getItemMeta();
		helmetMeta.addEnchant(Enchantment.THORNS, 2, true);
		helmetMeta.setUnbreakable(true);
		helmet.setItemMeta(helmetMeta);
		lich.getEquipment().setHelmet(helmet);
		// Lich Wand
		ItemStack stick = new ItemStack(Material.BLAZE_ROD, 1);
		ItemMeta stickMeta = helmet.getItemMeta();
		stickMeta.addEnchant(Enchantment.DURABILITY, 1, true);
		stickMeta.setUnbreakable(true);
		stick.setItemMeta(stickMeta);
		lich.getEquipment().setItemInMainHand(stick);
		// init
		this.bossInstance = lich;
		this.addBossBar();
		spawnMinions();
		startSpawnSequence();
		startDebuffSequence();
		return lich;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onLichDmg(EntityDamageByEntityEvent event) {
		if (event.getDamager().equals(this.bossInstance)) {
			// Lich makes Dmg
			event.setDamage(0);
			if (!(event.getEntity() instanceof Player)) {
				return;
			}
			Player p = (Player) event.getEntity();
			p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 0), true);
			event.setCancelled(true);
			for (Mob minion : this.minions) {
				minion.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 80, 0), true);
			}
		}
		if(event.getEntity().equals(this.bossInstance)) {
		// Lich gets dmg
			if(!(event.getDamager( ) instanceof Player)) {
				return;
			}
			Player p = (Player) event.getDamager();
			for(Mob m : this.minions) {
				m.setTarget(p);
			}
			return;
		}
	}

	private void startDebuffSequence() {
		int amountPlayer = arena.getAliveParticipants().size();
		List<Player> partCopy = new LinkedList<Player>();
		for (Player pl : arena.getAliveParticipants()) {
			partCopy.add(pl);
		}
		Collections.shuffle(partCopy);
		int border = amountPlayer / 2;
		final List<Player> speedTargets = partCopy.subList(0, border);
		final List<Player> slowTargets = partCopy.subList(border, partCopy.size());
		Bukkit.getScheduler().runTaskLater(MobArenaPlugin.getInstance(), new Runnable() {
			public void run() {
				if (bossInstance.getHealth() > 0 && arena.getActiveBoss().getMobInstance().equals(bossInstance)) {
					for(Player p : speedTargets) {
						p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 140, 0), true);
					}
					for(Player p : slowTargets) {
						p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 140, 0), true);
					}
					startDebuffSequence();
				}
			}
		}, Config.LichLordDebuffCD * 20);
	}

	private void startSpawnSequence() {
		Bukkit.getScheduler().runTaskLater(MobArenaPlugin.getInstance(), new Runnable() {
			public void run() {
				if (bossInstance.getHealth() > 0 && arena.getActiveBoss().getMobInstance().equals(bossInstance)) {
					clearMinions();
					spawnMinions();
					startSpawnSequence();
				}
			}
		}, Config.LichLordSpawnCD * 20);
	}

	@SuppressWarnings("deprecation")
	private void spawnMinions() {
		int amount = Config.LichLordMinionPerPlayer;
		World w = this.bossInstance.getLocation().getWorld();
		Random rand = new Random();
		for (Player p : this.arena.getAliveParticipants()) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 1), true);
			List<Location> spawnlocs = new LinkedList<Location>();
			for (Location loc : arena.getMobspawns()) {
				spawnlocs.add(loc);
			}
			Collections.shuffle(spawnlocs);
			for (int i = 1; i < (arena.getParticipants().size() * amount); i++) {
				Mob minion;
				// Minion is skeleton
				if (rand.nextInt(2) == 0) {
					minion = (Mob) w.spawnEntity(spawnlocs.get(i % spawnlocs.size()), EntityType.SKELETON);
					minion.setTarget(p);
					// Minion is melee-skeleton
					if (rand.nextInt(2) == 0) {
						// Stone knockback sword
						ItemStack sword = new ItemStack(Material.STONE_SWORD, 1);
						ItemMeta swordMeta = sword.getItemMeta();
						swordMeta.addEnchant(Enchantment.KNOCKBACK, 2, true);
						makeItemUnbreakable(sword);
						sword.setItemMeta(swordMeta);
						minion.getEquipment().setItemInMainHand(sword);
					}
				}
				// Minion is zombie
				else {
					minion = (Mob) w.spawnEntity(spawnlocs.get(i % spawnlocs.size()), EntityType.ZOMBIE);
					((Zombie) minion).setBaby(false);
					// Iron sword
					ItemStack sword = new ItemStack(Material.IRON_SWORD, 1);
					makeItemUnbreakable(sword);
					minion.getEquipment().setItemInMainHand(sword);
				}
				equipRandomArmor(minion);
				this.arena.setScaledMobHealth(minion);
				minion.setCustomName("§0Min§fion");
				minion.setCustomNameVisible(true);
				minion.setPersistent(true);
				addToMinions(minion, this.arena);
			}
		}
	}

	private void equipRandomArmor(Mob minion) {
		Random rand = new Random();
		if (rand.nextInt(3) == 0) {
			// Helmet
			ItemStack helmet = new ItemStack(Material.LEATHER_HELMET, 1);
			makeItemUnbreakable(helmet);
			minion.getEquipment().setHelmet(helmet);
		}
		else {
			ItemStack helmet = new ItemStack(Material.STONE_BUTTON, 1);
			minion.getEquipment().setHelmet(helmet);
		}
		if (rand.nextInt(3) == 0) {
			// Chestplate
			ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
			makeItemUnbreakable(chest);
			minion.getEquipment().setChestplate(chest);
		}
		if (rand.nextInt(3) == 0) {
			// Leggins
			ItemStack leggins = new ItemStack(Material.LEATHER_LEGGINGS, 1);
			makeItemUnbreakable(leggins);
			minion.getEquipment().setLeggings(leggins);
		}
		if (rand.nextInt(3) == 0) {
			// Boots
			ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
			makeItemUnbreakable(boots);
			minion.getEquipment().setBoots(boots);
		}
	}

	private void makeItemUnbreakable(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		meta.setUnbreakable(true);
		item.setItemMeta(meta);
	}

}