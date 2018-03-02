/**
 * 
 */
package inf101.v18.rogue101.player;

import inf101.v18.grid.GridDirection;
import inf101.v18.rogue101.game.IGame;
import inf101.v18.rogue101.objects.IItem;
import inf101.v18.rogue101.objects.IPlayer;
import javafx.scene.input.KeyCode;

/**
 * @author Carl
 *
 */
public class Player implements IPlayer {

	private String name;
	private int health;
	private int maxHealth;
	
	/**
	 * 
	 */
	public Player() {
		this.maxHealth = 100;
		this.health = 100;
		this.name = "Gange-Rolf";
		
	}

	/* (non-Javadoc)
	 * @see inf101.v18.rogue101.objects.IActor#getAttack()
	 */
	@Override
	public int getAttack() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see inf101.v18.rogue101.objects.IActor#getDamage()
	 */
	@Override
	public int getDamage() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see inf101.v18.rogue101.objects.IItem#getCurrentHealth()
	 */
	@Override
	public int getCurrentHealth() {
		return health;
	}

	/* (non-Javadoc)
	 * @see inf101.v18.rogue101.objects.IItem#getDefence()
	 */
	@Override
	public int getDefence() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	/* (non-Javadoc)
	 * @see inf101.v18.rogue101.objects.IItem#getMaxHealth()
	 */
	@Override
	public int getMaxHealth() {
		return maxHealth;
	}

	
	/* (non-Javadoc)
	 * @see inf101.v18.rogue101.objects.IItem#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	
	/* (non-Javadoc)
	 * @see inf101.v18.rogue101.objects.IItem#getSize()
	 */
	@Override
	public int getSize() {
		return 5;
	}

	
	/* (non-Javadoc)
	 * @see inf101.v18.rogue101.objects.IItem#getSymbol()
	 */
	@Override
	public String getSymbol() {
		return "@";
	}

	
	/* (non-Javadoc)
	 * @see inf101.v18.rogue101.objects.IItem#handleDamage(inf101.v18.rogue101.game.IGame, inf101.v18.rogue101.objects.IItem, int)
	 */
	@Override
	public int handleDamage(IGame game, IItem source, int amount) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	/* (non-Javadoc)
	 * @see inf101.v18.rogue101.objects.IPlayer#keyPressed(inf101.v18.rogue101.game.IGame, javafx.scene.input.KeyCode)
	 */
	@Override
	public void keyPressed(IGame game, KeyCode key) {
		
		switch (key) {
		case LEFT:
			tryToMove(game, GridDirection.WEST);
			break;
		case RIGHT:
			tryToMove(game, GridDirection.EAST);
			break;
		case UP:
			tryToMove(game, GridDirection.NORTH);
			break;
		case DOWN:
			tryToMove(game, GridDirection.SOUTH);
			break;
		default:
			break;
		}
		
		showStatus(game);

	}

	private void tryToMove(IGame game, GridDirection dir) {
		
		if (game.canGo(dir))
			game.move(dir);
		else
			game.displayMessage("Nope.");
	}

	private void showStatus(IGame game) {
		game.formatStatus("Player name: %s || Health: %d/%d || Defense: %d || Attack: %d", getName(), getCurrentHealth(), getMaxHealth(), getDefence(), getAttack() );
	}
	
}


















