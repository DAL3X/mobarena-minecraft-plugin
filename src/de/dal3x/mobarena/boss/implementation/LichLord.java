package de.dal3x.mobarena.boss.implementation;

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
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.dal3x.mobarena.arena.Arena;
import de.dal3x.mobarena.boss.IBoss;
import de.dal3x.mobarena.boss.MinionBoss;
import de.dal3x.mobarena.config.Config;
import de.dal3x.mobarena.main.MobArenaPlugin;

public class LichLord extends MinionBoss implements IBoss, Listener {

	public LichLord(Arena arena) {
		super("§0§lLich§f§lLord", arena);
		this.arena = arena;
	}

	public Mob spawn(Location loc) {
		WitherSkeleton lich = (WitherSkeleton) loc.getWorld().spawnEntity(loc, EntityType.WITHER_SKELETON);
		lich.setGlowing(true);
		lich.setCustomName(this.name);
		lich.setCustomNameVisible(true);
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
		spawnMinions(arena.getParticipants().size() * Config.LichLordMinionPerPlayer);
		startSpawnSequence();
		return this.bossInstance;
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
			p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 1), true);
			event.setCancelled(true);
			for (Mob minion : this.minions) {
				minion.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1), true);
				minion.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 1), true);
				minion.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 200, 1), true);
			}
		}
	}

	private void startSpawnSequence() {
		Bukkit.getScheduler().runTaskLater(MobArenaPlugin.getInstance(), new Runnable() {
			public void run() {
				if (bossInstance.getHealth() > 0 && arena.getActiveBoss().equals(bossInstance)) {
					clearMinions();
					spawnMinions((arena.getParticipants().size() * Config.LichLordMinionPerPlayer));
					startSpawnSequence();
				}
			}
		}, Config.LichLordSpawnCD * 20);
	}

	private void spawnMinions(int amount) {
		World w = this.bossInstance.getLocation().getWorld();
		Random rand = new Random();
		for (int i = 0; i < amount; i++) {
			for (Player p : this.arena.getParticipants()) {
				Location loc = getMinionSpawnPosition(i, p.getLocation());
				Mob minion;
				// Minion is skeleton
				if (rand.nextInt(2) == 0) {
					minion = (Mob) w.spawnEntity(loc, EntityType.SKELETON);
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
					minion = (Mob) w.spawnEntity(loc, EntityType.ZOMBIE);
					// Iron sword
					ItemStack sword = new ItemStack(Material.IRON_SWORD, 1);
					makeItemUnbreakable(sword);
					minion.getEquipment().setItemInMainHand(sword);
				}
				equipRandomArmor(minion);
				this.arena.setScaledMobHealth(minion);
				minion.setCustomName("§0Min§fion");
				minion.setCustomNameVisible(true);
				addToMinions(minion, this.arena);
			}
		}
	}

	private Location getMinionSpawnPosition(int counter, Location loc) {
		int mod = counter % 4;
		switch (mod) {
		case 0:
			return loc.add(1, 1, 0);
		case 1:
			return loc.add(-1, 1, 0);
		case 2:
			return loc.add(0, 1, 1);
		case 3:
			return loc.add(0, 1, -1);
		}
		return loc;

	}

	private void equipRandomArmor(Mob minion) {
		Random rand = new Random();
		if (rand.nextInt(3) == 0) {
			// Helmet
			ItemStack helmet = new ItemStack(Material.LEATHER_HELMET, 1);
			makeItemUnbreakable(helmet);
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