package inf101.v18.rogue101.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

import inf101.v18.gfx.Screen;
import inf101.v18.gfx.gfxmode.ITurtle;
import inf101.v18.gfx.gfxmode.TurtlePainter;
import inf101.v18.gfx.textmode.Printer;
import inf101.v18.grid.GridDirection;
import inf101.v18.grid.IGrid;
import inf101.v18.grid.ILocation;
import inf101.v18.rogue101.Main;
import inf101.v18.rogue101.examples.Carrot;
import inf101.v18.rogue101.examples.Mushroom;
import inf101.v18.rogue101.examples.Rabbit;
import inf101.v18.rogue101.map.GameMap;
import inf101.v18.rogue101.map.IGameMap;
import inf101.v18.rogue101.map.IMapView;
import inf101.v18.rogue101.map.MapReader;
import inf101.v18.rogue101.objects.AncientMachine;
import inf101.v18.rogue101.objects.Armour;
import inf101.v18.rogue101.objects.Battery;
import inf101.v18.rogue101.objects.DepletedCrystal;
import inf101.v18.rogue101.objects.Dust;
import inf101.v18.rogue101.objects.Exit;
import inf101.v18.rogue101.objects.IActor;
import inf101.v18.rogue101.objects.IItem;
import inf101.v18.rogue101.objects.INonPlayer;
import inf101.v18.rogue101.objects.IPlayer;
import inf101.v18.rogue101.objects.Key;
import inf101.v18.rogue101.objects.MonsterNS;
import inf101.v18.rogue101.objects.MonsterR;
import inf101.v18.rogue101.objects.MonsterWE;
import inf101.v18.rogue101.objects.Sword;
import inf101.v18.rogue101.objects.Wall;
import inf101.v18.rogue101.player.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

public class Game implements IGame {
	/**
	 * All the IActors that have things left to do this turn
	 */
	private List<IActor> actors = Collections.synchronizedList(new ArrayList<>());
	/**
	 * For fancy solution to factory problem
	 */
	private Map<String, Supplier<IItem>> itemFactories = new HashMap<>();
	/**
	 * Useful random generator
	 */
	private Random random = new Random();
	/**
	 * The game map. {@link IGameMap} gives us a few more details than
	 * {@link IMapView} (write access to item lists); the game needs this but
	 * individual items don't.
	 */
	private IGameMap map;
	private IActor currentActor;
	private ILocation currentLocation;
	private int movePoints = 0;
	private final ITurtle painter;
	private final Printer printer;
	private int numPlayers = 0;
	
	//true if game is waiting for user to choose an option in menu
	private boolean options;
	
	private String[] log;
	
	//track which map (level) user is on
	private int currentLevel = 1;
	
	// track player location
	private ILocation playerLoc;

	private boolean gameOver = false;

	public Game(Screen screen, ITurtle painter, Printer printer) {
		this.painter = painter;
		this.printer = printer;
		this.options = false;

		// TODO: (*very* optional) for advanced factory technique, use
		// something like "itemFactories.put("R", () -> new Rabbit());"
		// must be done *before* you read the map

		// NOTE: in a more realistic situation, we will have multiple levels (one map
		// per level), and (at least for a Roguelike game) the levels should be
		// generated
		//
		// inputGrid will be filled with single-character strings indicating what (if
		// anything)
		// should be placed at that map square
		IGrid<String> inputGrid = MapReader.readFile("maps/startlevel.txt");
		if (inputGrid == null) {
			System.err.println("Map not found – falling back to builtin map");
			inputGrid = MapReader.readString(Main.BUILTIN_MAP);
		}
		this.map = new GameMap(inputGrid.getArea());
		for (ILocation loc : inputGrid.locations()) {
			IItem item = createItem(inputGrid.get(loc));
			if (item != null) {
				map.add(loc, item);
			}
			
			if (item instanceof IPlayer)
				playerLoc = map.getLocation(item);
		}
		
		initializeLog();
		
	}

