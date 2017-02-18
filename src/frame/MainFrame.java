package frame;

import java.awt.Cursor;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.prefs.Preferences;

import javax.swing.JOptionPane;

/**
 * This is the BackBone Frame that holds everything together. It is a
 * Container.Frame that holds a Thread where every 16millis calls the active
 * MiniGame to draw on the screen <br>
 * <br>
 * Don't forget to start() the Frame. The Thread is running 2x/sec waiting for
 * you to begin. <br>
 * The reason for this is for you to make sure the MiniGame is initialized
 * before starting to draw. Although the code won't break if you don't
 * initialize, that null-error is annoying
 *
 * @author danv
 */
public abstract class MainFrame extends Frame implements Runnable, KeyListener {
	public static final boolean DEBUG = false;
	/**
	 * The current MiniGame being displayed and what-not. If frame is
	 * {@link #stop() stopped} Frame will try to call the MiniGame's
	 * {@link MiniGame#sleeping() sleeping} method.
	 */
	private MiniGame active;
	private String baseTitle = "";
	/**
	 * I used to draw directly on the Frame but when the frame has a title bar
	 * it messes up the drawing area. This solves that by getting only the
	 * drawable area.
	 */
	private final DrawableCanvas drawable = new DrawableCanvas();

	private long esctime;

	private boolean closeLoop = false;
	private boolean loading = false;

	/**
	 * Thread checks if game is on or off.
	 */
	private boolean on = false;

	/**
	 * Stack of MiniGames. A MiniGame has the capability of telling the parent
	 * to {@link #addCanvas(MiniGame) stack} another MiniGame on top of itself.
	 * Once the new MiniGame tells the parent to pop it off the Stack, control
	 * returns to the previous MiniGame.
	 */
	private final Stack<MiniGame> previous = new Stack<>();

