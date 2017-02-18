package frame;

/**
 * Just a regular Stack. You can pop and peek and all that good stuff.
 *
 * @author danv
 *
 * @param <J>
 *            Any object You want. Enjoy.
 */
class Stack<J> {
	private class Node<E> {
		private final E item;
		private Node<E> next;

		private Node(E e) {
			next = null;
			item = e;
		}

		private Node(E e, Node<E> n) {
			item = e;
			next = n;
		}
	}

	/**
	 * The thing after the dummy Node is the actual first item
	 */
	private final Node<J> dummy = new Node<>(null);

	/**
	 * Amount of items in the stack. Usually just to make sure stack is/isn't
	 * empty.
	 */
	private int size = 0;

	/**
	 * Removes all items from the stack. size becomes 0.
	 */
	public void clear() {
		size = 0;
		dummy.next = null;
	}

	/**
	 * Get how many items are in the stack
	 *
	 * @return amount of items in stack.
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Ask the stack if it is empty or not
	 *
	 * @return true is stack is empty. false otherwise.
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Returns the top item without manipulating the stack.
	 *
	 * @return the top item. null if empty.
	 */
	public J peek() {
		return size == 0 ? dummy.item : dummy.next.item;
	}

	/**
	 * If stack is not empty removes and returns top item in stack.
	 *
	 * @return top item in stack. null if empty.
	 */
	public J pop() {
		if (size == 0)
			return dummy.item;
		J m = dummy.next.item;
		dummy.next = dummy.next.next;
		size--;
		return m;
	}

	/**
	 * Add an item to the top of the stack.
	 *
	 * @param e
	 *            Item to add
	 */
	public void push(J e) {
		dummy.next = new Node<>(e, dummy.next);
		size++;
	}
}