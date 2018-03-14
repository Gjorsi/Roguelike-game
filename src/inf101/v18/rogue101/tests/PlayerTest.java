package inf101.v18.rogue101.tests;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


import org.junit.jupiter.api.Test;

import inf101.v18.grid.GridDirection;
import inf101.v18.grid.ILocation;
import inf101.v18.rogue101.examples.Rabbit;
import inf101.v18.rogue101.game.Game;
import inf101.v18.rogue101.game.IGame;
import inf101.v18.rogue101.map.GameMap;
import inf101.v18.rogue101.map.IMapView;
import inf101.v18.rogue101.objects.IItem;
import inf101.v18.rogue101.objects.IPlayer;
import javafx.scene.input.KeyCode;

class PlayerTest {
	public static String TEST_MAP = "40 5\n" //
			+ "########################################\n" //
			+ "#...... ..C.R ......R.R......... ..R...#\n" //
			+ "#.R@R...... ..........RC..R...... ... .#\n" //
			+ "#... ..R........R......R. R........R.RR#\n" //
			+ "########################################\n" //
	;

	@Test
	void testPlayer1() {
		// new game with our test map
		Game game = new Game(TEST_MAP);
		// pick (3,2) as the "current" position; this is where the player is on the
		// test map, so it'll set up the player and return it
		IPlayer player = (IPlayer) game.setCurrent(3, 2);

		
		// find players location
		ILocation loc = game.getLocation();
		// press "UP" key
		player.keyPressed(game, KeyCode.UP);
		// see that we moved north
		assertEquals(loc.go(GridDirection.NORTH), game.getLocation());
		
		loc = game.getLocation();
		player.keyPressed(game, KeyCode.UP);
		// player should not be able to move UP from this pos (wall), check that pos is not changed after trying
		assertEquals(loc, game.getLocation());
		
		game.doTurn();
		
		player.keyPressed(game, KeyCode.RIGHT);
		// see that we moved east
		assertEquals(loc.go(GridDirection.EAST), game.getLocation());
		
		loc = game.getLocation();
		player.keyPressed(game, KeyCode.DOWN);
		// player should not be able to move DOWN from this pos (another actor in the way)
		assertNotEquals(loc.go(GridDirection.SOUTH), game.getLocation());
		
		
	}
	
	@Test
	void testAttack() {
		// new game with our test map
		Game game = new Game(TEST_MAP);
		// pick (3,2) as the "current" position; this is where the player is on the
		// test map, so it'll set up the player and return it
		IMapView map = game.getMap();
		Rabbit rabbit = (Rabbit)game.getActorAt(map.getLocation(4, 2));
		IPlayer player = (IPlayer) game.setCurrent(3,2);

		assertNotNull(rabbit);
		assertNotNull(player);
		
		// press x to attack and 1 to choose direction EAST
		player.keyPressed(game, KeyCode.X);
		player.keyPressed(game, KeyCode.DIGIT1);
		
		// after an attack, line 3 in log should be either "Success!" or "Failed!"
		// if success, test whether rabbit has lost health
		// if failure, test whether rabbit is still at max, then attack until rabbit loses health, check success
		String lastMessage = game.getLogLine(3);
		if (lastMessage.contains("Success")) {
			assertTrue(rabbit.getCurrentHealth() < rabbit.getMaxHealth());
		} else if (lastMessage.contains("Failed")) {
			assertTrue(rabbit.getCurrentHealth() == rabbit.getMaxHealth());
			
			while (rabbit.getCurrentHealth() == rabbit.getMaxHealth()) {
				player.keyPressed(game, KeyCode.X);
				player.keyPressed(game, KeyCode.DIGIT1);
			}
			
			assertTrue(game.getLogLine(3).contains("Success"));
			
			
		} else 
			fail("No fail or success recorded in log ?");
	}


}
