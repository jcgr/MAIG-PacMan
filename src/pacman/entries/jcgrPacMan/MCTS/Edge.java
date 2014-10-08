/**
 * 
 */
package pacman.entries.jcgrPacMan.MCTS;

import pacman.game.Constants.MOVE;

/**
 * 
 * 
 * @author Jacob
 */
public class Edge
{
	public Node A;
	public Node B;
	
	public MOVE dirFromA;
	public MOVE dirFromB;
	
	public Edge(Node a, Node b, MOVE fromA, MOVE fromB)
	{
		this.A = a;
		this.B = b;
		this.dirFromA = fromA;
		this.dirFromB = fromB;
	}
}
