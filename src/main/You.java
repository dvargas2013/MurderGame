package main;

import java.awt.Graphics;

import bases.ScreenPoint;
import bases.Sprite;
import statics.Global;

/**
 * A sprite that represents you
 *
 * @author danv
 */
public class You extends Sprite {
	private static final String directory = "You/";
	private static final int DOWN = 0, LEFT = 1, UP = 2, RIGHT = 3, STATIC = 0;
	private static final String[] poseAnims;
	private static final int poseDurations;
	private static final String[] poses = new String[] { "d_", "l_", "u_", "r_" };

	static {
		String[] input = Global.getFileContent(directory + "init");
		// Should be a series of images with directory+poses[]+poseAnims[]
		poseAnims = input[0].split(" ");
		poseDurations = Integer.parseInt(input[1]);
	}

	private boolean movedD, movedU, movedL, movedR;

	public You() {
		super(directory, poses, poseAnims, poseDurations);
	}

	@Override
	public void draw(Graphics g) {
		if (setAnims())
			super.draw(g);
		else
			super.draw(g, STATIC);
	}

	@Override
	public void draw(Graphics g, ScreenPoint cam) {
		if (setAnims())
			super.draw(g, cam.plus(width / 2, .9 * height));
		else
			super.draw(g, STATIC, cam.plus(width / 2, .9 * height));
	}

	private boolean setAnims() {
		if (movedU)
			setCurrentAnimation(UP);
		else if (movedD)
			setCurrentAnimation(DOWN);
		else if (movedL)
			setCurrentAnimation(LEFT);
		else if (movedR)
			setCurrentAnimation(RIGHT);
		else
			return false;
		movedU = movedD = movedL = movedR = false;
		return true;
	}

	@Override
	public void moveDownBy(int dy) {
		movedD = true;
		super.moveDownBy(dy);
	}

	@Override
	public void moveLeftBy(int dx) {
		movedL = true;
		super.moveLeftBy(dx);
	}

	@Override
	public void moveRightBy(int dx) {
		movedR = true;
		super.moveRightBy(dx);
	}

	@Override
	public void moveUpBy(int dy) {
		movedU = true;
		super.moveUpBy(dy);
	}

	@Override
	public String toString() {
		return "You(" + super.toString() + ");";
	}
}
