/**
 * 
 */
package inf101.v18.rogue101.examples;

import inf101.v18.rogue101.game.IGame;
import inf101.v18.rogue101.objects.IItem;

/**
 * @author Carl
 *
 */
public class Mushroom implements IItem {

	/**
	 * 
	 */
	public Mushroom() {
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
		return null;
	}

	@Override
	public int getSize() {
		return 0;
	}

	@Override
	public String getSymbol() {
		return "M";
	}

	@Override
	public int handleDamage(IGame game, IItem source, int amount) {
		return 0;
	}

	@Override
	public int getWeight() {
		return 0;
	}

}
