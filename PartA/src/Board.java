
public class Board {
	// These are the cells of the board
	public Cell[][] cells;
	
	// The size of the board
	public int size;
	
	public Board(int size) {
		// Allocate memory for the cells
		cells = new Cell[size][size];
		
		// Fill our board up
		for(int i=0;i<size;i++) {
			for(int j=0;j<size;j++) {
				cells[i][j] = new Cell(0);
			}
		}
	}
	
	// Marks each cell as either redundent or not
	public void markRed() {
		// Mark stuff
	}
	
	// Build graphs
	public void buildGraphs() {
		// Mark stuff as redunedent
		markRed();
		
		// Build graphs
		for(int x=0;x<this.size;x++) {
			for(int y=0;y<this.size;y++) {
				Cell cell = getCell(x, y);
				
				// Check if there is a player in this cell
				if(cell.player != 0) {
					if(cell != null) {
						Cell[] adj = getAdj(x, y);
						
						for(int i=0;i<6;i++) {
							Cell adjCell = adj[i];
							
							// Check if they are the same player
							if(adjCell.player == cell.player) {
								if(!cell.red) {
									if(!adjCell.red && adjCell.loopGraph != null) {
										cell.loopGraph = adjCell.loopGraph;
									} else {
										cell.loopGraph = new LoopGraph(cell.player);
									}
								}
								
								if(adjCell.tripodGraph != null) {
									cell.tripodGraph = adjCell.tripodGraph;
								} else {
									cell.tripodGraph = new TripodGraph(cell.player);
								}
							}
						}
					}
				
				}
				
			}
		}
	}
	
	// Checks if a cell is valid
	public boolean isValidCell(int x, int y) {
		return true;
	}
	
	// Returns a cell
	public Cell getCell(int x, int y) {
		// Check if the cell is valid
		if(!isValidCell(x, y)) {
			return null;
		}
		
		return cells[x][y];
	}
	
	// Sets a player into a cell
	public void setCell(int x, int y, int player) {
		Cell cell = getCell(x, y);
		
		if(cell != null) {
			cell.setPlayer(player);
		}
	}
	
	// Returns the side a cell is on (0 if no side)
	public int getSide(int x, int y) {
		return 0;
	}
	
	public Cell[] getAdj(int x, int y) {
		// Create list for cells
		Cell[] list = new Cell[6];
		
		// Build list
		list[0] = getCell(x-1, x+1);	// Fill these in
		
		return list;
	}
}
