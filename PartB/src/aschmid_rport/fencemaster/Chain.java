package aschmid_rport.fencemaster;

import java.util.ArrayList;

/**
 * This class represents tripods om a board
 * @author aschmid (584770), rport (586116)
 *
 */
public class Chain {
	/** The ID of the chain */
	int id;
	
	/** The length of the chain */
	int length;
	
	/** The ID of the player who has this chain */
	int playerID;
	
	/** The number of sides */
	public static final int SIDE_NUM = 6;
	
	/** The number of sides this chain touches*/
	private int side_count;
	
	/** Which sides this chain touches*/
	private int sides;
	
	/** All the cells in this chain*/
	private ArrayList<Cell> cells;
	
	/**
	 * Creates a new chain
	 * @param an_id A unique ID for this chain
	 * @param a_playerID The player who owns this chain
	 */
	public Chain(int an_id, int a_playerID)
	{
		id = an_id;
		playerID = a_playerID;
		side_count = 0;
		sides = 0;
		length = 0;
		cells = new ArrayList<Cell>();
	}
	
	/**
	 * Copy constructor for the Chain class
	 * @param original
	 */
	public Chain(Chain original)
	{
		side_count = original.getSide_Count();
		sides = original.getSides();
		cells = new ArrayList<Cell>();
		id = original.getID();
		playerID = original.getPlayerID();
		length = 0;
	}
	
	/**
	 * Gets the number of sides this chain is touching
	 * @return side_count
	 */
	public int getSide_Count()
	{
		return side_count;
	}
	
	/**
	 * Gets the ID of the chain
	 * @return Chain ID
	 */
	public int getID()
	{
		return id;
	}
	
	/**
	 * Gets the playerID of the chain
	 * @return playerID
	 */
	public int getPlayerID()
	{
		return playerID;
	}
	
	/**
	 * Gets the length of the chain
	 * @return length
	 */
	public int getLength()
	{
		return length;
	}
	
	/**
	 * Checks if chain touches the side
	 * @param side
	 * @return true if touches, false if not
	 */
	public boolean hasSide(int side)
	{
		// Check if a given side is touched
		return (sides&side) != 0;
	}
	
	/**
	 * Sets the side as touched
	 * @param side
	 */
	public void setSide(int side)
	{
		if(side > 0 && !hasSide(side)) {
			// Increase number of sides touched
			side_count++;
			
			// Mark this side as touched
			sides += side;
		}
	}
	
	/**
	 * Gets the sides of the chain
	 * @return sides
	 */
	public int getSides()
	{
		return sides;
	}
	
	/**
	 * Gets the cells of the chain
	 * @return cells
	 */
	public ArrayList<Cell> getCells()
	{
		return cells;
	}
	
	/**
	 * Adds a cell into the chain
	 * @param cell The cell to add into this chain
	 */
	public void add_cell(Cell cell)
	{
		cells.add(cell);
		cell.setChainID(id);
		length++;
	}
	
}
