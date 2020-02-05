package de.dal3x.mobarena.utility;

public class EnchantmentMeta {

	private String name;
	private int level;
	
	public EnchantmentMeta(String name, int level) {
		this.name = name;
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}
