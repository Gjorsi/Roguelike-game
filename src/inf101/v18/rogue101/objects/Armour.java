package inf101.v18.rogue101.objects;

import java.util.Random;

import inf101.v18.rogue101.game.IGame;

public class Armour implements IEquipment {

	private int health;
	
	private Random random = new Random();
	private int modifyDefence;
	private int modifyViewRange = 0;
	private int modifyMaxHealth;
	private String name;
	
	public Armour(int level) {
		this.name = "Fancy Robe";
		this.health = getMaxHealth();
		this.modifyDefence = random.nextInt(level+3);
		
		if (modifyDefence > 5) {
			this.name = "Plate Mail";
		} else if (modifyDefence > 3) {
			this.name = "Chain Mail";
		} else if (modifyDefence > 1) {
			this.name = "Leather Armour";
		}
		
		if (random.nextBoolean()) {
			modifyMaxHealth = random.nextInt(level*20)+10;
			this.name += " of Health";
		} else {
			modifyMaxHealth = 0;
		}
		
		
		// chance to find armour with a helmet obstructing view (only for metal armour)
		if (random.nextInt(10) < 3 && modifyDefence > 3)
			modifyViewRange = -1;
		
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
		return this.name;
	}

	@Override
	public int getSize() {
		return 2;
	}

	@Override
	public int getWeight() {
		return modifyDefence*5+30;
	}

	@Override
	public String getSymbol() {
		return "🛡";
	}

	@Override
	public int handleDamage(IGame game, IItem source, int amount) {
		health -= amount;
		return amount;
	}

	@Override
	public int modifyAttack() {
		return 0;
	}

	@Override
	public int modifyDefence() {
		return modifyDefence;
	}

	@Override
	public int modifyViewRange() {
		return modifyViewRange;
	}

	@Override
	public int modifyMaxHealth() {
		return modifyMaxHealth;
	}

}
