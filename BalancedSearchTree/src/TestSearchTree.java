import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/////////////////////////////////////////////////////////////////////////////
// Semester:         CS400 Spring 2018
// PROJECT:          P2, Balanced Search Tree
// FILES:            TestSearchTree.java
//                   SearchTreeADT.java
//                   BalancedSearchTree.java
//
// Author:			 Nathan Kolbow, nkolbow@wisc.edu
// Due date:		 10:00 PM on Monday, February 26th
// Outside sources:	 None
//
// Instructor:       Deb Deppeler (deppeler@cs.wisc.edu)
// Bugs:             No known bugs
//
//////////////////////////// 80 columns wide //////////////////////////////////

public class TestSearchTree {

	SearchTreeADT<Integer> tree = null;
	String expected = null;
	String actual = null;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		tree = new BalancedSearchTree<Integer>();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {

	}

	@Test
	/** tests if the tree is empty immediately after its instantiation */
	public void test01_isEmpty_on_empty_tree() {
		expected = "true";
		actual = "" + tree.isEmpty();
		if (!expected.equals(actual))
			fail("expected: " + expected + " actual: " + actual);
	}

	@Test
	/**
	 * tests if the tree's inAscendingOrder method returns a blank string
	 * immediately after its instantiation
	 */
	public void test02_ascending_order_on_empty_tree() {
		expected = "";
		actual = tree.inAscendingOrder();
		if (!expected.equals(actual))
			fail("expected: " + expected + " actual: " + actual);
	}

	@Test
	/** tests that the height of an empty tree is 0 */
	public void test03_height_on_empty_tree() {
		expected = "0";
		actual = "" + tree.height();
		if (!expected.equals(actual))
			fail("expected: " + expected + " actual: " + actual);
	}

	@Test
	/** tests that the tree isn't empty after a single insert */
	public void test04_isEmpty_after_one_insert() {
		tree.insert(1);
		expected = "false";
		actual = "" + tree.isEmpty();
		if (!expected.equals(actual))
			fail("expected: " + expected + " actual: " + actual);
	}

	@Test
	/** tests that the ascending order after inserting A item is "A," */
	public void test05_ascending_order_after_one_insert() {
		tree.insert(1);
		expected = "1,";
		actual = tree.inAscendingOrder();
		if (!expected.equals(actual))
			fail("expected: " + expected + " actual: " + actual);
	}

	@Test
	/** tests that the height after inserting A is 1 */
	public void test06_height_after_one_insert() {
		tree.insert(1);
		expected = "1";
		actual = "" + tree.height();
		if (!expected.equals(actual))
			fail("expected: " + expected + " actual: " + actual);
	}

	@Test
	/** tests that the height after inserting A and deleting it is 0 */
	public void test07_height_after_one_insert_and_delete() {
		tree.insert(1);
		tree.delete(1);
		expected = "0";
		actual = "" + tree.height();
		if (!expected.equals(actual))
			fail("expected: " + expected + " actual: " + actual);
	}

	@Test
	/** tests that the tree's height is correct after 50 inserts */
	public void test08_height_after_50_inserts() {
		expected = "9"; // 9 is not log2(50), but the tree is a red black tree so this is the correct
						// value for an in-order insertion
		for (int i = 0; i <= 50; i++) {
			tree.insert(i);
		}
		actual = "" + tree.height();
		if (!expected.equals(actual))
			fail("expected: " + expected + " actual: " + actual);
	}

	@Test
	/**
	 * tests if the tree throws an IllegalArgumentException when duplicate keys are
	 * inserted
	 */
	public void test09_duplicate_insert_throws_IllegalArgumentException() {
		tree.insert(42);
		try {
			tree.insert(42);
			fail("An IllegalArgumentException was not thrown when a duplicate key was inserted.");
		} catch (IllegalArgumentException e) {

		} catch (Exception e) {
			fail("An IllegalArgumentException was not thrown when a duplicate key was inserted.");
		}
	}

	@Test
	/** tests lookup after a single value is inserted */
	public void test10_lookup_finds_value_after_single_insert() {
		tree.insert(42);
		expected = "true";
		actual = "" + tree.lookup(42);
		if (!expected.equals(actual))
			fail("expected: " + expected + " actual: " + actual);
	}

	@Test
	/** tests lookup after many values are inserted */
	public void test11_lookup_finds_all_values_after_many_insertions() {
		for (int i = 0; i < 23; i++) {
			tree.insert(i);
		}
		expected = "true";
		ArrayList<String> notContained = new ArrayList<String>();
		actual = "true";
		for (int i = 0; i < 23; i++) {
			if (!tree.lookup(i)) {
				notContained.add("" + i);
				actual = "false";
			}
		}
		if (!expected.equals(actual))
			fail("After inserting 0 through 49, the tree did not have: " + notContained.toString());
	}

	@Test
	/**
	 * tests the tree's height after many insertions and deletes (the tree is empty
	 * when the height method is run)
	 */
	public void test12_height_after_many_inserts_and_deletes() {
		expected = "0";
		for (int i = 0; i < 50; i++) {
			tree.insert(i);
		}
		for (int i = 0; i < 50; i++) {
			tree.delete(i);
		}
		actual = "" + tree.height();
		if (!expected.equals(actual))
			fail("expected: " + expected + " actual: " + actual);
	}

