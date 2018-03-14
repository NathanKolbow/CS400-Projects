import java.lang.reflect.Array;

public class DirectedGraph<K> {
	
	private GraphNode<K> cast = new GraphNode<K>(null, -1);
	// vertex list
	private GraphNode<K>[] vertices;
	// adjacency matrix
	private boolean[][] am;
	private int INITIAL_SIZE = 10;
	
	@SuppressWarnings("unchecked")
	public DirectedGraph() {
		am = new boolean[INITIAL_SIZE][INITIAL_SIZE];
		vertices = (GraphNode<K>[]) Array.newInstance(cast.getClass(), INITIAL_SIZE);
	}
	
	private void expandAM(int newSize) {
		boolean[][] temp = new boolean[newSize][newSize];
		for(int y = 0; y < am.length; y++) {
			for(int x = 0; x < am.length; x++) {
				temp[y][x] = am[y][x];
			}
		}
		am = temp;
		@SuppressWarnings("unchecked")
		GraphNode<K>[] vTemp = (GraphNode<K>[]) Array.newInstance(cast.getClass(), newSize);
		for(int i = 0; i < vertices.length; i++) {
			vTemp[i] = vertices[i];
		}
		vertices = vTemp;
	}
	
	protected class GraphNode<Key> {
		Key key;
		int index;
		
		public GraphNode(Key key, int index) {
			this.key = key;
			this.index = index;
		}
	}
}
