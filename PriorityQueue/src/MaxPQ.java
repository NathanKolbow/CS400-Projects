//////////////////////////////////////////////////////////////////////////////////////////
//
//  Assignment: P1, PriorityQueueADT
//  Author: Nathan Kolbow, nkolbow@wisc.edu
//  Due date: 10:00 PM on Monday, February 5th
//  Outside sources/credits: None
//  Known bugs: None
//
//////////////////////////////////////////////////////////////////////////////////////////

public class MaxPQ<E extends Comparable<E>> implements PriorityQueueADT<E> {
	private E[] items; // internal array of all items in the PQ
	// index 1 is used as the PQ's head to make child and parent math simpler
	private static final int INITIAL_SIZE = 10; // initial size of the internal array
	private int count = 0; // current amount of items in the PQ

	/**
	 * Creates a priority queue (PQ) with 10, empty values
	 */
	public MaxPQ() {
		this.items = (E[]) new Comparable[INITIAL_SIZE];
	}

	/**
	 * Returns whether the PQ is empty or not
	 * 
	 * @return true if the PQ is empty, else false
	 */
	@Override
	public boolean isEmpty() {
		return items[1] == null;
	}

	/**
	 * Adds an items to the PQ. If the PQ's internal array isn't large enough to
	 * include another item, the capacity of the internal array is doubled
	 * 
	 * @param item
	 *            - the item to be added to the PQ
	 */

	@Override
	public void insert(E item) {
		if (count + 1 == items.length) {
			E[] temp = (E[]) new Comparable[items.length * 2];
			for (int i = 1; i < items.length; i++) {
				temp[i] = items[i];
			}
			items = temp;
		}

		items[count + 1] = item;
		boolean isDuplicate = addHelper(count + 1);
		count++;

		if (isDuplicate) {
		}

	}

	/**
	 * Gets the current maximum value in the PQ without removing it.
	 * 
	 * @throws EmptyQueueException
	 *             Thrown if the method is called on an empty PQ
	 * @return returns the max value in the PQ
	 */
	@Override
	public E getMax() throws EmptyQueueException {
		if (isEmpty())
			throw new EmptyQueueException();
		return items[1];
	}

	/**
	 * Gets and removes the maximum value in the PQ.
	 * 
	 * @throws EmptyQueueException
	 *             Thrown if the method is called on an empty PQ
	 * @return returns the removed, max value
	 */
	@Override
	public E removeMax() throws EmptyQueueException {
		if (isEmpty())
			throw new EmptyQueueException();

		E temp = items[1];
		items[1] = items[count];
		items[count] = null;
		count--;
		removeHelper(1);

		return temp;
	}

	/**
	 * Sorts data downwards through the PQ. If either of the value's children are
	 * greater than it, the largest child is swapped with its parent and the method
	 * is then recursively called on the original parent's new index.
	 * 
	 * This method also check to ensure that all child indices are valid.
	 * 
	 * @param _index
	 *            The index of the value to be sorted
	 */
	private void removeHelper(int _index) {
		int _rchild = getRightChild(_index);
		int _lchild = getLeftChild(_index);

		if (_rchild >= count) {
			if (_lchild >= count) { // neither child exists
				return;
			}
			// only the left child exists
			if (items[_lchild].compareTo(items[_index]) > 0) {
				swap(_lchild, _index);
				removeHelper(_lchild);
			}
		}
		// both children exist
		int _max = (items[_rchild].compareTo(items[_lchild]) > 0) ? _rchild : _lchild;
		if (items[_max].compareTo(items[_index]) > 0) {
			swap(_max, _index);
			removeHelper(_lchild);
		}
	}

	/**
	 * Gets the total amount of values in the PQ
	 * 
	 * @return Returns the total amount of values in the PQ
	 */
	@Override
	public int size() {
		return count;
	}

	/**
	 * Gets parent index of a given index. No validity checking is done within the
	 * method.
	 * 
	 * @param index
	 *            The index who's parent is returned
	 * @return Returns the index of the provided index's parent
	 */
	private int getParent(int index) {
		return index / 2;
	}

	/**
	 * Gets the right child index of a given index. No validity checking is done
	 * within the method.
	 * 
	 * @param index
	 *            The index who's right child is returned
	 * @return Returns the index of the provided index's right child
	 */
	private int getRightChild(int index) {
		return index * 2 + 1;
	}

	/**
	 * Gets the left child index of a given index. No validity checking is done
	 * within the method.
	 * 
	 * @param index
	 *            The index who's left child is returned
	 * @return Returns the index of the provided index's left child
	 */
	private int getLeftChild(int index) {
		return index * 2;
	}

	/**
	 * Swaps the values at two indeces within the PQ.
	 * 
	 * @param _index1
	 *            The first index to be swapped
	 * @param _index2
	 *            The second index to be swapped
	 */
	private void swap(int _index1, int _index2) {
		E temp = items[_index1];
		items[_index1] = items[_index2];
		items[_index2] = temp;
	}

	/**
	 * Sorts data upwards through the PQ. If the given value is greater than its
	 * parent, it is swapped with its parent and the method is then recursively
	 * called on the original value's new index.
	 * 
	 * This method also check to ensure that all parent indices are valid.
	 * 
	 * @param _index
	 *            The index of the value to be sorted
	 */
	private boolean addHelper(int _index) {

		if (getParent(_index) < 1)
			return false;

		E temp = items[getParent(_index)];
		if (items[_index].compareTo(temp) > 0) {
			swap(_index, getParent(_index));
			return addHelper(getParent(_index));
		}
		if (items[_index].compareTo(temp) < 0) {
			return false;
		}

		return true;
	}

}