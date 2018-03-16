package inf101.v18.rogue101.objects;

import java.util.Random;

import inf101.v18.grid.GridDirection;
import inf101.v18.rogue101.game.IGame;

public class MonsterR implements IMonster {
	private int health;
	private int maxHealth;
	private int attack;
	private int defence;
	private int size;
	private Random random = new Random();
	
	public MonsterR(IGame game) {
		/**
		 * health, attack, defence and size scale with map level (difficulty)
		 */
		this.maxHealth = 15*game.getCurrentLevel();
		this.health = getMaxHealth();
		this.attack = game.getCurrentLevel();
		this.defence = 10+game.getCurrentLevel();
		this.size = 2+game.getCurrentLevel();
	}

	@Override
	public void doTurn(IGame game) {
		
		// attack player if adjacent, otherwise move in random direction (if not obstructed)
		if (!huntPlayer(game)) {
			int r = random.nextInt(4)+1;
			int n = 1;
			for (GridDirection dir : GridDirection.FOUR_DIRECTIONS) {
				if (n == r && game.canGo(dir)) {
					game.move(dir);
					break;
				} 
				n++;
			}
		}
	}

	@Override
	public int getAttack() {
		return attack;
	}

	@Override
	public int getCurrentHealth() {
		return health;
	}

	@Override
	public int getDefence() {
		return defence;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	@Override
	public String getName() {
		return "Primitive Alien";
	}

	@Override
	public int getSize() {
		return size;
	}

	@Override
	public int getWeight() {
		return size*200;
	}

	@Override
	public String getSymbol() {
		return "👾";
	}

	@Override
	public int handleDamage(IGame game, IItem source, int amount) {
		health -= amount;
		return amount;
	}

}
