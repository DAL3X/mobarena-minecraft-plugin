package de.dal3x.mobarena.listener;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import de.dal3x.mobarena.arena.Arena;
import de.dal3x.mobarena.classes.ClassController;
import de.dal3x.mobarena.classes.PlayerClass;
import de.dal3x.mobarena.main.MobArenaPlugin;

public class SkillListener implements Listener {

	private Arena arena;

	public SkillListener(Arena arena, MobArenaPlugin p) {
		p.getServer().getPluginManager().registerEvents(this, p);
		this.arena = arena;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerRightClickSkill(PlayerInteractEvent event) {
		if (!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			return;
		}
		if (!this.arena.isParticipant(event.getPlayer())) {
			return;
		}
		PlayerClass pClass = ClassController.getInstance().getClassForPlayer(event.getPlayer());
		if (pClass.getRightClickSkill() != null) {
			pClass.getRightClickSkill().execute(event.getPlayer());
		}

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerLeftClickSkill(EntityDamageByEntityEvent event) {
		if (!(event.getDamager() instanceof Player)) {
			return;
		}
		Player caster = (Player) event.getDamager();
		if (!this.arena.isParticipant(caster)) {
			return;
		}
		if (!(event.getEntity() instanceof LivingEntity)) {
			return;
		}
		LivingEntity target = (LivingEntity) event.getEntity();
		PlayerClass pClass = ClassController.getInstance().getClassForPlayer(caster);
		if (pClass.getLeftClickSkill() != null) {
			pClass.getLeftClickSkill().execute(caster, target);
		}
	}

	public void applyPassive(Player p) {
		PlayerClass pClass = ClassController.getInstance().getClassForPlayer(p);
		if (pClass.getPassiveSkill() != null) {
			pClass.getPassiveSkill().apply(p, this.arena);
		}
	}

	public void disapplyPassive(Player p) {
		PlayerClass pClass = ClassController.getInstance().getClassForPlayer(p);
		if (pClass.getPassiveSkill() != null) {
			pClass.getPassiveSkill().disapply(p, this.arena);
		}
	}

}
