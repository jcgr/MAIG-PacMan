/**
 * 
 */
package pacman.entries.jcgrPacMan.BT;

import java.util.ArrayList;

/**
 * 
 * 
 * @author Jacob
 */
public class Selector extends Node
{

	/**
	 * A list of the subnodes of the selector.
	 */
	private ArrayList<Node> subNodes;
	
	/**
	 * The current node being processed.
	 */
	private int currentNode;
	
	/**
	 * Creates a new instance of the Selector class.
	 * @param name The name of the selector node.
	 */
	public Selector(String name)
	{
		super(name);
		this.subNodes = new ArrayList<Node>();
		this.currentNode = 0;
	}
	
	/**
	 * Creates a new instance of the Selector class.
	 * @param name The name of the selector node.
	 * @param subNodes A list of subnodes for the selector.
	 */
	public Selector(String name, ArrayList<Node> subNodes)
	{
		super(name);
		this.currentNode = 0;
		this.subNodes = new ArrayList<Node>();
		this.subNodes.addAll(subNodes);
	}
	
	/**
	 * Adds a node to the end of the list of nodes 
	 * the selector is in charge of.
	 * @param node
	 */
	public void addNode(Node node)
	{
		subNodes.add(node);
	}
	
	/**
	 * Iterates over all subnodes and returns SUCCESS if any
	 * subnode succeeds.
	 * @return SUCCESS if any subnode succeeds; FAILURE if 
	 *         all subnodes fail; otherwise returns the status
	 *         of the subnode that is currently being processed.
	 * @see pacman.entries.jcgrPacMan.BT.Node#run(pacman.entries.jcgrPacMan.BT.Context)
	 */
	@Override
	public STATUS run(Context context)
	{
		while (true)
		{
			STATUS tempStatus = subNodes.get(currentNode).update(context);
			if (tempStatus != STATUS.FAILURE)
				return tempStatus;
			
			currentNode++;
			if (currentNode == subNodes.size())
				return STATUS.FAILURE;
		}
	}

	/**
	 * @param context
	 * @see pacman.entries.jcgrPacMan.BT.Node#start(pacman.entries.jcgrPacMan.BT.Context)
	 */
	@Override
	public void start(Context context)
	{
		currentNode = 0;
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
