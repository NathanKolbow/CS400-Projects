
//////////////////////////////////////////////////////////////////////////////////////////
//
//  Assignment: P1, PriorityQueueADT
//  Author: Nathan Kolbow, nkolbow@wisc.edu
//  Due date: 10:00 PM on Monday, February 5th
//  Outside sources/credits: None
//  Known bugs: None
//
//////////////////////////////////////////////////////////////////////////////////////////

import java.util.Arrays;
import java.util.Random;

public class TestMaxPQ {

	/**
	 * Main method that runs all of the PQ's tests and prints their results
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(insertRemoveTestSet() + "\n");
		System.out.println(getMaxTestSet() + "\n");
		System.out.println(sizeTestSet() + "\n");
		System.out.println(isEmptyTestSet() + "\n");
		System.out.println(bigDataTestSet() + "\n");
		System.out.println(dupTestSet());
	}

	/**
	 * Tests the PQ's size() method.
	 * 
	 * @return Returns the results of all of the tests
	 */
	private static String sizeTestSet() {
		MaxPQ<Character> pq = new MaxPQ<Character>();
		int size = pq.size();
		String out1 = (size == 0) ? "size_test1 passed, a size of 0 was correctly returned after initializing a new PQ"
				: "size_test1 failed, a size of " + size
						+ " was returned when 0 was expected after initializing a new PQ";

		pq.insert('a');
		size = pq.size();
		String out2 = (size == 1)
				? "size_test2 passed, a size of 1 was correctly returned when only 1 value was in the PQ"
				: "size_test2 failed, a size of " + size + " was returned when 1 was expected";

		pq.removeMax();
		size = pq.size();
		String out3 = (size == 0) ? "size_test3 passed, a size of 0 was correctly returned after clearing the PQ"
				: "size_test3 failed, a size of " + size + " was returned when 0 was expected";

		Random r = new Random();
		int removeCount = 0;
		for (int i = 0; i < 1000; i++) {
			pq.insert((char) i);
			if (r.nextInt(2) == 0) {
				pq.removeMax();
				removeCount++;
			}
		}
		size = pq.size();
		String out4 = (1000 - removeCount == size)
				? "size_test4 passed, a size of " + (1000 - removeCount) + " was expected and returned"
				: "size_test4 failed, a size of " + (1000 - removeCount) + " was expected but " + size
						+ " was returned";

		return "### size() Test Set ###\n" + out1 + "\n" + out2 + "\n" + out3 + "\n" + out4;
	}

	/**
	 * Tests whether or not the PQ properly handles inserting duplicate values
	 * 
	 * @return Returns the results of all the tests
	 */
	private static String dupTestSet() {
		MaxPQ<Integer> pq = new MaxPQ<Integer>();
		pq.insert(3);
		pq.insert(2);
		for (int i = 0; i < 10; i++) {
			pq.insert(i / 5);
		}

		Integer[] expected = new Integer[] { 3, 2, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0 };
		Integer[] actual = new Integer[12];
		for (int i = 0; i < 12; i++) {
			actual[i] = pq.removeMax();
		}

		String act = Arrays.toString(actual);
		String exp = Arrays.toString(expected);

		String out1 = (Arrays.deepEquals(expected, actual))
				? "dupEntry_test1 passed, all values were and were expected to return in the following order: " + act
				: "dupEntry_test1 failed, return order " + exp + " was expected, but instead " + act + " was received";

		for (int i = 0; i < 1000000; i++) {
			pq.insert(13);
		}

		int temp;
		do {
			temp = pq.removeMax();
		} while (temp == 13 && !pq.isEmpty());

		String out2 = (pq.isEmpty())
				? "dupEntry_test2 passed, 1,000,000 duplicate values were properly inserted and removed"
				: "dupEntry_test2 failed, the value " + temp + " was returned instead of 13 with " + pq.size()
						+ " values remaining in the PQ";

		return "### Duplicate Data Test Set ###\n" + out1 + "\n" + out2;
	}

	/**
	 * Tests whether or not the PQ properly expands itself to include large sets of
	 * data.
	 * 
	 * @return Returns the results of all of the tests
	 */
	private static String bigDataTestSet() {
		MaxPQ<Integer> pq = new MaxPQ<Integer>();
		String out1;
		try {
			for (int i = 0; i < 100; i++) {
				pq.insert(i);
			}
			out1 = "bigData_test1 passed, the PQ successfully expanded to include 100 data points";
		} catch (ArrayIndexOutOfBoundsException e) {
			out1 = "bigData_test1 failed, the PQ did not expand to include 100 data points";
		}

		pq = new MaxPQ<Integer>();
		String out2;
		try {
			for (int i = 0; i < 1000000; i++) {
				pq.insert(i);
			}
			out2 = "bigData_test2 passed, the PQ successfully expanded to include 1,000,000 data points";
		} catch (ArrayIndexOutOfBoundsException e) {
			out2 = "bigData_test2 failed, the PQ did not expand to include 1,000,000 data points";
		}

		return "### Big Data Test Set ###\n" + out1 + "\n" + out2;
	}

