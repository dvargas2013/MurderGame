package bases;

import java.awt.Graphics;

/**
 * Lines do not move. Do not try to make them move. Once initialized they will
 * generate an origin point and a unit direction vector. The origin is the
 * midpoint of the points sent in (inaccurate within 1 or 2 pixels because of
 * average of integers)
 *
 * @author danv
 */
public class Line {
	private static final int infiniti = 500;
	private final int X, Y;
	public final double xv, yv, xN, yN;

	public Line(int x0, int y0, int x1, int y1) {
		X = (x0 + x1) / 2;
		Y = (y0 + y1) / 2;

		double mag = Math.sqrt((x0 - x1) * (x0 - x1) + (y0 - y1) * (y0 - y1));

		xv = (x0 - x1) / mag;
		yv = (y0 - y1) / mag;
		xN = -yv;
		yN = xv;
	}

	public Line(ScreenPoint p1, ScreenPoint p2) {
		this(p1.x, p1.y, p2.x, p2.y);
	}

	public double distance(int x, int y) {
		return yv * (x - X) - xv * (y - Y);
	}

	public double distance(ScreenPoint p) {
		return yv * (p.x - X) - xv * (p.y - Y); // |u|*Sin(ø) = |u x v|
	}

	public void draw(Graphics g) {
		// Start at the X,Y origin and go both directions a lot
		getMidPoint().draw(g);
		g.drawLine((int) (X - infiniti * xv), (int) (Y - infiniti * yv), (int) (X + infiniti * xv),
				(int) (Y + infiniti * yv));
	}

	public void draw(Graphics g, ScreenPoint cam) {
		// Start at the X,Y origin and go both directions a lot
		getMidPoint().draw(g);
		g.drawLine((int) (X - cam.x - infiniti * xv), (int) (Y - cam.y - infiniti * yv),
				(int) (X - cam.x + infiniti * xv), (int) (Y - cam.y + infiniti * yv));
	}

	public ScreenPoint getClosestScreenPointTo(ScreenPoint p) {
		double d = xv * (p.x - X) + yv * (p.y - Y);
		return new ScreenPoint(X + (int) (xv * d), Y + (int) (yv * d));
	}

	public ScreenPoint getMidPoint() {
		return new ScreenPoint(X, Y);
	}
}
