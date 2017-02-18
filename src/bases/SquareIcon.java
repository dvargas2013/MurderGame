package bases;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import statics.Global;

public class SquareIcon extends ScreenPoint {
	public static final int WH = 64;
	/**
	 * When accessing using the get method it will save in here
	 */
	private static final Map<String, SquareIcon> icons = new HashMap<>();

	/**
	 * Tries to find the name in previously created icons. <br>
	 * If not tries to create a new Icon from the name <br>
	 * If neither will throw ArguentException
	 */
	public static SquareIcon get(String name) {
		if (icons.containsKey(name))
			return icons.get(name);

		File f = Global.root.resolve("icons/" + name + ".png").toFile();

		if (f.exists()) {
			SquareIcon icon = new SquareIcon(f);
			icons.put(name, icon);
			return icon;
		}

		throw new IllegalArgumentException("Icon: '" + name + "' does not exist");
	}

	private final BufferedImage icon;

	private SquareIcon(File f) {
		icon = resizeImage(Global.getImage(f), WH, WH);
	}

	public SquareIcon(String s) {
		icon = resizeImage(Global.getImage(s), WH, WH);
	}

	public boolean containsPoint(ScreenPoint contained) {
		return contained.x > x && contained.x < x + WH && contained.y > y && contained.y < y + WH;
	}

	public static boolean containsPoint(ScreenPoint contained, ScreenPoint loc) {
		return contained.x > loc.x && contained.x < loc.x + WH && contained.y > loc.y && contained.y < loc.y + WH;
	}

	/**
	 * Draws the icon with the TopLeft corner at 0,0
	 */
	@Override
	public void draw(Graphics g) {
		g.drawImage(icon, x, y, null);
	}

	@Override
	public void draw(Graphics g, ScreenPoint cam) {
		g.drawImage(icon, x - cam.x, y - cam.y, null);
	}

	/**
	 * Move the point to the new coefficients. Basically equivalent to making a
	 * new point
	 */
	public void moveCenterTo(int xx, int yy) {
		x = xx - WH / 2;
		y = yy - WH / 2;
	}

	public void moveCenterTo(MouseEvent e) {
		x = e.getX() - WH / 2;
		y = e.getY() - WH / 2;
	}

	/**
	 * Moves the point to the same location as point passed in.
	 */
	public void moveCenterTo(ScreenPoint p) {
		x = p.x - WH / 2;
		y = p.y - WH / 2;
	}

	private ScreenPoint save;

	public void revertLocation() {
		if (save != null)
			this.moveTo(save);
	}

	public void saveLocation() {
		if (save == null)
			save = new ScreenPoint();
		save.moveTo(this);
	}

	/**
	 * Does what it says. Do not question my magic powers.
	 *
	 * @return a new BufferedImage. now resized. Does not alter the passed in
	 *         image.
	 */
	private static BufferedImage resizeImage(BufferedImage img, int newW, int newH) {
		Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
		int t = img.getType();
		if (t == 0)
			t = BufferedImage.TYPE_INT_ARGB;
		BufferedImage dimg = new BufferedImage(newW, newH, t);

		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		return dimg;
	}

}
