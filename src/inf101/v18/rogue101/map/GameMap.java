package inf101.v18.rogue101.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import inf101.v18.gfx.gfxmode.ITurtle;
import inf101.v18.gfx.textmode.Printer;
import inf101.v18.grid.GridDirection;
import inf101.v18.grid.IArea;
import inf101.v18.grid.ILocation;
import inf101.v18.grid.IMultiGrid;
import inf101.v18.grid.MultiGrid;
import inf101.v18.rogue101.Main;
import inf101.v18.rogue101.game.IllegalMoveException;
import inf101.v18.rogue101.objects.IActor;
import inf101.v18.rogue101.objects.IItem;
import inf101.v18.rogue101.objects.Wall;
import javafx.scene.paint.Color;
import javafx.scene.canvas.GraphicsContext;

public class GameMap implements IGameMap {
	/**
	 * The grid that makes up our map
	 */
	private final IMultiGrid<IItem> grid;
	/**
	 * These locations have changed, and need to be redrawn
	 */
	private final Set<ILocation> dirtyLocs = new HashSet<>();

	/**
	 * An index of all the items in the map and their locations.
	 */
	// an IdentityHashMap uses object identity as a lookup key, so items are
	// considered equal if they are the same object (a == b)
	private final Map<IItem, ILocation> items = new IdentityHashMap<>();

	public GameMap(IArea area) {
		grid = new MultiGrid<>(area);
	}

	public GameMap(int width, int height) {
		grid = new MultiGrid<>(width, height);
	}

	@Override
	public void add(ILocation loc, IItem item) {
		// keep track of location of all items
		items.put(item, loc);
		// also keep track of whether we need to redraw this cell
		dirtyLocs.add(loc);

		// do the actual adding
		List<IItem> list = grid.get(loc);
		
		// finds first smaller item in list, and inserts at that point
		// if no smaller item is found, insert at end
		for (int i=0; i<list.size(); i++) {
			if (list.get(i).compareTo(item) < 0) {
				list.add(i, item);
				return;
			}
		}
		
		list.add(list.size(), item);
	}

	@Override
	public boolean canGo(ILocation to) {
		return !grid.contains(to, (i) -> (i instanceof Wall || i instanceof IActor));
	}

	@Override
	public boolean hasNeighbour(ILocation from, GridDirection dir) {
		return from.canGo(dir);
	}

	@Override
	public boolean canGo(ILocation from, GridDirection dir) {
		if (!from.canGo(dir))
			return false;
		ILocation loc = from.go(dir);
		return canGo(loc);
	}

	@Override
	public void draw(ITurtle painter, Printer printer, Set<ILocation> visible) {
		Iterable<ILocation> cells;
		if (Main.MAP_DRAW_ONLY_DIRTY_CELLS) {
			if (dirtyLocs.isEmpty())
				return;
			else
				cells = dirtyLocs;
		} else {
			cells = grid.locations();
			painter.as(GraphicsContext.class).clearRect(0, 0, getWidth() * printer.getCharWidth(),
					getHeight() * printer.getCharHeight());
			printer.clearRegion(1, 1, getWidth(), getHeight());
		}
		GraphicsContext ctx = painter.as(GraphicsContext.class);
		double h = printer.getCharHeight();
		double w = printer.getCharWidth();
		if (Main.MAP_AUTO_SCALE_ITEM_DRAW) {
			ctx.save();
			ctx.scale(w / h, 1.0);
			w = h;
		}
		try {
			for (ILocation loc : cells) {
				List<IItem> list = grid.get(loc);
				String sym = " ";
				if (!list.isEmpty()) {
					if (Main.MAP_DRAW_ONLY_DIRTY_CELLS) {
						ctx.clearRect(loc.getX() * w, loc.getY() * h, w, h);
						// ctx.fillRect(loc.getX() * w, loc.getY() * h, w, h);
					}
					painter.save();
					painter.jumpTo((loc.getX() + 0.5) * w, (loc.getY() + 0.5) * h);
					boolean dontPrint = list.get(0).draw(painter, w, h);
					painter.restore();
					if (!dontPrint) {
						sym = list.get(0).getPrintSymbol();
					}
				}
				
				printer.printAt(loc.getX() + 1, loc.getY() + 1, sym);
				
				// fill loc with black if not within visible range
				if (!visible.contains(loc)) {
					painter.jumpTo((loc.getX() + 0.5) * w, (loc.getY() + 0.5) * h);
					painter.shape().rectangle().width(w).height(h).fillPaint(Color.BLACK).fill();
				}
				
			}
		} finally {
			if (Main.MAP_AUTO_SCALE_ITEM_DRAW) {
				ctx.restore();
			}
		}
		dirtyLocs.clear();
	}