	/**
	 * It's a Frame. It has a thread. It calls MiniGames to draw. They're pushed
	 * into a Stack sometimes. It's not /that/ complicated.
	 */
	public MainFrame() {
		super();
		setIgnoreRepaint(true);
		/* Ignored so it doesn't draw before active is set */

		int w = Preferences.userNodeForPackage(getClass()).getInt("width", -1),
				h = Preferences.userNodeForPackage(getClass()).getInt("height", -1),
				y = Preferences.userNodeForPackage(getClass()).getInt("top", -1),
				x = Preferences.userNodeForPackage(getClass()).getInt("left", -1);
		/* get last used dimensions if at all possible */
		if (x < 0 || y < 0)
			setLocation(0, 0);
		else
			setLocation(x, y);
		// TODO might cause errors
		// you might want to check if it's actually being
		// displayed on the screen properly

		/* close window when (x) is pressed */
		addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				close();
			}
		});
		/* Close window on a sigterm */
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				savePreferences();
			}
		});

		/* set up drawable region */
		setVisible(true);
		add(drawable);

		/* Double buffering, page flipping, etc */
		drawable.createBufferStrategy(2);

		drawable.addKeyListener(this);
		/* this listens to an escape press to end */
		addKeyListener(this);

		if (w > 0 && h > 0)
			setSize(w, h);
		else
			setExtendedState(MAXIMIZED_BOTH);

		/* initialize the extended implementation of MainFrame */
		gainControl();
		/*
	  Game loop Thread. This calls sleeping() or draw() in the {@link #active}
	  MiniGame depending on if the Frame was start()ed or not.
		 */
		new Thread(this).start();
		start();
	}

	/**
	 * Add active game to previous and add the new game
	 *
	 * @param mg
	 *            Another MiniGame
	 */
	protected void addCanvas(MiniGame mg) {
		stop();

		if (active != null) { /* put active into previous */
			previous.push(active);
			active.loseControl();
		}

		drawable.controlChanging();
		mg.gainControl(drawable); /* give control to sent MiniGame */
		mg.resized();
		drawable.controlChangeComplete();

		active = mg;
		start();
	}

	public void clear() {
		previous.clear();
	}

	public void close() {
		if (JOptionPane.showConfirmDialog(this, "Are you sure to close this window?", "Really Closing?",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
			closing();
	}

	/**
	 * Calls System.exit by default. Is called when it's about to close
	 * obviously. It also saves preferences for window size. If you are going to
	 * override it, please call back to it at the end of your override.
	 */
	private void closing() {
		savePreferences();
		closeLoop = true;
	}

	/**
	 * I recommend having a main Canvas that you add back to the Frame when you
	 * gain control. This gets called when you pop from an empty MiniGame stack.
	 */
	public abstract void gainControl();

	@Override
	public void keyPressed(KeyEvent e) {
		if (esctime == 0 && e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			esctime = e.getWhen();
			close();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			if (esctime > 0 && e.getWhen() - esctime > 500)
				closing();
			esctime = 0;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) { //
	}

	/**
	 * Remove the active game and add looks through stack to get the previous
	 * game
	 */
	void popCanvas() {
		stop();

		if (active != null)
			active.loseControl();

		drawable.controlChanging();
		/* mark which listeners are changing */

		if (!previous.isEmpty()) {
			active = previous.pop();
			/* remove previous and give it control */
			active.gainControl(drawable);
			active.resized();
		} else
			gainControl(); /* previous is empty */

		drawable.controlChangeComplete();

		start();
	}

	@Override
	public void run() {
		while (!closeLoop) {/* GameLoop - Forever running and sleeping */
			if (loading)
				drawable.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			if (on) { /* Frame is on wait 16millis and draw */
				try {
					Thread.sleep(16);
				} catch (InterruptedException e) {
					System.out.println("TFW you were interrupted");
					e.printStackTrace();
				}

				if (active != null) {
					int h = drawable.getHeight(), w = drawable.getWidth();
					if (h != MiniGame.height || w != MiniGame.width) {
						MiniGame.height = h;
						MiniGame.width = w;
						active.resized();
					}

					MiniGame act = active;
					active.prepaint();
					if (active != act)
						continue;

					BufferStrategy strategy = drawable.getBufferStrategy();
					Graphics g = strategy.getDrawGraphics();
					g.clearRect(0, 0, getWidth(), getHeight());
					active.painting(g);
					g.dispose();
					strategy.show();

					if (loading) {
						loading = false;
						drawable.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					}
				} else
					System.out.println("Active is Null");
			} else { /* Frame is off wait for start() */
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					System.out.println("TFW you were interrupted before you ever started");
					e.printStackTrace();
				}

				if (active != null)
					active.sleeping();
			}
		}
		savePreferences();
		System.exit(0);
	}

	private void savePreferences() {
		Preferences.userNodeForPackage(getClass()).putInt("width", getWidth());
		Preferences.userNodeForPackage(getClass()).putInt("height", getHeight());
		Preferences.userNodeForPackage(getClass()).putInt("top", getY());
		Preferences.userNodeForPackage(getClass()).putInt("left", getX());

	}

	public void setBaseTitle(String s) {
		baseTitle = s;
		super.setTitle(baseTitle);
	}

	@Override
	public void setTitle(String s) {
		super.setTitle(baseTitle + s);
	}

	/**
	 * Make the gameloop {@link MiniGame#painting display} the {@link #active}
	 * canvas every 16 milliseconds
	 */
	public void start() {
		if (!on)
			on = true;
	}

	/**
	 * Make the gameloop call {@link MiniGame#sleeping() sleeping()} every 500
	 * milliseconds
	 */
	public void stop() {
		if (on)
			on = false;
		loading = true;
		/* loading only sets to false once another graphics is disposed */
	}

	/*
	 * Full Screen GraphicsEnvironment ge =
	 * GraphicsEnvironment.getLocalGraphicsEnvironment(); GraphicsDevice[] gs =
	 * ge.getScreenDevices(); GraphicsDevice myDevice = gs[0]; Window window =
	 * (Window) frame; try { myDevice.setFullScreenWindow(myWindow); } finally {
	 * myDevice.setFullScreenWindow(null); }
	 */
}
