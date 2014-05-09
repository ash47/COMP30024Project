package aschmid_rport.fencemaster;

import java.io.PrintStream;

import aiproj.fencemaster.* ;

public class aschmid implements Player, Piece {
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
		// TODO Auto-generated method stub
		
	}
}
