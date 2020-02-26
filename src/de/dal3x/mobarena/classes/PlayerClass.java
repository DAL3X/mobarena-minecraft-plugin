package de.dal3x.mobarena.classes;

import de.dal3x.mobarena.skill.CooldownSkill;
import de.dal3x.mobarena.skill.IClonableSkill;
import de.dal3x.mobarena.skill.ILeftClickSkill;
import de.dal3x.mobarena.skill.IPassiveSkill;
import de.dal3x.mobarena.skill.IRightClickSkill;

public class PlayerClass {

	private String name;
	private ILeftClickSkill leftClickSkill;
	private IRightClickSkill rightClickSkill;
	private IPassiveSkill passiveSkill;
	private String[] equip;
	private int glory;
	
	public PlayerClass(String name, ILeftClickSkill leftClickSkill, IRightClickSkill rightClickSkill, IPassiveSkill passive, String[] equip, int glory) {
		setGlory(glory);
		setName(name);
		setLeftClickSkill(leftClickSkill);
		setRightClickSkill(rightClickSkill);
		setEquip(equip);
		setPassiveSkill(passive);
	}
	
	public PlayerClass(String name) {
		setName(name);
	}
	
	//Constructor is only for cloning and should only be used for that
	private PlayerClass(String name ,IClonableSkill clone, IClonableSkill clone2, IClonableSkill clone3, String[] eq, int glory) {
		setGlory(glory);
		setName(name);
		setLeftClickSkill((ILeftClickSkill) clone);
		setRightClickSkill((IRightClickSkill) clone2);
		setEquip(equip);
		setPassiveSkill((IPassiveSkill) clone3);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ILeftClickSkill getLeftClickSkill() {
		return leftClickSkill;
	}

	public void setLeftClickSkill(ILeftClickSkill leftClickSkill) {
		this.leftClickSkill = leftClickSkill;
	}

	public IRightClickSkill getRightClickSkill() {
		return rightClickSkill;
	}

	public void setRightClickSkill(IRightClickSkill rightClickSkill) {
		this.rightClickSkill = rightClickSkill;
	}

	public String[] getEquip() {
		return equip;
	}

	public void setEquip(String[] equip) {
		this.equip = equip;
	}

	public int getGlory() {
		return glory;
	}

	public void setGlory(int glory) {
		this.glory = glory;
	}

	public IPassiveSkill getPassiveSkill() {
		return passiveSkill;
	}

	public void setPassiveSkill(IPassiveSkill passiveSkill) {
		this.passiveSkill = passiveSkill;
	}
	
	public PlayerClass clone() {
		IClonableSkill left = null;
		IClonableSkill right = null;
		IClonableSkill passive = null;
		if(leftClickSkill != null) {
			left = leftClickSkill.clone();
		}
		if(rightClickSkill != null) {
			right = rightClickSkill.clone();
		}
		if(passiveSkill != null) {
			passive = passiveSkill.clone();
		}
		return new PlayerClass(this.name, left, right, passive, this.equip, this.glory);
	}
	
	public void activateSkills() {
		if(this.leftClickSkill instanceof CooldownSkill) {
			((CooldownSkill)leftClickSkill).activateSkill();
		}
		if(this.rightClickSkill instanceof CooldownSkill) {
			((CooldownSkill)rightClickSkill).activateSkill();
		}
	}

}
