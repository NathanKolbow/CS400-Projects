import java.lang.reflect.Array;
import java.util.AbstractMap.SimpleEntry;
import java.util.Comparator;
import java.util.PriorityQueue;

public class DirectedWeightedGraph<K extends Comparable<K>> {

	// object used to create instances of generic arrays
	private GraphNode<K> cast = new GraphNode<K>(null, -1);
	// vertex list
	private GraphNode<K>[] vertices;
	private int count = 0;
	// adjacency matrix; am[source][destination]
	private int[][] am;

	public DirectedWeightedGraph() {
		this(10);
	}

	@SuppressWarnings("unchecked")
	public DirectedWeightedGraph(int initialSize) {
		am = new int[initialSize][initialSize];
		vertices = (GraphNode<K>[]) Array.newInstance(cast.getClass(), initialSize);
	}

	public boolean addVertex(K key) {
		if (count == vertices.length)
			expandAM(vertices.length * 2);

		int i = 0;
		while (vertices[i] != null) {
			if (vertices[i].key.equals(key))
				return false;
			i++;
		}

		GraphNode<K> node = new GraphNode<K>(key, i);
		vertices[i] = node;
		populateAM(i);
		count++;

		return true;
	}

	public boolean addEdge(K from, K to, int weight) {
		return addEdge(from, to, weight, false);
	}

	public boolean addEdge(K from, K to, int weight, boolean twoWay) {
		if (from.equals(to))
			throw new IllegalArgumentException("Simple cycles are not allowed.");

		int _from = -1;
		int _to = -1;
		int i = 0;
		while (i < vertices.length && (_from == -1 || _to == -1)) {
			if (vertices[i].key.equals(from))
				_from = i;
			else if (vertices[i].key.equals(to))
				_to = i;
			i++;
		}

		if (_from == -1 || _to == -1)
			return false;

		am[_from][_to] = weight;
		if (twoWay)
			am[_to][_from] = weight;
		return true;
	}

	public boolean removeVertex(K key) {
		int i = 0;
		while (i < vertices.length && vertices[i] != null) {
			if (vertices[i].key.equals(key)) {
				populateAM(i);
				vertices[i] = null;
				return true;
			}
			i++;
		}
		return false;
	}

	public boolean removeEdge(K from, K to) {
		return removeEdge(from, to, false);
	}

	public boolean removeEdge(K from, K to, boolean twoWay) {
		if (from.equals(to))
			throw new IllegalArgumentException("Simple cycles are not allowed.");

		int _from = -1;
		int _to = -1;
		int i = 0;
		while (i < vertices.length && (_from == -1 || _to == -1)) {
			if (_from != -1 && _to != -1)
				break;
			if (vertices[i].key.equals(from))
				_from = i;
			else if (vertices[i].key.equals(to))
				_to = i;
			i++;
		}

		if (_from == -1 || _to == -1)
			return false;

		am[_from][_to] = -1;
		if (twoWay)
			am[_to][_from] = -1;
		return true;
	}

	private void populateAM(int index) {
		for (int i = 0; i < am.length; i++) {
			am[i][index] = -1;
			am[index][i] = -1;
		}
	}

	private void expandAM(int newSize) {
		int[][] temp = new int[newSize][newSize];
		for (int y = 0; y < am.length; y++) {
			for (int x = 0; x < am.length; x++) {
				temp[y][x] = am[y][x];
			}
		}
		am = temp;
		@SuppressWarnings("unchecked")
		GraphNode<K>[] vTemp = (GraphNode<K>[]) Array.newInstance(cast.getClass(), newSize);
		for (int i = 0; i < vertices.length; i++) {
			vTemp[i] = vertices[i];
		}
		vertices = vTemp;
	}
	
	@SuppressWarnings("unchecked")
	public K[] shortestPath(K start, K end) {
		PriorityQueue<SimpleEntry<KeyValuePair<GraphNode<K>>, Integer>> pq = new PriorityQueue<SimpleEntry<KeyValuePair<GraphNode<K>>, Integer>>();
		KeyValuePair<GraphNode<K>> cast = new KeyValuePair<GraphNode<K>>(null, null);
		KeyValuePair<GraphNode<K>>[] vertices = (KeyValuePair<GraphNode<K>>[]) Array.newInstance(cast.getClass(), this.vertices.length);
		
		KeyValuePair<GraphNode<K>> startV = null;
		KeyValuePair<GraphNode<K>> endV = null;
		
		int count = 0;
		for(int i = 0; i < this.vertices.length; i++) {
			if(this.vertices[i] != null) {
				KeyValuePair<GraphNode<K>> kvp = new KeyValuePair<GraphNode<K>>(this.vertices[i], Integer.MAX_VALUE);
				if(kvp.key.equals(start))
					startV = kvp;
				else if(kvp.key.equals(end))
					endV = kvp;
				vertices[count] = kvp;
				count++;
			}
		}
		
		if(startV == null || endV == null || startV == endV)
			return null;
		
		startV.weight = 0;
		pq.add(new SimpleEntry<KeyValuePair<GraphNode<K>>, Integer>(startV, 0));
		
		while(!pq.isEmpty()) {
			SimpleEntry<KeyValuePair<GraphNode<K>>, Integer> c = pq.remove();
			c.getKey().visited = true;
			
			for(int i = 0; i < am.length; i++) {
				if(am[c.getKey().key.index][i] >= 0) {
					
				}
			}
		}
		
		return null;
	}

	@SuppressWarnings("serial")
	protected class KeyValuePair<Key> extends SimpleEntry<Key, Integer> implements Comparable<KeyValuePair<Key>> {
		Integer weight;
		Key key;
		KeyValuePair<Key> pred;
		boolean visited;
		
		public KeyValuePair(Key key, Integer weight) {
			super(key, weight);
			this.weight = weight;
			this.key = key;
			pred = null;
			visited = false;
		}

		@Override
		public int compareTo(KeyValuePair<Key> kvp) {
			return -1 * kvp.getValue().compareTo(this.getValue());
		}
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
