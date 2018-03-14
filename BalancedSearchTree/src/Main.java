
public class Main {
	
	public static void main(String[] args) {
		BalancedSearchTree<Integer> bst = new BalancedSearchTree<Integer>();
		for (int i = 0; i < 25; i++) {
			bst.insert(i);
		}
		for (int i = 6; i < 20; i++) {
			System.out.println("^ Current level order, deleting " + i);
			bst.delete(i);
		}
		System.out.println(bst.inAscendingOrder());
	}

}
