package inf101.v18.rogue101.objects;

import inf101.v18.rogue101.game.IGame;

public class Battery implements IItem {

	private int size;
	private int health = getMaxHealth();
	
	/**
	 * An item which can be consumed by player to regain health
	 * Size and health given scales with level
	 * 
	 * @param level used to scale health
	 */
	public Battery(int level) {
		this.size = level;
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
		return 10;
	}

	@Override
	public String getName() {
		return "Battery";
	}

	@Override
	public int getSize() {
		return size;
	}

	@Override
	public int getWeight() {
		return size*4;
	}

	@Override
	public String getSymbol() {
		return "🔋";
	}

	@Override
	public int handleDamage(IGame game, IItem source, int amount) {
		health = -1;
		return 10*size;
	}

}
