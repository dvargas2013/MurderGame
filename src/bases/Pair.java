package bases;

/**
 * Two integers combined into a single object.
 *
 * @author danv
 */
public class Pair {
	public final int i;
	public final int j;

	public Pair(int ii, int jj) {
		i = ii;
		j = jj;
	}

	public ScreenPoint toPoint() {
		return new ScreenPoint(i, j);
	}
}