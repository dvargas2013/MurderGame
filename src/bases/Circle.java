package bases;

import java.awt.Graphics;

/**
 * Extends ScreenPoint and thus is also drawable.
 *
 * Has the extra information of a radius, as Circles usually do.
 *
 * The ScreenPoint defines the Center point.
 *
 * @author danv
 */
public class Circle extends ScreenPoint {
	public int r;

	/**
	 * Set the Position and radius of the circle
	 */
	public Circle(int x, int y, int r) {
		super(x, y);
		this.r = r;
	}

	/**
	 * Set the position as a point and the radius
	 */
	public Circle(ScreenPoint point, int r) {
		super(point.x, point.y);
		this.r = r;
	}

	/**
	 * Calculates if the point is inside the circle
	 */
	public boolean containsPoint(ScreenPoint p) {
		return distanceSquared(p) < r * r;
	}

	/**
	 * Draws an oval with center at (x,y) according to camera With radius r
	 */
	@Override
	public void draw(Graphics g) {
		// Get the topleft point
		// height and width is 2r
		// so that center point has radius r
		g.fillOval(x - r, y - r, 2 * r, 2 * r);
	}

	@Override
	public void draw(Graphics g, ScreenPoint cam) {
		// Get the topleft point
		// height and width is 2r
		// so that center point has radius r
		g.fillOval(x - cam.x - r, y - cam.y - r, 2 * r, 2 * r);
	}

	/**
	 * @return a random point inside the circle such that <b>Circle</b>
	 *         .containsPoint( <b>Circle</b> .randomScreenPointInCircle() ) ==
	 *         true;
	 */
	public ScreenPoint randomScreenPointInCircle() {
		double t = 2 * Math.PI * Math.random(), rr = Math.random() / 2 + .5;
		return new ScreenPoint(x + (int) (r * rr * Math.cos(t)), y + (int) (r * rr * Math.sin(t)));
	}
}