	public Game(String mapString) {
		printer = new Printer(1280, 720);
		painter = new TurtlePainter(1280, 720);
		IGrid<String> inputGrid = MapReader.readString(mapString);
		this.map = new GameMap(inputGrid.getArea());
		for (ILocation loc : inputGrid.locations()) {
			IItem item = createItem(inputGrid.get(loc));
			if (item != null) {
				map.add(loc, item);
			}
		}
		
		initializeLog();
		
	}
	
	
	public void initializeLog() {
		this.log = new String[20];
		// initialise log
		for (int i=0; i<log.length; i++) {
			log[i] = "";
		}
		
		// give game-intro and controller instructions
		String[] instructions = new String[18];
		instructions[0] = "You find yourself on an alien planet,";
		instructions[1] = "sent by the humans 👨‍🚀 of earth to ";
		instructions[2] = "find a cure for entropy.";
		instructions[3] = "";
		instructions[4] = "The humans believe this planet was ";
		instructions[5] = "once inhabited by an ancient race, ";
		instructions[6] = "which was way ahead of our technological";
		instructions[7] = "level. But now, there are only remnants,";
		instructions[8] = "and a primitive civilization.";
		instructions[9] = "";
		instructions[10] = "Controls:";
		instructions[11] = "Arrows = move";
		instructions[12] = "I = show inventory";
		instructions[13] = "C = drop item";
		instructions[14] = "P = pick up item";
		instructions[15] = "X = attack";
		instructions[16] = "E = equip an item";
		instructions[17] = "Find a key and use it to advance.";
		displayOptions(instructions);
	}
	
	@Override
	public void addItem(IItem item) {
		map.add(currentLocation, item);
	}
	
	public void gameOver() {
		gameOver = true;
	}

	@Override
	public void addItem(String sym) {
		IItem item = createItem(sym);
		if (item != null)
			map.add(currentLocation, item);
	}

	@Override
	public ILocation attack(GridDirection dir, IItem target) {
		ILocation loc = getLocation(dir);
		if (!map.has(loc, target))
			throw new IllegalMoveException("Target isn't there!");
		
		int attackRoll = random.nextInt(20)+1+currentActor.getAttack();

		String[] s = new String[5];
		
		s[0] = "Attack!";
		s[1] = String.format("%s rolled %d against" , currentActor.getName(), attackRoll);
		s[2] = String.format("%s's armor class of %d.", target.getName(), target.getDefence());
		
		if (attackRoll >= target.getDefence()) {
			//hit
			target.handleDamage(this, currentActor, currentActor.getDamage());
			s[3] = "Success!";
			s[4] = String.format("%s takes %d damage.", target.getName(), currentActor.getDamage());
			displayOptions(s);
			map.clean(loc);
			
		} else {
			//miss
			s[3] = "Failed!";
			s[4] = String.format("%s deflects %s's attack", target.getName(), currentActor.getName());
			displayOptions(s);
		}
		
		if (target.isDestroyed()) {
			return move(dir);
		} else {
			movePoints--;
			return currentLocation;
		}
	}

	/**
	 * Begin a new game turn, or continue to the previous turn
	 * 
	 * @return True if the game should wait for more user input
	 */
	public boolean doTurn() {
		if (gameOver) return false;
		
		do {
			
			if (actors.isEmpty()) {
				// System.err.println("new turn!");

				// no one in the queue, we're starting a new turn!
				// first collect all the actors:
				beginTurn();
			}

			// process actors one by one; for the IPlayer, we return and wait for keypresses
			// Possible TODO: for INonPlayer, we could also return early (returning
			// *false*), and then insert a little timer delay between each non-player move
			// (the timer
			// is already set up in Main)
			while (!actors.isEmpty()) {
				// get the next player or non-player in the queue
				currentActor = actors.remove(0);
				if (currentActor.isDestroyed()) // skip if it's dead
					continue;
				currentLocation = map.getLocation(currentActor);
				if (currentLocation == null) {
					displayDebug("doTurn(): Whoops! Actor has disappeared from the map: " + currentActor);
				}
				movePoints = 1; // everyone gets to do one thing

				if (currentActor instanceof INonPlayer) {
					// computer-controlled players do their stuff right away
					((INonPlayer) currentActor).doTurn(this);
					// remove any dead items from current location
					map.clean(currentLocation);
				} else if (currentActor instanceof IPlayer) {
					if (currentActor.isDestroyed()) {
						// a dead human player gets removed from the game
						// TODO: you might want to be more clever here
						displayMessage("YOU DIE!!!");
						map.remove(currentLocation, currentActor);
						currentActor = null;
						currentLocation = null;
					} else {
						
						playerLoc = currentLocation;
						
						for (IItem item : getLocalItems()) {
							if (item instanceof DepletedCrystal) {
								String[] s = new String[]{"You've found an Eternity Crystal, ",
										"but it seems to be depleted.. somehow.",
										"I wonder how these aliens could use",
										"up all that energy?",
										"and what did they use it for?"};
								displayOptions(s);
							}
						}
						
						//check if player is on exit location and has a key
						if (getLocalItems().size() > 0)
							if (getLocalItems().get(0) instanceof Exit && ((IPlayer)currentActor).useKey())
								newLevel(++currentLevel);
						
						
						
						// For the human player, we need to wait for input, so we just return.
						// Further keypresses will cause keyPressed() to be called, and once the human
						// makes a move, it'll lose its movement point and doTurn() will be called again
						//
						// NOTE: currentActor and currentLocation are set to the IPlayer (above),
						// so the game remembers who the player is whenever new keypresses occur. This
						// is also how e.g., getLocalItems() work – the game always keeps track of
						// whose turn it is.
						return true;
					}
				} else {
					displayDebug("doTurn(): Hmm, this is a very strange actor: " + currentActor);
				}
				
				// A3 Bedre Gulrøtter c)
				//possibly add a new Carrot (if there is no carrot at chosen random loc, and there are less than 9 items at loc)
//				A: if (random.nextInt(100) < 5) {
//					ILocation newCarrotLoc = map.getLocation(random.nextInt(getWidth()), random.nextInt(getHeight()));
//					if (map.canGo(newCarrotLoc) && map.getItems(newCarrotLoc).size() < 9) {
//						for (IItem item : getItems(newCarrotLoc)) {
//							if (item instanceof Carrot)
//								break A;
//						}
//						map.add(newCarrotLoc, new Carrot());
//					}
//				}
					
			}
		} while (numPlayers > 0); // we can safely repeat if we have players, since we'll return (and break out of
									// the loop) once we hit the player
		return true;
	}

