package aschmid_rport.fencemaster;

import java.util.ArrayList;

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
	 * Gets the side count
	 * @return side_count
	 */
	public int getSide_Count()
	{
		return side_count;
	}
	
	/**
	 * Gets the ID
	 * @return Chain ID
	 */
	public int getID()
	{
		return id;
	}
	
	/**
	 * Gets the playerID
	 * @return playerID
	 */
	public int getPlayerID()
	{
		return playerID;
	}
	
	/**
	 * Gets the length
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
	
	public void add_cell(Cell cell)
	{
		cells.add(cell);
		cell.setChainID(id);
		length++;
	}
	
}
