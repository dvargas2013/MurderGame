package bases;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

/**
 * Nice little Data Point. Uses the powers of math.
 *
 * @author danv
 */
public class ScreenPoint implements Cloneable {
	public int x = 0, y = 0;

	/**
	 * Point is initialized at (0,0)
	 */
	public ScreenPoint() {
	}

	public ScreenPoint(double xx, double yy) {
		x = (int) xx;
		y = (int) yy;
	}

	/**
	 * Point is initialized at (x,y)
	 */
	public ScreenPoint(int xx, int yy) {
		x = xx;
		y = yy;
	}

	public ScreenPoint(MouseEvent e) {
		x = e.getX();
		y = e.getY();
	}

	@Override
	public ScreenPoint clone() {
		return new ScreenPoint(x, y);
	}

	/**
	 * dx**2 + dy**2 = D**2
	 *
	 * @param pointx
	 *            - x value of point
	 * @param pointy
	 *            - y value of point
	 * @return the Euclidean distance between the points squared
	 */
	public int distanceSquared(int pointx, int pointy) {
		int dx = x - pointx;
		int dy = y - pointy;
		return dx * dx + dy * dy;
	}

	/**
	 * dx**2 + dy**2 = D**2
	 *
	 * @param p
	 *            another Point
	 * @return the Euclidean distance between the points squared
	 */
	public int distanceSquared(ScreenPoint p) {
		int dx = x - p.x;
		int dy = y - p.y;
		return dx * dx + dy * dy;
	}

	public void draw(Graphics g) {
		g.fillOval(x - 5, y - 5, 10, 10);
	}

	public void draw(Graphics g, ScreenPoint cam) {
		g.fillOval(x - cam.x - 5, y - cam.y - 5, 10, 10);
	}

	/**
	 * Essentially subtracts the coefficients between the points
	 *
	 * @param s
	 *            a Point passed
	 * @return a new point that could represent the vector P to S
	 */
	public ScreenPoint minus(ScreenPoint s) {
		return new ScreenPoint(x - s.x, y - s.y);
	}

	/**
	 * Change the point by adding the doubles passed
	 */
	public void moveBy(double dx, double dy) {
		x += dx;
		y += dy;
	}

	/**
	 * Change the point by adding the integers passed
	 */
	public void moveBy(int dx, int dy) {
		x += dx;
		y += dy;
	}

	/**
	 * Change the point by adding the coefficients of the passed point
	 */
	public void moveBy(ScreenPoint p) {
		x += p.x;
		y += p.y;
	}

	public void moveDownBy(int dy) {
		y += dy;
	}

	public void moveLeftBy(int dx) {
		x -= dx;
	}

	public void moveRightBy(int dx) {
		x += dx;
	}

	/**
	 * Move the point to the new coefficients. Basically equivalent to making a
	 * new point
	 */
	public void moveTo(int xx, int yy) {
		x = xx;
		y = yy;
	}

	public void moveTo(MouseEvent e) {
		x = e.getX();
		y = e.getY();
	}

	/**
	 * Moves the point to the same location as point passed in.
	 */
	public void moveTo(ScreenPoint p) {
		x = p.x;
		y = p.y;
	}

	public void moveUpBy(int dy) {
		y -= dy;
	}

	/**
	 * Change the point by subtracting the coefficients of the passed point
	 */
	public void oppMoveBy(ScreenPoint p) {
		x -= p.x;
		y -= p.y;
	}

	public ScreenPoint plus(MouseEvent e) {
		return new ScreenPoint(x + e.getX(), y + e.getY());
	}

	/**
	 * Essentially adds the coefficients between the points
	 *
	 * @param s
	 *            a Point passed
	 * @return a new point that could represent the sum vector P + S
	 */
	public ScreenPoint plus(ScreenPoint s) {
		return new ScreenPoint(x + s.x, y + s.y);
	}

	public ScreenPoint plus(int xx, int yy) {
		return new ScreenPoint(x + xx, y + yy);
	}

	public ScreenPoint plus(double xx, double yy) {
		return new ScreenPoint(x + xx, y + yy);
	}

	public boolean at(int xx, int yy) {
		return x == xx && y == yy;
	}

	public boolean at(ScreenPoint xy) {
		return x == xy.x && y == xy.y;
	}

	@Override
	public String toString() {
		return "ScreenPoint(" + x + "," + y + ")";
	}
}
