package frame;

import java.awt.Canvas;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;

/**
 * Keeps track of the Mouse, Key, and MouseMotion Listeners for the MainFrame.
 * Also is the canvas being drawn on so that you don't draw directly to the
 * MainFrame.
 *
 * @author danv
 */
public class DrawableCanvas extends Canvas {
	private KeyListener activeKeyListener;
	private MouseListener activeMouseListener;
	private MouseMotionListener activeMouseMotionListener;
	private MouseWheelListener activeMouseWheelListener;

	private boolean AKL = false;
	private boolean AML = false;
	private boolean AMML = false;
	private boolean AMWL = false;

	private final KListener keys = new KListener();
	private final MMListener motion = new MMListener();
	private final MListener mouse = new MListener();
	private final MWListener wheel = new MWListener();

	/**
	 * Sets KeyListener to the default one defined by KListener and returns it.
	 *
	 * @return {@link KListener}
	 */
	public KListener activateDefaultKeyListener() {
		keys.reset();
		addKeyListener(keys);
		return keys;
	}

	/**
	 * Sets MouseListener to the default one defined by MListener and returns
	 * it.
	 *
	 * @return {@link MListener}
	 */
	public MListener activateDefaultMouseListener() {
		mouse.reset();
		addMouseListener(mouse);
		return mouse;
	}

	/**
	 * Sets MouseMotionListener to the default one defined by MMListener and
	 * returns it.
	 *
	 * @return {@link MMListener}
	 */
	public MMListener activateDefaultMouseMotionListener() {
		motion.reset();
		addMouseMotionListener(motion);
		return motion;
	}

	/**
	 * Sets MouseWheelListener to the default one defined by MWListener and
	 * returns it.
	 *
	 * @return {@link MWListener}
	 */
	public MWListener activateDefaultMouseWheelListener() {
		wheel.reset();
		addMouseWheelListener(wheel);
		return wheel;
	}

	/**
	 * If a Listener is not touched during transition that must mean that the
	 * current MiniGame does not use it.
	 */
	void controlChangeComplete() {
		if (!AML)
			addMouseListener(null);
		if (!AKL)
			addKeyListener(null);
		if (!AMML)
			addMouseMotionListener(null);
		if (!AMWL)
			addMouseWheelListener(null);
	}

	/**
	 * When changing MiniGames this is called to keep track of which Listeners
	 * change or not.
	 */
	void controlChanging() {
		AML = false;
		AKL = false;
		AMML = false;
		AMWL = false;
	}

	/**
	 * Removes the previously assigned KeyListener and assigns the one passed
	 * in.
	 */
	@Override
	public synchronized void addKeyListener(KeyListener kl) {
		AKL = true;
		if (activeKeyListener != kl) {
			super.removeKeyListener(activeKeyListener);
			activeKeyListener = kl;
			if (kl != null)
				super.addKeyListener(kl);
			if (MainFrame.DEBUG)
				System.out.println("KeyListener changed");
		}
	}

	/**
	 * Removes previously assigned MouseListener and assigns the one passed in.
	 */
	@Override
	public synchronized void addMouseListener(MouseListener ml) {
		AML = true;
		if (activeMouseListener != ml) {
			super.removeMouseListener(activeMouseListener);
			activeMouseListener = ml;
			if (ml != null)
				super.addMouseListener(ml);
			if (MainFrame.DEBUG)
				System.out.println("MouseListener changed");
		}
	}

	/**
	 * Removes previously assigned MouseMotionListener and assigns the one
	 * passed in.
	 */
	@Override
	public synchronized void addMouseMotionListener(MouseMotionListener mml) {
		AMML = true;
		if (activeMouseMotionListener != mml) {
			super.removeMouseMotionListener(activeMouseMotionListener);
			activeMouseMotionListener = mml;
			if (mml != null)
				super.addMouseMotionListener(mml);
			if (MainFrame.DEBUG)
				System.out.println("MouseMotionListener changed");
		}
	}

	/**
	 * Removes previously assigned MouseWheelListener and assigns the one passed
	 * in.
	 */
	@Override
	public synchronized void addMouseWheelListener(MouseWheelListener mwl) {
		AMWL = true;
		if (activeMouseWheelListener != mwl) {
			super.removeMouseWheelListener(activeMouseWheelListener);
			activeMouseWheelListener = mwl;
			if (mwl != null)
				super.addMouseWheelListener(mwl);
			if (MainFrame.DEBUG)
				System.out.println("MouseWheelListener changed");
		}
	}
}
