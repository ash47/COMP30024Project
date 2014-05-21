package aschmid_rport.fencemaster;

import java.io.PrintStream;

import aiproj.fencemaster.* ;

public class schmipo implements Player, Piece {
	
	/** The ID of this player */
	int playerID;
	
	
	/** The board dimension */
	public int dim;
	
	public int init(int n, int p) {
		return 0;
	}
	
	public Move makeMove() {
		return new Move(1, false, 1, 1);
	}
	
	public int opponentMove(Move m) {
		return 0;
	}
	
	public int getWinner() {
		return 0;
	}

	@Override
	public void printBoard(PrintStream output) {
		output.print("hello world!");
		// TODO Auto-generated method stub
		
	}
}
