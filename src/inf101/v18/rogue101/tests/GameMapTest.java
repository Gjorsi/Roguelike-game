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
		
		// insert 20 random items at location
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
		
		// check that each item is larger or equal in size to next item
		for (int i=0;i<items.size()-1;i++) {
			assertTrue(items.get(i).compareTo(items.get(i+1)) >= 0);
		}
		
		// check that there are exactly 20 items
		assertTrue(gameMap.getAll(location).size() == 20);
	}
	
	@Test
	void testGameMapVisibility() {
		GameMap gameMap = new GameMap(20, 20);
		
		// test that no positions outside distance are included
		ILocation centre = gameMap.getLocation(10, 10);
		List<ILocation> centreNeighbours = gameMap.getNeighbourhood(centre, 4);
		for (ILocation loc : centreNeighbours) {
			assertTrue(centre.gridDistanceTo(loc) <= 4);
		}
		
		// test that no positions outside distance are included when dist is 1
		List<ILocation> centreNeighbours1 = gameMap.getNeighbourhood(centre, 1);
		for (ILocation loc : centreNeighbours1) {
			assertTrue(centre.gridDistanceTo(loc) <= 1);
		}
		
		// test that number of neighbours in distance 1 from centre is 8
		assertTrue(centreNeighbours1.size() == 8);
		
		// test that number of neighbours in distance 3 from corner is 15
		ILocation corner = gameMap.getLocation(19, 19);
		List<ILocation> cornerNeighbours = gameMap.getNeighbourhood(corner, 3);
		assertTrue(cornerNeighbours.size() == 15);
		
		// test that number of neighbours in distance 2 from edge is 14
		ILocation edge = gameMap.getLocation(19, 10);
		List<ILocation> edgeNeighbours = gameMap.getNeighbourhood(edge, 2);
		assertTrue(edgeNeighbours.size() == 14);
		
		// test that the list of neighbours are sorted in order of increasing distance from centre
		for (int i=0; i<centreNeighbours.size()-1; i++) {
			assertTrue(centreNeighbours.get(i).gridDistanceTo(centre) <= centreNeighbours.get(i+1).gridDistanceTo(centre));
		}
		
	}
}
