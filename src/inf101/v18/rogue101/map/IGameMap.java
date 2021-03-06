package inf101.v18.rogue101.map;

import java.util.List;
import java.util.Set;

import inf101.v18.gfx.gfxmode.ITurtle;
import inf101.v18.gfx.textmode.Printer;
import inf101.v18.grid.ILocation;
import inf101.v18.rogue101.objects.IItem;

/**
 * Extra map methods that are for the game class only!
 * 
 * @author anya
 *
 */
public interface IGameMap extends IMapView {

	/**
	 * Draw the map
	 * 
	 * @param painter
	 * @param printer
	 */
	void draw(ITurtle painter, Printer printer, Set<ILocation> visible);

	/**
	 * Get a modifiable list of items
	 * 
	 * @param loc
	 * @return
	 */
	List<IItem> getAllModifiable(ILocation loc);

	/**
	 * Remove any destroyed items at the given location (items where {@link IItem#isDestroyed()} is true)
	 * 
	 * @param loc
	 */
	void clean(ILocation loc);

	/**
	 * Remove an item
	 * @param loc
	 * @param item
	 */
	void remove(ILocation loc, IItem item);

	/**
	 * get all locations in a simplified circle around the loc
	 * 
	 * @param loc
	 * center of circle
	 * @param dist
	 * approx radius of "circle"
	 * @return Set<ILocation>
	 */
	Set<ILocation> getVisibleLocs(ILocation loc, int dist);

}
