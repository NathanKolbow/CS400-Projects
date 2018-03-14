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

public class BalancedSearchTree<J extends Comparable<J>> implements SearchTreeADT<J> {

	public Treenode<J> root = null;

	/**
	 * Returns whether the tree is empty or not
	 */
	public boolean isEmpty() {
		return root == null;
	}

	/**
	 * Returns the height of the tree
	 */
	public int height() {
		if (root == null)
			return 0;
		return height(root);
	}

	/**
	 * Private helper method that recursively finds the height of any node.
	 * 
	 * @param root
	 *            The current node whose height is being obtained
	 * @return The height of root
	 */
	private int height(Treenode<J> root) {
		if (root.left == null && root.right == null)
			return 1;
		if (root.left == null)
			return 1 + height(root.right);
		if (root.right == null)
			return 1 + height(root.left);

		int rHeight = height(root.right);
		int lHeight = height(root.left);

		if (rHeight > lHeight)
			return 1 + rHeight;
		return 1 + lHeight;
	}

	/**
	 * Returns whether or not the tree contains "item"
	 */
	public boolean lookup(J item) {
		if (root == null)
			return false;
		return lookupHelper(item, root);
	}

	/**
	 * Private helper method that recursively checks for a given value
	 * 
	 * @param item
	 *            Value being searched for
	 * @param root
	 *            Current node being checked
	 * @return True if the root's key is the value being checked for
	 */
	private boolean lookupHelper(J item, Treenode<J> root) {
		Treenode<J> left = root.left;
		Treenode<J> right = root.right;
		if (item.equals(root.key))
			return true;
		if (item.compareTo(root.key) > 0) {
			if (right == null) {
				return false;
			}
			return lookupHelper(item, right);
		} else {
			if (left == null) {
				return false;
			}
			return lookupHelper(item, left);
		}
	}

	/**
	 * Inserts "item" into the tree, throws an exception if a duplicate key is
	 * inserted
	 */
	public void insert(J item) throws IllegalArgumentException {
		if (item == null)
			throw new IllegalArgumentException("Cannot insert null.");

		Treenode<J> _balance = root;
		if (root == null) {
			root = new Treenode<J>(item, null);
		} else {
			_balance = insertHelper(new Treenode<J>(item, null), root);
		}

		insertBalance(_balance);
	}

	/**
	 * Recursively inserts a new node into the tree.
	 * 
	 * @param node
	 *            the new node to be inserted into the tree
	 * @param root
	 *            the local root where the new node is being compared and sent
	 *            elsewhere from
	 * @return the inserted node
	 */
	private Treenode<J> insertHelper(Treenode<J> node, Treenode<J> root) {
		Treenode<J> left = root.left;
		Treenode<J> right = root.right;

		if (node.key.equals(root.key))
			throw new IllegalArgumentException("You cannot insert duplicate keys.");

		if (node.key.compareTo(root.key) > 0) { // if the inserted node is greater than the root
			if (right == null) {
				root.right = node;
				node.parent = root;
				return node;
			} else {
				return insertHelper(node, right);
			}
		} else { // if the inserted node is less than the root
			if (left == null) {
				root.left = node;
				node.parent = root;
				return node;
			} else {
				return insertHelper(node, left);
			}
		}
	}