	private void newLevel(int n) {
		
		//save player object
		IItem player = currentActor;
		
		//clear actors from previous level
		actors.clear();
		currentActor = null;
		currentLocation = null;
		
		IGrid<String> inputGrid = MapReader.readFile("maps/level" + n + ".txt");
		if (inputGrid == null) {
			System.err.println("Map not found – falling back to builtin map");
			inputGrid = MapReader.readString(Main.BUILTIN_MAP);
		}
		this.map = new GameMap(inputGrid.getArea());
		
		IItem item = null;
		for (ILocation loc : inputGrid.locations()) {
			if (inputGrid.get(loc).equals("@")) {
				item = player;
				playerLoc = loc;
			} else
				item = createItem(inputGrid.get(loc));
			
//			item = createItem(inputGrid.get(loc));
//			
//			if (item instanceof IPlayer) {
//				item = player;
//			}
			
			if (item != null) {
				map.add(loc, item);
			}
		}
		
		displayMessage("Loaded level " + n);
//		draw();
//		beginTurn();
	}
	
	/**
	 * Go through the map and collect all the actors.
	 */
	private void beginTurn() {
		numPlayers = 0;
		// this extra fancy iteration over each map location runs *in parallel* on
		// multicore systems!
		// that makes some things more tricky, hence the "synchronized" block and
		// "Collections.synchronizedList()" in the initialization of "actors".
		// NOTE: If you want to modify this yourself, it might be a good idea to replace
		// "parallelStream()" by "stream()", because weird things can happen when many
		// things happen
		// at the same time! (or do INF214 or DAT103 to learn about locks and threading)
		map.getArea().parallelStream().forEach((loc) -> { // will do this for each location in map
			List<IItem> list = map.getAllModifiable(loc); // all items at loc
			Iterator<IItem> li = list.iterator(); // manual iterator lets us remove() items
			while (li.hasNext()) { // this is what "for(IItem item : list)" looks like on the inside
				IItem item = li.next();
				if (item.getCurrentHealth() < 0) {
					// normally, we expect these things to be removed when they are destroyed, so
					// this shouldn't happen
					synchronized (this) {
//						formatDebug("beginTurn(): found and removed leftover destroyed item %s '%s' at %s%n",
//								item.getName(), item.getSymbol(), loc);
					}
					li.remove();
					map.remove(loc, item); // need to do this too, to update item map
				} else if (item instanceof IPlayer) {
					actors.add(0, (IActor) item); // we let the human player go first
					synchronized (this) {
						numPlayers++;
					}
				} else if (item instanceof IActor) {
					actors.add((IActor) item); // add other actors to the end of the list
				}
			}
		});
	}

	@Override
	public boolean canGo(GridDirection dir) {
		return map.canGo(currentLocation, dir);
	}

