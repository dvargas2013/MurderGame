package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;

import bases.Circle;
import bases.Line;
import bases.Pair;
import bases.PairList;
import bases.ScreenPoint;
import statics.Global;

/**
 * A Room is a space that can display itself, the floor of its neighbors, and
 * its drawable content <br>
 * <br>
 * It also has 4 Boundary Lines and 5 Spawn Points. As well as an Icon
 * representation and a name.
 *
 * @author danv
 */
public class Room extends ScreenPoint {
	public final String name;
	private final Path roomdir;

	/**
	 * Given a name of a directory in Rooms/ will initialize a Room of that name
	 */
	private Room(String givenname) {
		name = givenname;
		roomdir = Global.root.resolve("Rooms/" + givenname);
		setImages();
		initLines();
	}

	@Override
	public void draw(Graphics g) {
		draw(g, lastcam);
	}

	private final ScreenPoint lastcam = new ScreenPoint();
	private static final int floordepth = 3;
	private double floorid = 0;

	private void drawFloor(Graphics g, ScreenPoint cam) {
		int w = floor.getWidth() / 2, h = floor.getHeight() / 2;
		for (int i = 0; i < tileW; i++)
			for (int j = 0; j < tileH; j++)
				g.drawImage(floor, x + (i - j) * w - cam.x, y + (i + j) * h - cam.y, null);
	}

	private static void drawFloor(Graphics g, Room room, ScreenPoint cam, int depth, double id) {
		if (depth < 0 || room.floorid == id)
			return;

		room.floorid = id;

		// iterate floors
		ArrayList<Room> ey = new ArrayList<>();
		for (Room r : room.connectedTo)
			if (r != null && !r.at(0, 0)) {
				int i;
				for (i=0; i<ey.size(); i++) // insertion sort
					if (ey.get(i).lastvisit > r.lastvisit) {
						ey.add(i, r);
						break;
					}
				if (i==ey.size())
					ey.add(r);
			}
		for (Room r : ey)
			drawFloor(g, r, cam.minus(room), depth - 1, id);

		room.drawFloor(g, cam);

		if (MainCanvas.DEBUG && depth == floordepth)
			for (int i = 0; i < room.connectedTo.length; i++) {
				Room r = room.connectedTo[i];
				if (r != null)
					g.setColor(Color.GREEN);
				else
					g.setColor(Color.RED);
				room.lines[i].draw(g, cam);
				room.spawns[i].draw(g, cam);
			}
	}

	/**
	 * Draws the floor and the floor of neighbors, draws the doors, and draws
	 * the contents
	 */
	@Override
	public void draw(Graphics g, ScreenPoint c) {
		lastcam.moveTo(c);
		ScreenPoint cam = c.plus(character);
		g.drawImage(floor, x - cam.x, y - cam.y, null);

		drawFloor(g, this, cam, floordepth, Math.random());
		// change = false;
		// Draw Top doors
		if (connectedTo[TopLeft] != null)
			door.drawRight(g, cam.minus(spawns[TopLeft]));
		if (connectedTo[TopRight] != null)
			door.drawLeft(g, cam.minus(spawns[TopRight]));

		// Center floor radius. aka weapon spawn points
		if (MainCanvas.DEBUG) {
			g.setColor(Color.CYAN);
			spawns[Center].draw(g, cam);
		}

		// Draw Drawables
		g.setColor(Color.BLACK);
		for (Weapon d : weapons)
			d.draw(g, cam);
		if (MainCanvas.DEBUG)
			g.drawString(name, spawns[Center].x - cam.x, spawns[Center].y - cam.y);
		character.draw(g, cam);

		// Draw bottoms doors
		if (connectedTo[BotRight] != null)
			door.drawRight(g, cam.minus(spawns[BotRight]));
		if (connectedTo[BotLeft] != null)
			door.drawLeft(g, cam.minus(spawns[BotLeft]));
	}

	private You character;
	private long lastvisit;

	public void addCamera(You camera) {
		character = camera;
	}

	/**
	 * When activating a room you need to move the room to (0,0) and move the
	 * neighboring rooms to match where they need to go
	 */
	public void makeActive(Room oldroom) {
		if (oldroom == null)
			character.moveTo(spawns[Center]);
		else
			character.moveBy(oldroom.minus(this));
		// theoretically oldroom should be at (0,0)

		moveTo(0, 0);
		lastvisit = System.currentTimeMillis();

		for (int i = 0; i < 4; i++) {
			Room r = connectedTo[i];
			if (r != null)
				r.moveTo(spawns[i].minus(r.spawns[(i + 2) % 4]));
		}
		// change = true;
	}

	// static boolean change = false;

	final ArrayList<Weapon> weapons = new ArrayList<>();

	/**
	 * Add a Weapon or You or any other Drawable to the drawable contents of the
	 * room
	 */
	public void add(Weapon wep) {
		weapons.add(wep);
	}

	/**
	 * Remove a Weapon or You or any other Drawable from the drawable contents
	 * of the room
	 */
	public void remove(Weapon wep) {
		weapons.remove(wep);
	}

	@Override
	public String toString() {
		return "Room(" + name + ");";
	}

	private BufferedImage floor;
	public RoomIcon icon;
	private Door door;

	private void setImages() {
		File f = roomdir.resolve("Floor.png").toFile();
		if (!f.exists())
			f = Global.root.resolve("Rooms/[default]/Floor.png").toFile();
		icon = new RoomIcon(this, f.toString());
		floor = floorTileTransform(Global.getImage(f));
		door = new Door("Rooms/" + name + "/Door.png");
	}

