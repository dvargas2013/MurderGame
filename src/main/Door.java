package main;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import bases.ScreenPoint;
import statics.Global;

/**
 * A Small Door Class that saves the image of a door at a point to display.
 *
 * @author danv
 */
public class Door extends ScreenPoint {
	private final BufferedImage img;
	private BufferedImage L, R;
	private int LX, LY, RX, RY;

	// TODO draw person behind door depending on the Z axis c;
	/**
	 * Init the point at (0,0) and create the image to display
	 *
	 * @param file
	 *            String of path to file
	 */
	public Door(String file) {
		super();
		if (Global.root.resolve(file).toFile().exists())
			img = Global.getImage(file);
		else
			img = Global.getImage("Rooms/[default]/Door.png");
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(img, x, y, null);
	}

	@Override
	public void draw(Graphics g, ScreenPoint cam) {
		g.drawImage(img, x - cam.x, y - cam.y, null);
	}

	public void drawLeft(Graphics g, ScreenPoint cam) {
		if (L == null)
			leftTransform();
		g.drawImage(L, x - cam.x - LX, y - cam.y - LY, null);
	}

	public void drawRight(Graphics g, ScreenPoint cam) {
		if (R == null)
			rightTransform();
		g.drawImage(R, x - cam.x - RX, y - cam.y - RY, null);
	}

	private void leftTransform() {
		AffineTransform at = new AffineTransform();
		at.shear(0, -.5);
		L = useTransform(img, at);
		LX = L.getWidth() / 2;
		LY = img.getHeight() + (L.getHeight() - img.getHeight()) / 2;
	}

	private void rightTransform() {
		AffineTransform at = new AffineTransform();
		at.shear(0, .5);
		R = useTransform(img, at);
		RX = R.getWidth() / 2;
		RY = img.getHeight() + (R.getHeight() - img.getHeight()) / 2;
	}

	private static BufferedImage useTransform(BufferedImage image, AffineTransform at) {
		/* make a rectangle and apply transform to it to get new bounds */
		Rectangle wh = at.createTransformedShape(new Rectangle(image.getWidth(), image.getHeight())).getBounds();
		if (wh.x != 0 || wh.y != 0) {
			AffineTransform TX = new AffineTransform();
			TX.translate(-wh.x, -wh.y);
			at.preConcatenate(TX);
		}
		BufferedImage displayImage = new BufferedImage(wh.width, wh.height, BufferedImage.TYPE_INT_ARGB);
		/* draw old image as per transformation onto new image */
		displayImage.createGraphics().drawImage(image, at, null);
		return displayImage;
	}
}
