package bases;

import java.awt.Graphics;

/**
 * An array of Animations combined with a Point. It's magical really.
 *
 * @author danv
 */
abstract public class Sprite extends ScreenPoint {
	private final Animation[] anims;
	private int currentAnim;
	protected int width, height;

	/**
	 * Height and Width are initialized to 0. So don't be surprised nothing
	 * shows up.<br>
	 * There are poses.length different animations. Each animation has
	 * anims.length different frames. All animations have the same frame rate =
	 * duration.
	 *
	 * @param name
	 *            - basename of the sprite
	 * @param poses
	 *            - name + poses[i] is how each Animation is initialized
	 * @param anims
	 *            - name + poses[i] + anims[j] is each image in Animation[i]
	 * @param durations
	 *            - the duration is passed to the Animations
	 */
	public Sprite(String name, String[] poses, String[] anims, int durations) {
		this.anims = new Animation[poses.length];
		for (int i = 0; i < poses.length; i++)
			this.anims[i] = new Animation(name + poses[i], anims, durations);
		width = currentAnimation().getImage(0).getWidth();
		height = currentAnimation().getImage(0).getHeight();
	}

	public Animation currentAnimation() {
		return anims[currentAnim];
	}

	/**
	 * Draws the current animation at the drawable location.
	 */
	@Override
	public void draw(Graphics g) {
		g.drawImage(currentAnimation().getImage(), x, y, null);
	}

	/**
	 * Draw the Specific Image in the Animation.
	 *
	 * @param i
	 *            index of image inside the current animation. could throw index
	 *            error
	 */
	public void draw(Graphics g, int i) {
		g.drawImage(currentAnimation().getImage(i), x, y, null);
	}

	public void draw(Graphics g, int i, ScreenPoint cam) {
		g.drawImage(currentAnimation().getImage(i), x - cam.x, y - cam.y, null);
	}

	@Override
	public void draw(Graphics g, ScreenPoint cam) {
		g.drawImage(currentAnimation().getImage(), x - cam.x, y - cam.y, null);
	}

	public void setCurrentAnimation(int i) {
		currentAnim = i;
	}
}
