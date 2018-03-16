package inf101.v18.rogue101.objects;

import inf101.v18.rogue101.game.IGame;

public class AncientMachine implements IItem {

	/**
	 * Simple cosmetic item
	 */
	public AncientMachine() {
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
		return "Ancient Machine";
	}

	@Override
	public int getSize() {
		return 3;
	}

	@Override
	public int getWeight() {
		return 2000;
	}

	@Override
	public String getSymbol() {
		return "🎡";
	}

	@Override
	public int handleDamage(IGame game, IItem source, int amount) {
		return 0;
	}

}
