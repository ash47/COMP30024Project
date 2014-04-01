/**
 * Main board class
 * @author aschmid
 */
public class Board {
	/** These are the cells of the board */
	public Cell[][] cells;
	
	/** The size of the board */
	public int size;
	
	/** The id of aN unknown player (invalid input) */
	public static final int PLAYER_UNKNOWN = -1;
	
	/** The id of a blank player */
	public static final int PLAYER_NONE = 0;
	
	/** The id of a white player */
	public static final int PLAYER_WHITE = 1;
	
	/** The id of a black player */
	public static final int PLAYER_BLACK = 2;
	
	/** The token for no player */
	public static final char PLAYER_NONE_TOKEN = '-';
	
	/** The token for white player */
	public static final char PLAYER_WHITE_TOKEN = 'W';
	
	/** The token for black player */
	public static final char PLAYER_BLACK_TOKEN = 'B';
	
	/** The max number of adacencies possible */
	public static final int MAX_ADJ = 6;
	
	/**
	 * @param size The size of the board
	 */
	public Board(int size) {
		// Store size
		this.size = size;
		
		// Allocate memory for the cells
		cells = new Cell[2*size][2*size];
		
		// Fill our board up
		for(int i=0;i<2*size;i++) {
			for(int j=0;j<2*size;j++) {
				// Check if it's a valid cell
				if(this.isValidCell(i, j)) {
					// Store it
					cells[i][j] = new Cell(0);
				}
			}
		}
	}
	
	/**
	 * Marks each cell as either redundent or not
	 */
	public void markRed() {
		while(true) {
			System.out.println("Loop");
			
			// Did we make a change?
			boolean changed = false;
			
			// Loop over every square
			for(int y = 0; y<2*this.size;y++) {
				for(int x=0; x<2*this.size; x++) {
					// Grab a cell
					Cell cell = this.getCell(x, y);
					
					// Make sure it is valid (and not red)
					if(cell != null && !cell.isRed() && cell.getPlayer() != PLAYER_NONE) {
						// Grab all adj cells
						Cell[] adj = getAdj(x, y);
						
						// The mode we are upto
						int mode = 0;
						
						// Workout if this cell is red
						for(int i=0; i<MAX_ADJ; i++) {
							Cell adjCell = adj[i];
							
							// Check if this cell is a block, or a gap
							boolean block = false;
							if(adjCell != null && cell.getPlayer() == adjCell.getPlayer() && !adjCell.isRed()) {
								block = true;
							}
							
							if(mode == 0) { // Search for block
								// Block
								if(block) {
									mode = 1;
								}
							} else if(mode == 1) { // Searching for gap
								// Gap
								if(!block) {
									mode = 2;
								}
							} else if(mode == 2) { // Searching for block
								// Block
								if(block) {
									mode = 3;
								}
							} else if(mode == 3) { // Searching for gap
								// Gap
								if(!block) {
									mode = 4;
								}
							}
						}
						
						// We need to consider the round nature of this search
						if(mode == 3) {
							// check initial block for gap
							Cell adjCell = adj[0];
							
							// Check if this cell is a block, or a gap
							if(adjCell == null || cell.getPlayer() != adjCell.getPlayer() || adjCell.isRed()) {
								mode = 4;
							}
						}
						
						// If the mode is 4, this block is needed, otherwise not
						if(mode < 4) {
							// Mark this cell as redundent
							cell.setRed(true);
							
							// Store that we made a change
							changed = true;
							
							System.out.println("found red");
							System.out.println(cell.getPlayer());
						}
					}
				}
			}
			
			// Check if no change was made
			if(!changed) {
				break;
			}
		}
		
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
				if(cell.getPlayer() != 0) {
					if(cell != null) {
						Cell[] adj = getAdj(x, y);
						
						for(int i=0;i<MAX_ADJ;i++) {
							Cell adjCell = adj[i];
							
							// Check if they are the same player
							if(adjCell != null && adjCell.getPlayer() == cell.getPlayer()) {
								if(!cell.isRed()) {
									System.out.println("Found a loop!");
								}
								
								if(adjCell.tripodGraph != null) {
									cell.tripodGraph = adjCell.tripodGraph;
								} else {
									cell.tripodGraph = new TripodGraph(cell.getPlayer());
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
	 * Gets the first valid x coordinate for the given y coordinate
	 * @param y The y coordinate to check against
	 * @return The first valid x position for the given y, or -1 for no valid positions
	 */
	public int getFirstX(int y) {
		// Increasing curve
		if(y < this.size) {
			return 0;
		} else {
			return y - this.size;
		}
	}
	
	/**
	 * Returns the size of a given row
	 * @param y The y coordinate of the row to find the size of
	 * @return The size of a given row
	 */
	public int getRowSize(int y) {
		// Increasing curve
		if(y < this.size) {
			return this.size + y;
		}
		
		// Decreasing curve
		return 2*this.size - (y - this.size) - 2;
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
	 * @return An array of size MAX_ADJ, containing the adjacent cells, cells will be null if they dont exist
	 */
	public Cell[] getAdj(int x, int y) {
		// Create list for cells
		Cell[] list = new Cell[MAX_ADJ];
		
		// Build list
		list[0] = getCell(x  , y-1);	// Up right
		list[1] = getCell(x+1, y  );	// Right
		list[2] = getCell(x+1, y+1);	// Down right
		list[3] = getCell(x  , y+1);	// Down left
		list[4] = getCell(x-1, y  );	// Left
		list[5] = getCell(x-1, y-1);	// Up left
		
		return list;
	}
	
	/**
	 * Returns the ID of the player
	 * @param id The character of the player, B, W or -
	 * @return
	 */
	public static int getPlayerID(char id) {
		if(id == PLAYER_BLACK_TOKEN) {
			// Black player
			return PLAYER_BLACK;
		} else if(id == PLAYER_WHITE_TOKEN) {
			// White player
			return PLAYER_WHITE;
		} else if(id == PLAYER_NONE_TOKEN) {
			// No player
			return PLAYER_NONE;
		} else {
			// Unknwon player (invalid input)
			return PLAYER_UNKNOWN;
		}
	}
}
