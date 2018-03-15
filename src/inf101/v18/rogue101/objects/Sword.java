package inf101.v18.rogue101.objects;

import java.util.Random;

import inf101.v18.rogue101.game.IGame;

public class Sword implements IEquipment {
	
	private int health;
	
	private Random random = new Random();
	private int modifyAttack;
	private int modifyViewRange;
	private String name = "Sword";
	
	public Sword(int level) {
		this.health = getMaxHealth();
		this.modifyAttack = random.nextInt(level+1);
		
		this.modifyViewRange = random.nextInt(10)/4;
		
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
		// TODO Auto-generated method stub
		return 100;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int modifyViewRange() {
		// TODO Auto-generated method stub
		return modifyViewRange;
	}

	@Override
	public int modifyMaxHealth() {
		// TODO Auto-generated method stub
		return 0;
	}

}
