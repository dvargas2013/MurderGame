package main;

import java.awt.Graphics;

import bases.SquareIcon;
import statics.Global;

/**
 * Icon with a name. The name of a Person to be exact.
 *
 * @author danv
 */
public class Person extends SquareIcon {
	final String name;

	/**
	 * Give the image file. That will also be the name.
	 */
	private Person(String file) {
		super("People/" + file);
		name = file.split("\\.", 2)[0];
		// image has a extension. That's not the name
	}

	public void draw(Graphics g, boolean showname) {
		super.draw(g);
		if (showname)
			g.drawString(name, x + WH / 2 - g.getFontMetrics().stringWidth(name) / 2, y + WH);
	}

	@Override
	public String toString() {
		return "Person(" + name + ");";
	}

	public static Person[] chosen;
	public final static String[] People = Global.root.resolve("People").toFile()
			.list((dir, name) -> !name.startsWith("."));

	public static void init(int c) {
		chosen = translate(Global.shuffle(People, c));
	}

	public static Person rand() {
		return chosen[(int) (Math.random() * chosen.length)];
	}

	private static Person[] translate(String[] sh) {
		// Go through the string[] and make the Person objects
		Person[] ret = new Person[sh.length];
		for (int i = 0; i < sh.length; i++)
			ret[i] = new Person(sh[i]);
		return ret;
	}
}
