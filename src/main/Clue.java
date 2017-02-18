package main;

import java.util.ArrayList;

import statics.Global;
import statics.MurderSets;

/**
 * Location can be the 4 things in MurderSet. Given that you and guess (as a
 * MurderSet) you will give a clue as to what actually happened. <br>
 * Given the location and the guess it will tell you how many murders happened
 * in location <br>
 * (aka with that weapon, in that room, or to that victim)<br>
 * (you can't inspect a killer since the killers are also victims so that would
 * get confusing). <br>
 * as well as how many it will also give a little bit of extra information.
 *
 * @author danv
 */
class Clue {
	private static final int ALLWRONG = 0, RIGHT1 = 1, WRONG1 = 2, ALLRIGHT = 3;
	/**
	 * Where you found this clue
	 */
	public static final int BODY = 1, WEAPON = 2, ROOM = 3;

	@SuppressWarnings("unchecked")
	private final static ArrayList<String>[] clues = new ArrayList[] { new ArrayList<String>(), new ArrayList<String>(),
			new ArrayList<String>(), new ArrayList<String>() };

	static { // TODO add cooler clue conversation thingoes
		String[] input = Global.getFileContent("clues");
		for (String line : input)
			clues[Integer.parseInt(line.substring(1, 2))].add(line.substring(4));
	}

	private static String typeName(int i) {
		// Added to the clue to tell what kind of thing is (index)
		switch (i) {
		case WEAPON:
			return "Weapon";
		case ROOM:
			return "Room";
		case BODY:
			return "Victim";
		case 0:
			return "Killer";
		}
		return "[type]";
	}

	private MurderSet compareAgainst;
	private final MurderSet guess;

	/**
	 * How many murders happened in room or with weapon Or if you solved this
	 * victim already it's 0
	 */
	private int howmany = 0;
	private final int location;

	private boolean[] same; // killer, killed, weapon, room

	final String string;

	/**
	 * Representation of a clue. Given a guess it will tell you how many of the
	 * guess is correct and a little extra bit of information if needed.
	 * Depending on the Location the clue was found it will look through the
	 * possible answers and make sure you are finding clues for that thing. <br>
	 * For example, if you are checking out the Weapon but no murder used that
	 * weapon it should say that the weapon is clean.
	 *
	 * @param loc
	 *            Depending on where the clue is found. One for Bodies, Weapons,
	 *            and Rooms.
	 * @param g
	 *            A MurderSet representing the guess. (It specifies who you
	 *            think the killer, the victim, the weapon, and the room)
	 */
	public Clue(int loc, MurderSet g) {
		location = loc;
		guess = g;

		MurderSet[] related = {};
		switch (location) { // Find the MurderSets that match
		case BODY:
			related = MurderSets.get(guess.getKilled());
			break;
		case WEAPON:
			related = MurderSets.get(guess.getWeapon());
			break;
		case ROOM:
			related = MurderSets.get(guess.getRoom());
			break;
		}

		howmany = related.length;

		if (howmany != 0) {
			compareAgainst = related[0];

			same = new boolean[] { guess.sameKiller(compareAgainst.getKiller()),
					guess.sameKilled(compareAgainst.getKilled()), guess.sameWeapon(compareAgainst.getWeapon()),
					guess.sameRoom(compareAgainst.getRoom()) };
		}

		string = getStr();
	}

	/**
	 * Give you the guess made. AKA the location and then what the guess was for
	 * that given location.
	 *
	 * @return A String representing the MurderSet Guessed with distinction on
	 *         where the guess is made
	 */
	public String clueTag() {
		switch (location) {
		case BODY:
			return "Asking About " + guess.getKilledName() + ": (" + guess.getKillerName() + "," + guess.getWeaponName()
					+ "," + guess.getRoomName() + ")";
		case ROOM:
			return "In " + guess.getRoomName() + ": (" + guess.getKillerName() + "," + guess.getKilledName() + ","
					+ guess.getWeaponName() + ")";
		case WEAPON:
			return "Found " + guess.getWeaponName() + ": (" + guess.getKillerName() + "," + guess.getKilledName() + ","
					+ guess.getRoomName() + ")";
		}
		return "";
	}

	private String getName(int i) { // get the name of the thing in that
									// location
		switch (i) {
		case BODY:
			return guess.getKilledName();
		case ROOM:
			return guess.getRoomName();
		case WEAPON:
			return guess.getWeaponName();
		case 0:
			return guess.getKillerName();
		}
		return "[name]";
	}

	private String getStr() { //
		String out = "";
		if (howmany == 0)
			switch (location) {
			case BODY:
				Phone.removePerson(guess.getKilled()); // Remove from phone but
														// could always be a
														// killer
				return "You already solved the murder of " + guess.getKilledName();
			case WEAPON:
				guess.getWeapon().moveTo(-100, -100); // Never need that weapon
														// again
				return "There are no murders using " + guess.getWeaponName();
			case ROOM:
				guess.getRoom().icon.moveTo(-100, -100); // Never need that
															// room again
				return "There are no murders in " + guess.getRoomName();
			}
		else if (howmany > 1)
			switch (location) { // Only weapons and rooms can be used twice.
			// since people can't be murdered twice
			case WEAPON:
				out += "There are " + howmany + " murders using " + guess.getWeaponName()
						+ " but the obvious most recent one gives this clue:\n";
				break;
			case ROOM:
				out += "There are " + howmany + " murders in " + guess.getRoomName()
						+ " but the obvious most recent one gives this clue:\n";
				break;
			}

		// If you are down here then that means that howmany >= 1
		// that means something matches in compareAgainst
		int c = -1; // Count how many the same not counting the one you know
					// matches by default (the one in location)
		for (boolean b : same)
			c += b ? 1 : 0;
		out += clues[c].get((int) (clues[c].size() * Math.random()));

		int i;
		switch (c) {
		// Remember there is always at least 1 right which is the location
		case ALLWRONG: // 3 wrong 1 right
			break;
		case RIGHT1: // 2 wrong 2 right
			i = oneCorrect();
			out = out.replaceAll("__", typeName(i));
			out = out.replaceAll("_", getName(i));
			break;
		case WRONG1: // 1 wrong 3 right
			i = oneWrong();
			out = out.replaceAll("__", typeName(i));
			out = out.replaceAll("_", getName(i));
			break;
		case ALLRIGHT: // 0 wrong 4 right
			compareAgainst.solved = true;
			Phone.removePerson(guess.getKilled());
			// Remove from phone but could always be a killer
			break;
		}

		return out.replaceAll("-", getName(location));
	}

	private int oneCorrect() { // Find location of the one that is right
		for (int i = 0; i < 4; i++) {
			if (i == location)
				continue;
			if (same[i])
				return i;
		}
		return -1;
	}

	private int oneWrong() { // Find location of the one that is wrong
		for (int i = 0; i < 4; i++) {
			if (i == location)
				continue;
			if (!same[i])
				return i;
		}
		return -1;
	}

	@Override
	public String toString() {
		return "Clue( " + guess + " , " + compareAgainst + " , " + string + " );";
	}
}
