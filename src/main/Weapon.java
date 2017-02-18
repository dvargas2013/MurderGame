package main;

import java.awt.Graphics;

import bases.SquareIcon;
import statics.Global;

/**
 * Icon with a name. The name of a weapon to be exact.
 *
 * @author danv
 */
public class Weapon extends SquareIcon {
	final String name; // protected so that MurderSet can see it.

	private Weapon(String file) {
		super("Weapons/" + file);
		name = file.split("\\.", 2)[0];
		assignToRoom(Room.rand());
	}

	private void assignToRoom(Room room) {
		// Move to a random spawn point
		moveCenterTo(room.centerSpawn().randomScreenPointInCircle());
		room.add(this); // Add to its drawables
	}

	public void draw(Graphics g, boolean showname) {
		super.draw(g);
		if (showname)
			g.drawString(name, x + WH / 2 - g.getFontMetrics().stringWidth(name) / 2, y + WH);
	}

	@Override
	public String toString() {
		return "Weapon(" + name + "," + super.toString() + ");";
	}

	public static Weapon[] chosen;
	public final static String[] Weapons = Global.root.resolve("Weapons").toFile()
			.list((dir, name) -> !name.startsWith("."));

	public static void init(int c) {
		chosen = translate(Global.shuffle(Weapons, c));
	}

	public static Weapon rand() {
		return chosen[(int) (Math.random() * chosen.length)];
	}

	private static Weapon[] translate(String[] sh) {
		// make Weapon s out of the String[]
		Weapon[] ret = new Weapon[sh.length];
		for (int i = 0; i < sh.length; i++)
			ret[i] = new Weapon(sh[i]);
		return ret;
	}
}
