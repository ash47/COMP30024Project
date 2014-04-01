import java.util.Scanner;


/**
 * The main entry point for our program
 * @author aschmid
 *
 */
public class Main {
	/** The minimal size of a board */
	public static final int MIN_BOARD_SIZE = 1;
	
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
			System.out.print("Size must be at least "+MIN_BOARD_SIZE);
			
			// Exit
			return;
		}
		
		// Create new board
		Board board = new Board(size);
		
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
				System.out.print("Invalid input, line "+(y+2)+" is the wrong length (Got "+line.length()+", expected "+rowSize+")");
				
				// Exit
				return;
			}
			
			for(int i=0; i<rowSize; i++) {
				// Get the id of who is in this cell
				int playerID = Board.getPlayerID(line.charAt(i));
				
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
	}
}
