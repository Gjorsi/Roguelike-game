package inf101.v18.rogue101.objects;

import inf101.v18.grid.GridDirection;
import inf101.v18.rogue101.game.IGame;

public class MonsterNS implements IMonster {
	private int health;
	private int maxHealth;
	private int attack;
	private int defence;
	private int size;
	private boolean north = true;
	public MonsterNS(IGame game) {
		this.maxHealth = 15*game.getCurrentLevel();
		this.health = getMaxHealth();
		this.attack = game.getCurrentLevel();
		this.defence = 10+game.getCurrentLevel();
		this.size = 2+game.getCurrentLevel();
	}

	@Override
	public void doTurn(IGame game) {
		if (!huntPlayer(game)) {
			if (north) {
				if (game.canGo(GridDirection.NORTH)) {
					game.move(GridDirection.NORTH);
				} else {
					north = false;
					if (game.canGo(GridDirection.SOUTH)) {
						game.move(GridDirection.SOUTH);
					}
				}
			} else {
				if (game.canGo(GridDirection.SOUTH)) {
					game.move(GridDirection.SOUTH);
				} else {
					north = true;
					if (game.canGo(GridDirection.NORTH)) {
						game.move(GridDirection.NORTH);
					}
				}
			}
		}

		// some movement pattern?
		
		
	}

	@Override
	public int getAttack() {
		// TODO Auto-generated method stub
		return attack;
	}

	@Override
	public int getCurrentHealth() {
		// TODO Auto-generated method stub
		return health;
	}

	@Override
	public int getDefence() {
		// TODO Auto-generated method stub
		return defence;
	}

	public int getMaxHealth() {
		// TODO Auto-generated method stub
		return maxHealth;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Primitive Alien";
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return size;
	}

	@Override
	public int getWeight() {
		// TODO Auto-generated method stub
		return size*200;
	}

	@Override
	public String getSymbol() {
		// TODO Auto-generated method stub
		return "👽";
	}

	@Override
	public int handleDamage(IGame game, IItem source, int amount) {
		health -= amount;
		return amount;
	}

}
