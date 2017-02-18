package frame;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * This is the MouseListener I put in my games. It doesn't do much. It save the
 * last location of actions.
 *
 * <pre>
 * It has a few type of <b>events</b>:
 * 	press
 * 	release
 * 	click *
 * 	enter
 * 	exit
 *
 * It has similar <b>methods</b> for each type:
 * 	hasNew[_] - checks if there is a new [_] event
 * 	noNew[_]s - checks if there are no new [_] events
 * 	peek[_] - whatever the last [_] event listener set the values as
 * 	consume[_] - hasNew[_] ? peek[_] : null; once consumed hasNew[_] will return false;
 *	is[_]d - returns true if the state of the doubled event is [_]
 * where [_] is event type
 *
 * Note: * events don't have is[_]d functions
 * </pre>
 *
 * @author danv
 */
public class MListener implements MouseListener {
	private boolean Cupdated, EEupdated, PRupdated, held, in;
	private MouseEvent clicked, enter, exit, press, release;

	MListener() {
		reset();
	}

	public MouseEvent peekClick() {
		return clicked;
	}

	public MouseEvent peekEnter() {
		return enter;
	}

	public MouseEvent peekExit() {
		return exit;
	}

	public MouseEvent peekPress() {
		return press;
	}

	public MouseEvent peekRelease() {
		return release;
	}

	public MouseEvent consumeClick() {
		if (Cupdated) {
			Cupdated = false;
			return clicked;
		}
		return null;
	}

	public MouseEvent consumeEnter() {
		if (EEupdated && in) {
			EEupdated = false;
			return enter;
		}
		return null;
	}

	public MouseEvent consumeExit() {
		if (EEupdated && !in) {
			EEupdated = false;
			return exit;
		}
		return null;
	}

	public MouseEvent consumePress() {
		if (PRupdated && held) {
			PRupdated = false;
			return press;
		}
		return null;
	}

	public MouseEvent consumeRelease() {
		if (PRupdated && !held) {
			PRupdated = false;
			return release;
		}
		return null;
	}

	public boolean hasNewClick() {
		return Cupdated;
	}

	public boolean hasNewEnter() {
		return EEupdated && in;
	}

	public boolean hasNewEnterExit() {
		return EEupdated;
	}

	public boolean hasNewExit() {
		return EEupdated && !in;
	}

	public boolean hasNewPress() {
		return PRupdated && held;
	}

	public boolean hasNewPressRelease() {
		return PRupdated;
	}

	public boolean hasNewRelease() {
		return PRupdated && !held;
	}

	public boolean isEntered() {
		return in;
	}

	public boolean isExited() {
		return !in;
	}

	public boolean isPressed() {
		return held;
	}

	public boolean isReleased() {
		return !held;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		clicked = e;
		Cupdated = true;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		enter = e;
		in = EEupdated = true;
	}

	@Override
	public void mouseExited(MouseEvent e) {
		exit = e;
		in = false;
		EEupdated = true;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		press = e;
		PRupdated = held = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		release = e;
		held = false;
		PRupdated = true;
	}

	public boolean noNewClicks() {
		return !Cupdated;
	}

	public boolean noNewEnterExits() {
		return !EEupdated;
	}

	public boolean noNewEnters() {
		return !EEupdated || !in;
	}

	public boolean noNewExits() {
		return !EEupdated || in;
	}

	public boolean noNewPresses() {
		return !PRupdated || !held;
	}

	public boolean noNewPressReleases() {
		return !PRupdated;
	}

	public boolean noNewReleases() {
		return !PRupdated || held;
	}

	/**
	 * Make all the booleans false. Good for when the action listener is removed
	 * while something is still pressed.
	 */
	public void reset() {
		PRupdated = held = Cupdated = EEupdated = in = false;
		press = release = clicked = enter = exit = null;
	}
}
