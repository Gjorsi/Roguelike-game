/**
 * 
 */
package inf101.v18.rogue101.player;

import java.awt.Event;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import inf101.v18.grid.GridDirection;
import inf101.v18.rogue101.game.IGame;
import inf101.v18.rogue101.objects.IItem;
import inf101.v18.rogue101.objects.IPlayer;
import inf101.v18.rogue101.objects.Key;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * @author Carl
 *
 */
public class Player implements IPlayer {

	private String name;
	private int health;
	private int attack;
	private int defence;
	private List<IItem> pItems;
	private int nOptions;
	private enum opt {pickUp, drop, attackDir, attackTar};
	private opt currentOpt;
	private List<GridDirection> validDirections;
	private List<IItem> possibleTargets;
	private int chosenDir;
	
	/**
	 * 
	 */
	public Player() {
		this.health = getMaxHealth();
		this.name = "Gange-Rolf";
		this.pItems = new ArrayList<>();
		this.nOptions = 0;
		this.defence = 10;
		this.attack = 1;
		this.validDirections = new ArrayList<>();
		this.possibleTargets = new ArrayList<>();
		
	}

	/* (non-Javadoc)
	 * @see inf101.v18.rogue101.objects.IActor#getAttack()
	 */
	@Override
	public int getAttack() {
		return attack;
	}

	/* (non-Javadoc)
	 * @see inf101.v18.rogue101.objects.IActor#getDamage()
	 */
	@Override
	public int getDamage() {
		return getAttack()*5;
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
		return defence;
	}

	
	/* (non-Javadoc)
	 * @see inf101.v18.rogue101.objects.IItem#getMaxHealth()
	 */
	@Override
	public int getMaxHealth() {
		return 100;
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
		return "🤖";
	}
	
