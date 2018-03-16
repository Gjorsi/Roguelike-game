package inf101.v18.rogue101.objects;

import inf101.v18.grid.GridDirection;
import inf101.v18.rogue101.game.IGame;

public class MonsterWE implements IMonster {
	private int health;
	private int maxHealth;
	private int attack;
	private int defence;
	private int size;
	private boolean north = true;
	
	public MonsterWE(IGame game) {
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
		// attack player if adjacent, otherwise continue moving west/east or change direction 180 if hindered
		if (!huntPlayer(game)) {
			if (north) {
				if (game.canGo(GridDirection.EAST)) {
					game.move(GridDirection.EAST);
				} else {
					north = false;
					if (game.canGo(GridDirection.WEST)) {
						game.move(GridDirection.WEST);
					}
				}
			} else {
				if (game.canGo(GridDirection.WEST)) {
					game.move(GridDirection.WEST);
				} else {
					north = true;
					if (game.canGo(GridDirection.EAST)) {
						game.move(GridDirection.EAST);
					}
				}
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
		return "👽";
	}

	@Override
	public int handleDamage(IGame game, IItem source, int amount) {
		health -= amount;
		return amount;
	}

}