	@Override
	public IItem createItem(String sym) {
		switch (sym) {
		case "#":
			return new Wall();
		case ".":
			return new Dust();
		case "R":
			return new Rabbit();
		case "C":
			return new Carrot();
		case "M":
			return new Mushroom();
		case "@":
			return new Player();
		case "E":
			return new Exit();
		case "K":
			return new Key();
		case "N":
			return new MonsterNS(this);
		case "W":
			return new MonsterWE(this);
		case "D":
			return new MonsterR(this);
		case "S":
			return new Sword(currentLevel);
		case "B":
			return new Battery(currentLevel);
		case "A":
			return new Armour(currentLevel);
		case "H":
			return new DepletedCrystal();
		case "T":
			return new AncientMachine();
		case " ":
			return null;
		default:
			// alternative/advanced method
			Supplier<IItem> factory = itemFactories.get(sym);
			if (factory != null) {
				return factory.get();
			} else {
				System.err.println("createItem: Don't know how to create a '" + sym + "'");
				return null;
			}
		}
	}

	@Override
	public void displayDebug(String s) {
		printer.clearLine(Main.LINE_DEBUG);
		printer.printAt(1, Main.LINE_DEBUG, s, Color.DARKRED);
		System.err.println(s);
	}

	@Override
	public void displayMessage(String s) {
		// it should be safe to print to lines Main.LINE_MSG1, Main.LINE_MSG2,
		// Main.LINE_MSG3
		// TODO: you can save the last three lines, and display/scroll them
		printer.clearLine(Main.LINE_MSG1);
		printer.printAt(1, Main.LINE_MSG1, s);
		System.out.println("Message: «" + s + "»");
	}

	@Override
	public void displayStatus(String s) {
		printer.clearLine(Main.LINE_STATUS);
		printer.printAt(1, Main.LINE_STATUS, s);
		System.out.println("Status: «" + s + "»");
	}
	
	@Override
	public void displayOptions(String[] s) {
		
		// move all text down to make room for new String s
		for (int i=19; i>s.length; i--) {
			log[i] = log[i-s.length-1];
		}
		
		log[s.length] = "---------------------------------";
		
		// put new String s on top of the display
		for (int i=0; i<s.length; i++) {
			log[i] = s[i];
		}
		
		// print log
		for (int i=0; i<20; i++) {
			printer.clearLine(i+1);
			printer.printAt(41, i+1, log[i]);
			if (log[i].length()>0)
				System.out.println("Log: «" + log[i] + "»");
		}
	}
	
	public void clearMessages() {
//		for (int i=1;i<20;i++) {
//			printer.clearLine(i);
//		}
		printer.clearLine(Main.LINE_MSG1);
	}
	
	// used by PlayerTest.java
	public String getLogLine(int i) {
		return log[i];
	}
	
	public int getCurrentLevel() {
		return currentLevel;
	}

	public void draw() {
		
//		System.out.println(playerLoc);
//		System.out.println(getActorAt(playerLoc).getName());
//		System.out.println(map.getVisibleLocs(playerLoc, 2));
		
		map.draw(painter, printer, map.getVisibleLocs(playerLoc, ((Player) getActorAt(playerLoc)).getViewRange()));
	}

	@Override
	public boolean drop(IItem item) {
		if (item != null) {
			map.add(currentLocation, item);
			return true;
		} else
			return false;
	}

	@Override
	public void formatDebug(String s, Object... args) {
		displayDebug(String.format(s, args));
	}

	@Override
	public void formatMessage(String s, Object... args) {
		displayMessage(String.format(s, args));
	}

	@Override
	public void formatStatus(String s, Object... args) {
		displayStatus(String.format(s, args));
	}

	@Override
	public int getHeight() {
		return map.getHeight();
	}

	@Override
	public List<IItem> getLocalItems() {
		return map.getItems(currentLocation);
	}
	
	@Override
	public List<IItem> getItems(ILocation loc) {
		return map.getItems(loc);
	}

	@Override
	public ILocation getLocation() {
		return currentLocation;
	}

	@Override
	public ILocation getLocation(GridDirection dir) {
		if (currentLocation.canGo(dir))
			return currentLocation.go(dir);
		else
			return null;
	}
	
	@Override
	public List<IItem> allItemsActors(ILocation loc) {
		List<IItem> itemsActors = getItems(loc);
		
		// should only be one actor per location
		if (map.hasActors(loc))
			itemsActors.add((IItem)map.getActors(loc).get(0));
		
		return itemsActors;	
	}

