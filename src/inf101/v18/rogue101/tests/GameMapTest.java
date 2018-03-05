package inf101.v18.rogue101.tests;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

import inf101.v18.grid.ILocation;
import inf101.v18.rogue101.examples.Carrot;
import inf101.v18.rogue101.examples.Rabbit;
import inf101.v18.rogue101.map.GameMap;
import inf101.v18.rogue101.objects.Dust;
import inf101.v18.rogue101.objects.IItem;
import inf101.v18.rogue101.player.Player;

class GameMapTest {

	private Random random = new Random();
	
	@Test
	void testSortedAdd() {
		GameMap gameMap = new GameMap(20, 20);
		ILocation location = gameMap.getLocation(10, 10);
		
		for (int i=0;i<20;i++) {
			if (random.nextInt(40) < 10)
				gameMap.add(location, new Carrot());
			else if (random.nextInt(40) < 20)
				gameMap.add(location, new Dust());
			else if (random.nextInt(40) < 35)
				gameMap.add(location, new Rabbit());
			else 
				gameMap.add(location, new Player());
		}
		
		List<IItem> items = gameMap.getAll(location);
		
		for (int i=0;i<items.size()-1;i++) {
			assertTrue(items.get(i).compareTo(items.get(i+1)) >= 0);
		}
		
		
	}

}
