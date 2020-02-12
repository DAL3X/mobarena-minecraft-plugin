package de.dal3x.mobarena.classes;

import de.dal3x.mobarena.skill.ILeftClickSkill;
import de.dal3x.mobarena.skill.IRightClickSkill;

public class PlayerClass {

	private String name;
	private ILeftClickSkill leftClickSkill;
	private IRightClickSkill rightClickSkill;
	private String[] equip;
	private int glory;
	
	public PlayerClass(String name, ILeftClickSkill leftClickSkill, IRightClickSkill rightClickSkill, String[] equip, int glory) {
		setGlory(glory);
		setName(name);
		setLeftClickSkill(leftClickSkill);
		setRightClickSkill(rightClickSkill);
		setEquip(equip);
	}
	
	public PlayerClass(String name) {
		setName(name);
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
	
	

}
