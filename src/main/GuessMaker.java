package main;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import bases.ScreenPoint;
import bases.SquareIcon;
import frame.DrawableCanvas;
import frame.MainFrame;
import frame.MiniGame;
import statics.Global;

/**
 * A little user interface for making guesses.
 *
 * @author danv
 */
public class GuessMaker extends MiniGame {
	private static final int KILLER = 1, VICTIM = 0, WEAPON = 2, ROOM = 3;

	private static int IconType(SquareIcon ic) {
		/*
		 * There are 3 types of Icons People can go in the Victim and Killer
		 * slot
		 */
		if (ic instanceof Person)
			return VICTIM;
		if (ic instanceof Weapon)
			return WEAPON;
		return ROOM;
	}

	private int activeMenuType;
	private final ArrayList<SquareIcon> draggables = new ArrayList<>();

	private final SquareIcon GO = SquareIcon.get("go");
	private int iconDragType = -1;
	private SquareIcon itemBeingDragged;

	final NotePad note;
	private final Person[] people = Person.chosen;
	private final ArrayList<RoomIcon> rooms = new ArrayList<>();
	private final ArrayList<Weapon> wepons = new ArrayList<>();

	private final SquareIcon[] slots = { new SquareIcon("icons/square.png"), new SquareIcon("icons/square.png"),
			new SquareIcon("icons/square.png"), new SquareIcon("icons/square.png") };

	private final SquareIcon[] undraggable = new SquareIcon[4];


	/**
	 * @param main
	 *            as a MiniGame I need the parent Frame
	 * @param note
	 *            I need to reference the NotePad in order to add my clues to it
	 *
	 */
	public GuessMaker(MainFrame main, NotePad note) {
		super(main);
		this.note = note;
		setSlotPositions();
		for (Person p : people)
			makeDraggable(p);
	}

	public void addWeapon(Weapon w) {
		wepons.add(w);
		makeDraggable(w);
	}

	@Override
	public void gainControl(DrawableCanvas listenerController) {
		switch (activeMenuType) {
		case Clue.BODY:
			setTitle(" - MourgeCelPhone");
			break;
		case Clue.ROOM:
			setTitle(" - Room Menu");
			break;
		case Clue.WEAPON:
			setTitle(" - Pocket Lint Clues");
			break;
		}
		mouse = listenerController.activateDefaultMouseListener();
		motion = listenerController.activateDefaultMouseMotionListener();
	}

	private boolean goButton() {
		// YOU DONT NEED TO SPECIFY THE KILLER
		// if you don't specify the killer, I'll assume you are guessing it was
		// a suicide
		for (int i = 0; i < slots.length; i++) {
			if (i == KILLER)
				continue;
			if (undraggable[i] == null)
				return false;
		}
		return true;
	}

	@Override
	public void loseControl() {
		super.loseControl();
		for (SquareIcon ic : undraggable)
			if (ic != null)
				makeDraggable(ic);
	}

	private void makeDraggable(SquareIcon ic) {
		if (draggables.contains(ic)) {
			System.out.println("" + ic + " shouldn't be draggable");
			draggables.remove(ic);
		}
		ic.revertLocation();
		for (int i = 0; i < slots.length; i++)
			if (ic == undraggable[i]) {
				undraggable[i] = null;
				break;
			}
		draggables.add(ic);
	}

	private void makeUndraggable(SquareIcon ic, int i) {
		if (undraggable[i] != null)
			makeDraggable(undraggable[i]);
		ic.moveTo(slots[i]);
		undraggable[i] = ic;
		draggables.remove(ic);
	}

