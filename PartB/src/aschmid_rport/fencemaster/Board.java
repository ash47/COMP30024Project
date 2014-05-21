package aschmid_rport.fencemaster;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;

import aiproj.fencemaster.Piece;

public class Board {
	/** The size of the board */
	private int dim;
	
	/** Stores the winner */
	private int winner;
	
	/** The current redundent level (used for fast updates) */
	private int redLevel;

	/** These are the cells of the board */
	private Cell[][] cells;
	
	/** The max number of adacencies possible */
	public static final int MAX_ADJ = 6;
	
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
	
	public Board(int dim) {
		// Store the dimension
		this.dim = dim;
		
		// Set the redundant level to 1
		this.redLevel = 1;
		
		// Create an array to store cells
		cells = new Cell[2*dim-1][];
		
		// Create each cell
		for(int y=0; y<2*dim-1; y++) {
			int rowSize = getRowSize(y);
			cells[y] = new Cell[rowSize];
			for(int x=0; x<rowSize; x++) {
				cells[y][x] = new Cell(PLAYER_NONE);
			}
		}
		
		// There is no winner at the start
		this.winner = Piece.INVALID;
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
		if(	Math.abs(x-(this.dim-1)) > (this.dim-1) ||
			Math.abs(y-(this.dim-1)) > (this.dim-1) ||
			Math.abs(x-y) > (this.dim-1)) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Maps X in given format to X in stored array format
	 * @param x given x
	 * @param y given y
	 * @return Returns the x in stored array format
	 */
	public int mapX(int x, int y){
		if(y < this.dim) {
			return x;
		} else {
			int takeAway = Math.abs((y - (this.dim-1)));
			return (x - takeAway);
		}
	}
	
	/**
	 * Gets the first valid x coordinate for the given y coordinate
	 * @param y The y coordinate to check against
	 * @return The first valid x position for the given y, or -1 for no valid positions
	 */
	public int getFirstX(int y) {
		// Increasing curve
		if(y < this.dim) {
			return 0;
		} else {
			return y - ((this.dim) - 1);
		}
	}
	
	/**
	 * Returns the size of a given row
	 * @param y The y coordinate of the row to find the size of
	 * @return The size of a given row
	 */
	public int getRowSize(int y) {
		// Increasing curve
		if(y < this.dim) {
			return this.dim + y;
		}
		
		// Decreasing curve
		return 2*this.dim - (y - this.dim) - 2;
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
		
		x = mapX(x, y);
		
		return cells[y][x];
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
	 * @param x The x coordinate of the cell to fill
	 * @param y The y coordinate of the cell to fill
	 * @param player The ID of the player to put into this cell
	 */
	public void fillCell(int x, int y, int player) {
		int pattern = getRedPattern(x, y, player);
		
		// Get the cell that needs changing
		Cell cell = getCell(x, y);
		cell.setPlayer(player);
		
		if(pattern == -1) {
			// This cell is a block cell
			cell.setRed(0);
		} else {
			// Check if it's red
			if(pattern < 4) {
				// It's red, store current red level
				cell.setRed(this.redLevel);
			} else {
				// Do a full red scan
				fullRedScan();
			}
		}
	}
	
	/**
	 * @param x The x coordinate of the cell you want to swap
	 * @param y The y coordinate of the cell you want to swap
	 * @param player The ID of the new player
	 */
	public void swapCell(int x, int y, int player) {
		// Just update the cell
		setCell(x, y, player);
	}
	
	/**
	 * Scans the entire board for redness
	 */
	public void fullRedScan() {
		// Increase the red level
		this.redLevel += 1;
		
		// Create a list of cells to check
		ArrayList<Vec2> toCheck = new ArrayList<Vec2>();
		
		// Loop over all rows
		for(int y=0; y<this.dim*2-1; y++) {
			// Get the size of this row
			int rowSize = this.getRowSize(y);
			
			// Loop over each cell in this
			for(int x=0; x<rowSize; x++) {
				// Get the current cell
				Cell cell = getCell(x, y);
				
				// Grab the player
				int player = cell.getPlayer();
				
				if(player != PLAYER_NONE) {
					// Get the pattern
					int pattern = getRedPattern(x, y, cell.getPlayer());
					
					// Check if it is red or not
					if(pattern == -1 || pattern == 4) {
						// Not red
						cell.setRed(0);
						
						// We need to rescan this cell
						toCheck.add(new Vec2(x, y));
					} else {
						// Red
						cell.setRed(this.redLevel);
					}
				}
			}
		}
		
		// Win states
		boolean whiteWin = false;
		boolean blackWin = false;
		
		boolean changed = true;
		while(changed && toCheck.size() > 0) {
			// We haven't changed anything this time round
			changed = false;
			whiteWin = false;
			blackWin = false;
			
			// Iterate over all remaining cells
			Iterator<Vec2> it = toCheck.iterator();
			while(it.hasNext()) {
				// Get the position
				Vec2 pos = it.next();
				int x = pos.getX();
				int y = pos.getY();
				
				// Get the cell and player
				Cell cell = getCell(x, y);
				int player = cell.getPlayer();
				
				// Grab the pattern
				int pattern = getRedPattern(x, y, player);
				
				// Check if it's red or not
				if(pattern >= 0 && pattern < 4) {
					// Cell is now red
					cell.setRed(this.redLevel);
					
					// Remove from iterator
					it.remove();
					
					// Store that there was a change
					changed = true;
				} else {
					// One of these players won
					if(player == PLAYER_BLACK) {
						blackWin = true;
					} else {
						whiteWin = true;
					}
				}
			}
		}
		
		// Check if we have a winner
		if(toCheck.size() > 0) {
			if(blackWin) {
				this.winner = Piece.BLACK;
			} else if(whiteWin) {
				this.winner = Piece.WHITE;
			}
		}
	}
	
	/**
	 * @param x The x coordinate of the cell to check
	 * @param y The y coordinate of the cell to check
	 * @param player The ID of the player to check against
	 * @return The pattern around that cell
	 */
	public int getRedPattern(int x, int y, int player) {
		// Grab all adj cells
		Cell[] adj = getAdj(x, y);
		
		// The mode we are upto
		int mode = 0;
		
		// Work out if this cell is red
		for(int i=0; i<MAX_ADJ; i++) {
			Cell adjCell = adj[i];
			
			// Check if this cell is a block, or a gap
			boolean block = false;
			if(adjCell != null && adjCell.getPlayer() == player && adjCell.getRed() != this.redLevel) {
				block = true;
			}
			
			if(mode == 0) { // Search for block
				// Block
				if(block) {
					// Check if it's the first cell or not
					if(i == 0) {
						// First cell
						mode = -1;
					} else {
						// Not first cell
						mode = 1;
					}
				}
			} else if(mode == 1 || mode == -1) { // Searching for gap
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
			if(adjCell == null || adjCell.getPlayer() != player || adjCell.getRed() == this.redLevel) {
				mode = 4;
			}
		}
		
		return mode;
	}
	
	/**
	 * @param x The x coordinate of the cell to check
	 * @param y The y coordinate of the cell to check
	 * @return If a cell is taken or not
	 */
	public boolean cellTaken(int x, int y) {
		// Check if the cell has a player in it
		return getCell(x, y).getPlayer() != PLAYER_NONE;
	}
	
	
	/**
	 * Get the side a cell is on (0 if no side)
	 * @param x The x coordinate of the cell to check
	 * @param y The y coordinate of the cell to check
	 * @return The ID of the side this cell touches, or 0 for no side
	 */
	public int getSide(int x, int y) {
		if(x == 0) { // Top left side
			if(y > 0 && y < this.dim-1) {
				return 1;
			}
		}
		if(y == 0) { // Top Side
			if(x > 0 && x < this.dim-1) {
				return 2;
			}
		}
		if(x == this.getRowSize(y)-1) { // Top right
			if(y > 0 && y < this.dim-1) {
				return 4;
			}
		}
		if(x == 2*(this.dim-1)) { // Bottom right
			if(y > this.dim-1 && y < (this.dim-1)*2) {
				return 8;
			}
		}
		if(y == 2*(this.dim-1)) { // Bottom Side
			if(x >= this.dim && x < 2*(this.dim-1)) {
				return 16;
			}
		}
		if(x == this.getFirstX(y)) { // Bottom left
			if(y < 2*(this.dim-1) && y > this.dim-1) {
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
	 * @return The winner with the current board state
	 */
	public int getWinner() {
		return winner;
	}
	
	/**
	 * @param output The output stream to print to
	 */
	public void print(PrintStream output) {
		char[] char_array = new char[3];
		char_array[0] = PLAYER_NONE_TOKEN;
		char_array[1] = PLAYER_WHITE_TOKEN;
		char_array[2] = PLAYER_BLACK_TOKEN;
		
		// Iterates over whole board printing out each token
		for(int y = 0; y < 2*this.dim - 1; y++) {
			// Adds space buffer for nice hexagon effect
			for(int i = 0; i < Math.abs((this.dim - 1) - y); i++) {
				output.print(' ');
			}
			
			// Prints the tokens for the row
			for(int x = 0; x < 2*this.dim - 1; x++) {
				Cell current = getCell(x,y);
				if(current != null) {
					output.print(char_array[current.getPlayer()]);
					output.print(' ');
				}
			}
			
			// Prints new line for the next row
			output.println();
		}
	}
}
