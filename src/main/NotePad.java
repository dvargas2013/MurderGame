package main;

import java.awt.Graphics;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

import bases.ScreenPoint;
import bases.TextBox;
import frame.DrawableCanvas;
import frame.MainFrame;
import frame.MiniGame;

/**
 * Where all the found clues go. <br>
 * You can scroll up and down.
 *
 * @author danv
 */
public class NotePad extends MiniGame implements MouseWheelListener {
	/**
	 * Save where the Camera is. Pair is less memory intensive not that it
	 * really matters. You could use a point if you really want.
	 */
	private final ScreenPoint Camera = new ScreenPoint();
	/**
	 * Clues generate strings. Strings are saved in TextBox. TextBoxes are saved
	 * for display.
	 */
	private final ArrayList<TextBox> texts = new ArrayList<>();
	private TextBox active;

	public NotePad(MainFrame main) {
		super(main);
	}

	/**
	 * Add a Clue to the Texts being displayed.
	 */
	public void addClue(Clue c) {
		if (MainCanvas.DEBUG)
			System.out.println(c);
		active = new TextBox(c.clueTag() + " " + c.string, width - 10);
		active.moveTo(5, 100); // Right Under Icon Menu.EXIT
		texts.add(active);
		if (Phone.len() == 0)
			parent.clear();
	}

	@Override
	public void gainControl(DrawableCanvas listenerController) {
		setTitle(" - NoteBook");
		Camera.moveTo(0, 0);
		mouse = listenerController.activateDefaultMouseListener();
		motion = listenerController.activateDefaultMouseMotionListener();
		listenerController.addMouseWheelListener(this);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		Camera.moveUpBy(e.getWheelRotation());
	}

	@Override
	public void resized() {
		for (TextBox tb : texts)
			tb.changeWidth(width - 10);
	}

	@Override
	public void painting(Graphics g) {
		g.drawString("Note Pad", width / 2, 10);

		if (active != null && active == texts.get(texts.size() - 1) && active.getHeight() < 0) {
			active.draw(g);
			int c = active.getHeight();
			for (TextBox tb : texts)
				tb.moveDownBy(c * 2);
			active = null;
		}

		int s = texts.size();
		if (s > 0)
			for (TextBox tb : texts)
				tb.draw(g, Camera);
		else
			g.drawString("This is where the clues you have found will appear", 20, height / 2 - Camera.y);

		if (active != null)
			g.drawLine(active.x - Camera.x, active.y - Camera.y, active.x - Camera.x + width - 10, active.y - Camera.y);

		Menu.EXIT.draw(g);
	}

	@Override
	public void prepaint() {
		if (mouse.hasNewClick()) {
			ScreenPoint p = new ScreenPoint(mouse.consumeClick());
			active = null;
			if (Menu.EXIT.containsPoint(p)) {
				popCanvas();
				return;
			}
		}

		if (active != null && motion.peekDrag() != null
				&& System.currentTimeMillis() - 250 > motion.peekDrag().getWhen())
			active = null;

		if (motion.hasNewDrag()) {
			// if drag occurs after being released a whole second
			ScreenPoint p = new ScreenPoint(motion.consumeDrag());
			if (active != null)
				active.y = p.y + Camera.y;
			else
				for (TextBox tb : texts) // TODO Optimize this
					if (tb.getHeight() > 0 && tb.y < p.y && tb.y + tb.getHeight() > p.y)
						active = tb;
		}
	}
}