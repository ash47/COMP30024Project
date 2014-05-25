package aschmid_rport.fencemaster;

public class Cell {
	/** The ID of the player who is in this cell */
	private int player;
	
	/** The y co-ordinate of the cell */
	private int y;
	
	/** The x co-ordinate of the cell*/
	private int x;
	
	/** Is this cell redundant */
	private int red;
	
	public Cell(int player, int y, int x) {
		this.player = player;
		this.red = 0;
		this.y = y;
		this.x = x;
	}
	
	/**
	 * Copy constructor for cell class
	 * @param original cell to be copied
	 */
	public Cell(Cell original)
	{
		player = original.getPlayer();
		red = original.getRed();
		y = original.getY();
		x = original.getX();
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
	 * @return This cell's red state
	 */
	public int getRed() {
		return this.red;
	}
	
	/**
	 * @return This cell's y co-ordinate
	 */
	public int getY() {
		return this.y;
	}
	
	
	/**
	 * @return This cell's x co-ordinate
	 */
	public int getX() {
		return this.x;
	}
	
	/**
	 * @param red The red state to set this cell to
	 */
	public void setRed(int red) {
		this.red = red;
	}
}
