package aschmid_rport.fencemaster;

public class Cell {
	/** The ID of the player who is in this cell */
	private int player;
	
	/** Is this cell redundant */
	private int red;
	
	public Cell(int player) {
		this.player = player;
		this.red = 0;
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
	 * @param red The red state to set this cell to
	 */
	public void setRed(int red) {
		this.red = red;
	}
}
