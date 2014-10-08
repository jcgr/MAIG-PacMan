/**
 * 
 */
package pacman.entries.jcgrPacMan.MCTS;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * @author Jacob
 */
public class Node
{
	List<Edge> edges;
	
	public Node()
	{
		edges = new ArrayList<Edge>();
	}
	
	public void addEdge(Edge e)
	{
		edges.add(e);
	}
}
