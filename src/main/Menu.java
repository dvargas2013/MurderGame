package main;

import java.awt.Color;
import java.awt.Graphics;

import bases.ScreenPoint;
import bases.SquareIcon;
import frame.MainFrame;
import frame.MiniGame;

/**
 * Phone, Past weapons, NotePad, save game? exit
 *
 * @author danv
 */
public class Menu extends MiniGame {
	private static final SquareIcon[] clickables = { SquareIcon.get("phone"), SquareIcon.get("weapon"),
			SquareIcon.get("notepad")
	};
	/**
	 * The EXIT icon on the top left corner of screen
	 */
	public static final SquareIcon EXIT = SquareIcon.get("exit");
	private final static int FONE = 0, WEPS = 1, NOTE = 2, SAVE = 3;

	private Room background;
	private final NotePad note = new NotePad(parent);
	private final GuessMaker guess = new GuessMaker(parent, note);
	private final Phone fone = new Phone(parent, guess);
	private final Pocket weps = new Pocket(parent, guess);

	public Menu(MainFrame main) {
		super(main, MOUSE);
	}

	@Override
	public void resized() {
		setTitle(" - Main Menu");
		int x = width / 2;

		int c = height / (clickables.length + 2);

		for (int i = 0; i < clickables.length; i++)
			clickables[i].moveTo(x, (i + 1) * c);
	}

	@Override
	public void painting(Graphics g) {
		background.draw(g);
		// Draw the background stuff
		// before opening the menu I save the room to draw

		g.setColor(new Color(150, 150, 250, 200));
		g.fillRect(0, 0, width, height);
		// Make a hazy blue screen over the room to show it's inactivity

		g.setColor(Color.BLACK);
		g.drawString("Main Menu", 250, 10);
		for (SquareIcon clickable : clickables)
			clickable.draw(g);
		EXIT.draw(g);
	}

	@Override
	public void prepaint() {
		// Check what you are clicking
		if (mouse.hasNewClick()) {
			ScreenPoint p = new ScreenPoint(mouse.consumeClick());
			if (EXIT.containsPoint(p)) {
				popCanvas();
				return;
			}

			for (int i = 0; i < clickables.length; i++)
				if (clickables[i].containsPoint(p)) {
					switch (i) {
					case FONE:
						addCanvas(fone);
						break;
					case WEPS:
						addCanvas(weps);
						break;
					case NOTE:
						addCanvas(note);
						break;
					case SAVE:
						System.out.println("YOU CLICKED TEH SAVE");
						break;
					}
					break; // END LOOP
				}
		}
	}

	public void prepare(Room room) {
		guess.prepare(room);
		addCanvas(guess);
	}

	public void setBackground(Room bg) {
		background = bg;
	}

	public void visitRoom(Room newroom) {
		guess.visitRoom(newroom);
	}

	public void addClue(Clue clue) {
		note.addClue(clue);
		addCanvas(note);
	}

	public void addWeapon(Weapon w) {
		weps.addWeapon(w);
		addCanvas(weps);
	}

}
