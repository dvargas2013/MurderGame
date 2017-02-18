package statics;

import java.awt.Graphics;

import javax.swing.ImageIcon;

import bases.ScreenPoint;
import bases.SquareIcon;
import bases.TextBox;
import frame.MainFrame;
import frame.MiniGame;
import main.MainCanvas;
import main.Person;
import main.Room;
import main.Weapon;

/**
 * Where you land when MurderGame Lands. Allows for editing and/or playing.
 */
public class LandingPage extends MiniGame {
	private boolean back = false; // true if you win yay

	private final SquareIcon PLAY = SquareIcon.get("play");
	private final SquareIcon[] clicky = new SquareIcon[] { PLAY };

	private final ScreenPoint dancerloc = new ScreenPoint();
	private final ImageIcon dancey = new ImageIcon("resources/You/dance.gif");

	private final int ROOMS = 0, WEAPONS = 1, PEOPLE = 2;
	private final TextBox[] texts = new TextBox[] { new TextBox("Rooms:", width / 5),
			new TextBox("Weapons:", width / 5),
			new TextBox("People:", width / 5) };
	private final int[] ints = new int[] { 5, 2, 2 };
	private int intamounts;

	private final TextBox rules = new TextBox(
			"There are some Dead People™ - - - All you know is that they only had a few Rooms and Weapons available - - - "
					+ "Make theories about the bodies, weapons, and rooms to get Hints™. - - - "
					+ "Walk to a room and press <X> to examine the Room Menu™. Press <C> to open Regular Menu™. "
					+ "(Found Weapons™ (pick up with <Z>) and your Mourge Cellphone™ are in the Regular Menu™) - - - "
					+ "Obviously, all the people are Victims. But not all rooms and weapons are used. "
					+ "Every so often rooms and weapons are used twice. (In these cases only the most recent unsolved one will yield clues)",
					width - 10);
	// MiniGame tutor = new Tutorial(parent);

	public LandingPage(MainFrame main) {
		super(main, MOUSE | KEYS | MOTION);
	}

	@Override
	public void painting(Graphics g) {
		for (SquareIcon element : clicky)
			element.draw(g);
		for (int i = 0; i < texts.length; i++) {
			texts[i].draw(g);
			if (texts[i].getHeight() > 0)
				g.drawString("" + ints[i], texts[i].x, texts[i].y + texts[i].getHeight());
		}
		rules.draw(g);
		if (intamounts < 3)
			g.drawString("Global Positions are getting Crowded", width / 8, texts[0].y - texts[0].getHeight() / 2);
		if (back)
			dancey.paintIcon(parent, g, dancerloc.x, dancerloc.y);
	}

	@Override
	public void prepaint() {
		if (keys.UP) {
			change(+1);
			keys.UP = false;
		}
		if (keys.DOWN) {
			change(-1);
			keys.DOWN = false;
		}

		if (mouse.hasNewClick()) {
			ScreenPoint p = new ScreenPoint(mouse.consumeClick());
			if (PLAY.containsPoint(p))
				addGame();
			else
				dancerloc.moveTo(p);
		}
	}

	private void change(int i) {
		ScreenPoint p = new ScreenPoint(motion.peekMove());
		int closest = 0;
		for (int j = 1; j < texts.length; j++)
			if (texts[j].distanceSquared(p) < texts[closest].distanceSquared(p))
				closest = j;
		ints[closest] += i;
		if (i < 0 && ints[closest] < 1)
			ints[closest] = 1;
		if (i > 0) {
			switch (closest) {
			case ROOMS:
				if (ints[ROOMS] > Room.Rooms.length)
					ints[ROOMS] = Room.Rooms.length;
				break;
			case PEOPLE:
				if (ints[PEOPLE] > Person.People.length)
					ints[PEOPLE] = Person.People.length;
				break;
			case WEAPONS:
				if (ints[WEAPONS] > Weapon.Weapons.length)
					ints[WEAPONS] = Weapon.Weapons.length;
				break;
			}

			shrinkNumsIfNeeded();
		}
	}

	private void shrinkNumsIfNeeded() {
		intamounts = 0;
		for (int j : ints)
			intamounts += j;
		while (intamounts > Global.positions.length())
			for (int j = 0; j < ints.length; j++)
				if (ints[j] > 0 && ints[j] > (intamounts % 3 == 0 ? intamounts / 3 - 1 : intamounts / 3)) {
					ints[j]--;
					intamounts--;
				}
	}

	private void growNumstoMax() {
		ints[ROOMS] = Room.Rooms.length;
		ints[PEOPLE] = Person.People.length;
		ints[WEAPONS] = Weapon.Weapons.length;

		shrinkNumsIfNeeded();
	}

	private void addGame() {
		back = true;
		Person.init(ints[PEOPLE]);
		Room.init(ints[ROOMS]);
		Weapon.init(ints[WEAPONS]);
		addCanvas(new MainCanvas(parent));
	}

	@Override
	public void resized() {
		setTitle(" - Landing Page");
		int yc = height / (clicky.length + 2);
		for (int i = 0; i < clicky.length; i++)
			clicky[i].moveTo(width / 2, (i + 1) * yc);
		rules.moveTo(10, yc * (clicky.length + 1));
		rules.changeWidth(width - 10);
		yc /= 2;
		int xc = width / (texts.length + 1);
		for (int i = 0; i < texts.length; i++) {
			texts[i].moveTo((i + 1) * xc, yc);
			texts[i].changeWidth(width / 3);
		}
		dancerloc.moveTo(2 * width / 3, 2 * height / 3);
		Global.initPositions(width, height);
		growNumstoMax();
	}
}