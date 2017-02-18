package bases;

import java.util.ArrayList;

/**
 * A list of double integers.
 *
 * @author danv
 */
public class PairList {
	private final ArrayList<Pair> data = new ArrayList<>();

	public void add(int i, int j) {
		data.add(new Pair(i, j));
	}

	public void clear() {
		data.clear();
	}

	public int length() {
		return data.size();
	}

	public Pair popRandom() {
		int o = (int) (data.size() * Math.random());
		Pair p = data.get(o);
		data.remove(o);
		return p;
	}
}
