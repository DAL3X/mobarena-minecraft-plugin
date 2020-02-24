package de.dal3x.mobarena.skill.implementation;

import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.dal3x.mobarena.arena.Arena;
import de.dal3x.mobarena.output.IngameOutput;
import de.dal3x.mobarena.skill.CooldownSkill;
import de.dal3x.mobarena.skill.IRightClickSkill;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class TauntSkill extends CooldownSkill implements IRightClickSkill {

	public TauntSkill() {
		this.name = "§3Verspotten";
		setCooldown(10);
	}

	public void execute(Player p, Arena a) {
		if (!a.isParticipant(p)) {
			return;
		}
		if (p.isSneaking()) {
			if (this.isReady()) {
				putOnCooldown(p);
				for (Mob m : a.getActiveMobs()) {
					if (p.getLocation().distance(m.getLocation()) < 20) {
						m.setTarget(p);
						m.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 0), true);
					}
				}
			} else {
				p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(name + IngameOutput.SkillNotReady));
			}
		}
	}

}
