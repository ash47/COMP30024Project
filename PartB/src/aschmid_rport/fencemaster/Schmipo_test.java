package aschmid_rport.fencemaster;

import java.io.PrintStream;
import java.util.Scanner;

import aiproj.fencemaster.Move;
import aiproj.fencemaster.Piece;
import aiproj.fencemaster.Player;

/**
 * This is a test client where you can manually enter moves
 * @author aschmid (584770), rport (586116)
 *
 */
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
	
	/** The scanner to read from */
	private Scanner sc;
	
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
		
		// Create a scanner to read input
		this.sc = new Scanner(System.in);

		// Init was successful
		return 0;
	}
	
	public Move makeMove() {
		// Check if swapping is a valid move or not
		if(turn == 1) {
			canSwap = true;
		} else {
			canSwap = false;
		}
		
		String line;
		
		// Keep asking for a move until we get a valid one
		boolean OK_move = false;
		int col = 0;
		int row = 0;
		while(OK_move == false) {
			// Get a row
			System.out.println("Enter row: ");
			line = this.sc.nextLine();
			// Ensure they parsed a number
			try {
				row = Integer.parseInt(line);
			} catch(NumberFormatException e) {
				// Print error
				System.out.println("You need to enter a number! You entered: "+line);
				
				// Try Again
				continue;
			}
			
			// Get a column
			System.out.println("Enter column: ");
			line = this.sc.nextLine();
			// Ensure they parsed a number
			try {
				col = Integer.parseInt(line);
			} catch(NumberFormatException e) {
				// Print error
				System.out.println("You need to enter a number! You entered: "+line);
				
				// Try Again
				continue;
			}
			
			// Check if the cell is valid and is free
			if(board.isValidCell(col, row)) {
				if(!board.cellTaken(col, row)) {
					// All good, lets move on
					OK_move = true;
				} else {
					if(canSwap) {
						// Do the swap
						board.swapCell(col, row, playerID);
						turn++;
						return new Move(playerID, true, row, col);
					} else {
						// Cell already taken :(
						System.out.println("That cell has already been taken!");
					}
				}
			} else {
				// Invalid move, tell the user
				System.out.println("Invalid move, try again.");
			}
		}
		
		// Fill the board
		board.fillCell(col, row, playerID);
		turn++;
		
		// Finally, return the move
		return new Move(playerID, false, row, col);
	}
	
	public int opponentMove(Move m) {
		// Check if swapping is valid
		if(turn == 1) {
			canSwap = true;
		} else {
			canSwap = false;
		}
		
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
				
				// Increase the total number of turns so far
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
