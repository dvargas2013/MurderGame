package statics;

import main.MurderSet;
import main.Person;
import main.Room;
import main.Weapon;

/**
 * The Answer to the puzzle at hand. The game is afoot. Etc etc. puns.
 *
 * @author danv
 */
public abstract class MurderSets {
	private static MurderSet[] Answer;

	/**
	 * Get the MurderSet with that victim
	 *
	 * @param p
	 *            Person representing the victim
	 * @return a MurderSet[] of length 0 or 1.
	 */
	public static MurderSet[] get(Person p) {
		MurderSet[] ret = new MurderSet[getAmount(p)];
		if (ret.length == 0)
			return ret;
		for (MurderSet m : Answer)
			if (!m.solved && m.sameKilled(p)) {
				ret[0] = m;
				return ret; // only 1 of these should return
			}
		// if you are down here something is wrong. you cant kill someone twice
		// something is wrong with the init method probably
		System.out.println("MurderSets.get(" + p + ") has returned with length " + ret.length);
		return ret;
	}

	/**
	 * Get the MurderSets with that room
	 *
	 * @param r
	 *            a Room
	 * @return a MurderSet[] with all the unsolved MurderSet s with that room
	 */
	public static MurderSet[] get(Room r) {
		MurderSet[] ret = new MurderSet[getAmount(r)];
		int c = 0;
		for (MurderSet m : Answer)
			if (!m.solved && m.sameRoom(r))
				ret[c++] = m;
		return ret;
	}

	/**
	 * All the unsolved MurderSet s with that weapon
	 *
	 * @param w
	 *            a Weapon
	 * @return a MurderSet[] with all the unsolved murders with that weapon
	 */
	public static MurderSet[] get(Weapon w) {
		MurderSet[] ret = new MurderSet[getAmount(w)];
		int c = 0;
		for (MurderSet m : Answer)
			if (!m.solved && m.sameWeapon(w))
				ret[c++] = m;
		return ret;
	}

	/**
	 * Find out how many times this person was killed. If you solved that murder
	 * it should return 0.
	 *
	 * @param p
	 *            Person representing the victim
	 * @return 1 if there is unsolved murder of that victim, 0 if murder has
	 *         been solved.
	 */
	public static int getAmount(Person p) {
		int c = 0;
		for (MurderSet m : Answer)
			if (!m.solved && m.sameKilled(p))
				c++;
		assert c < 2;
		return c;
	}

	/**
	 * How many unsolved murders happened in this room?
	 *
	 * @param r
	 *            a Room
	 * @return number of unsolved murders in room
	 */
	public static int getAmount(Room r) {
		int c = 0;
		for (MurderSet m : Answer)
			if (!m.solved && m.sameRoom(r))
				c++;
		return c;
	}

	/**
	 * @param w
	 *            a Weapon
	 * @return the number of unsolved murders with that weapon
	 */
	public static int getAmount(Weapon w) {
		int c = 0;
		for (MurderSet m : Answer)
			if (!m.solved && m.sameWeapon(w))
				c++;
		return c;
	}

	/**
	 * Create the puzzle. There are as many murders as there are people. Make
	 * sure you initialized People, Weapons, and Rooms first.
	 */
	public static void init() {
		Answer = new MurderSet[Person.chosen.length];
		for (int i = 0; i < Person.chosen.length; i++)
			Answer[i] = new MurderSet(Person.chosen[i], Person.rand(), Weapon.rand(), Room.rand());
	}
}
