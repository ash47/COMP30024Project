
/**
 * The base class for all our graphs
 * @author aschmid
 *
 */
public class Graph {
	/** A cell in this graph */
	public Cell start;
	
	/** Number of cells in this  graph */
	int totalCells;
	
	/** The player this graph belongs to */
	int player;
	
	/**
	 * Creates a new graph
	 * @param player The player ID that owns this graph
	 */
	public Graph(int player) {
		this.totalCells = 0;
		this.player = player;
	}
}
