package inf101.v18.rogue101.objects;

import java.util.Random;

import inf101.v18.rogue101.game.IGame;

public class Armour implements IEquipment {

	private int health;
	
	private Random random = new Random();
	private int modifyDefence;
	private int modifyViewRange = 0;
	private int modifyMaxHealth;
	private String name = "";
	
	public Armour(int level) {
		
		this.health = getMaxHealth();
		this.modifyDefence = random.nextInt(level+1);
		
		if (modifyDefence > 5) {
			name = "Plate Mail";
		} else if (modifyDefence > 3) {
			name = "Chain Mail";
		} else if (modifyDefence > 1) {
			name = "Leather Armour";
		} else {
			name = "Fancy Robe";
		}
		
		if (random.nextBoolean()) {
			modifyMaxHealth = random.nextInt(level*20);
		} else {
			modifyMaxHealth = 0;
		}
		
		
		// chance to find armour with a helmet obstructing view (only for metal armour)
		if (random.nextInt(10) < 3 && modifyDefence > 3)
			modifyViewRange = -1;
		
	}

	@Override
	public int getCurrentHealth() {
		// TODO Auto-generated method stub
		return health;
	}

	@Override
	public int getDefence() {
		// TODO Auto-generated method stub
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
		return 0;
	}

	@Override
	public int getWeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getSymbol() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int handleDamage(IGame game, IItem source, int amount) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int modifyAttack() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int modifyDefence() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int modifyViewRange() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int modifyMaxHealth() {
		// TODO Auto-generated method stub
		return 0;
	}

}
