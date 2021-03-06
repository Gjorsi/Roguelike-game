package inf101.v18.rogue101.objects;

import inf101.v18.rogue101.game.IGame;

public class DepletedCrystal implements IItem {

	public DepletedCrystal() {
	}

	@Override
	public int getCurrentHealth() {
		return 0;
	}

	@Override
	public int getDefence() {
		return 0;
	}

	@Override
	public int getMaxHealth() {
		return 0;
	}

	@Override
	public String getName() {
		return "Depleted Crystal";
	}

	@Override
	public int getSize() {
		return 2;
	}

	@Override
	public int getWeight() {
		return 50;
	}

	@Override
	public String getSymbol() {
		return "💎";
	}

	@Override
	public int handleDamage(IGame game, IItem source, int amount) {
		return 0;
	}

}
