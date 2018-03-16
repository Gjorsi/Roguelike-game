package inf101.v18.rogue101.objects;

import inf101.v18.rogue101.game.IGame;

public class DepletedCrystal implements IItem {

	public DepletedCrystal() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getCurrentHealth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getDefence() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxHealth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Depleted Crystal";
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public int getWeight() {
		// TODO Auto-generated method stub
		return 50;
	}

	@Override
	public String getSymbol() {
		// TODO Auto-generated method stub
		return "💎";
	}

	@Override
	public int handleDamage(IGame game, IItem source, int amount) {
		// TODO Auto-generated method stub
		return 0;
	}

}
