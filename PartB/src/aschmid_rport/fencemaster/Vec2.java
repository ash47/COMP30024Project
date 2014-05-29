package aschmid_rport.fencemaster;

/**
 * Prepresents an (x,y) position on the board
 * @author aschmid (584770), rport (586116)
 *
 */
public class Vec2 {
	/** The x coordinate */
	private int x;
	
	/** The y coordinate */
	private int y;
	
	/**
	 * Creates a new Vec2
	 * @param x The x coordinate
	 * @param y The y coordinate
	 */
	public Vec2(int x, int y) {
		// Store position
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Gets this vector's x coordinate
	 * @return The x coordinate
	 */
	public int getX() {
		return this.x;
	}
	
	/**
	 * Gets this vector's y coordinate
	 * @return The y coordinate
	 */
	public int getY() {
		return this.y;
	}
}
