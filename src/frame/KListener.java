package frame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * This is the KeyListener I put in my games. It has booleans for when things
 * are clicked.
 *
 * @author danv
 */
public class KListener implements KeyListener {
	private String accumulatetype;
	public boolean DOWN, UP, LEFT, RIGHT, ENTER, X, MENU;

	KListener() {
	}

	public String getString() {
		String s = accumulatetype;
		accumulatetype = "";
		return s;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_S:
			DOWN = true;
			break;
		case KeyEvent.VK_UP:
		case KeyEvent.VK_W:
			UP = true;
			break;
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_A:
			LEFT = true;
			break;
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_D:
			RIGHT = true;
			break;
		case KeyEvent.VK_Z:
		case KeyEvent.VK_I:
		case KeyEvent.VK_ENTER:
			ENTER = true;
			break;
		case KeyEvent.VK_X:
		case KeyEvent.VK_O:
			X = true;
			break;
		case KeyEvent.VK_C:
		case KeyEvent.VK_P:
			MENU = true;
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_S:
			DOWN = false;
			break;
		case KeyEvent.VK_UP:
		case KeyEvent.VK_W:
			UP = false;
			break;
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_A:
			LEFT = false;
			break;
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_D:
			RIGHT = false;
			break;
		case KeyEvent.VK_Z:
		case KeyEvent.VK_I:
		case KeyEvent.VK_ENTER:
			ENTER = false;
			break;
		case KeyEvent.VK_X:
		case KeyEvent.VK_O:
			X = false;
			break;
		case KeyEvent.VK_C:
		case KeyEvent.VK_P:
			MENU = false;
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		accumulatetype += e.getKeyChar();
	}

	/**
	 * Make all the booleans false. Good for when the action listener is removed
	 * while something is still pressed.
	 */
	public void reset() {
		DOWN = UP = LEFT = RIGHT = ENTER = X = MENU = false;
		accumulatetype = "";
	}
}
