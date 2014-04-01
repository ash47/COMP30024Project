
/**
 * Main board class
 * @author aschmid
 */
public class Board {
	/** These are the cells of the board */
	public Cell[][] cells;
	
	/** The size of the board */
	public int size;
	
	/**
	 * @param size The size of the board
	 */
	public Board(int size) {
		// Allocate memory for the cells
		cells = new Cell[size][size];
		
		// Fill our board up
		for(int i=0;i<size;i++) {
			for(int j=0;j<size;j++) {
				cells[i][j] = new Cell(0);
			}
		}
	}
	
	/**
	 * Marks each cell as either redundent or not
	 */
	public void markRed() {
		// Mark stuff
	}
	
	/**
	 * Build graphs
	 */
	public void buildGraphs() {
		// Mark stuff as redunedent
		markRed();
		
		// Build graphs
		for(int x=0;x<this.size;x++) {
			for(int y=0;y<this.size;y++) {
				Cell cell = getCell(x, y);
				
				// Check if there is a player in this cell
				if(cell.player != 0) {
					if(cell != null) {
						Cell[] adj = getAdj(x, y);
						
						for(int i=0;i<6;i++) {
							Cell adjCell = adj[i];
							
							// Check if they are the same player
							if(adjCell.player == cell.player) {
								if(!cell.red) {
									if(!adjCell.red && adjCell.loopGraph != null) {
										cell.loopGraph = adjCell.loopGraph;
									} else {
										cell.loopGraph = new LoopGraph(cell.player);
									}
								}
								
								if(adjCell.tripodGraph != null) {
									cell.tripodGraph = adjCell.tripodGraph;
								} else {
									cell.tripodGraph = new TripodGraph(cell.player);
								}
							}
						}
					}
				
				}
				
			}
		}
	}
	
	/**
	 * Checks if a cell is valid
	 * @param x The x coordinate of the cell
	 * @param y The y coordinate of the cell
	 * @return Is this cell valid?
	 */
	public boolean isValidCell(int x, int y) {
		// Valid cells are integer pairings on the plane described by {abs(x-(n-1))<=(n-1), abs(y-(n-1))<=(n-1), abs(x-y)<=n-1}
		// Checks for integers out of the range and returns false if found
		if(	Math.abs(x-(this.size-1)) > (this.size-1) ||
			Math.abs(y-(this.size-1)) > (this.size-1) ||
			Math.abs(x-y) > (this.size-1)){
			return false;
		}
		
		return true;
	}
	
	/**
	 * Gets a cell in a specified cell
	 * @param x The x coordinate of the cell you want
	 * @param y The y coordinate of the cell you want
	 * @return A cell if it exists, or null
	 */
	public Cell getCell(int x, int y) {
		// Check if the cell is valid
		if(!isValidCell(x, y)) {
			return null;
		}
		
		return cells[x][y];
	}
	
	
	/**
	 * Sets a player ID into a cell
	 * @param x The x coordinate of the cell you want
	 * @param y The y coordinate of the cell you want
	 * @param player The color of the player to put into this cell
	 */
	public void setCell(int x, int y, int player) {
		Cell cell = getCell(x, y);
		
		if(cell != null) {
			cell.setPlayer(player);
		}
	}
	
	
	/**
	 * Get the side a cell is on (0 if no side)
	 * @param x The x coordinate of the cell to check
	 * @param y The y coordinate of the cell to check
	 * @return The ID of the side this cell touches, or 0 for no side
	 */
	public int getSide(int x, int y) {
		return 0;
	}
	
	/**
	 * Gets all the adjacent cells
	 * @param x The x coordinate of the cell to get adjacencies for
	 * @param y The y coordinate of the cell to get adjacencies for
	 * @return An array of size 6, containing the adjacent cells, cells will be null if they dont exist
	 */
	public Cell[] getAdj(int x, int y) {
		// Create list for cells
		Cell[] list = new Cell[6];
		
		// Build list
		list[0] = getCell(x-1, y-1);	// Up left
		list[1] = getCell(x  , y-1);	// Up right		
		list[2] = getCell(x-1, y  );	// Left
		list[3] = getCell(x+1, y  );	// Right
		list[4] = getCell(x  , y+1);	// Down left
		list[5] = getCell(x+1, y+1);	// Down right
		
		return list;
	}
}
