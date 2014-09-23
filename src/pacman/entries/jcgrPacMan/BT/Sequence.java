/**
 * 
 */
package pacman.entries.jcgrPacMan.BT;

import java.util.ArrayList;

/**
 * A sequence of nodes that must all succeed if the Sequence is
 * to return a success.
 * 
 * @author Jacob
 */
public class Sequence extends Node
{
	/**
	 * A list of the subnodes of the sequence.
	 */
	private ArrayList<Node> subNodes;
	
	/**
	 * The current node being processed.
	 */
	private int currentNode;
	
	/**
	 * Creates a new instance of the Sequence class.
	 * @param name The name of the sequence node.
	 */
	public Sequence(String name)
	{
		super(name);
		this.subNodes = new ArrayList<Node>();
		this.currentNode = 0;
	}
	
	/**
	 * Creates a new instance of the Sequence class.
	 * @param name The name of the sequence node.
	 * @param subNodes A list of subnodes for the sequence.
	 */
	public Sequence(String name, ArrayList<Node> subNodes)
	{
		super(name);
		this.currentNode = 0;
		this.subNodes = new ArrayList<Node>();
		this.subNodes.addAll(subNodes);
	}
	
	/**
	 * Adds a node to the end of the list of nodes 
	 * the sequence is in charge of.
	 * @param node
	 */
	public void addNode(Node node)
	{
		subNodes.add(node);
	}

	/**
	 * Iterates over all subnodes and returns success if
	 * all subnodes succeed. If not, it will return the 
	 * status of the subnode that did not succeed (can be
	 * "running", in which case the sequence will continue
	 * from that node next iteration).
	 * @return SUCCESS if all subnodes succeed; otherwise returns the 
	 * 		   status of the first subnode encountered that did not
	 *         succeed.
	 * @see pacman.entries.jcgrPacMan.BT.Node#run(pacman.entries.jcgrPacMan.BT.Context)
	 */
	@Override
	public STATUS run(Context context)
	{
		while (true)
		{
			STATUS tempStatus = this.subNodes.get(currentNode).update(context);
			if (tempStatus != STATUS.SUCCESS) 
				return tempStatus;
			
			currentNode++;
			if (currentNode == subNodes.size())
				return STATUS.SUCCESS;
		}
	}

	/**
	 * Sets the currentNode value to 0.
	 * @see pacman.entries.jcgrPacMan.BT.Node#start(pacman.entries.jcgrPacMan.BT.Context)
	 */
	@Override
	public void start(Context context)
	{
		this.currentNode = 0;
	}

	/**
	 * @see pacman.entries.jcgrPacMan.BT.Node#finish(pacman.entries.jcgrPacMan.BT.STATUS, pacman.entries.jcgrPacMan.BT.Context)
	 */
	@Override
	public void finish(STATUS status, Context context)
	{
		// TODO Auto-generated method stub
	}

}
