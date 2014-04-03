
/**
 * A graph that represents tripods
 * @author aschmid, rport
 *
 */
public class TripodGraph extends Graph {
	/** The number of edges this graph is touching */
	private int edgeCount;
	
	/** This stores which sides have been touched by our graph */
	private int sides;
	
	/** Used to allocate every tripod a unique ID */
	private static int totalTripods = 0;
	
	/** This tripod's ID */
	public int tripodID;
	
	/**
	 * Creates a new tripod graph
	 * @param player The ID of the player who owns this graph
	 */
	public TripodGraph(int player) {
		super(player);
		this.edgeCount = 0;
		
		// Allocate an ID to this tripod
		this.tripodID = ++totalTripods;
	}
	
	/**
	 * Marks that this tripod is touching a given side
	 * @param side The ID of the side to touch
	 */
	public void touchSide(int side) {
		if(side > 0 && !isSideTouched(side)) {
			// Increase number of sides touched
			edgeCount++;
			
			// Mark this side as touched
			sides += side;
		}
	}
	
	/**
	 * Checks if a given side has been touched
	 * @param side The ID of the side to check
	 * @return If a given side has already been touched
	 */
	public boolean isSideTouched(int side) {
		// Check if a given side is touched
		return (sides&side) != 0;
	}
	
	/**
	 * Gets this tripod's edge count
	 * @return This tripod's edge count
	 */
	public int getEdgeCount() {
		return this.edgeCount;
	}
	
	public void mergeGraph(Graph graph) {
		// Update all cell's tripods
		for(Cell cell : graph.getCells()) {
			cell.setTripodGraph(this);
		}
		
		// Check if we merged a tripod graph
		if(graph instanceof TripodGraph) {
			// Cast a tripod graph
			TripodGraph tripod = (TripodGraph)graph;
			
			int side = 1;
			
			// Merge touched sides
			for(int i=0; i<Board.MAX_ADJ; i++) {
				// Check if this side is touched
				if(tripod.isSideTouched(side)) {
					// Touch that side on this graph
					this.touchSide(side);
				}
				
				// Move onto the next side
				side *= 2;
			}
		}
	}
}
