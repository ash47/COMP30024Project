
public class Cell {
	// The ID of the player who is in this cell
	public int player;
	
	// Is this cell redundent
	public boolean red;
	
	public LoopGraph loopGraph;
	public TripodGraph tripodGraph;
	
	public Cell(int player) {
		this.player = player;
		
		// By default this cell is needed in the graph
		this.red = false;
	}
	
	public void setPlayer(int player) {
		this.player = player;
	}
}
