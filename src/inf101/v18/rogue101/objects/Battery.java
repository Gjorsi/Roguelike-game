package inf101.v18.rogue101.objects;

import inf101.v18.rogue101.game.IGame;

public class Battery implements IItem {

	private int size;
	private int health = getMaxHealth();
	
	public Battery(int level) {
		this.size = level;
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
		return 10;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Battery";
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return size;
	}

	@Override
	public int getWeight() {
		// TODO Auto-generated method stub
		return size*4;
	}

	@Override
	public String getSymbol() {
		// TODO Auto-generated method stub
		return "🔋";
	}

	@Override
	public int handleDamage(IGame game, IItem source, int amount) {
		health = -1;
		return 10*size;
	}

}
