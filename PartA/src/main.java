import java.util.Scanner;


/**
 * The main entry point for our program
 * @author aschmid, rport
 *
 */
public class Main {
	/** The minimal size of a board */
	public static final int MIN_BOARD_SIZE = 1;
	
	/** The message to output when a draw happens */
	public static final String MESSAGE_DRAW = "Draw";
	
	/** The other message to output when a draw happens */
	public static final String MESSAGE_DRAW_STATE = "Nil";
	
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
	
	/** Are we in debug mode? */
	public static final boolean debug = false;

	/**
	 * The main entry point into our program
	 * @param args The argument list parsed into the program
	 */
	public static void main(String [] args) {
		// Read in data
		Scanner sc = new Scanner(System.in);
		
		// Find the size of the board
		String line = sc.nextLine();
		int size;
		
		// Ensure they parsed a number
		try {
			size = Integer.parseInt(line);
		} catch(NumberFormatException e) {
			// Close scanner
			sc.close();
			
			// Print error
			System.out.println("The first line needs to be a number, you entered: "+line);
			
			// Exit
			return;
		}
		
		
		// Validate the size
		if(size < MIN_BOARD_SIZE) {
			// Close scanner
			sc.close();
			
			// Print error
			System.out.println("Size must be at least "+MIN_BOARD_SIZE);
			
			// Exit
			return;
		}
		
		// Create new board
		Board board = new Board(size);
		
		// A flag to detect if every square is taken
		boolean allTaken = true;
		
		// Read input
		for(int y=0; y< (2*size - 1); y++) {
			// Get info for this row
			int x = board.getFirstX(y);
			int rowSize = board.getRowSize(y);
			
			// Read the next line
			line = sc.nextLine();
			//Trims all the whitespace from the input line
			line = line.replaceAll("\\s","");
			
			// Valid is the this line
			if(line.length() != rowSize) {
				// Close scanner
				sc.close();
				
				// Print error
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
					
					// Print error		
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
		
		// Check for a winner
		if(!board.checkForWinner()) {
			// If there is no clear winner and everything is taken
			if(allTaken) {
				// Must be a draw
				System.out.println(MESSAGE_DRAW);
			} else {
				// No winner, and game still has spots left
				System.out.println(MESSAGE_NONE);
			}
			
			//  Print nil state
			System.out.println(MESSAGE_DRAW_STATE);
		}
		
		// Check if debug mode is on
		if(isDebug()) {
			// Print boards
			System.out.println("Board as people see it:");
			board.print();
			System.out.println("Loops that exist:");
			board.printLoops();
			System.out.println("Tripod numbers:");
			board.printTripods();
			
			// Print huge warning that debug is on
			System.out.println("\nWARNING: DEBUG MODE IS ON! DO NOT SUBMIT!");
		}
	}
	
	/**
	 * Checks if we are in debug mode
	 * @return If debug mode is on or not
	 */
	public static boolean isDebug() {
		return debug;
	}
}
