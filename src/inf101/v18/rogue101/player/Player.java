/**
 * 
 */
package inf101.v18.rogue101.player;

import java.util.ArrayList;
import java.util.List;

import inf101.v18.grid.GridDirection;
import inf101.v18.rogue101.game.IGame;
import inf101.v18.rogue101.objects.IItem;
import inf101.v18.rogue101.objects.IPlayer;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * @author Carl
 *
 */
public class Player implements IPlayer {

	private String name;
	private int health;
	private int maxHealth;
	private List<IItem> pItems;
	
	/**
	 * 
	 */
	public Player() {
		this.maxHealth = 100;
		this.health = 100;
		this.name = "Gange-Rolf";
		this.pItems = new ArrayList<>();
		
	}

	/* (non-Javadoc)
	 * @see inf101.v18.rogue101.objects.IActor#getAttack()
	 */
	@Override
	public int getAttack() {
		// TODO Auto-generated method stub
		return 1;
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
		return 1;
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
		case R:
			tryPickUp(game);
			break;
		case F:
			dropItem(game);
			break;
		case I:
			displayInventory();
			break;
		default:
			break;
		}
		
		showStatus(game);

	}

	private void dropItem(IGame game) {
		
		if (pItems.size()>1) {
			
		} else if (pItems.size() == 1) {
			game.addItem(pItems.get(0));
			pItems.remove(0);
		} else {
			game.displayMessage("You have nothing to drop.");
		}
		
	}

	private void displayInventory() {
		String[] s = new String[2];
		s[0] = "Inventory:";
		s[1] = "";
		
		if (pItems.size()>0) {
			for (int i=0;i<pItems.size();i++) {
				s[1] += String.format("%d - %s || ", i+1, pItems.get(i).getName());
			}
		} else {
			s[1] = "Empty.";
		}
		
	}

	private void tryPickUp(IGame game) {
		List<IItem> availableItems = game.getLocalItems();
		
		String itemOptions[] = new String[2];
				
		itemOptions[0] = "Choose item to attempt to pick up:";
		itemOptions[1] = "";
		
		if (availableItems.size() == 1) {
			pItems.add(game.pickUp(availableItems.get(0)));
			
		} else if (availableItems.size() > 1){
			
			for (int i=0;i<availableItems.size();i++) {
				itemOptions[1] += String.format("%d - %s \n", i+1, availableItems.get(i).getName());
			}
			
			
			
			game.displayOptions(itemOptions);
			
		} else {
			game.displayMessage("No item found.");
		}
		
	}

	private void tryToMove(IGame game, GridDirection dir) {
		
		if (game.canGo(dir))
			game.move(dir);
		else
			game.displayMessage("Nope.");
	}

	private void showStatus(IGame game) {
		game.formatStatus("Player name: %s || Health: %d/%d || Defense: %d || Attack: %d ", getName(), getCurrentHealth(), getMaxHealth(), getDefence(), getAttack() );
	}
	
}


















