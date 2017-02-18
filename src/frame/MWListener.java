package frame;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * This is the MouseMotionListener I put in my games. It doesn't do much. It
 * save the last location of actions.
 *
 * <pre>
 * It has a few type of <b>events</b>:
 * 	leftright
 *  updown
 *
 * It has similar <b>methods</b> for each type:
 * 	hasNew[_] - checks if there is a new [_] event
 * 	noNew[_]s - checks if there are no new [_] events
 * 	peek[_] - whatever the last [_] event listener set the values as
 * 	consume[_] - hasNew[_] ? peek[_] : null; once consumed hasNew[_] will return false;
 * where [_] is event type
 * </pre>
 *
 * @author danv
 */
public class MWListener implements MouseWheelListener {
	private int leftright, updown;
	private boolean LRupdated, UDupdated;
	private MouseWheelEvent firstLR, lastLR, firstUD, lastUD;

	MWListener() {
		reset();
	}

	public MouseWheelEvent peekFirstLeftRight() {
		return firstLR;
	}

	public MouseWheelEvent peekLastLeftRight() {
		return lastLR;
	}

	public MouseWheelEvent peekFirstUpDown() {
		return firstUD;
	}

	public MouseWheelEvent peekLastUpDown() {
		return lastUD;
	}
	public int peekLeftRight() {
		return leftright;
	}

	public int peekUpDown() {
		return updown;
	}

	public boolean hasNewLeftRight() {
		return LRupdated;
	}

	public boolean hasNoNewLeftRights() {
		return !LRupdated;
	}

	public boolean hasNewUpDown() {
		return UDupdated;
	}

	public boolean hasNoNewUpDowns() {
		return !UDupdated;
	}

	public int consumeLeftRight() {
		if (LRupdated) {
			LRupdated = false;
			return leftright;
		}
		return 0;
	}

	public int consumeUpDown() {
		if (UDupdated) {
			UDupdated = false;
			return updown;
		}
		return 0;
	}

	public MouseWheelEvent consumeFirstLeftRight() {
		if (LRupdated) {
			LRupdated = false;
			return firstLR;
		}
		return null;
	}

	public MouseWheelEvent consumeFirstUpDown() {
		if (UDupdated) {
			UDupdated = false;
			return firstUD;
		}
		return null;
	}

	public MouseWheelEvent consumeLastLeftRight() {
		if (LRupdated) {
			LRupdated = false;
			return lastLR;
		}
		return null;
	}

	public MouseWheelEvent consumeLastUpDown() {
		if (UDupdated) {
			UDupdated = false;
			return lastUD;
		}
		return null;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.isShiftDown()) {
			if (!LRupdated) {
				firstLR = e;
				leftright = 0;
			}
			LRupdated = true;
			lastLR = e;
			leftright += e.getWheelRotation();
		} else {
			if (!UDupdated) {
				firstUD = e;
				updown = 0;
			}
			UDupdated = true;
			lastUD = e;
			updown += e.getWheelRotation();
		}
	}

	/**
	 * Make all the booleans false. Good for when the action listener is removed
	 * while something is still pressed.
	 */
	public void reset() {
		leftright = updown = 0;
		LRupdated = UDupdated = false;
		firstLR = lastLR = firstUD = lastUD = null;
	}
}