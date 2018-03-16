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
import inf101.v18.rogue101.objects.Battery;
import inf101.v18.rogue101.objects.Crystal;
import inf101.v18.rogue101.objects.IEquipment;
import inf101.v18.rogue101.objects.IItem;
import inf101.v18.rogue101.objects.IPlayer;
import inf101.v18.rogue101.objects.Key;
import inf101.v18.rogue101.objects.Sword;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * @author Carl
 *
 */
public class Player implements IPlayer {

	private String name;
	private int maxHealth = 100;
	private int health;
	private int attack;
	private int defence;
	
	// players inventory
	private List<IItem> pItems;
	
	// track number of options when in menu
	private int nOptions;
	
	// possible menus
	private enum opt {pickUp, drop, attackDir, attackTar, equip};
	
	// track current menu type
	private opt currentOpt;
	
	// track valid directions when choosing
	private List<GridDirection> validDirections;
	
	// track targets when in attack menu
	private List<IItem> possibleTargets;
	
	//track chosen direction to attack, until chosing target
	private int chosenDir;
	
	// the distance the player can see in the darkness
	private int viewRange;
	
	// temporary list of equipment used when chosing what to equip
	private List<IEquipment> equipment;
	
	// player's two equipment "slots"
	private IEquipment armour = null;
	private IEquipment sword = null;
	
	/**
	 * 
	 */
	public Player() {
		this.health = getMaxHealth();
		this.name = "MultiVac";
		this.pItems = new ArrayList<>();
		this.nOptions = 0;
		this.defence = 10;
		this.attack = 1;
		this.validDirections = new ArrayList<>();
		this.possibleTargets = new ArrayList<>();
		this.equipment = new ArrayList<>();
		this.viewRange = 3;
		
	}

	@Override
	public int getAttack() {
		return attack;
	}

	@Override
	public int getDamage() {
		return getAttack()*8;
	}

	@Override
	public int getCurrentHealth() {
		return health;
	}

	@Override
	public int getDefence() {
		return defence;
	}

