
/**
 * This class represents a cell on the board
 * @author aschmid
 *
 */
public class Cell {
	/** The ID of the player who is in this cell */
	public int player;
	
	/** Is this cell redundent */
	public boolean red;
	
	/** The loop graph this cell belongs to */
	public LoopGraph loopGraph;
	
	/** The tripod this cell belongs to */
	public TripodGraph tripodGraph;
	
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
	 * Sets the player that is in this cell
	 * @param player The ID of the player to put into this cell
	 */
	public void setPlayer(int player) {
		this.player = player;
	}
}
