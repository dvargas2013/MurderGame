package frame;

import java.awt.Graphics;

/**
 * That which acts like a canvas but really just pretends. There is an actual
 * Canvas on the MainFrame this just tells it what to draw.
 *
 * @author danv
 */
abstract public class MiniGame {
	public static int width, height;

	protected static final int KEYS = 0b1, MOUSE = 0b10, MOTION = 0b100, WHEEL = 0b1000;
	private final int autolistener;

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		MainFrame mf = new MainFrame() {
			@Override
			public void gainControl() {
				setBaseTitle("DEMO");
				addCanvas(new MiniGame(this, MOTION) {
					boolean collide = false;
					int x, y;

					@Override
					public void gainControl(DrawableCanvas listenerController) {
						setTitle(" - Canvas");
					}

					@Override
					public void painting(Graphics g) {
						if (collide)
							g.setColor(java.awt.Color.RED);
						g.drawRect(100, 100, 100, 100);
						g.fillOval(x, y, 10, 10);
					}

					@Override
					public void prepaint() {
						if (motion.hasNewMove()) {
							java.awt.event.MouseEvent e = motion.consumeMove();
							if (e.getX() - 5 < 200 && e.getX() + 5 > 100 && e.getY() - 5 < 200 && e.getY() + 5 > 100)
								collide = true;
							else {
								x = e.getX() - 5;
								y = e.getY() - 5;
								collide = false;
							}
						}
					}
				});
			}
		};
	}

	protected final MainFrame parent;
	protected KListener keys;
	protected MListener mouse;
	protected MMListener motion;
	protected MWListener wheel;

	/**
	 * Set the parent of the MiniGame
	 */
	protected MiniGame(MainFrame main) {
		parent = main;
		autolistener = 0;
	}

	protected MiniGame(MainFrame main, int autolisten) {
		parent = main;
		autolistener = autolisten;
	}

	/**
	 * Set the parent of the MiniGame as the same as this one
	 */
	protected MiniGame(MiniGame main) {
		parent = main.parent;
		autolistener = 0;
	}

	protected MiniGame(MiniGame main, int autolisten) {
		parent = main.parent;
		autolistener = autolisten;
	}

	/**
	 * Tells the parent to add a new display canvas. Will call
	 * {@link #loseControl} on the top canvas (which should be this but doesn't
	 * have to be) and calls {@link #gainControl} on mg
	 *
	 * @param mg
	 *            - anything that extends MiniGame is a canvas
	 */
	protected void addCanvas(MiniGame mg) {
		parent.addCanvas(mg);
	}

	/**
	 * Same as calling {@link #addCanvas};
	 */
	public void addSelf() {
		parent.addCanvas(this);
	}

	/**
	 * This is where you should add your listeners to the parent. <br>
	 * If you have set the autolistener variable and expect it to work you
	 * should extend rather than override this method. Please call super. <br>
	 * If you want to call DrawableCanvas.activateDefault_Listener yourself be
	 * my guest.
	 */
	public void gainControl(DrawableCanvas listenerController) {
		if (autolistener != 0) {
			if ((autolistener & KEYS) != 0)
				keys = listenerController.activateDefaultKeyListener();
			if ((autolistener & MOUSE) != 0)
				mouse = listenerController.activateDefaultMouseListener();
			if ((autolistener & MOTION) != 0)
				motion = listenerController.activateDefaultMouseMotionListener();
			if ((autolistener & WHEEL) != 0)
				wheel = listenerController.activateDefaultMouseWheelListener();
		}
	}

	/**
	 * Just unloads the listeners you set (actually DrawableCanvas takes care of
	 * that, not this function). You really don't /have/ to call super if you
	 * override this since this only sets the references to null and the
	 * references are still stored in DrawableCanvas anyway. I set these to null
	 * just to make sure someone doesn't accidently try to use the listeners
	 * when they don't have control of the screen.
	 */
	public void loseControl() {
		keys = null;
		mouse = null;
		motion = null;
		wheel = null;
	}

	/**
	 * If you need a graphics object you do it here ;P Please don't over use.
	 * Only if you need a graphics object nothing else. Use {@link #prepaint} if
	 * it doesn't need a graphics object.
	 *
	 * @param g
	 *            The Graphics object same as a paint() method.
	 */
	public abstract void painting(Graphics g);

	/**
	 * Tells the parent to remove the top most display canvas. If you are the
	 * the top canvas it will eventually call {@link #loseControl}
	 */
	protected void popCanvas() {
		parent.popCanvas();
	}

	/**
	 * This should be where you should do things like user input checking and AI
	 * and all the movements and collision checks, stuff like that.
	 */
	public abstract void prepaint();

	/**
	 * Does nothing by default but if you need to do something when the
	 * MainFrame is resized do it here. Called before {@link #prepaint} if at
	 * all. Also called right before {@link #gainControl}
	 */
	public void resized() { //
	}

	/**
	 * Tells the parent to concatenate the baseString with the given string to
	 * make the title
	 */
	protected void setTitle(String s) {
		parent.setTitle(s);
	}

	/**
	 * Does nothing by default. Called when parent is off
	 */
	public void sleeping() { //
	}
}
