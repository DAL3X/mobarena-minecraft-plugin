package de.dal3x.mobarena.skill;

import de.dal3x.mobarena.skill.implementation.ArrowBurnSkill;
import de.dal3x.mobarena.skill.implementation.BerserkerSkill;
import de.dal3x.mobarena.skill.implementation.BuffRangePassiveSkill;
import de.dal3x.mobarena.skill.implementation.InfiniteArrowSkill;
import de.dal3x.mobarena.skill.implementation.SelfHealSkill;
import de.dal3x.mobarena.skill.implementation.SlowRangeSkill;
import de.dal3x.mobarena.skill.implementation.SpeedUpSkill;
import de.dal3x.mobarena.skill.implementation.TargetHealSkill;
import de.dal3x.mobarena.skill.implementation.TauntSkill;

public class SkillController {

	public static ILeftClickSkill getLeftClickSkill(String name) {
		if(name.equalsIgnoreCase("TargetHeal")) {
			return new TargetHealSkill();
		}
		return null;
	}
	
	public static IRightClickSkill getRightClickSkill(String name) {
		if(name.equalsIgnoreCase("Berserker")) {
			return new BerserkerSkill();
		}
		if(name.equalsIgnoreCase("SelfHeal")) {
			return new SelfHealSkill();
		}
		if(name.equalsIgnoreCase("Taunt")) {
			return new TauntSkill();
		}
		if(name.equalsIgnoreCase("RangeSlow")) {
			return new SlowRangeSkill();
		}
		return null;
	}
	
	public static IPassiveSkill getPassiveSkill(String name) {
		if(name.equalsIgnoreCase("SpeedUp")) {
			return new SpeedUpSkill();
		}
		if(name.equalsIgnoreCase("RangeBuff")) {
			return new BuffRangePassiveSkill();
		}
		if(name.equalsIgnoreCase("infiniteArrow")) {
			return new InfiniteArrowSkill();
		}
		if(name.equalsIgnoreCase("FireArrow")) {
			return new ArrowBurnSkill();
		}
		return null;
	}
}
