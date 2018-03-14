
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

import java.io.*;
import java.util.ArrayList;
import java.util.TreeMap;

public class PerformanceAnalysisHash implements PerformanceAnalysis {

	// The input data from each file is stored in this/ per file
	private ArrayList<String> inputData;
	// List of operations that are output on the final report
	private ArrayList<Operation> operationList;
	private String fileName;
	// HashTable used for comparison
	private HashTable<String, String> table;
	// TreeMap used for comparison
	private TreeMap<String, String> tm;
	// initial size of the HashTable, defaults to 11 but is changed based on the
	// size of inputData
	private int INITIAL_SIZE = 11;
	// default max load factor of the HashTable. I set my default load factor to 0.9
	// because I use primes for my tables sizes which makes my hash function work
	// very well.
	private final double LOAD_FACTOR = 0.9;

	/**
	 * PerformanceAnalysisHash constructor, creates a PerformanceAnalysisHash
	 * object, initializes the operation list and loads the file data to be used for
	 * comparisons.
	 * 
	 * @param details_filename
	 *            first file to be loaded
	 * @throws IOException
	 *             thrown when details_filename does not exist
	 */
	public PerformanceAnalysisHash(String details_filename) throws IOException {
		operationList = new ArrayList<Operation>();
		loadData(details_filename);
	}

	/**
	 * Compares the put, get and remove operations of my HashTable and Java's
	 * TreeMap.
	 */
	@Override
	public void compareDataStructures() {
		operationList = new ArrayList<Operation>();
		compareInsertion();
		compareSearch();
		compareDeletion();
	}

	/**
	 * Prints the statistics for all of the operations performed by each data
	 * structure.
	 */
	@Override
	public void printReport() {
		System.out.println(
				"------------------------------------------------------------------------------------------------");
		System.out.printf("|%20s|%15s|%15s|%25s|%15s|", "FileName", "Operation", "Data Structure",
				"Time Taken (nano seconds)", "Bytes Used");
		System.out.println(
				"\n------------------------------------------------------------------------------------------------");

		for (Operation op : operationList) {
			System.out.printf("|%20s|%15s|%15s|%25s|%15s|", op.fileName, op.operation, op.dataStruct, op.timeTaken,
					op.bytesUsed);
			System.out.println();
		}

		System.out.println(
				"------------------------------------------------------------------------------------------------");
	}

	/**
	 * Compares the insertion time and memory used of the HashTable against that of
	 * Java's TreeMap.
	 */
	@Override
	public void compareInsertion() {
		long startMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		long startTime = System.nanoTime();

		table = new HashTable<String, String>(INITIAL_SIZE, LOAD_FACTOR);
		for (String str : inputData) {
			table.put(str, str);
		}

		long endMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		long endTime = System.nanoTime();

		long hashTotalTime = endTime - startTime;
		long hashMemUsed = endMem - startMem;

		startMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		startTime = System.nanoTime();

		tm = new TreeMap<String, String>();
		for (String str : inputData) {
			tm.put(str, str);
		}

		endMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		endTime = System.nanoTime();

		long treeTotalTime = endTime - startTime;
		long treeMemUsed = endMem - startMem;

		operationList.add(new Operation(fileName, "PUT", "HASHMAP", hashTotalTime, hashMemUsed));
		operationList.add(new Operation(fileName, "PUT", "TREEMAP", treeTotalTime, treeMemUsed));

		table = null;
		tm = null;
		System.gc();
	}

	/**
	 * Compares the delete functions of my HashTable and Java's TreeMap against each
	 * other.
	 */
	@Override
	public void compareDeletion() {
		table = new HashTable<String, String>(INITIAL_SIZE, LOAD_FACTOR);
		for (String str : inputData) {
			table.put(str, str);
		}

		long startMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		long startTime = System.nanoTime();

		for (String str : inputData) {
			table.remove(str);
		}

		long endMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		long endTime = System.nanoTime();

		long hashTotalTime = endTime - startTime;
		long hashMemUsed = endMem - startMem;

		tm = new TreeMap<String, String>();
		for (String str : inputData) {
			tm.put(str, str);
		}

		startMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		startTime = System.nanoTime();

		for (String str : inputData) {
			tm.remove(str);
		}

		endMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		endTime = System.nanoTime();

		long treeTotalTime = endTime - startTime;
		long treeMemUsed = endMem - startMem;

		operationList.add(new Operation(fileName, "REMOVE", "HASHMAP", hashTotalTime, hashMemUsed));
		operationList.add(new Operation(fileName, "REMOVE", "TREEMAP", treeTotalTime, treeMemUsed));

		table = null;
		tm = null;
		System.gc();
	}

	/**
	 * Compares the search methods of my HashTable and Java's TreeMap against each
	 * other.
	 */
	@Override
	public void compareSearch() {
		table = new HashTable<String, String>(INITIAL_SIZE, LOAD_FACTOR);
		for (String str : inputData) {
			table.put(str, str);
		}

		long startMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		long startTime = System.nanoTime();

		for (String str : inputData) {
			table.get(str);
		}

		long endMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		long endTime = System.nanoTime();

		long hashTotalTime = endTime - startTime;
		long hashMemUsed = endMem - startMem;

		tm = new TreeMap<String, String>();
		for (String str : inputData) {
			tm.put(str, str);
		}

		startMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		startTime = System.nanoTime();

		for (String str : inputData) {
			tm.get(str);
		}

		endMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		endTime = System.nanoTime();

		long treeTotalTime = endTime - startTime;
		long treeMemUsed = endMem - startMem;

		operationList.add(new Operation(fileName, "GET", "HASHMAP", hashTotalTime, hashMemUsed));
		operationList.add(new Operation(fileName, "GET", "TREEMAP", treeTotalTime, treeMemUsed));

		table = null;
		tm = null;
		System.gc();
	}

	/*
	 * An implementation of loading files into local data structure is provided to
	 * you Please feel free to make any changes if required as per your
	 * implementation. However, this function can be used as is.
	 */
	@Override
	public void loadData(String filename) throws IOException {

		// Opens the given test file and stores the objects each line as a string
		File file = new File(filename);
		BufferedReader br = new BufferedReader(new FileReader(file));
		inputData = new ArrayList<>();
		String line = br.readLine();
		while (line != null) {
			inputData.add(line);
			line = br.readLine();
		}
		br.close();

		fileName = filename;
		INITIAL_SIZE = inputData.size();
	}

	/**
	 * An inner class used to more easily keep track of all operations performed
	 * thus far. This class also makes it easier to later print out the final
	 * report.
	 */
	protected class Operation {
		protected final String fileName;
		protected final String operation;
		protected final String dataStruct;
		protected final long timeTaken;
		protected final long bytesUsed;

		public Operation(String fileName, String operation, String struct, long timeTaken, long bytesUsed) {
			this.fileName = fileName;
			this.operation = operation;
			this.dataStruct = struct;
			this.timeTaken = timeTaken;
			this.bytesUsed = bytesUsed;
		}
	}
}