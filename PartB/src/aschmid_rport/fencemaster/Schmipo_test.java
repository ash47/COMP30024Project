package aschmid_rport.fencemaster;

import java.io.PrintStream;

import aiproj.fencemaster.Move;
import aiproj.fencemaster.Piece;
import aiproj.fencemaster.Player;

public class Schmipo_test implements Player, Piece {
	
	/** The ID of this player */
	int playerID;
	
	/** The turn we are up to */
	int turn;
	
	
	/** The board dimension */
	public int dim;
	
	/** The board we use */
	private Board board;
	
	/** Stores if a swap is valid or not */
	private boolean canSwap;
	
	public int init(int n, int p) {
		// Store dimension
		this.dim = n;

		// Store player
		this.playerID = p;
		
		// User can swap
		this.canSwap = false;
		
		// Create a new board
		this.board = new Board(this.dim);
		
		// Set turn to 0
		this.turn = 0;

		// Init was successful
		return 0;
	}
	
	public Move makeMove() {
		if(turn == 1)canSwap = true;
		else canSwap = false;
		boolean willSwap = false;
		boolean OK_move = false;
		int col = 0;
		int row = 0;
		while(OK_move == false){
			System.out.print("Enter row: ");
			row = StdIn.readInt();
			System.out.print("Enter column: ");
			col = StdIn.readInt();
			if(board.cellTaken(col, row) == false)OK_move = true;
			if(OK_move == false)System.out.println("Invalid move, try again.");
		}
		
		Move move = new Move(playerID, willSwap, row, col);
		board.fillCell(col, row, playerID);
		turn++;
		return move;
	}
	
	public int opponentMove(Move m) {
		if(turn == 1)canSwap = true;
		else canSwap = false;
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
				
				turn++;
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
		turn++;
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