	private static BufferedImage floorTileTransform(BufferedImage image) {
		AffineTransform at = new AffineTransform();
		at.scale(1, .5);
		at.rotate(Math.PI / 4);
		/* make a rectangle and apply transform to it to get new bounds */
		Rectangle wh = at.createTransformedShape(new Rectangle(image.getWidth(), image.getHeight()))
				.getBounds();
		if (wh.x != 0 || wh.y != 0) {
			AffineTransform TX = new AffineTransform();
			TX.translate(-wh.x, -wh.y);
			at.preConcatenate(TX);
		}
		BufferedImage displayImage = new BufferedImage(wh.width, wh.height, BufferedImage.TYPE_INT_ARGB);
		/* draw old image as per transformation onto new image */
		displayImage.createGraphics().drawImage(image, at, null);
		return displayImage;
	}

	private int tileW, tileH;

	private void initLines() {
		String[] input;
		if (roomdir.resolve("init").toFile().exists())
			input = Global.getFileContent("Rooms/" + name + "/init");
		else
			input = Global.getFileContent("Rooms/[default]/init");

		String[] ints = input[0].split(" ");
		int a = Integer.parseInt(ints[0]);
		int b = Integer.parseInt(ints[1]);
		int c, d;
		if (input.length > 1) {
			ints = input[1].split(" ");
			c = Integer.parseInt(ints[0]);
			d = Integer.parseInt(ints[1]);
		} else {
			c = a;
			d = b;
		}
		if (Math.random() < .5) {
			tileW = a + (int) (b * Math.random());
			tileH = c + (int) (d * Math.random());
		} else {
			tileW = c + (int) (d * Math.random());
			tileH = a + (int) (b * Math.random());
		}

		setLines();
	}

	final private static int TopLeft = 0, TopRight = 1, BotRight = 2, BotLeft = 3, Center = 4;
	final Room[] connectedTo = new Room[4];
	final Line[] lines = new Line[4];
	final Circle[] spawns = new Circle[5];
	private final boolean[] posArr = new boolean[4];

	public Circle centerSpawn() {return spawns[Room.Center];}

	/**
	 * Gives a boolean to if there is another room against that wall.
	 *
	 * @param j
	 *            - Room.TopLeft, Room.TopRight, Room.BotLeft, Room.BotRight
	 * @return true if there is a connected room at that location
	 */
	private boolean connectedAt(int j) {
		return connectedTo[j % 4] != null;
	}

	/**
	 * Which side of that lines should you be on?
	 *
	 * @param i
	 *            Line number
	 * @return true if lines[i].distance(p) should be greater than 0 else false
	 */
	public boolean positivity(int i) {
		return posArr[i];
	}

	/**
	 * Changes what this room and the room passed it are connected to.<br>
	 * r1.connectTo(r2, Room.TopLeft) is equivalent to r2.connectTo(r1,
	 * Room.BotRight) since rooms are connected by relative opposite sides
	 *
	 * @param room
	 *            Another room
	 * @param i
	 *            Which of the 4 sides to connect the room to
	 */
	private void connectTo(Room room, int i) {
		connectedTo[i % 4] = room;
		room.connectedTo[(i + 2) % 4] = this;
	}

	/**
	 * If tileW and tileH have changed call this to reset the lines and spawns
	 */
	private void setLines() {
		int w = floor.getWidth() / 2, h = floor.getHeight() / 2;
		// shifted by w, to align stuff; don't ask, it's math
		ScreenPoint[] points = new ScreenPoint[] {
				new ScreenPoint(w, 0), // 0,0
				new ScreenPoint((tileW + 1) * w, tileW * h), // 1,0
				new ScreenPoint((tileW - tileH + 1) * w, (tileW + tileH) * h), // 1,1
				new ScreenPoint((-tileH + 1) * w, tileH * h) // 0,1
		};
		for (int i = 0; i < 4; i++) {
			lines[i] = new Line(points[i], points[(i + 1) % 4]);
			spawns[i] = new Circle(lines[i].getMidPoint(), 20);
		}

		// TODO center spawn size
		spawns[Center] = new Circle((tileW - tileH + 2) * w / 2, (tileW + tileH) * h / 2,
				Math.min(tileW, tileH) * Math.min(w, h) / 2);
		for (int i = 0; i < lines.length; i++)
			posArr[i] = lines[i].distance(spawns[Center]) > 0;
	}

	public static Room[] chosen;
	public final static String[] Rooms = Global.root.resolve("Rooms").toFile()
			.list((dir, name) -> !name.startsWith(".") && !name.startsWith("["));

	public static void init(int c) {
		/* choose a few, create them, yay */
		chosen = translate(Global.shuffle(Rooms, c));

		/* connect the rooms in randomly fancy ways */
		PairList outs = new PairList();
		for (int j = 0; j < 4; j++) // For each place room is connected
			outs.add(0, j);

		for (int roomindex = 1; roomindex < chosen.length; roomindex++) {
			Pair p = outs.popRandom();
			chosen[p.i].connectTo(chosen[roomindex], p.j);

			for (int j = 0; j < 4; j++)
				if (!chosen[roomindex].connectedAt(j))
					outs.add(roomindex, j);
		}
	}

	public static Room rand() {
		return chosen[(int) (Math.random() * chosen.length)];
	}

	private static Room[] translate(String[] sh) {
		Room[] ret = new Room[sh.length];
		for (int i = 0; i < sh.length; i++)
			ret[i] = new Room(sh[i]);
		return ret;
	}
}