	/**
	 * Tests the insert() and remove() methods of the PQ.
	 * 
	 * @return Returns the results of all of the tests
	 */
	private static String insertRemoveTestSet() {
		MaxPQ<Integer> pq = new MaxPQ<Integer>();
		pq.insert(1);
		int out = pq.removeMax();
		String out1 = (out == 1) ? "insertAndRemove_test1 passed, correctly removed and returned 1"
				: "insertAndRemove_test2 failed, removed and returned " + out + " instead of 1";

		String out2;
		try {
			pq.removeMax();
			out2 = "insertAndRemove_test2 failed, did not throw EmptyQueueException when remove was called on an empty PQ";
		} catch (EmptyQueueException e) {
			out2 = "insertAndRemove_test2 passed, threw an EmptyQueueException when remove was called on an empty PQ";
		}

		for (int i = -500; i <= 500; i += 100) {
			pq.insert(i);
		}
		out = pq.removeMax();
		String out3 = (out == 500)
				? "insertAndRemove_test3 passed, correctly removed and returned 500 out of 10 entries"
				: "insertAndRemove_test3 failed, removed and returned " + out + " instead of 500";

		pq = new MaxPQ<Integer>();
		String out4;
		try {
			pq.removeMax();
			out4 = "insertAndRemove_test4 failed, did not throw EmptyQueueException when remove was called on a new PQ";
		} catch (EmptyQueueException e) {
			out4 = "insertAndRemove_test4 passed, threw an EmptyQueueException when remove was called on a new PQ";
		}

		return "### insert() and remove() Test Set ###\n" + out1 + "\n" + out2 + "\n" + out3 + "\n" + out4;
	}

	/**
	 * Tests the getMax() method of the PQ.
	 * 
	 * @return Returns the results of all the tests
	 */
	private static String getMaxTestSet() {
		MaxPQ<Integer> pq = new MaxPQ<Integer>();

		String out1;
		try {
			pq.removeMax();
			out1 = "getMax_test1 failed, didn't throw exception when called on a new PQ";
		} catch (EmptyQueueException e) {
			out1 = "getMax_test1 passed, threw an EmptyQueueException when called on a new PQ";
		}

		for (int i = 0; i <= 10000; i++) {
			pq.insert(i);
		}

		int out = pq.getMax();
		String out2 = (out == 10000) ? "getMax_test2 passed, correctly returned 10000"
				: "getMax_test2 failed, returned " + out + " instead of 10000";

		pq = new MaxPQ<Integer>();
		for (int i = -1000; i <= 0; i++) {
			pq.insert(i);
		}

		out = pq.getMax();
		String out3 = (out == 0) ? "getMax_test3 passed, correctly returned 0"
				: "getMax_test3 failed, returned" + out + " instead of 0";

		return "### getMax() Test Set ###\n" + out1 + "\n" + out2 + "\n" + out3;
	}

	/**
	 * Tests the PQ's isEmpty() method
	 * 
	 * @return Returns the results of all of the tests
	 */
	private static String isEmptyTestSet() {
		MaxPQ<Integer> pq = new MaxPQ<Integer>();
		String out1 = (pq.isEmpty() == true) ? "isEmpty_test1 passed, returned true after initialization"
				: "isEmpty_test1 failed, returned false after initialization";

		for (int i = 0; i < 10000; i++) {
			pq.insert(i);
		}
		for (int i = 0; i < 10000; i++) {
			pq.removeMax();
		}

		String out2 = (pq.isEmpty() == true) ? "isEmpty_test2 passed, return true after removing 10,000/10,000 values"
				: "isEmpty_test2 failed, return false after remove 10,000/10,000 values";

		pq.insert(1);

		String out3 = (pq.isEmpty() == false) ? "isEmpty_test3 passed, returned false when 1 value was in the PQ"
				: "isEmpty_test3 failed, returned true when 1 value was in the PQ";

		pq.insert(10);
		pq.removeMax();
		for (int i = 0; i < 1000; i++) {
			pq.insert(i * 100);
		}

		String out4 = (pq.isEmpty() == false) ? "isEmpty_test4 passed, returned false when 1001 values were in the PQ"
				: "isEmpty_test4 failed, returned true when 1001 values were in the PQ";

		return "### isEmpty() Test Set ###\n" + out1 + "\n" + out2 + "\n" + out3 + "\n" + out4;
	}
}
