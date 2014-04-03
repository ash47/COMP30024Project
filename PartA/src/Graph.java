import java.util.ArrayList;


/**
 * The base class for all our graphs
 * @author aschmid, rport
 *
 */
public class Graph {
	/** A cell in this graph */
	public Cell start;
	
	/** Number of cells in this  graph */
	//private int totalCells;
	
	/** The player this graph belongs to */
	//private int player;
	
	/** A list of cells in this graph */
	private ArrayList<Cell> cellList;
	
	/**
	 * Creates a new graph
	 * @param player The player ID that owns this graph
	 */
	public Graph(int player) {
		// Init variables
		//this.totalCells = 0;
		//this.player = player;
		this.cellList = new ArrayList<Cell>();
	}
	
	/**
	 * Adds a cell to this graph
	 * @param cell The cell to add to this graph
	 */
	public void addCell(Cell newCell) {
		// Check if this cell is already in the cell
		for(Cell cell : this.cellList) {
			if(cell == newCell) {
				return;
			}
		}
		
		// Add to this graph's cell list
		this.cellList.add(newCell);
	}
	
	/**
	 * Gets all the cells in this graph
	 * @return The list of cells in this graph
	 */
	public ArrayList<Cell> getCells() {
		return this.cellList;
	}
	
	/**
	 * Merges a graph into this graph
	 * @param graph The graph to add into this graph
	 */
	public void mergeGraph(Graph graph) {
		for(Cell cell : graph.getCells()) {
			// Add this cell
			this.addCell(cell);
		}
	}
}
