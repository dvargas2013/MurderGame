package main;

/**
 * Answers and Guesses are denoted by MurderSet s.<br>
 * This contains a 2 Person s and a Weapon and a Room.<br>
 * There are only getter functions. You could also get names rather than the
 * objects themselves.
 *
 * @author danv
 */
public class MurderSet {
	final private Person killed;
	final private Person killer;
	final private Room room;
	public boolean solved = false;
	final private Weapon weapon;

	/**
	 * The parameters are finalized. If you need another MurderSet make a new
	 * one.
	 */
	public MurderSet(Person killed, Person killer, Weapon weapon, Room room) {
		this.killed = killed;
		this.killer = killer;
		this.weapon = weapon;
		this.room = room;
	}

	public Person getKilled() {
		return killed;
	}

	public String getKilledName() {
		if (killed != null)
			return killed.name;
		return "";
	}

	public Person getKiller() {
		return killer;
	}

	public String getKillerName() {
		if (killer != null)
			return killer.name;
		return "";
	}

	public Room getRoom() {
		return room;
	}

	public String getRoomName() {
		if (room != null)
			return room.name;
		return "";
	}

	public Weapon getWeapon() {
		return weapon;
	}

	public String getWeaponName() {
		if (weapon != null)
			return weapon.name;
		return "";
	}

	public boolean sameKilled(Person p) {
		return killed == p;
	}

	public boolean sameKiller(Person p) {
		return killer == p;
	}

	public boolean sameRoom(Room r) {
		return room == r;
	}

	public boolean sameWeapon(Weapon w) {
		return weapon == w;
	}

	@Override
	public String toString() {
		return "MurderSet(" + killed + killer + weapon + room + ")";
	}

}
