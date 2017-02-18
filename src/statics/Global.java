package statics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;

import bases.PairList;
import frame.MainFrame;

/**
 * This is where the main method is. And where I have file and array handlers.
 *
 * @author danv
 */
public abstract class Global {
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		MainFrame mainFrame = new MainFrame() {
			private LandingPage mc;

			@Override
			public void gainControl() {
				setBaseTitle("MURDER GAME");
				if (mc == null)
					mc = new LandingPage(this);
				addCanvas(mc);
			}
		};
	}

	public static final PairList positions = new PairList();
	public static final Path root;

	// TODO remove getParent when exporting to jar file
	// static {
	// root =
	// Paths.get(Global.class.getResource("/").getFile()).resolve("resources");
	// System.out.println(root);
	// }

	static {
		root = Paths.get(Global.class.getResource("/").getFile()).getParent().resolve("resources");
		changeOutStream();
		System.out.println(root);
	}

	private static void changeOutStream() {
		System.setOut(new PrintStream(System.out) {
			private StackTraceElement getCallSite() {
				for (StackTraceElement e : Thread.currentThread().getStackTrace())
					if (!e.getMethodName().equals("getStackTrace") && !e.getClassName().equals(getClass().getName()))
						return e;
				return null;
			}

			@Override
			public void println(Object o) {
				StackTraceElement e = getCallSite();
				String callSite = e == null ? "??" : String.format("(%s:%d)", e.getFileName(), e.getLineNumber());
				super.println(String.format("%-30s", callSite) + o);
			}

			@Override
			public void println(String s) {
				println((Object) s);
			}
		});
	}

	public static void initPositions(int w, int h) {
		// Grab all the places you can put things in
		Global.positions.clear();
		for (int i = 100; i < w - 64; i += 100)
			for (int j = 100; j < h - 64; j += 100)
				Global.positions.add(i, j + 16);
		// You need enough spots to place things in.
		// If window isn't big enough idk what to tell you
	}

	public static String[] getFileContent(String file) {
		ArrayList<String> lines = new ArrayList<>();

		try (Scanner scanner = new Scanner(root.resolve(file).toFile())) {
			while (scanner.hasNextLine())
				lines.add(scanner.nextLine());
			scanner.close();
		} catch (IOException e) {
			System.out.println("Can't read file " + file);
			e.printStackTrace();
		}

		return lines.toArray(new String[lines.size()]);
	}

	/**
	 * That path you gave me better exist.
	 */
	public static BufferedImage getImage(File file) {
		try {
			return ImageIO.read(file);
		} catch (IOException e) {
			System.out.println("Can't get image");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Resolves string against root resources directory and finds image
	 */
	public static BufferedImage getImage(String file) {
		try {
			return ImageIO.read(root.resolve(file).toFile());
		} catch (@SuppressWarnings("unused") IOException e) {
			System.out.println("Can't get image " + file);
		}
		return null;
	}

	/**
	 * Will Shuffle the String[] given and return a new String[cutoff] with non
	 * repeated elements from the String[] given
	 *
	 * @param list
	 *            String[] you want to shuffle. It will shuffle the whole list.
	 * @param cutoff
	 *            Integer to show how many elements you want, will self adjust
	 *            cutoff if its too big or small
	 * @return new String[cutoff] with elements [0,cutoff) of list
	 */
	public static String[] shuffle(String[] list, int cutoff) {
		int i = list.length, r;
		String item;
		while (0 != i) { // Shuffle list
			r = (int) (Math.random() * i);
			item = list[--i];
			list[i] = list[r];
			list[r] = item;
		}

		int c = cutoff;
		if (cutoff > list.length)
			c = list.length;
		else if (cutoff < 0)
			c = 0;

		String[] lis = new String[c];
		for (i = 0; i < c; i++)
			lis[i] = list[i];
		return lis;
	}

	public static void print(Object[] chosen) {
		for (Object element : chosen)
			System.out.print(element + ", ");
		System.out.println();
	}
}