	@Override
	public void painting(Graphics g) {
		g.drawString("Guess Maker", width, 10);
		Menu.EXIT.draw(g);
		for (Person p : people)
			p.draw(g, MainCanvas.DEBUG);
		for (Weapon w : wepons)
			w.draw(g, MainCanvas.DEBUG);
		for (RoomIcon r : rooms)
			r.draw(g, MainCanvas.DEBUG);

		switch (iconDragType) {
		case VICTIM:
			slots[VICTIM].draw(g);
			g.drawString("VICTIM", slots[VICTIM].x - g.getFontMetrics().stringWidth("VICTIM") / 2, slots[VICTIM].y);
			slots[KILLER].draw(g);
			g.drawString("KILLER", slots[KILLER].x - g.getFontMetrics().stringWidth("KILLER") / 2, slots[KILLER].y);
			((Person) itemBeingDragged).draw(g, true);
			break;
		case WEAPON:
			slots[iconDragType].draw(g);
			g.drawString("WEAPON", slots[WEAPON].x - g.getFontMetrics().stringWidth("WEAPON") / 2, slots[WEAPON].y);
			((Weapon) itemBeingDragged).draw(g, true);
			break;
		case ROOM:
			slots[iconDragType].draw(g);
			g.drawString("ROOM", slots[ROOM].x - g.getFontMetrics().stringWidth("ROOM") / 2, slots[ROOM].y);
			((RoomIcon) itemBeingDragged).draw(g, true);
			break;
		default:
			for (SquareIcon slot : slots)
				slot.draw(g);
		}

		if (goButton())
			GO.draw(g);
	}

	@Override
	public void prepaint() {
		if (motion.hasNewDrag()) {
			MouseEvent e = motion.consumeDrag();
			if (iconDragType != -1)
				itemBeingDragged.moveTo(e.getX(), e.getY());
			else if (mouse.hasNewPress()) {
				ScreenPoint p = new ScreenPoint(mouse.consumePress());
				for (SquareIcon ic : draggables)
					if (ic.containsPoint(p)) {
						itemBeingDragged = ic;
						iconDragType = IconType(ic);
						break;
					}
			}
		}

		if (mouse.hasNewRelease()) {
			ScreenPoint p = new ScreenPoint(mouse.consumeRelease());
			if (Menu.EXIT.containsPoint(p)) {
				popCanvas();
				return;
			}

			if (iconDragType != -1) {
				switch (iconDragType) {
				case VICTIM:
				case KILLER:
					if (slots[VICTIM].containsPoint(itemBeingDragged))
						makeUndraggable(itemBeingDragged, VICTIM);
					else if (slots[KILLER].containsPoint(itemBeingDragged))
						makeUndraggable(itemBeingDragged, KILLER);
					else
						itemBeingDragged.revertLocation();
					break;
				case WEAPON:
				case ROOM:
					if (slots[iconDragType].containsPoint(itemBeingDragged))
						makeUndraggable(itemBeingDragged, iconDragType);
					else
						itemBeingDragged.revertLocation();
					break;
				}

				iconDragType = -1;
			} else if (goButton() && GO.containsPoint(p)) {
				Person vic = (Person) undraggable[VICTIM];
				Person kil;
				if (undraggable[KILLER] == null)
					kil = vic;
				else
					kil = (Person) undraggable[KILLER];
				Weapon wep = (Weapon) undraggable[WEAPON];
				RoomIcon rom = (RoomIcon) undraggable[ROOM];

				note.addClue(new Clue(activeMenuType, new MurderSet(vic, kil, wep, rom.room)));
				popCanvas();
				addCanvas(note);
			}
		}
	}

	public void prepare(Person p) {
		makeUndraggable(p, VICTIM);
		activeMenuType = Clue.BODY;
	}

	public void prepare(Room r) {
		makeUndraggable(r.icon, ROOM);
		activeMenuType = Clue.ROOM;
	}

	public void prepare(Weapon w) {
		makeUndraggable(w, WEAPON);
		activeMenuType = Clue.WEAPON;
	}

	/**
	 * When first created will need to place the 4 slots and the Go Button
	 * somewhere.
	 */
	private void setSlotPositions() {
		int c = width / (slots.length + 3);
		for (int i = 0; i < slots.length; i++)
			slots[i].moveTo((i + 2) * c, 10);
		GO.moveTo((slots.length + 2) * c, 10);
	}

	public void visitRoom(Room r) {
		if (!rooms.contains(r.icon)) {
			if (MainCanvas.DEBUG)
				System.out.println("Found " + r);
			r.icon.moveTo(Global.positions.popRandom().toPoint());
			r.icon.saveLocation();

			makeDraggable(r.icon);
			rooms.add(r.icon);
		}
	}
}
