package bases;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import statics.Global;

/**
 * Simple animation class
 *
 * @author danv
 */
public class Animation {
	private int curr = 0;
	private final int duration;
	private final BufferedImage[] images;

	/**
	 * Create Animation
	 *
	 * @param basename
	 *            for images to display in animation. May be empty.
	 * @param stills
	 *            are joined with the basename to create the path to the images
	 * @param duration
	 *            will make each image be drawn that many times before changing
	 *            to next image
	 */
	public Animation(String basename, String[] stills, int duration) {
		images = new BufferedImage[stills.length];
		for (int i = 0; i < stills.length; i++)
			images[i] = Global.getImage(basename + stills[i]);
		this.duration = duration;
	}

	/**
	 * Given a graphics object will getImage() and draw it at (0,0)
	 */
	public void draw(Graphics g) {
		g.drawImage(getImage(), 0, 0, null);
	}

	/**
	 * Get the next Image to be displayed.
	 *
	 * @return the next image according to duration
	 */
	public BufferedImage getImage() {
		curr++;
		if (curr >= images.length * duration)
			curr = 0;
		return images[curr / duration];
	}

	/**
	 * Get Image that corresponds with the index
	 *
	 * @param i
	 *            index of image. Will correspond to image with file name
	 *            basename+stills[i]
	 * @return Image with that filename
	 */
	public BufferedImage getImage(int i) {
		return images[i];
	}
}
