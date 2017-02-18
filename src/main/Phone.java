package main;

import java.awt.Graphics;
import java.util.ArrayList;

import bases.ScreenPoint;
import frame.DrawableCanvas;
import frame.MainFrame;
import frame.MiniGame;
import statics.Global;

/**
 * This is where all the peopel are kept. So you can make guesses based on them.
 *
 * @author danv
 */
public class Phone extends MiniGame {
	/**
	 * People that haven't been solved yet. Must be static to be called from
	 * Clue.
	 */
	private static final ArrayList<Person> people = new ArrayList<>();

	public static int len() {
		return people.size();
	}

	/**
	 * You solved that person it may go away.
	 */
	public static void removePerson(Person p) {
		people.remove(p);
	}

	/**
	 * A GuessMaker is needed. So that it can be called when needed
	 */
	private final GuessMaker guess;

	public Phone(MainFrame main, GuessMaker gm) {
		super(main);
		guess = gm;
		for (Person per : Person.chosen) {
			per.moveTo(Global.positions.popRandom().toPoint());
			per.saveLocation();
			people.add(per);
		}
	}

	@Override
	public void gainControl(DrawableCanvas listenerController) {
		setTitle(" - Mourge Cellphone");
		mouse = listenerController.activateDefaultMouseListener();
	}

	@Override
	public void painting(Graphics g) {
		g.drawString("Magic Phone™", width / 2, 10);
		Menu.EXIT.draw(g); // Draw the exit
		for (Person p : people) // Draw the people
			p.draw(g);
	}

	@Override
	public void prepaint() {
		if (mouse.hasNewClick()) {
			ScreenPoint p = new ScreenPoint(mouse.consumeClick());
			if (Menu.EXIT.containsPoint(p)) {
				popCanvas();
				return;
			}

			for (Person per : people)
				if (per.containsPoint(p)) { // clicked a person?
					guess.prepare(per);
					addCanvas(guess);
					break;
				}
		}
	}
}