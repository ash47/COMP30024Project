package aschmid_rport.fencemaster;

import java.io.PrintStream;

import aiproj.fencemaster.* ;

public class Schmipo implements Player, Piece {
	
	/** The ID of this player */
	int playerID;
	
	
	/** The board dimension */
	public int dim;
	
	/** The board we use */
	private Board board;
	
	/** Stores if a swap is valid or not */
	private boolean canSwap;
	
	public int init(int n, int p) {
		// Store dimension
		this.dim = n;
		
		// User can swap
		this.canSwap = true;
		
		// Create a new board
		this.board = new Board(this.dim);

		// Init was successful
		return 0;
	}
	
	public Move makeMove() {
		Move move = board.makeMove(playerID);
		return move;
	}
	
	public int opponentMove(Move m) {
		// Grab useful info
		int x = m.Col;
		int y = m.Row;
		
		// Check if it is a valid position
		if(!board.isValidCell(x, y)) {
			return -1;
		}
		
		// Check if the cell is already taken
		if(board.cellTaken(x, y)) {
			// Check if it was a swap
			if(m.IsSwap && this.canSwap) {
				// Swap the cell
				board.swapCell(x, y, m.P);
				
				// A swap can no longer happen
				this.canSwap = false;
				
				// Return success
				return 0;
			}
			
			// Nope, invalid move
			return -1;
		}
		
		// User can no longer swap
		this.canSwap = false;
		
		// Fill this cell in
		board.fillCell(x, y, m.P);
		
		// Return Success
		return 0;
	}
	
	public int getWinner() {
		// Get the winner
		return this.board.getWinner();
	}

	@Override
	public void printBoard(PrintStream output) {
		// Print the board out
		this.board.print(output);
	}
}
