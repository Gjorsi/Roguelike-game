package inf101.v18.rogue101.objects;

import inf101.v18.rogue101.game.IGame;

public class Key implements IItem {
	private int health;

	public Key() {
		this.health = getMaxHealth();
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
		return 500;
	}

	@Override
	public String getName() {
		return "Key";
	}

	@Override
	public int getSize() {
		return 2;
	}

	@Override
	public String getSymbol() {
		return "🔑";
	}

	@Override
	public int handleDamage(IGame game, IItem source, int amount) {
		health -= amount;
		
		return amount;
	}

	@Override
	public int getWeight() {
		return 1;
	}

}
