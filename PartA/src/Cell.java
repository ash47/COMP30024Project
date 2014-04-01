
/**
 * This class represents a cell on the board
 * @author aschmid, rport
 *
 */
public class Cell {
	/** The ID of the player who is in this cell */
	private int player;
	
	/** Is this cell redundent */
	private boolean red;
	
	/** The tripod this cell belongs to */
	private TripodGraph tripodGraph;
	
	/**
	 * Gets this cells tripod graph
	 * @return This cell's tripod graph
	 */
	public TripodGraph getTripodGraph() {
		return tripodGraph;
	}

	/**
	 * Sets this tripods graph
	 * @param tripodGraph The graph to merge this cell into
	 */
	public void setTripodGraph(TripodGraph tripodGraph) {
		this.tripodGraph = tripodGraph;
	}

	/**
	 * Creates a new cell
	 * @param player The ID of the player to set this cell to
	 */
	public Cell(int player) {
		this.player = player;
		
		// By default this cell is needed in the graph
		this.red = false;
	}
	
	/**
	 * Gets who owns this cell
	 * @return The ID of the player that owns this cell
	 */
	public int getPlayer() {
		return player;
	}
	
	/**
	 * Sets the player that is in this cell
	 * @param player The ID of the player to put into this cell
	 */
	public void setPlayer(int player) {
		this.player = player;
	}
	
	/**
	 * Grabs this cells current redundent state
	 * @return If this cell is redundent
	 */
	public boolean isRed() {
		return this.red;
	}

	/**
	 * Sets this cell to redudent or not
	 * @param red Should this cell be set to redundent
	 */
	public void setRed(boolean red) {
		this.red = red;
	}
}
