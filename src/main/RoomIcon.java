package main;

import java.awt.Graphics;

import bases.SquareIcon;

/**
 * Icon representation of the Room. So that room doesn't have to carry it.
 *
 * @author danv
 */
public class RoomIcon extends SquareIcon {
	public final Room room;

	RoomIcon(Room r, String s) {
		super(s);
		room = r;
	}

	public void draw(Graphics g, boolean showname) {
		super.draw(g);
		if (showname)
			g.drawString(room.name, x + WH / 2 - g.getFontMetrics().stringWidth(room.name) / 2, y + WH / 2);
	}
}
