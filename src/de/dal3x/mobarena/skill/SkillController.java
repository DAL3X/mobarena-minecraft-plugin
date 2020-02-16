package de.dal3x.mobarena.skill;

import de.dal3x.mobarena.skill.implementation.SpeedUpSkill;

public class SkillController {

	public static ILeftClickSkill getLeftClickSkill(String name) {
		return null;
	}
	
	public static IRightClickSkill getRightClickSkill(String name) {
		return null;
	}
	
	public static IPassiveSkill getPassiveSkill(String name) {
		if(name == "SpeedUp") {
			return new SpeedUpSkill();
		}
		return null;
	}
}
