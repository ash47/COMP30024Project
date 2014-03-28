
public class TripodGraph extends Graph {
	// The number of edges this graph is touching
	public int edgeCount;
	
	// This stores which sides have been touched by our graph
	public int sides;
	
	public TripodGraph(int player) {
		super(player);
		this.edgeCount = 0;
	}
	
	public void touchSide(int side) {
		if(!isSideTouched(side)) {
			// Increase number of sides touched
			edgeCount++;
			
			// Mark this side as touched
			sides += side;
		}
	}
	
	public boolean isSideTouched(int side) {
		// Check if a given side is touched
		return (sides&side) != 0;
	}
}
