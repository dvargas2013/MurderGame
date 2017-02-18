package bases;

import java.awt.Graphics;
import java.util.ArrayList;

/**
 * Given a point and a width can draw text inside a box. The Height changes
 * automatically to fit the text.
 *
 * @author danv
 */
public class TextBox extends ScreenPoint {
	/**
	 * ints.get(0) == 0 and ints.last() == height of text box <br>
	 * ints.length == lines.length + 1 <br>
	 * Tell you where to draw the line in lines
	 */
	private final ArrayList<Integer> ints = new ArrayList<>();
	/**
	 * Each time the string is greater than the width it is split and the rest
	 * is put on a new line
	 */
	private final ArrayList<String> lines = new ArrayList<>();
	private final String str;
	private int width, _width;

	/**
	 * Initializes the Point to (0,0) and given a width will draw the String
	 *
	 * @param s
	 *            String to display
	 * @param w
	 *            width to display it with
	 */
	public TextBox(String s, int w) {
		str = s;
		width = w;
	}

	/**
	 * Given a new width will resize the textbox next time you try to draw it
	 */
	public void changeWidth(int w) {
		width = w;
	}

	/**
	 * Draws the textbox so that no line has width greater than the set width
	 */
	@Override
	public void draw(Graphics g) {
		if (lines.size() == 0 || _width != width)
			recalculate(g);
		for (int i = 0; i < lines.size(); i++)
			g.drawString(lines.get(i), x, y + ints.get(i));
	}

	@Override
	public void draw(Graphics g, ScreenPoint cam) {
		if (lines.size() == 0 || _width != width)
			recalculate(g);
		for (int i = 0; i < lines.size(); i++)
			g.drawString(lines.get(i), x - cam.x, y - cam.y + ints.get(i));
	}

	/**
	 * If you've drawn it you can check what the height is of the textbox
	 *
	 * @return The height of the textbox. If you haven't drawn it yet it will
	 *         return -1
	 */
	public int getHeight() {
		if (ints.size() == 0 || _width != width)
			return -1;
		return ints.get(ints.size() - 1);
	}

	/**
	 * Called when it is being drawn to recalculate the locations of the lines.
	 *
	 * @param g
	 *            You need this in order to calculate the width of a string
	 */
	private void recalculate(Graphics g) {
		ints.clear();
		lines.clear();
		String insert = "";
		int num = 0;
		ints.add(0);
		_width = width;
		for (String word : str.split("\\s"))
			if (g.getFontMetrics().stringWidth(insert + " " + word) > width) {
				lines.add(insert);
				num += g.getFontMetrics().getHeight() * 1.5;
				ints.add(num);
				insert = word;
			} else
				insert += " " + word;
		lines.add(insert);
		num += g.getFontMetrics().getHeight() * 1.5;
		ints.add(num);
	}
}