	/**
	 * Private helper method that maintains the tree's balance after an insertion
	 * 
	 * @param child
	 *            The local node being balanced
	 */
	private void insertBalance(Treenode<J> child) {
		if (child == null)
			return;

		Treenode<J> parent = child.parent;
		Treenode<J> gParent = (parent == null) ? null : parent.parent;
		Treenode<J> sibling = (gParent == null) ? null : getSibling(parent);

		Treenode<J> nextBalance = parent;

		if (parent == null) { // the root is the only node in the tree

		} else if (gParent == null) { // only the root and 1 or 2 children are in the tree
			// the only violation in this case would be a root property violation, which is
			// fixed later in the method

		} else {
			if (parent.color == Color.RED && child.color == Color.RED) { // red-property violation, fix needed
				if (sibling == null || sibling.color == Color.BLACK) { // p's sibling is null
																		// (black), TNR required
					if (parent == gParent.left) {
						if (child == parent.left) {
							parent.color = Color.BLACK;
							gParent.color = Color.RED;
							if (sibling != null)
								sibling.color = Color.BLACK;

							Treenode<J> rParent = parent.right;

							if (root == gParent) {
								root = parent;
								root.parent = null;
							} else {
								parent.parent = gParent.parent;
								if (parent.parent.right == gParent)
									parent.parent.right = parent;
								else
									parent.parent.left = parent;
							}

							gParent.left = rParent;
							if (parent.right != null)
								parent.right.parent = gParent;
							parent.right = gParent;
							gParent.parent = parent;

							nextBalance = child;
						} else {
							gParent.color = Color.RED;
							child.color = Color.BLACK;

							Treenode<J> cRight = child.right;
							Treenode<J> cLeft = child.left;

							if (root == gParent) {
								root = child;
								root.parent = null;
							} else {
								child.parent = gParent.parent;
								if (child.parent.right == gParent)
									child.parent.right = child;
								else
									child.parent.left = child;
							}

							child.right = gParent;
							gParent.parent = child;
							parent.right = cLeft;
							if (cLeft != null)
								cLeft.parent = parent;
							gParent.left = cRight;
							if (cRight != null)
								cRight.parent = parent;
							child.left = parent;
							parent.parent = child;

							nextBalance = parent;
						}
					} else { // parent node is the grandparent's right child
						if (child == parent.right) {
							parent.color = Color.BLACK;
							gParent.color = Color.RED;

							Treenode<J> pLeft = parent.left;
							parent.left = gParent;

							if (root == gParent) {
								root = parent;
								root.parent = null;
							} else {
								parent.parent = gParent.parent;
								if (parent.parent.right == gParent)
									parent.parent.right = parent;
								else
									parent.parent.left = parent;
							}

							gParent.parent = parent;
							gParent.right = pLeft;
							if (pLeft != null)
								pLeft.parent = gParent;

							nextBalance = child;
						} else {
							child.color = Color.BLACK;
							gParent.color = Color.RED;

							if (root == gParent) {
								root = child;
								root.parent = null;
							} else {
								child.parent = gParent.parent;
								if (child.parent.right == gParent)
									child.parent.right = child;
								else
									child.parent.left = child;
							}

							parent.left = child.right;
							if (child.right != null)
								child.right.parent = parent;
							gParent.right = child.left;
							if (child.left != null)
								child.left.parent = gParent;
							child.right = parent;
							parent.parent = child;
							child.left = gParent;
							gParent.parent = child;

							nextBalance = parent;
						}
					}
				} else if (sibling.color == Color.RED) { // re-coloring required
					sibling.color = Color.BLACK;
					parent.color = Color.BLACK;
					gParent.color = Color.RED;
				}
			}
		}

		if (root.color == Color.RED) // maintaining the root property
			root.color = Color.BLACK;

		insertBalance(nextBalance);
	}

	/**
	 * Removes a given key from the tree
	 */
	public void delete(J item) {
		if (root == null)
			return;

		Treenode<J> right = root.right;
		Treenode<J> left = root.left;

		if (item.equals(root.key)) {
			if (right == null) {
				root = root.left;
				if (root != null)
					root.parent = null;
			} else if (left == null) {
				root = root.right;
				root.parent = null;
			} else {
				Treenode<J> leftMost = leftMost(right);
				root.key = leftMost.key;
				if (leftMost.right != null) {
					leftMost.parent.left = leftMost.right;
					leftMost.right.color = (leftMost.right.color == Color.RED) ? Color.BLACK : Color.BLACK_BLACK;
					leftMost.right.parent = leftMost.parent;
					deleteBalance(leftMost.right);
				} else {
					leftMost.key = null;
					leftMost.color = Color.BLACK_BLACK;
					deleteBalance(leftMost);
				}
			}
		} else {
			Treenode<J> toBalance = deleteHelper(item, root);
			if (toBalance != null)
				deleteBalance(toBalance);
		}
	}