	@Test
	/** tests inAscendingOrder after many inserts */
	public void test13_in_ascending_order_after_many_inserts() {
		expected = "0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,";
		for (int i = 0; i < 25; i++) {
			tree.insert(i);
		}
		actual = tree.inAscendingOrder();
		if (!expected.equals(actual))
			fail("expected: " + expected + " actual: " + actual);
	}

	@Test
	/** tests inAscendingOrder after many inserts and several deletes */
	public void test14_in_ascending_order_after_many_inserts_and_deletes() {
		expected = "0,1,2,3,4,5,20,21,22,23,24,";
		for (int i = 0; i < 25; i++) {
			tree.insert(i);
		}
		for (int i = 6; i < 20; i++) {
			tree.delete(i);
		}
		actual = tree.inAscendingOrder();
		if (!expected.equals(actual))
			fail("expected: " + expected + " actual: " + actual);
	}

	@Test
	/**
	 * tests if an IllegalArgumentException is thrown when delete is called on an
	 * empty tree
	 */
	public void test15_delete_on_empty_tree_doesnt_throw_exception() {
		try {
			tree.delete(1);
		} catch (Exception e) {
			fail("Deleting on an empty tree threw an exception.");
		}
	}

	@Test
	/**
	 * tests isEmpty after many inserts and deletes (the tree is empty when isEmpty
	 * is run)
	 */
	public void test16_isEmpty_after_many_inserts_and_deletes() {
		expected = "true";
		for (int i = 0; i < 100; i++) {
			tree.insert(i);
		}
		for (int i = 0; i < 100; i++) {
			tree.delete(i);
		}
		actual = "" + tree.isEmpty();
		if (!expected.equals(actual))
			fail("expected: " + expected + " actual: " + actual);
	}

	@Test
	/** tests lookup after one insert and delete */
	public void test17_lookup_after_one_insert_and_delete() {
		expected = "false";
		tree.insert(1);
		tree.delete(1);
		actual = "" + tree.lookup(1);
		if (!expected.equals(actual))
			fail("expected: " + expected + " actual: " + actual);
	}

	@Test
	/**
	 * tests lookup after many inserts and deletes (the tree is empty when all
	 * lookups are run)
	 */
	public void test18_lookup_many_values_after_many_inserts_and_deletes() {
		expected = "false";
		actual = "false";
		for (int i = 0; i < 100; i++) {
			tree.insert(i);
		}
		for (int i = 0; i < 100; i++) {
			tree.delete(i);
		}
		for (int i = 0; i < 100; i++) {
			if (tree.lookup(i))
				actual = "true";
		}
		if (!expected.equals(actual))
			fail("expected: " + expected + " actual: " + actual);
	}

	@Test
	/** tests that delete on a nonexistent key doesn't throw any exceptions */
	public void test19_delete_nonexistent_key_doesnt_throw_exception() {
		tree.insert(1);
		tree.insert(2);
		tree.insert(3);
		tree.insert(4);
		tree.insert(5);
		try {
			tree.delete(6);
		} catch (Exception e) {
			fail("Tree threw an exception when a nonexistent key was deleted");
		}
	}

	@Test
	/**
	 * adds 100 items to a list, afterwards it deletes them one at a time making
	 * sure the list includes all of the values it should
	 */
	public void test20_many_inserts_and_many_individual_delete_lookup_tests() {
		ArrayList<Integer> actualList = new ArrayList<Integer>();
		int hasCount;
		for (int i = 0; i < 100; i++)
			tree.insert(i);
		for (int i = 0; i < 100; i++) {
			tree.delete(i);
			hasCount = 0;
			for (int j = 0; j < 100; j++) {
				if (tree.lookup(j))
					hasCount++;
			}
			actualList.add(hasCount);
		}
		ArrayList<Integer> expectedList = new ArrayList<Integer>();
		for (int i = 99; i >= 0; i--) {
			expectedList.add(i);
		}
		actual = actualList.toString();
		expected = expectedList.toString();
		if (!expected.equals(actual)) {
			fail("After many deletes, the tree did not contain all of the values it was supposed to. Expected sequential array of positive lookup counts: "
					+ expected + " actual: " + actual);
		}
	}

	@Test
	/**
	 * tests that balance is maintained throughout the tree as items are added and
	 * deleted
	 */
	public void test21_test_balance_after_many_inserts_and_deletes() {
		// expected height is less than or equal to 2*log(2n), as put in the rubric
		for (int i = 0; i < 100; i++) {
			tree.insert(i);
		}
		expected = "15";
		actual = "" + tree.height();
		if (Integer.parseInt(expected) < Integer.parseInt(actual)) {
			fail("Height-balance was not maintained within the tree. After 100 inserts, expected max-height was: "
					+ expected + " but actual height was: " + actual);
		}
		for (int i = 20; i < 80; i++) {
			tree.delete(i);
		}
		expected = "13";
		if (Integer.parseInt(expected) < Integer.parseInt(actual)) {
			fail("Height-balance was not maintained within the tree. After 100 inserts and 60 deletes, expected max-height was: "
					+ expected + " but actual height was: " + actual);
		}
	}

}