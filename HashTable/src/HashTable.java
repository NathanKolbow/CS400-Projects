
/////////////////////////////////////////////////////////////////////////////
// Semester:         CS400 Spring 2018
// PROJECT:          P3, HashTable Performance Analysis
// FILES:            HashTableADT.java
//					 HashTable.java
// 					 PerformanceAnalysis.java
//					 PerformanceAnalysisHash.java
//					 AnalysisTest.java
//
// Author:			 Nathan Kolbow, nkolbow@wisc.edu
// Due date:		 10:00 PM on Monday, March 19th
// Outside sources:	 None
//
// Instructor:       Deb Deppeler (deppeler@cs.wisc.edu)
// Bugs:             No known bugs
//
//////////////////////////// 80 columns wide //////////////////////////////////

import java.lang.reflect.Array;

public class HashTable<K, V> implements HashTableADT<K, V> {

	// list of primes used for the table size
	private final int[] primeList = new int[] { 11, 23, 47, 97, 197, 397, 797, 1597, 3203, 6421, 12853, 25717, 51437,
			102877, 205759, 411527, 823117, 1646237, 3292489, 6584983, 13169977, 26339969, 52679969, 105359939 };

	// inner array used for the HashTable. The table controls nodes because the
	// HashTable utilizes a linked bucket data structure.
	private HashNode<K, V>[] hashTable;
	// current size of the HashTable; may not be 11 depending on the constructor
	// called.
	private int tableSize = 11;
	// current amount of items in the HashTable
	private int count = 0;
	// maximum load factor of the HashTable
	private final double maxLoadFactor;

	/**
	 * HashTable constructor; creates a HashTable object, sets the initial size of
	 * the hash table, initializes the inner hash table, and sets the hash table's
	 * max load factor.
	 * 
	 * @param initial_size
	 * @param loadFactor
	 */
	public HashTable(int initial_size, double loadFactor) {
		HashNode<K, V> node = new HashNode<K, V>(null, null, null, 0);
		if (loadFactor > 1 && loadFactor < 0)
			throw new IllegalArgumentException("The load factor of the hash table must be between 0 and 1");

		try {
			int n = 0;
			while (primeList[n] < initial_size)
				n++;
			tableSize = primeList[n];
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("The initial size you entered was too large.");
		}

		hashTable = (HashNode<K, V>[]) (Array.newInstance(node.getClass(), initial_size));
		this.maxLoadFactor = loadFactor;
	}

	/**
	 * Helper method used to insert an arbitrary node into an arbitrary hash table.
	 * Using this method allows for easy rehashing of the inner hash table.
	 * 
	 * @param table
	 *            hash table to be added to
	 * @param node
	 *            node to add to the table parameter
	 */
	private void insert(HashNode<K, V>[] table, HashNode<K, V> node) {
		int hash = getIndex(node.key, table.length);
		if (table[hash] == null) {
			table[hash] = node;
		} else {
			HashNode<K, V> curr = table[hash];

			while (curr.next != null)
				curr = curr.next;

			curr.next = node;
		}
	}

	/**
	 * Gets the hash (table index) of a given key for a given table size.
	 * 
	 * @param key
	 *            the key whose hash is being returned
	 * @param TS
	 *            table size of the table the key is being hashed to
	 * @return the key's hash for the given table size
	 */
	private int getIndex(K key, int TS) {
		int hash = key.hashCode();

		while (hash < 0)
			hash *= -1;

		return hash % TS;
	}

	/**
	 * Added a given key and value into the hash table.
	 */
	@Override
	public V put(K key, V value) {
		int hash = getIndex(key, hashTable.length);
		HashNode<K, V> node = new HashNode<K, V>(key, value, null, hash);

		insert(hashTable, node);
		count++;

		if (getLoadFactor() > maxLoadFactor && tableSize != primeList[primeList.length - 1]) {
			resize();
		}

		return value;
	}

	/**
	 * Adjusts the inner hash table to be the next prime in primeList after the load
	 * factor of the hash table surpasses maxLoadFactor. It also rehashes all
	 * elements in the original hash table.
	 */
	private void resize() {
		int n = 0;
		try {
			while (primeList[n] < tableSize * 2)
				n++;
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("The hash table has run out of primes, too much data has been entered.");
		}

		HashNode<K, V> node = new HashNode<K, V>(null, null, null, 0);
		HashNode<K, V>[] temp = (HashNode<K, V>[]) (Array.newInstance(node.getClass(), primeList[n]));

		for (int i = 0; i < hashTable.length; i++) {
			node = hashTable[i];
			while (node != null) {
				insert(temp, new HashNode<K, V>(node.key, node.value, null, node.hash));
				node = node.next;
			}
		}

		hashTable = temp;
		tableSize = primeList[n];
	}

	/**
	 * Gets the current load factor of the hash table.
	 * 
	 * @return the main hash table's load factor
	 */
	private double getLoadFactor() {
		return count / tableSize;
	}

	/**
	 * Clears the hash table.
	 */
	@Override
	public void clear() {
		count = 0;
		tableSize = primeList[0];
		HashNode<K, V> node = new HashNode<K, V>(null, null, null, 0);
		hashTable = (HashNode<K, V>[]) (Array.newInstance(node.getClass(), primeList[0]));
	}

	/**
	 * Returns a key's respective value within the hash table. Returns null if the
	 * key is not in the hash table.
	 */
	@Override
	public V get(K key) {
		int hash = getIndex(key, hashTable.length);
		HashNode<K, V> node = hashTable[hash];
		while (node != null) {
			if (node.key.equals(key))
				return node.value;
			else
				node = node.next;
		}

		return null;
	}

	/**
	 * Returns whether or not the hash table is empty.
	 */
	@Override
	public boolean isEmpty() {
		return count == 0;
	}

	/**
	 * Removes a given key from the hash table and returns the key's value as a way
	 * to verify that the proper key was removed.
	 */
	@Override
	public V remove(K key) {
		int hash = getIndex(key, hashTable.length);
		if (hashTable[hash] == null) {
			return null;
		} else if (hashTable[hash].key.equals(key)) {
			V temp = hashTable[hash].value;
			hashTable[hash] = hashTable[hash].next;
			return temp;
		} else {
			HashNode<K, V> curr = hashTable[hash];
			HashNode<K, V> next = curr.next;
			do {
				if (next.key.equals(key)) {
					curr.next = next.next;
					return next.value;
				} else {
					curr = next;
					next = next.next;
				}
			} while (next != null);
		}
		return null;
	}

	/**
	 * Returns the size of the hash table's inner array.
	 */
	@Override
	public int size() {
		return tableSize;
	}

	/**
	 * Inner node class used to implement the linked buckets within the hash table.
	 * 
	 * @param <Key>
	 *            The node's key
	 * @param <Val>
	 *            The node's value
	 */
	protected class HashNode<Key, Val> {
		protected Key key;
		protected Val value;
		protected HashNode<Key, Val> next;
		protected int hash;

		public HashNode(Key key, Val value, HashNode<Key, Val> next, int hash) {
			this.key = key;
			this.value = value;
			this.next = next;
			this.hash = hash;
		}
	}

}