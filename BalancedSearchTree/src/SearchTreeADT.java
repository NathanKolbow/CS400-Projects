public interface SearchTreeADT<T extends Comparable<T>> {

	/**
	 * returns a string with the values in ascending order
	 * The form of the string returned is a comma separated list
	 * without additional spaces between keys ending with a comma.
	 * For example if the keys in the tree are A,B, and C
	 * then the String returned would be:
	 * 
	 *  <pre>
	 *  A,B,C,
	 *  </pre>
	 *  
	 *  If no keys have been inserted, return an empty string "" without any keys
	 *  
	 * @return comma-separated list of keys for this search tree in ascending order
	 */
	public String inAscendingOrder();

	// returns true if there are no keys in this structure
	public boolean isEmpty() ;	

	/**
	 * Computes and returns the height of this tree.
	 * The height of a tree is the number of nodes 
	 * along the path from root to lowest leaf node.
	 * 
	 * For example, the height of a tree with no keys is zero
	 * and the height of a tree with one key is 1.
	 *
	 * @return the height of this tree
	 */
	public int height() ;
	
	// if key is in this BST, return true; otherwise, return false
	public boolean lookup(T item) ;
	
	// add the item (key) to this search tree
	// throw IllegalArgumentException if item is null
	public void insert(T item);
	
	// remove the item (key) to this search tree
	// if the item is not found, return without error or side-effect
	public void delete(T item) ;

}