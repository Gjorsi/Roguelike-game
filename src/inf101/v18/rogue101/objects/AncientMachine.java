package inf101.v18.rogue101.objects;

import inf101.v18.rogue101.game.IGame;

public class AncientMachine implements IItem {

	public AncientMachine() {
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
		return "Ancient Machine";
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
	public int getWeight() {
		// TODO Auto-generated method stub
		return 2000;
	}

	@Override
	public String getSymbol() {
		// TODO Auto-generated method stub
		return "🎡";
	}

	@Override
	public int handleDamage(IGame game, IItem source, int amount) {
		// TODO Auto-generated method stub
		return 0;
	}

}