	public boolean useKey() {
		for (IItem item : pItems) {
			if (item instanceof Key)
				pItems.remove(item);
				return true;
		}
		
		return false;
	}

	
	/* (non-Javadoc)
	 * @see inf101.v18.rogue101.objects.IItem#handleDamage(inf101.v18.rogue101.game.IGame, inf101.v18.rogue101.objects.IItem, int)
	 */
	@Override
	public int handleDamage(IGame game, IItem source, int amount) {
		health -= amount;
		if (health <= 0) {
			game.displayMessage("Game over!");
		}
		showStatus(game);
		
		return amount;
	}

	
	/* (non-Javadoc)
	 * @see inf101.v18.rogue101.objects.IPlayer#keyPressed(inf101.v18.rogue101.game.IGame, javafx.scene.input.KeyCode)
	 */
	@Override
	public void keyPressed(IGame game, KeyCode key) {
		
		// hvis game ikke venter på option valg, godta vanlige tastetrykk,
		// ellers godtas kun "digit" taster
		if (!game.getOptions()) {
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
			case E:
				tryPickUp(game);
				break;
			case C:
				tryDrop(game);
				break;
			case I:
				displayInventory(game);
				break;
			case X:
				tryAttack(game);
				break;
			default:
				break;
			}
			
			showStatus(game);
		} else {
			if (key.isDigitKey()) {
				
				//key.tostring() returns "DIGIT1" etc, so needs some work to get an integer
				int digit = Integer.parseInt(Character.toString(key.toString().charAt(key.toString().length()-1)));
				
				if (digit > 0 && digit <= nOptions) {
					switch (currentOpt) {
					case drop:
						dropItem(game, digit-1);
						game.clearMessages();
						break;
					case pickUp:
						pickUp(game, digit-1);
						game.clearMessages();
						break;
					case attackDir:
						game.clearMessages();
						chosenDir = digit-1;
						attackDir(game, validDirections.get(digit-1));
						
						break;
					case attackTar:
						game.clearMessages();
						game.attack(validDirections.get(chosenDir), possibleTargets.get(digit-1));
						game.changeOptions();
						break;
					default:
						break;
					}
				} else {
					game.displayMessage("Enter number in range 1 - " + nOptions);
				}
			} else {
				game.displayMessage("Enter number in range 1 - " + nOptions);
			}
			

		}
		

	}

	// see if there are any targets in GridDirection.FOUR_DIRECTIONS
	// if there's only one possible direction to attack, run attackDir
	// if more than one possible dir, prompt user to choose dir
	private void tryAttack(IGame game) {
		
		validDirections.clear();
		
		// store directions where there are possible targets
		for (GridDirection dir : GridDirection.FOUR_DIRECTIONS) {
			if (game.allItemsActors(game.getLocation(dir)).size() > 0)
				validDirections.add(dir);
		}		
		
		if (validDirections.size() < 1) {
			game.displayMessage("There is nothing nearby to attack.");
			
		} else if (validDirections.size() == 1) {
			// if only one dir has any target(s), try attack in that dir
			attackDir(game, validDirections.get(0));
			
		} else {
			// more than one possible dir, we have to choose
			game.changeOptions();
			currentOpt = opt.attackDir;
			nOptions = validDirections.size();
			
			String[] s = new String[nOptions+1];
			s[0] = "Choose direction in which to attack:";
			for (int i=0; i<nOptions; i++) {
				s[i+1] = String.format("%d - %s", i+1, validDirections.get(i).name());
			}
			
			game.displayOptions(s);
			
		}
		
	}

	// attack dir is decided, now attack target if only 1, otherwise choose
	private void attackDir(IGame game, GridDirection dir) {
		possibleTargets.clear();
		possibleTargets = game.allItemsActors(game.getLocation(dir));
		if (possibleTargets.size() == 1) {
			game.attack(dir, possibleTargets.get(0));
			if (game.getOptions())
				game.changeOptions();
			
		} else {
			currentOpt = opt.attackTar;
			nOptions = possibleTargets.size();
			
			String[] s = new String[nOptions+1];
			s[0] = "Choose target:";
			for (int i=0; i<nOptions; i++) {
				s[i+1] = String.format("%d - %s", i+1, possibleTargets.get(i).getName());
			}
			
			game.displayOptions(s);
		}
	}

	private void tryDrop(IGame game) {
		
		if (pItems.size()>1) {
			
			if (game.getLocalItems().size() > 7) {
				game.displayMessage("Too many items on the ground. You have to find some other place to drop your trash.");
				return;
			}
			
			String itemOptions[] = new String[pItems.size()+1];
			itemOptions[0] = "Choose item to drop:";
			
			for (int i=0;i<pItems.size();i++) {
				itemOptions[i+1] = String.format("%d - %s", i+1, pItems.get(i).getName());
			}
			
			game.displayOptions(itemOptions);
			
			game.changeOptions();
			nOptions = pItems.size();
			currentOpt = opt.drop;
			
		} else if (pItems.size() == 1) {
			game.addItem(pItems.get(0));
			pItems.remove(0);
		} else {
			game.displayMessage("You have nothing to drop.");
		}
		
	}
	
	private void dropItem(IGame game, int n) {
		game.drop(pItems.get(n));
		pItems.remove(n);
		game.changeOptions();
	}

	private void displayInventory(IGame game) {
		
//		game.getPrinter().moveTo(1, 23);
//		game.getPrinter().print("TEST TEST");
		
		
		String[] s = new String[Math.max(2, pItems.size()+1)];
		s[0] = "Inventory:";
		
		if (pItems.size()>0) {
			for (int i=1;i<pItems.size()+1;i++) {
				s[i] = String.format("%d - %s", i, pItems.get(i-1).getName());
			}
		} else {
			s[1] = "Empty.";
		}
		
		game.displayOptions(s);
		
	}

	private void tryPickUp(IGame game) {
		List<IItem> availableItems = game.getLocalItems();
		String itemOptions[] = new String[availableItems.size()+1];
				
		itemOptions[0] = "Choose item to attempt to pick up:";
		
		//return if inventory is full (more than 8 items)
		if (pItems.size()>8) {
			game.displayMessage("Your inventory is full.");
			return;
		}
		
		if (availableItems.size() == 1) {
			pItems.add(game.pickUp(availableItems.get(0)));
			
		} else if (availableItems.size() > 1){
			
			for (int i=0;i<availableItems.size();i++) {
				itemOptions[i+1] = String.format("%d - %s", i+1, availableItems.get(i).getName());
			}
			
			game.displayOptions(itemOptions);
			
			currentOpt = opt.pickUp;
			nOptions = availableItems.size();
			game.changeOptions();
			
		} else {
			game.displayMessage("No item found.");
		}
		
	}
	
	private void pickUp(IGame game, int n) {
		List<IItem> availableItems = game.getLocalItems();
		pItems.add(game.pickUp(availableItems.get(n)));
		game.changeOptions();
	}

	private void tryToMove(IGame game, GridDirection dir) {
		
		if (game.canGo(dir)) {
			game.clearMessages();
			game.move(dir);
		} else
			game.displayMessage("Nope.");
	}

	@Override
	public void showStatus(IGame game) {
		game.formatStatus("Player name: %s || Health: %d/%d || Defense: %d || Attack: %d ", getName(), getCurrentHealth(), getMaxHealth(), getDefence(), getAttack() );
	}

	@Override
	public int getWeight() {
		return 800;
	}
	
}


