	@Override
	public int getMaxHealth() {
		return maxHealth;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getSize() {
		return 5;
	}
	
	/**
	 * Get player's range of sight in the darkness
	 * 
	 * @return viewRange
	 */
	public int getViewRange() {
		return viewRange;
	}

	@Override
	public String getSymbol() {
		return "🤖";
	}
	
	/**
	 * if player has a key, remove it and return true
	 * otherwise, return false
	 * 
	 * @return true if player has key
	 */
	public boolean useKey(IGame game) {	
		
		for (IItem item : pItems) {
			if (item instanceof Key) {
				
				// if at level 5, make sure player is not leaving without the crystal.
				A: if (game.getCurrentLevel() == 5) {
					for (IItem itemA : pItems) {
						if (itemA instanceof Crystal) {
							break A;
						}
					}
					game.displayOptions(new String[] {"You forgot the Crystal!", "Go pick it up before leaving."});
					return false;
				}
			
			
				pItems.remove(item);
				return true;
			}
		}
		
		return false;
	}

	@Override
	public int handleDamage(IGame game, IItem source, int amount) {
		health -= amount;
		if (health <= 0) {
			game.gameOver();
			game.displayMessage("Game over!");
		}
		showStatus(game);
		
		return amount;
	}

	@Override
	public void keyPressed(IGame game, KeyCode key) {
		
		// if game is not waiting for options choice, accept keys as follows
		// otherwise digit keys only
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
			case P:
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
			case E:
				tryEquip(game);
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
						showStatus(game);
						break;
					case equip:
						equip(game, digit-1);
						game.changeOptions();
						showStatus(game);
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

	/**
	 * Equip item i in List equipment
	 * 
	 * @param game
	 * @param i
	 * 
	 */
	private void equip(IGame game, int i) {
		if (equipment.get(i) instanceof Sword) {
			
			// if a sword is already equipped, add it to inventory and remove its buffs
			if (sword != null) {
				pItems.add(sword);
				attack -= sword.modifyAttack();
				viewRange -= sword.modifyViewRange();
			}
			
			// remove new sword from inventory, equip it and add its buffs
			pItems.remove(equipment.get(i));
			sword = equipment.get(i);
			attack += sword.modifyAttack();
			viewRange += sword.modifyViewRange();
			
		} else {
			double healthStatus = (double)health/maxHealth;
			
			if (armour != null) {
				pItems.add(armour);
				defence -= armour.modifyDefence();
				viewRange -= armour.modifyViewRange();
				maxHealth -= armour.modifyMaxHealth();
				
				// control health after maxHealth changes
				if (health >= maxHealth) 
					health = maxHealth;
			}
			
			pItems.remove(equipment.get(i));
			armour = equipment.get(i);
			defence += armour.modifyDefence();
			viewRange += armour.modifyViewRange();
			maxHealth += armour.modifyMaxHealth();
			
			// set health to same percentile it was before changing armour
			health = (int) (maxHealth*healthStatus);
		}
		
		game.displayOptions(new String[] {"Equipped " + equipment.get(i).getName()});
	}

	/**
	 * Attempt to equip something, prompts menu choice if there is anything to equip
	 * 
	 * @param game
	 */
	private void tryEquip(IGame game) {
		// if no items in inventory, inform and return
		if (pItems.isEmpty()) {
			game.displayMessage("You have nothing to equip.");
			return;
		}
		
		equipment.clear();
		// find items in inventory of IEquipment type
		for (IItem item : pItems) {
			if (item instanceof IEquipment) {
				equipment.add((IEquipment)item);
			}
		}
		
		// if no IEquipment items in inventory, inform and return
		if (equipment.isEmpty()) {
			game.displayMessage("You have nothing to equip.");
			return;
		}
		
		game.changeOptions();
		currentOpt = opt.equip;
		nOptions = equipment.size();
		
		String[] s = new String[nOptions+1];
		s[0] = "Choose item to equip:";
		for (int i=0; i<nOptions; i++) {
			s[i+1] = String.format("%d - %s", i+1, equipment.get(i).getName());
		}
		
		game.displayOptions(s);
		
	}
	
	/**
	 * Check for targets in GridDirection.FOUR_DIRECTIONS.
	 * If there's only one possible direction to attack, run attackDir().
	 * If more than one possible dir, prompt user to choose dir.
	 * KeyPressed will then call attackDir with chosen dir.
	 * 
	 * @param game
	 */
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
	
	/**
	 * attack direction is decided, now attack target if there is only 1, 
	 * otherwise prompt user for choice of target
	 * then KeyPressed will call attackTar with chosen target
	 * 
	 * @param game
	 * @param dir
	 * chosen or only possible direction to attack
	 */
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

	/**
	 * attempt to drop an item at current location.
	 * If there are more than 7 items at location, player cannot drop anything
	 * If player has only 1 item in inventory, that item will be dropped
	 * if player has more than 1 item in inventory, prompt user for choice of item to drop
	 * KeyPressed will call dropItem() with users choice
	 * 
	 * @param game
	 */
	private void tryDrop(IGame game) {
		
		if (game.getLocalItems().size() > 7) {
			game.displayMessage("Too many items on the ground. Find some other place to drop your trash.");
			return;
		}
		
		if (pItems.size()>1) {
			
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
	
	/**
	 * drop an item at current location
	 * 
	 * @param game
	 * @param n
	 * position of item in pItems list
	 */
	private void dropItem(IGame game, int n) {
		game.drop(pItems.get(n));
		pItems.remove(n);
		game.changeOptions();
	}

	/**
	 * Display the contents of player's inventory on the right side of game map
	 * 
	 * @param game
	 */
	private void displayInventory(IGame game) {
		
		String[] s = new String[Math.max(2, pItems.size()+1)];
		s[0] = "Inventory:";
		
		if (pItems.size()>0) {
			for (int i=1;i<=pItems.size();i++) {
				s[i] = String.format("%d - %s", i, pItems.get(i-1).getName());
			}
		} else {
			s[1] = "Empty.";
		}
		
		game.displayOptions(s);
		
	}

	/**
	 * attempt to pick up an item at current location
	 * if player inventory is full, return
	 * if there is only one item at loc, pick it up
	 * if more than 1 item, prompt user for choice
	 * KeyPressed will call pickUp() with user choice
	 * 
	 * @param game
	 */
	private void tryPickUp(IGame game) {
		
		//return if inventory is full (more than 8 items)
		if (pItems.size()>8) {
			game.displayMessage("Your inventory is full.");
			return;
		}
		
		List<IItem> availableItems = game.getLocalItems();
		String itemOptions[] = new String[availableItems.size()+1];
				
		itemOptions[0] = "Choose item to attempt to pick up:";
		
		if (availableItems.size() == 1) {
			pItems.add(game.pickUp(availableItems.get(0)));
			if (availableItems.get(0) instanceof Crystal)
				viewRange = 40;
			
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
	
	/**
	 * pick up item chosen by player
	 * 
	 * @param game
	 * @param n
	 * position of item choice in list availableItems
	 */
	private void pickUp(IGame game, int n) {
		List<IItem> availableItems = game.getLocalItems();
		pItems.add(game.pickUp(availableItems.get(n)));
		if (availableItems.get(n) instanceof Crystal)
			viewRange = 40;
		game.changeOptions();
	}

	/**
	 * try to move in chosen direction
	 * 
	 * also included consuming of potential battery after moving to new location
	 * 
	 * @param game
	 * @param dir
	 */
	private void tryToMove(IGame game, GridDirection dir) {
		
		if (game.canGo(dir)) {
			game.clearMessages();
			game.move(dir);
			
			for (IItem item : game.getItems(game.getLocation())) {
				if (item instanceof Battery) {
					health += item.handleDamage(game, this, 100);
					
					if (health > maxHealth)
						health = maxHealth;
				}
			}
			
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


















