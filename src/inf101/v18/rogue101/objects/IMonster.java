package inf101.v18.rogue101.objects;

import inf101.v18.grid.GridDirection;
import inf101.v18.rogue101.game.IGame;

public interface IMonster extends INonPlayer {
	
	default boolean huntPlayer(IGame game) {
		for (GridDirection dir : GridDirection.FOUR_DIRECTIONS) {
			if (game.getActorAt(game.getLocation(dir)) instanceof IPlayer) {
				game.attack(dir, game.getActorAt(game.getLocation(dir)));
				return true;
			}
		}
		
		return false;
	}
	
	default int getDamage() {
		return getAttack()*5+5;
	}
}
