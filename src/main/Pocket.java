package main;

import java.awt.Graphics;

import bases.ScreenPoint;
import frame.DrawableCanvas;
import frame.MainFrame;
import frame.MiniGame;
import statics.Global;
import statics.MurderSets;

/**
 * A Place to stash the Weapons you've found. And make guesses with them.
 *
 * @author danv
 */
public class Pocket extends MiniGame {
	private final GuessMaker guess;

	/**
	 * Weapons found so far. I know there are at most a set number. So no need
	 * for ArrayList.
	 */
	private final Weapon[] weaponsFound = new Weapon[Weapon.chosen.length];
	private int wepfoundind = 0;

	public Pocket(MainFrame main, GuessMaker guess) {
		super(main);
		this.guess = guess;
	}

	/**
	 * Add weapon to found list
	 */
	public void addWeapon(Weapon w) {
		weaponsFound[wepfoundind++] = w;
		w.moveTo(Global.positions.popRandom().toPoint());
		w.saveLocation();
		guess.addWeapon(w);
	}

	@Override
	public void gainControl(DrawableCanvas listenerController) {
		setTitle(" - Pocket");
		mouse = listenerController.activateDefaultMouseListener();
	}

	@Override
	public void painting(Graphics g) {
		g.drawString("Pocket™", width / 2, 10);
		Menu.EXIT.draw(g);
		for (int i = 0; i < wepfoundind; i++)
			weaponsFound[i].draw(g);
		if (wepfoundind == 0)
			g.drawString("THIS IS WHERE YOU WILL BE ABLE TO SEE THE WEAPONS", 10, height / 2);
	}

	@Override
	public void prepaint() {
		if (mouse.hasNewClick()) {
			ScreenPoint p = new ScreenPoint(mouse.consumeClick());
			if (Menu.EXIT.containsPoint(p)) {// Did you click on the EXIT?
				popCanvas();
				return;
			}

			for (int i = 0; i < wepfoundind; i++)
				if (weaponsFound[i].containsPoint(p)) { // Did you click on a
					// weapon?
					Weapon wep = weaponsFound[i];

					if (MurderSets.getAmount(wep) == 0) { // Weapon is clean
						guess.note.addClue(new Clue(Clue.WEAPON, new MurderSet(null, null, wep, null)));
						addCanvas(guess.note); // open notebook thing
					} else {
						guess.prepare(wep); // open guess maker
						addCanvas(guess);
					}

					break;
				}
		}
	}
}
