package inf101.v18.rogue101.objects;

import java.util.Random;

import inf101.v18.rogue101.game.IGame;

public class Sword implements IEquipment {
	
	private int health;
	
	private Random random = new Random();
	private int modifyAttack;
	private int modifyViewRange = 0;
	private String name = "Sword";
	
	public Sword(int level) {
		this.health = getMaxHealth();
		
		//randomised attack boost up to current level
		this.modifyAttack = random.nextInt(level+1);
		
		// small chance of glowing / burning sword giving higher view range
		int n = random.nextInt(12);
		if (n==10)
			this.modifyViewRange = 1;
		if (n==11)
			this.modifyViewRange = 2;
		
		if (modifyViewRange == 1)
			name = "Glowing " + name;
		if (modifyViewRange == 2)
			name = "Blazing " + name;
		
		if (modifyAttack > 0)
			name += " of Strength";
	}

	@Override
	public int getCurrentHealth() {
		return health;
	}

	@Override
	public int getDefence() {
		return 0;
	}

	@Override
	public int getMaxHealth() {
		return 100;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getSize() {
		return 2;
	}

	@Override
	public int getWeight() {
		return 45;
	}

	@Override
	public String getSymbol() {
		return "🗡";
	}

	@Override
	public int handleDamage(IGame game, IItem source, int amount) {
		health -= amount;
		return amount;
	}

	@Override
	public int modifyAttack() {
		return modifyAttack;
	}

	@Override
	public int modifyDefence() {
		return 0;
	}

	@Override
	public int modifyViewRange() {
		return modifyViewRange;
	}

	@Override
	public int modifyMaxHealth() {
		return 0;
	}

}
