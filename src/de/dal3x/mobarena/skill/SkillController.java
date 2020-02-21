package de.dal3x.mobarena.skill;

import de.dal3x.mobarena.skill.implementation.BerserkerSkill;
import de.dal3x.mobarena.skill.implementation.SelfHeal;
import de.dal3x.mobarena.skill.implementation.SpeedUpSkill;
import de.dal3x.mobarena.skill.implementation.TargetHeal;

public class SkillController {

	public static ILeftClickSkill getLeftClickSkill(String name) {
		if(name.equalsIgnoreCase("TargetHeal")) {
			return new TargetHeal();
		}
		return null;
	}
	
	public static IRightClickSkill getRightClickSkill(String name) {
		if(name.equalsIgnoreCase("Berserker")) {
			return new BerserkerSkill();
		}
		if(name.equalsIgnoreCase("SelfHeal")) {
			return new SelfHeal();
		}
		return null;
	}
	
	public static IPassiveSkill getPassiveSkill(String name) {
		if(name.equalsIgnoreCase("SpeedUp")) {
			return new SpeedUpSkill();
		}
		return null;
	}
}