	/**
	 * Private helper method that recursively finds and removes a given node
	 * 
	 * @param item
	 *            Key being removed
	 * @param root
	 *            Local node being compared to and either deleted or used to find
	 *            the next node visited
	 */
	private Treenode<J> deleteHelper(J item, Treenode<J> root) {
		if (root == null)
			return null;

		// the root node will always be replaced by the leftmost node of its right child
		if (item.equals(root.key)) {
			Treenode<J> left = root.left;
			Treenode<J> right = root.right;
			Treenode<J> parent = root.parent;
			if (left == null && right == null) { // if the node is a leaf
				root.key = null;
				root.color = Color.BLACK_BLACK;
				return root;
			} else if (right == null && left != null || right != null && left == null) {
				if (root == parent.left) {
					if (root.left == null) {
						parent.left = right;
						right.parent = parent;
						right.color = (right.color == Color.RED) ? Color.BLACK : Color.BLACK_BLACK;
						return right;
					} else {
						parent.left = left;
						left.parent = parent;
						left.color = (left.color == Color.RED) ? Color.BLACK : Color.BLACK_BLACK;
						return left;
					}
				} else {
					if (root.left == null) {
						parent.right = right;
						right.parent = parent;
						right.color = (right.color == Color.RED) ? Color.BLACK : Color.BLACK_BLACK;
						return right;
					} else {
						parent.right = left;
						left.parent = parent;
						left.color = (left.color == Color.RED) ? Color.BLACK : Color.BLACK_BLACK;
						return left;
					}
				}
			} else {
				Treenode<J> leftMost = leftMost(root.right);
				root.key = leftMost.key;
				if (leftMost.right != null) {
					if (leftMost.parent.right == leftMost) {
						leftMost.parent.right = leftMost.right;
						leftMost.right.parent = leftMost.parent;
					} else {
						leftMost.parent.left = leftMost.right;
						leftMost.right.parent = leftMost.parent;
					}
					leftMost.right.color = (leftMost.right.color == Color.RED) ? Color.RED : Color.BLACK_BLACK;
					return leftMost.right;
				} else {
					leftMost.key = null;
					leftMost.color = Color.BLACK_BLACK;
					return leftMost;
				}
			}
		} else if (item.compareTo(root.key) > 0) { // item is to the right of root
			return deleteHelper(item, root.right);
		} else { // item is to the left of root
			return deleteHelper(item, root.left);
		}
	}

	/**
	 * Ensures that the tree is height-balanced after the deletion of a node.
	 * 
	 * @param node
	 *            The node being checked for a double-black color, and the node that
	 *            is fixed around.
	 */
	private void deleteBalance(Treenode<J> node) {
		if (node == root && node.color == Color.BLACK_BLACK) {
			node.color = Color.BLACK;
			return;
		}
		if (node.color != Color.BLACK_BLACK)
			return;

		Treenode<J> sibling = getSibling(node);
		Treenode<J> left = sibling.left;
		Treenode<J> right = sibling.right;
		Treenode<J> parent = node.parent;
		Treenode<J> toBalance = null;
		if (sibling != null) {
			if (sibling.color == Color.RED) {
				if (node == parent.left) {
					parent.color = Color.RED;
					sibling.color = Color.BLACK;
					parent.right = left;
					if (left != null)
						left.parent = parent;
					sibling.left = parent;
					sibling.parent = parent.parent;
					if (parent != root) {
						if (parent == parent.parent.left) {
							parent.parent.left = sibling;
						} else {
							parent.parent.right = sibling;
						}
					}
					parent.parent = sibling;
					if (root == parent) {
						root = sibling;
					}
					deleteBalance(node);
				} else {
					parent.color = Color.RED;
					sibling.color = Color.BLACK;
					parent.left = right;
					if (right != null)
						right.parent = parent;
					sibling.right = parent;
					sibling.parent = parent.parent;
					if (parent != root) {
						if (parent == parent.parent.left) {
							parent.parent.left = sibling;
						} else {
							parent.parent.right = sibling;
						}
					}
					parent.parent = sibling;
					if (root == parent)
						root = sibling;
					deleteBalance(node);
				}
			} else {
				if (right != null && left != null) {
					if (node == parent.right) {
						if (left.color == Color.BLACK && right.color == Color.RED) {
							sibling.color = Color.RED;
							right.color = Color.BLACK;
							parent.left = right;
							right.parent = parent;
							sibling.right = right.left;
							if (sibling.right != null)
								sibling.right.parent = sibling;
							right.left = sibling;
							sibling.parent = right;
							toBalance = node;
						} else if (right.color == Color.BLACK && left.color == Color.BLACK) {
							sibling.color = Color.RED;
							parent.color = (parent.color == Color.RED) ? Color.BLACK : Color.BLACK_BLACK;
							node.color = Color.BLACK;
							toBalance = parent;
						} else if (left.color == Color.RED) {
							left.color = Color.BLACK;
							node.color = Color.BLACK;
							parent.color = Color.BLACK;
							if (parent.parent != null) {
								if (parent.parent.right == parent) {
									parent.parent.right = sibling;
									sibling.parent = parent.parent;
								} else {
									parent.parent.left = sibling;
									sibling.parent = parent.parent;
								}
							}
							parent.left = right;
							right.parent = parent;
							sibling.right = parent;
							parent.parent = sibling;
							if (root == parent)
								root = sibling;
						}
					} else {
						if (right.color == Color.BLACK && left.color == Color.RED) {
							sibling.color = Color.RED;
							left.color = Color.BLACK;
							parent.right = left;
							left.parent = parent;
							sibling.left = left.right;
							if (sibling.left != null)
								sibling.left.parent = sibling;
							left.right = sibling;
							sibling.parent = left;
							toBalance = node;
						} else if (right.color == Color.BLACK && left.color == Color.BLACK) {
							sibling.color = Color.RED;
							parent.color = (parent.color == Color.RED) ? Color.BLACK : Color.BLACK_BLACK;
							node.color = Color.BLACK;
							toBalance = parent;
						} else if (right.color == Color.RED) {
							node.color = Color.BLACK;
							right.color = Color.BLACK;
							parent.color = Color.BLACK;
							if (parent.parent != null) {
								if (parent.parent.right == parent) {
									parent.parent.right = sibling;
									sibling.parent = parent.parent;
								} else {
									parent.parent.left = sibling;
									sibling.parent = parent.parent;
								}
							}
							parent.right = left;
							left.parent = parent;
							sibling.left = parent;
							parent.parent = sibling;
							if (root == parent)
								root = sibling;
						}
					}
				} else {
					if (left == null && right == null) {
						sibling.color = Color.RED;
						parent.color = (parent.color == Color.RED) ? Color.BLACK : Color.BLACK_BLACK;
						if (parent.right == node) {
							parent.right = null;
						} else {
							parent.left = null;
						}
						toBalance = parent;
					}
				}
			}
		}

		if (node.key == null) {
			if (node.parent.left == node) {
				node.parent.left = null;
			} else if (node.parent.right == node) {
				node.parent.right = null;
			}
		}

		if (toBalance != null)
			deleteBalance(toBalance);
	}

