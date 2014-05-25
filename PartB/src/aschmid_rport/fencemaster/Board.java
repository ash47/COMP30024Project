package aschmid_rport.fencemaster;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;

import aiproj.fencemaster.Move;
import aiproj.fencemaster.Piece;

public class Board {
	/** The size of the board */
	private int dim;
	
	/** The number of cells the board contains in total */
	private int total_cells;
	
	/** Stores the winner */
	private int winner;
	
	/** How many turns have been played so far*/
	private int turn;
	
	/** The current redundent level (used for fast updates) */
	private int redLevel;
	
	/** The number of moves where the board decides positions heuristically */
	private int heuristic_depth;
	
	/** The maximum depth of the minimax search */
	private int minimax_cutoff;

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
				cells[y][x] = new Cell(PLAYER_NONE, y, unmapX(x, y));
			}
		}
		
		// There is no winner at the start
		this.winner = Piece.INVALID;
		//No turns have occured yet
		this.turn = 0;
		this.heuristic_depth = 8;
		this.minimax_cutoff = 3;
		this.total_cells  = 3*(dim*dim - dim) + 1;
	}
	
	/**
	 * Copy constructor for the board class
	 * @param original board to be copied
	 */
	public Board(Board original)
	{
		dim = original.dim;
		redLevel = original.redLevel;
		winner = original.getWinner();
		turn = original.turn;
		heuristic_depth = original.heuristic_depth;
		minimax_cutoff = original.minimax_cutoff;
		
		cells = new Cell[2*dim - 1][];
		for(int y = 0; y < 2*dim - 1; y++)
		{
			int rowSize = getRowSize(y);
			cells[y] = new Cell[rowSize];
			for(int x = 0; x < rowSize; x++)
			{
				cells[y][x] = new Cell(original.cells[y][x]);
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
	 * Maps X in stored array format to X in given format
	 * @param x stored x
	 * @param y stored y
	 * @return Returns the x in given format
	 */
	public int unmapX(int x, int y){
		if(y < this.dim) {
			return x;
		} else {
			int addto = Math.abs((y - (this.dim-1)));
			return (x + addto);
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
		//A turn has been made
		this.turn++;
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
		turn++;
		
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
		//If all the cells are filled and there is no winner, the match is a draw
		if((turn >= total_cells)&&(winner < Piece.EMPTY)) winner = Piece.EMPTY;
	}
	
	/**
	 * @param x The x coordinate of the cell you want to swap
	 * @param y The y coordinate of the cell you want to swap
	 * @param player The ID of the new player
	 */
	public void swapCell(int x, int y, int player) {
		// Just update the cell
		setCell(x, y, player);
		// Though a turn was made, the board hasn't changed
		this.turn--;
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
	
	/**
	 * Chooses which type of move to make for the board
	 * @param playerID the id of the player
	 * @return The move that is calculated
	 */
	public Move makeMove(int playerID)
	{
		Move move;
		
		if(turn < 1) move = makefirstMove(playerID);
		else if(turn < heuristic_depth) move = makeheuristicMove(playerID);
		else move = makeminimaxMove(playerID);
		fillCell(move.Col, move.Row, move.P);
		
		return move;
	}
	
	/**
	 * Makes the move if the player is playing first
	 * @return The first move, at position 0,0
	 */
	private Move makefirstMove(int playerID)
	{
		return new Move(playerID, false, 0, 0);
	}
	
	/**
	 * Makes best move dictated by heuristics within the depth
	 * @return the best move
	 */
	private Move makeheuristicMove(int playerID)
	{
		
		//2nd turn, check if we wish to swap, if not take first move
		if(turn == 1)
		{
			int corner[] = check_corners();
			if(corner != null) return new Move(playerID, true, corner[0], corner[1]);
			else return makefirstMove(playerID);
		}
		//For the rest of the turns try and construct a good position using heuristics
		else
		{
			Cell rels[] = getAdj(0, 0);
			//try to place in optimal cells (touching sides)
			for(int i = 0; i < MAX_ADJ; i++)
			{
				Cell cell = rels[i];
				if(	(cell != null)&&//Cell is valid
					(cell.getPlayer() == 0)&&//Cell is empty
					(getSide(cell.getY(), cell.getX()) > 0))//Cell touches a side
				{
					return new Move(playerID, false, cell.getY(), cell.getX());
				}
			}
			//place in opposite cell 2,2 if no optimal cells remain
			Cell cell_opposite = getCell(2,2);
			if(cell_opposite.getPlayer() == 0) return new Move(playerID, false, 2, 2);
			
			//place in any adjacent cell if there are not optimal cells
			for(int i = 0; i < MAX_ADJ; i++)
			{
				Cell cell = rels[i];
				if(	(cell != null)&&//Cell is valid
					(cell.getPlayer() == 0))//Cell is empty
				{
					return new Move(playerID, false, cell.getY(), cell.getX());
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Makes move based on minimax algorithm
	 * @return best move according to minimax algorithm
	 */
	private Move makeminimaxMove(int playerID)
	{
		int me = playerID;
		int enemy = 2 - me + 1;
		
		//Initial depth is 0
		int depth = 0;
		
		//The move that will be returned
		Move move = new Move(me, false, 0, 0);
		
		//Create an array list of relevant cells to consider
		ArrayList<Vec2> relevant_cells = new ArrayList<Vec2>();
		get_rels(relevant_cells);
		
		//The best value so far
		int best_val = Integer.MIN_VALUE;
		int temp_val = 0;
		
		//Create an iterator for the relevant celsl
		Iterator<Vec2> rels = relevant_cells.iterator();
		
		while(rels.hasNext())
		{
			Vec2 curr = rels.next();
			Board cpy = new Board(this);
			cpy.fillCell(curr.getX(), curr.getY(), me);
			if((temp_val = min(cpy, enemy, depth + 1)) > best_val)
			{
				move.Row = curr.getY();
				move.Col = curr.getX();
				best_val = temp_val;
			}
			System.out.println("Relevant cell ["+curr.getY()+", "+curr.getX()+"] has value "+temp_val);
		}
		
		return move;
	}
	
	/**
	 * The min portion of the minimax algorith
	 * @param board the board to be used
	 * @param me the playerID of the player making the turn
	 * @param depth the current depth of the search
	 * @return the lowest value as evaluated by the function
	 */
	private int min(Board board, int me, int depth)
	{
		//Check for winner, returns -1 if enemy wins, 1 if me wins
		int the_winner = board.getWinner();
		int enemy = 2 - me + 1;
		if(the_winner != 0)
		{
			if(the_winner == me) return -1;
			else if(the_winner == Piece.INVALID) return 0;
			else return 1;
		}
		//If the max depth has been reached, return the evaluation of the board state
		else if(depth >= minimax_cutoff)
		{
			return board.eval(enemy);
		}
		
		else
		{
			
			//Create an array list of relevant cells to consider
			ArrayList<Vec2> relevant_cells = new ArrayList<Vec2>();
			board.get_rels(relevant_cells);
			
			//The best value so far
			int worst_val = Integer.MAX_VALUE;
			int temp_val = 0;
			
			//Create an iterator for the relevant cells
			Iterator<Vec2> rels = relevant_cells.iterator();
			
			while(rels.hasNext())
			{
				Vec2 curr = rels.next();
				Board cpy = new Board(this);
				cpy.fillCell(curr.getX(), curr.getY(), me);
				if((temp_val = max(cpy, enemy, depth + 1)) < worst_val)
				{
					worst_val = temp_val;
				}
			}
			return worst_val;
		}
	}
	
	/**
	 * The max portion of the minimax algorith
	 * @param board the board to be used
	 * @param me the playerID of the player making the turn
	 * @param depth the current depth of the search
	 * @return the highest value as evaluated by the function
	 */
	private int max(Board board, int me, int depth)
	{
		//Check for winner, returns -1 if enemy wins, 1 if me wins
		int the_winner = board.getWinner();
		int enemy = 2 - me + 1;
		if(the_winner != 0)
		{
			if(the_winner == me) return 1;
			else if(the_winner == Piece.INVALID) return 0;
			else return -1;
		}
		//If the max depth has been reached, return the evaluation of the board state
		else if(depth >= minimax_cutoff)
		{
			return board.eval(me);
		}
		
		else
		{
			
			//Create an array list of relevant cells to consider
			ArrayList<Vec2> relevant_cells = new ArrayList<Vec2>();
			board.get_rels(relevant_cells);
			
			//The best value so far
			int best_val = Integer.MIN_VALUE;
			int temp_val = 0;
			
			//Create an iterator for the relevant cells
			Iterator<Vec2> rels = relevant_cells.iterator();
			
			while(rels.hasNext())
			{
				Vec2 curr = rels.next();
				Board cpy = new Board(this);
				cpy.fillCell(curr.getX(), curr.getY(), me);
				if((temp_val = min(cpy, enemy, depth + 1)) > best_val)
				{
					best_val = temp_val;
				}
			}
			return best_val;
		}
	}
	
	/**
	 * Gets the relevant cells in the board to be considered for minimax
	 * Relevant cells are simply all the adjacent cells of all taken cells
	 * @param relevant_cells
	 */
	private void get_rels(ArrayList<Vec2> relevant_cells) 
	{
		boolean added[][] = new boolean[2*dim - 1][2*dim - 1];
		/**
		 * Loop over all cells to find taken cells
		 * then loop over adjacent cells of taken cells and add them if they haven't been added already
		 */
		for(int y = 0; y < 2*dim-1; y++) 
		{
			int rowSize = getRowSize(y);
			for(int x = 0; x < rowSize; x++) 
			{
				if(cells[y][x].getPlayer() != 0)
				{
					Cell cell = cells[y][x];
					int X = cell.getX();
					int Y = cell.getY();
					Cell adj[] = getAdj(X, Y);
					for(int i = 0; i < MAX_ADJ; i++)
					{
						if(adj[i] != null)
						{
							if(	(adj[i].getPlayer() == 0)&&
								(added[adj[i].getY()][adj[i].getX()] != true))
								{
									relevant_cells.add(new Vec2(adj[i].getX(), adj[i].getY()));
									added[adj[i].getY()][adj[i].getX()] = true;
								}
						}
					}
				}
			}
		}
		
	}

	/**
	 * The evaluation function for the current board state
	 * @param me the playerID of the player who called the minimax algorithm
	 * @return the evaluation value
	 */
	private int eval(int me)
	{
		int result = 0;
		
		return result;
	}
	
	/**
	 * Checks each corner of the board to see if there is a player there
	 * @return The first corner found with a player, if none have a player null is returned
	 */
	private int[] check_corners()
	{
		
		if(getCell(0, 0).getPlayer() > 0) return new int[] {0, 0};//Top left
		else if(getCell(dim - 1, 0).getPlayer() > 0) return new int[] {dim - 1, 0};//Top right
		else if(getCell(2*dim - 1, dim - 1).getPlayer() > 0) return new int[] {2*dim - 1, 0};//Middle right
		else if(getCell(dim - 1, 2*dim - 1).getPlayer() > 0) return new int[] {dim - 1, 2*dim - 1};//Bottom right
		else if(getCell(0, 2*dim - 1).getPlayer() > 0) return new int[] {0, 2*dim - 1};//Bottom left
		else if(getCell(0, dim - 1).getPlayer() > 0) return new int[] {0, dim - 1};//Middle left
		
		return null;
	}
}