	@Override
	public List<IActor> getActors(ILocation loc) {
		List<IActor> items = new ArrayList<>();
		for (IItem item : grid.get(loc)) {
			if (item instanceof IActor)
				items.add((IActor) item);
		}

		return items;
	}

	@Override
	public List<IItem> getAll(ILocation loc) {
		return Collections.unmodifiableList(grid.get(loc));
	}

	@Override
	public List<IItem> getAllModifiable(ILocation loc) {
		dirtyLocs.add(loc);
		return grid.get(loc);
	}

	@Override
	public void clean(ILocation loc) {
		// remove any items that have health < 0:
		if (grid.get(loc).removeIf((item) -> {
			if (item.isDestroyed()) {
				items.remove(item);
				return true;
			} else {
				return false;
			}
		})) {
			dirtyLocs.add(loc);
		}
	}

	@Override
	public IArea getArea() {
		return grid.getArea();
	}

	@Override
	public int getHeight() {
		return grid.getHeight();
	}

	@Override
	public List<IItem> getItems(ILocation loc) {
		List<IItem> items = new ArrayList<>(grid.get(loc));
		items.removeIf((i) -> i instanceof IActor);
		return items;
	}

	@Override
	public ILocation getLocation(IItem item) {
		return items.get(item);
	}

	@Override
	public ILocation getLocation(int x, int y) {
		return grid.getArea().location(x, y);
	}

	@Override
	public ILocation getNeighbour(ILocation from, GridDirection dir) {
		if (!hasNeighbour(from, dir))
			return null;
		else
			return from.go(dir);
	}

	@Override
	public int getWidth() {
		return grid.getWidth();
	}

	@Override
	public ILocation go(ILocation from, GridDirection dir) throws IllegalMoveException {
		if (!from.canGo(dir))
			throw new IllegalMoveException("Cannot move outside map!");
		ILocation loc = from.go(dir);
		if (!canGo(loc))
			throw new IllegalMoveException("Occupied!");
		return loc;
	}

	@Override
	public boolean has(ILocation loc, IItem target) {
		return grid.contains(loc, target);
	}

	@Override
	public boolean hasActors(ILocation loc) {
		return grid.contains(loc, (i) -> i instanceof IActor);
	}

	@Override
	public boolean hasItems(ILocation loc) {
		// true if grid cell contains an item which is not an IActor
		return grid.contains(loc, (i) -> !(i instanceof IActor));
	}

	@Override
	public boolean hasWall(ILocation loc) {
		return grid.contains(loc, (i) -> i instanceof Wall);
	}

	@Override
	public void remove(ILocation loc, IItem item) {
		grid.remove(loc, item);
		items.remove(item);
		dirtyLocs.add(loc);
	}

	@Override
	public Set<ILocation> getVisibleLocs(ILocation loc, int dist) {
		Set<ILocation> visible = new HashSet<ILocation>(getNeighbourhood(loc, dist));
		
		// add players loc
		visible.add(loc);
		
		Iterator<ILocation> it = visible.iterator();
		
		while (it.hasNext()) {
			if (it.next().geometricDistanceTo(loc) > dist+0.5)
				it.remove();
		}
		
		return visible;
	}
	
	@Override
	public List<ILocation> getNeighbourhood(ILocation loc, int dist) {
		if (dist < 0 || loc == null)
			throw new IllegalArgumentException();
		else if (dist == 0)
			return new ArrayList<>(); // empty!

		List<ILocation> locs = new ArrayList<>();
		
		// make sure x and y are not outside grid on negative side
		int startX = loc.getX()-dist >= 0 ? loc.getX()-dist : 0;
		int startY = loc.getY()-dist >= 0 ? loc.getY()-dist : 0;
		
		// get all locations in the form of a square with sides 1+dist*2 centered on loc, excluding locations outside grid
		for (int x=startX; x<=loc.getX()+dist && x<grid.getWidth(); x++) {
			for (int y=startY; y<=loc.getY()+dist && y<grid.getHeight(); y++) {
				if (!(x == loc.getX() && y == loc.getY())) {
					locs.add(getLocation(x, y));
				}
			}
		}
		
		if (dist > 1) 
			locs = sortNeighbourhood(locs, loc);
		
		return locs;
//		throw new UnsupportedOperationException();
	}

	private List<ILocation> sortNeighbourhood(List<ILocation> locs, ILocation loc) {
		List<ILocation> sortedLocs = new ArrayList<>();
		sortedLocs.add(locs.get(0));
		
		// insertion sort algorithm based on the locations' distances to loc
		for (int i=1; i<locs.size(); i++) {
			A: for (int j=0; j<sortedLocs.size(); j++) {
				if (locs.get(i).gridDistanceTo(loc) <= sortedLocs.get(j).gridDistanceTo(loc)) {
					sortedLocs.add(j, locs.get(i));
					break A;
				}
			}
		}
		
		return sortedLocs;
	}

}











