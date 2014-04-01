
/**
 * A graph that represents tripods
 * @author aschmid
 *
 */
public class TripodGraph extends Graph {
	/** The number of edges this graph is touching */
	public int edgeCount;
	
	/** This stores which sides have been touched by our graph */
	public int sides;
	
	/**
	 * Creates a new tripod graph
	 * @param player The ID of the player who owns this graph
	 */
	public TripodGraph(int player) {
		super(player);
		this.edgeCount = 0;
	}
	
	/**
	 * Marks that this tripod is touching a given side
	 * @param side The ID of the side to touch
	 */
	public void touchSide(int side) {
		if(!isSideTouched(side)) {
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
}