	// HINT: define this helper method that can find the smallest key
	// in a sub-tree with "node" as its root
	// PRE-CONDITION: node is not null
	/**
	 * Finds the leftmost value of a given node recursively. Pre-condition: the
	 * local node mustn't be null.
	 * 
	 * @param root
	 *            The local node whose child is being checked
	 * @return The leftmost node in the given root
	 */
	private Treenode<J> leftMost(Treenode<J> root) {
		if (root.left == null)
			return root;
		return leftMost(root.left);
	}

	/**
	 * Returns the tree's contents in ascending order
	 */
	public String inAscendingOrder() {
		if (root == null)
			return "";
		String out = inOrderTraversal(root);
		return out;
	}

	/**
	 * Private helper method that recursively obtains all of the tree's values in
	 * ascending order.
	 * 
	 * @param root
	 *            The local root being checked
	 * @return The local root's left child's key, the local root's key, and the
	 *         local root's right child's key
	 */
	private String inOrderTraversal(Treenode<J> root) {
		if (root == null)
			return "";
		String left = inOrderTraversal(root.left);
		String middle = root.key + ",";
		String right = inOrderTraversal(root.right);
		return left + middle + right;
	}

	/**
	 * Returns the sibling of a given node. Returns null if a sibling doesn't exist.
	 * 
	 * @param node
	 *            The node whose sibling is being obtained
	 * @return The sibling of "node"
	 */
	private Treenode<J> getSibling(Treenode<J> node) {
		Treenode<J> parent = node.parent;
		return (parent.left == node) ? parent.right : parent.left;
	}

	// inner node class used to store key items and links to other nodes
	protected class Treenode<K extends Comparable<K>> {
		public Treenode(K item, Treenode<K> parent) {
			this(item, null, null, parent, Color.RED);
		}

		public Treenode(K item, Treenode<K> left, Treenode<K> right, Treenode<K> parent, Color color) {
			key = item;
			this.left = left;
			this.right = right;
			this.parent = parent;
			this.color = color;
		}

		K key;
		Treenode<K> left;
		Treenode<K> right;
		Treenode<K> parent;
		Color color;
	}

	// inner Enumerator used for coloring nodes
	protected enum Color {
		RED, BLACK, BLACK_BLACK
	}
}
