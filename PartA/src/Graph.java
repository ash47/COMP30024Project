
public class Graph {
	// A cell in this graph
	public Cell start;
	
	// Number of cells in this  graph
	int totalCells;
	
	// The player this graph belongs to
	int player;
	
	public Graph(int player) {
		this.totalCells = 0;
		this.player = player;
	}
}