	/**
	 * Return the game map. {@link IGameMap} gives us a few more details than
	 * {@link IMapView} (write access to item lists); the game needs this but
	 * individual items don't.
	 */
	@Override
	public IMapView getMap() {
		return map;
	}

	@Override
	public List<GridDirection> getPossibleMoves() {
		
		List<GridDirection> possibleMoves = new ArrayList<>();
		
		for (GridDirection dir : GridDirection.FOUR_DIRECTIONS) {
			if (map.canGo(currentLocation, dir))
				possibleMoves.add(dir);
		}
		
		return possibleMoves;
		
//		throw new UnsupportedOperationException();
	}

	@Override
	public List<ILocation> getVisible() {
		// TODO: maybe replace 3 by some sort of visibility range obtained from
		// currentActor?
		return map.getNeighbourhood(currentLocation, 1);
	}

	@Override
	public int getWidth() {
		return map.getWidth();
	}

	
	public boolean getOptions() {
		return options;
	}
	
	public void changeOptions() {
		options = !options;
	}
	
	public boolean keyPressed(KeyCode code) {
		// only an IPlayer/human can handle keypresses, and only if it's the human's
		// turn
		
		// return true if player is not done with turn
		if (currentActor instanceof IPlayer) {
			if (currentActor.getCurrentHealth()<= 0)
				return true;
			((IPlayer) currentActor).keyPressed(this, code); // do your thing
			return movePoints > 0;
		}
		
		return false;
	}

	@Override
	public ILocation move(GridDirection dir) {
		if (movePoints < 1)
			throw new IllegalMoveException("You're out of moves!");
		ILocation newLoc = map.go(currentLocation, dir);
		map.remove(currentLocation, currentActor);
		map.add(newLoc, currentActor);
		currentLocation = newLoc;
		movePoints--;
		return currentLocation;
	}

	@Override
	public IItem pickUp(IItem item) {
		if (item != null && map.has(currentLocation, item)) {
			
			if (currentActor.getAttack()*10+40 >= item.getWeight()) {
				map.remove(currentLocation, item);
				return item;
			} else {
				displayMessage("You can't pick that up. Maybe you're not strong enough.");
				return null;
			}
			
		} else {
			displayMessage("There's nothing there.");
			return null;
		}
	}

	@Override
	public ILocation rangedAttack(GridDirection dir, IItem target) {
		return currentLocation;
	}

	@Override
	public ITurtle getPainter() {
		return painter;
	}

	@Override
	public Printer getPrinter() {
		return printer;
	}

	@Override
	public int[] getFreeTextAreaBounds() {
		int[] area = new int[4];
		area[0] = getWidth() + 1;
		area[1] = 1;
		area[2] = printer.getLineWidth();
		area[3] = printer.getPageHeight() - 5;
		return area;
	}

	@Override
	public void clearFreeTextArea() {
		printer.clearRegion(getWidth() + 1, 1, printer.getLineWidth() - getWidth(), printer.getPageHeight() - 5);
	}

	@Override
	public void clearFreeGraphicsArea() {
		painter.as(GraphicsContext.class).clearRect(getWidth() * printer.getCharWidth(), 0,
				painter.getWidth() - getWidth() * printer.getCharWidth(),
				(printer.getPageHeight() - 5) * printer.getCharHeight());
	}

	@Override
	public double[] getFreeGraphicsAreaBounds() {
		double[] area = new double[4];
		area[0] = getWidth() * printer.getCharWidth();
		area[1] = 0;
		area[2] = painter.getWidth();
		area[3] = getHeight() * printer.getCharHeight();
		return area;
	}

	@Override
	public IActor getActor() {
		return currentActor;
	}
	
	@Override
	public IActor getActorAt(ILocation loc) {
		if (map.getActors(loc).size() > 0)
			return map.getActors(loc).get(0);
		else
			return null;
	}

	public ILocation setCurrent(IActor actor) {
		currentLocation = map.getLocation(actor);
		if (currentLocation != null) {
			currentActor = actor;
			movePoints = 1;
		}
		return currentLocation;
	}

	public IActor setCurrent(ILocation loc) {
		List<IActor> list = map.getActors(loc);
		if (!list.isEmpty()) {
			currentActor = list.get(0);
			currentLocation = loc;
			movePoints = 1;
		}
		return currentActor;
	}

	public IActor setCurrent(int x, int y) {
		return setCurrent(map.getLocation(x, y));
	}
	
	@Override
	public Random getRandom() {
		return random;
	}
}
