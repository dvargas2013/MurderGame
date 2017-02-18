package frame;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * This is the MouseMotionListener I put in my games. It doesn't do much. It
 * save the last location of actions.
 *
 * <pre>
 * It has a few type of <b>events</b>:
 * 	dragged
 * 	moved
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
public class MMListener implements MouseMotionListener {
	private MouseEvent dragged, moved;
	private boolean Dupdated, Mupdated;

	MMListener() {
		reset();
	}

	public MouseEvent peekDrag() {
		return dragged;
	}

	public MouseEvent peekMove() {
		return moved;
	}

	public MouseEvent consumeDrag() {
		if (Dupdated) {
			Dupdated = false;
			return dragged;
		}
		return null;
	}

	public MouseEvent consumeMove() {
		if (Mupdated) {
			Mupdated = false;
			return moved;
		}
		return null;
	}

	public boolean hasNewDrag() {
		return Dupdated;
	}

	public boolean hasNewMove() {
		return Mupdated;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		dragged = e;
		Dupdated = true;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		moved = e;
		Mupdated = true;
	}

	public boolean noNewDrags() {
		return !Dupdated;
	}

	public boolean noNewMoves() {
		return !Mupdated;
	}

	/**
	 * Make all the booleans false. Good for when the action listener is removed
	 * while something is still pressed.
	 */
	public void reset() {
		dragged = moved = null;
		Dupdated = Mupdated = false;
	}
}
