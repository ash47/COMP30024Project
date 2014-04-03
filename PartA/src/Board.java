/**
 * Main board class
 * @author aschmid, rport
 */
public class Board {
	/** These are the cells of the board */
	private Cell[][] cells;
	
	/** The size of the board */
	private int size;
	
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
			// Did we make a change?
			boolean changed = false;
			
			int totalRows = (this.size-1)*2 + 1;
			int totalCells = totalRows*totalRows;
			
			int x = 0;
			int y = 0;
			int dir = 0;
			
			int maxCount = 2*(this.size-1)+1;
			int count = 0;
			
			boolean first = true;
			
			//System.out.println("Loop");
			
			// Loop over every square
			for(int j=0; j<totalCells; j++) {
				// debug
				//System.out.println("("+x+", "+y+")");
				
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
					}
				}
				
				// Spirial
				count++;
				if(dir == 0) {
					if(count < maxCount) {
						x++;
					} else {
						dir = 1;
						count = 1;
						if(first) {
							first = false;
						} else {
							maxCount--;
						}
					}
				}
				
				if(dir == 1) {
					if(count < maxCount) {
						y++;
					} else {
						dir = 2;
						count = 1;
					}
				}
				
				if(dir == 2) {
					if(count < maxCount) {
						x--;
					} else {
						dir = 3;
						count = 1;
						maxCount--;
					}
				}
				
				if(dir == 3) {
					if(count < maxCount) {
						y--;
					} else {
						dir = 0;
						count = 1;
						x++;
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
	 * Checks the board for a winner
	 * @return true if there is a winner, false if there is no winner
	 */
	public boolean checkForWinner() {
		// Mark stuff as redunedent
		markRed();
		
		// State controllers
		boolean loopBlack = false;
		boolean loopWhite = false;
		boolean tripodBlack = false;
		boolean tripodWhite = false;
		
		// Build graphs
		for(int x=0;x<2*this.size;x++) {
			for(int y=0;y<2*this.size;y++) {
				Cell cell = getCell(x, y);
				
				// Check if there is a player in this cell
				if(cell != null && cell.getPlayer() != 0) {
					// Check for loops
					if(!cell.isRed()) {
						// Found a loop
						if(cell.getPlayer() == PLAYER_WHITE) {
							loopWhite = true;
						} else if(cell.getPlayer() == PLAYER_BLACK) {
							loopBlack = true;
						}
					}
					
					// Grab all the adj cells
					Cell[] adj = getAdj(x, y);
					
					// Create a new tripod graph for this cell
					cell.setTripodGraph(new TripodGraph(cell.getPlayer()));
					
					// Loop over every cell
					for(int i=0;i<MAX_ADJ;i++) {
						Cell adjCell = adj[i];
						
						// Check if they are the same player
						if(adjCell != null && adjCell.getPlayer() == cell.getPlayer()) {
							// Get the adj's tripod
							TripodGraph adjTripod = adjCell.getTripodGraph();
							
							// Check if it has one
							if(adjTripod != null) {
								// Merge tripod
								adjTripod.mergeGraph(cell.getTripodGraph());
							}
						}
					}
					
					TripodGraph tr = cell.getTripodGraph();
					
					// Mark this side of the tripod as touched
					tr.touchSide(this.getSide(x, y));
					
					// Check if this tripod has 3 sides or more
					if(tr.getEdgeCount() >= 3) {
						// Found tripod
						if(cell.getPlayer() == PLAYER_WHITE) {
							tripodWhite = true;
						} else if(cell.getPlayer() == PLAYER_BLACK) {
							tripodBlack = true;
						}
					}
				
				}
				
			}
		}
		
		// Check who won
		if(loopBlack || tripodBlack) {
			// Black won, white unknown
			
			if(loopWhite || tripodWhite) {
				// Draw
				System.out.println(Main.MESSAGE_DRAW);
				System.out.println(Main.MESSAGE_DRAW_STATE);
			} else {
				if(!loopBlack) {
					// Tripod black won
					System.out.println(Main.MESSAGE_BLACK_WINS);
					System.out.println(Main.MESSAGE_TRIPOD_WINS);
				} else if(!tripodBlack) {
					// Loop black won
					System.out.println(Main.MESSAGE_BLACK_WINS);
					System.out.println(Main.MESSAGE_LOOP_WINS);
				} else {
					// Both black won
					System.out.println(Main.MESSAGE_BLACK_WINS);
					System.out.println(Main.MESSAGE_BOTH_WINS);
				}
				return true;
			}
			
			/**
			 * 
			 * THIS IS FOR DEBUGGING ONLY, REMOVE THIS BEFORE YOU SUBMIT!
			 * 
			 */
			
			//When draw, how each player won
			if(tripodBlack) {
				// Tripod black won
				System.out.println("Black tripod");
			}
			if(loopBlack) {
				// Loop black won
				System.out.println("Black loop");
			}
			if(tripodWhite) {
				// Tripod white won
				System.out.println("White tripod");
			}
			if(loopWhite) {
				// Loop white won
				System.out.println("White loop");
			}
			
			/**
			 * 
			 * END DEBUGGING BLOCK
			 * 
			 */
			
			return true;
		} else {
			// Black lost, white unknown
			
			if(loopWhite || tripodWhite) {
				if(!loopWhite) {
					// Tripod white won
					System.out.println(Main.MESSAGE_WHITE_WINS);
					System.out.println(Main.MESSAGE_TRIPOD_WINS);
				} else if(!tripodWhite) {
					// Loop white won
					System.out.println(Main.MESSAGE_WHITE_WINS);
					System.out.println(Main.MESSAGE_LOOP_WINS);
					
				} else {
					// Both white won
					System.out.println(Main.MESSAGE_WHITE_WINS);
					System.out.println(Main.MESSAGE_BOTH_WINS);
				}
			} else {
				// No winner
				return false;
			}
		}
		
		// Someone won
		return true;
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
			return y - ((this.size) - 1);
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
		if(x == 0) { // Top left side
			if(y > 0 && y < this.size-1) {
				return 1;
			}
		}
		if(y == 0) { // Top Side
			if(x > 0 && x < this.size-1) {
				return 2;
			}
		}
		if(x == this.getRowSize(y)-1) { // Top right
			if(y > 0 && y < this.size-1) {
				return 4;
			}
		}
		if(x == 2*(this.size-1)) { // Bottom right
			if(y > this.size-1 && y < (this.size-1)*2) {
				return 8;
			}
		}
		if(y == 2*(this.size-1)) { // Bottom Side
			if(x >= this.size && x < 2*(this.size-1)) {
				return 16;
			}
		}
		if(x == this.getFirstX(y)) { // Bottom left
			if(y < 2*(this.size-1) && y > this.size-1) {
				return 32;
			}
		}
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
	
	/**
	 * Prints the board nicely :)
	 */
	public void print(){
		char[] char_array = new char[3];
		char_array[0] = PLAYER_NONE_TOKEN;
		char_array[1] = PLAYER_WHITE_TOKEN;
		char_array[2] = PLAYER_BLACK_TOKEN;
		
		//Iterates over whole board printing out each token
		for(int y = 0; y < 2*size - 1; y++) {
			//Adds space buffer for nice hexagon effect
			for(int i = 0; i < Math.abs((size - 1) - y); i++) {
				System.out.print(' ');
			}
			
			//prints the tokens for the row
			for(int x = 0; x < 2*size - 1; x++) {
				Cell current = getCell(x,y);
				if(current != null) {
					System.out.print(char_array[current.getPlayer()]);
					System.out.print(' ');
				}
			}
			
			//prints new line for the next row
			System.out.println();
		}
	}
	
	public void printLoops() {
		char[] char_array = new char[3];
		char_array[0] = PLAYER_NONE_TOKEN;
		char_array[1] = PLAYER_WHITE_TOKEN;
		char_array[2] = PLAYER_BLACK_TOKEN;
		
		//Iterates over whole board printing out each token
		for(int y = 0; y < 2*size - 1; y++) {
			//Adds space buffer for nice hexagon effect
			for(int i = 0; i < Math.abs((size - 1) - y); i++) {
				System.out.print(' ');
			}
			
			//prints the tokens for the row
			for(int x = 0; x < 2*size - 1; x++) {
				Cell current = getCell(x,y);
				if(current != null) {
					if(current.isRed()){
						System.out.print('-');
					}
					else{
						System.out.print(char_array[current.getPlayer()]);						
					}
					System.out.print(' ');
				}
			}
			
			//prints new line for the next row
			System.out.println();
		}
	}
	
	/**
	 * Prints the sides of the board (for testing purposes)
	 */
	public void printSides() {
		char[] char_array = new char[33];
		char_array[0] = PLAYER_NONE_TOKEN;
		char_array[1] = '1';
		char_array[2] = '2';
		char_array[4] = '3';
		char_array[8] = '4';
		char_array[16] = '5';
		char_array[32] = '6';
		
		//Iterates over whole board printing out each token
		for(int y = 0; y < 2*size - 1; y++) {
			//Adds space buffer for nice hexagon effect
			for(int i = 0; i < Math.abs((size - 1) - y); i++) {
				System.out.print(' ');
			}
			
			//prints the tokens for the row
			for(int x = 0; x < 2*size - 1; x++) {
				Cell current = getCell(x,y);
				if(current != null) {
					System.out.print(char_array[getSide(x,y)]);
					System.out.print(' ');
				}
			}
			
			//prints new line for the next row
			System.out.println();
		}
	}
}
