package main;

import java.awt.Color;
import java.awt.Graphics;

import bases.Line;
import bases.ScreenPoint;
import bases.SquareIcon;
import frame.DrawableCanvas;
import frame.MainFrame;
import frame.MiniGame;
import statics.Global;
import statics.MurderSets;

/**
 * Main input:
 *
 * Arrows : Walk around : Enter Rooms
 *
 * C : Menu : Phone, Past weapons, NotePad, save game?
 *
 * X : Inspect Room : Enter Room Menu
 *
 * Z : Interact : Find weapon
 *
 * @author danv
 */
public class MainCanvas extends MiniGame {
	private static final int amountMove = 7;

	public static boolean DEBUG = false;
	private final ScreenPoint center = new ScreenPoint();
	// TODO Make a little musical thingie. I know how much you like that.
	private final Menu menu;
	private Room room;

	private final You u = new You();

	/**
	 * This is basically The Game™
	 */
	public MainCanvas(MainFrame main) {
		super(main);

		if (Global.positions.length() < Person.chosen.length + Weapon.chosen.length + Room.chosen.length)
			throw new IndexOutOfBoundsException("Global Positions is not Enough. Please increase ScreenSize");

		// Make the set of answers and what not that we will be checking against
		MurderSets.init();

		// Creates the Main Game Menu (The one with the phone and the weapons)
		menu = new Menu(parent);

		// Pick a random room and let's start this game
		spawnToCenter(Room.rand());

		if (DEBUG) {
			System.out.println();
			Global.print(Person.chosen);
			Global.print(Weapon.chosen);
			Global.print(Room.chosen);
		}
	}

	/**
	 * Changes the active room to newroom and moves you by the transform.
	 * Transform ScreenPoint is acting like a vector. Since Rooms are their own
	 * separate spaces the alignment of (de)spawn points is key.
	 */
	private void changeRoom(Room newroom) {
		menu.visitRoom(newroom);
		newroom.addCamera(u);
		newroom.makeActive(room);
		room = newroom;
	}

	/**
	 * Check if you are banging against a wall Check if you are on wrong side of
	 * a wall Check if you are banging into an object All that good stuff
	 */
	private void collision() {
		ScreenPoint p = u;
		ScreenPoint s = new ScreenPoint(); // Accumulate movement into this
		// vector point
		for (int i = 0; i < 4; i++) {
			Line l = room.lines[i]; // For each line
			double d = l.distance(p);
			boolean positivity = d > 0;
			if (positivity != room.positivity(i))
				// side
				if (room.connectedTo[i] == null || !room.spawns[i].containsPoint(p))
					s.moveBy(d * l.xN, d * l.yN);
				else
					changeRoom(room.connectedTo[i]);
		}
		// Get boomfed slowly
		room.weapons.stream().filter(d -> d.containsPoint(p)).forEach(d -> {
			ScreenPoint dd = p.minus(d);
			s.moveBy(dd.x / 4, dd.y / 4); // Get boomfed slowly
		});

		if (s.x != 0 || s.y != 0)
			u.moveBy(s);
	}

	@Override
	public void gainControl(DrawableCanvas listenerController) {
		setTitle("");
		keys = listenerController.activateDefaultKeyListener();
	}

	private void keylisten() {
		if (keys.DOWN)
			u.moveDownBy(amountMove);
		if (keys.UP)
			u.moveUpBy(amountMove);
		if (keys.LEFT)
			u.moveLeftBy(amountMove);
		if (keys.RIGHT)
			u.moveRightBy(amountMove);
		if (keys.ENTER) {
			pickupobject();
			return;
		}
		if (keys.MENU) {
			openMenu();
			return;
		}
		if (keys.X)
			openRoomMenu();
	}

	private void openMenu() {
		menu.setBackground(room);
		addCanvas(menu);
	}

	private void openRoomMenu() {
		if (MurderSets.getAmount(room) == 0)
			menu.addClue(new Clue(Clue.ROOM, new MurderSet(null, null, null, room)));
		else
			menu.prepare(room);
	}

	@Override
	public void painting(Graphics g) {
		g.fillRect(0, 0, width, height);

		// The room draws itself.
		room.draw(g, center);
		// You are part a drawable in the room as well

		if (DEBUG) {
			g.setColor(Color.RED);
			u.clone().draw(g);
			g.setColor(Color.CYAN);
			g.fillOval(width / 2 - 3, height / 2 - 3, 6, 6);
		}
	}

	/**
	 * Check if you are near an object. If you are pick it up if you can.
	 */
	private void pickupobject() {
		ScreenPoint p = u;

		for (Weapon w : room.weapons)
			if (w.distanceSquared(p) < 2 * SquareIcon.WH * SquareIcon.WH) {
				// Imagine circle is slightly larger
				room.remove(w);
				if (MurderSets.getAmount(w) == 0)
					menu.addClue(new Clue(Clue.WEAPON, new MurderSet(null, null, w, null)));
				else
					menu.addWeapon(w);
				return;
			}

		// This is just magic randomness cause I'm a butt.
		// If you dont pick something up. Randomly move to new point in room.
		// changeRoom(room,
		// room.spawns[Room.Center].randomPointInCircle().minus(p));
	}

	@Override
	public void prepaint() {
		keylisten();
		collision();
	}

	@Override
	public void resized() {
		center.moveTo(-width / 2, -height / 2);
	}

	/**
	 * I am pretty sure you only need to call this once when you first
	 * initialize the MainCanvas It resets the camera and places the room under
	 * the camera.
	 */
	private void spawnToCenter(Room r) {
		// Since at (0,0) the transform point is just where you want to go
		changeRoom(r);
	}
}
