import java.util.Scanner;


/**
 * The main entry point for our program
 * @author aschmid
 *
 */
public class Main {
	/** The minimal size of a board */
	public static final int MIN_BOARD_SIZE = 1;
	
	/** The message to output when a draw happens */
	public static final String MESSAGE_DRAW = "Draw";
	
	/** The message to output when there isn't a winning state */
	public static final String MESSAGE_NONE = "None";
	
	/** The message to output when a black wins */
	public static final String MESSAGE_BLACK_WINS = "Black";
	
	/** The message to output when a white wins */
	public static final String MESSAGE_WHITE_WINS = "White";
	
	/** The message to output when a loop was used to win */
	public static final String MESSAGE_LOOP_WINS = "Loop";
	
	/** The message to output when a tripod wins */
	public static final String MESSAGE_TRIPOD_WINS = "Tripod";
	
	/** The message to output when a tripod and a loop wins */
	public static final String MESSAGE_BOTH_WINS = "Both";
	
	/**
	 * The main entry point into our program
	 * @param args The argument list parsed into the program
	 */
	public static void main(String [] args) {
		// Read in data
		Scanner sc = new Scanner(System.in);
		
		// Find the size of the board
		String line = sc.nextLine();
		int size = Integer.parseInt(line);
		
		// Validate the size
		if(size < MIN_BOARD_SIZE) {
			// Close scanner
			sc.close();
			
			// Print error here
			System.out.println("Size must be at least "+MIN_BOARD_SIZE);
			
			// Exit
			return;
		}
		
		// Create new board
		Board board = new Board(size);
		
		// A flag to detect if every square is taken
		boolean allTaken = true;
		
		// Read input
		for(int y=0; y<size; y++) {
			// Get info for this row
			int x = board.getFirstX(y);
			int rowSize = board.getRowSize(y);
			
			// Read the next line
			line = sc.nextLine();
			
			// Valid is the this line
			if(line.length() != rowSize) {
				// Close scanner
				sc.close();
				
				// Print error here
				System.out.println("Invalid input, line "+(y+2)+" is the wrong length (Got "+line.length()+", expected "+rowSize+")");
				
				// Exit
				return;
			}
			
			for(int i=0; i<rowSize; i++) {
				// Get the id of who is in this cell
				int playerID = Board.getPlayerID(line.charAt(i));
				
				// Validate input
				if(playerID == Board.PLAYER_UNKNOWN) {
					// Close scanner
					sc.close();
					
					// Print error here
					System.out.println("Unknown player token on line "+(y+2)+" (Got "+line.charAt(i)+")");
					
					// Exit
					return;
				}else if(playerID == Board.PLAYER_NONE) {
					// There is at least one empty square
					allTaken = false;
				}
				
				// Store it
				board.setCell(x+i, y, playerID);
			}
		}
		
		// Close the scanner
		sc.close();
		
		// Check if every square is taken (mark flag)
		
		// Fill cells in
		
		// Build the graphs'
		board.buildGraphs();
		
		// Check for the win
		
		// Check tripods, simply look at the edgeCount >= 3
		
		// Check for loops in the loop graph
		
		// Output if someone won or not
		
		// If there is no clear winner and everything is taken
		if(allTaken) {
			// Must be a draw
			System.out.println(MESSAGE_DRAW);
		} else {
			// No winner, and game still has spots left
			System.out.println(MESSAGE_NONE);
		}
	}
}
