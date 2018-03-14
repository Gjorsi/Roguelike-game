/**
 * 
 */
package inf101.v18.rogue101.objects;

import inf101.v18.rogue101.game.IGame;

/**
 * @author Carl
 *
 */
public class Exit implements IItem {

	
	/**
	 * 
	 */
	
	private int health;
	
	public Exit() {
		this.health = getMaxHealth();
	}

	/* (non-Javadoc)
	 * @see inf101.v18.rogue101.objects.IItem#getCurrentHealth()
	 */
	@Override
	public int getCurrentHealth() {
		// TODO Auto-generated method stub
		return health;
	}

	/* (non-Javadoc)
	 * @see inf101.v18.rogue101.objects.IItem#getDefence()
	 */
	@Override
	public int getDefence() {
		// TODO Auto-generated method stub
		return 8;
	}

	/* (non-Javadoc)
	 * @see inf101.v18.rogue101.objects.IItem#getMaxHealth()
	 */
	@Override
	public int getMaxHealth() {
		// TODO Auto-generated method stub
		return 300;
	}

	/* (non-Javadoc)
	 * @see inf101.v18.rogue101.objects.IItem#getName()
	 */
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Exit";
	}

	/* (non-Javadoc)
	 * @see inf101.v18.rogue101.objects.IItem#getSize()
	 */
	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 6;
	}

	/* (non-Javadoc)
	 * @see inf101.v18.rogue101.objects.IItem#getSymbol()
	 */
	@Override
	public String getSymbol() {
		// TODO Auto-generated method stub
		return "🚪";
	}

	/* (non-Javadoc)
	 * @see inf101.v18.rogue101.objects.IItem#handleDamage(inf101.v18.rogue101.game.IGame, inf101.v18.rogue101.objects.IItem, int)
	 */
	@Override
	public int handleDamage(IGame game, IItem source, int amount) {
		health -= amount;
		return amount;
	}

	@Override
	public int getWeight() {
		return 1000;
	}

}
