package inf101.v18.rogue101.objects;

import inf101.v18.grid.GridDirection;
import inf101.v18.rogue101.game.IGame;

public interface IMonster extends INonPlayer {
	
	/**
	 * look for player in adjacent locs, attack if found
	 * 
	 * @param game
	 * @return true if the monster found and attacked player
	 */
	default boolean huntPlayer(IGame game) {
		for (GridDirection dir : GridDirection.FOUR_DIRECTIONS) {
			if (game.getActorAt(game.getLocation(dir)) instanceof IPlayer) {
				game.attack(dir, game.getActorAt(game.getLocation(dir)));
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Standardised damage based on attack (which scales with level for most if not all monsters)
	 */
	default int getDamage() {
		return getAttack()*4+3;
	}
}
