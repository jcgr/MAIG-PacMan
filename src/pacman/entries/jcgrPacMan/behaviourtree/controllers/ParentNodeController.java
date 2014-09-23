/**
 * 
 */
package pacman.entries.jcgrPacMan.behaviourtree.controllers;

import java.util.Vector;

import pacman.entries.jcgrPacMan.behaviourtree.nodes.Node;

/**
 * A class that is in charge of any ParentNode that is created.
 * 
 * @author Jacob
 */
public class ParentNodeController extends NodeController
{
	/**
	 * A list of the controller's subnodes.
	 */
	public Vector<Node> subNodes;
	
	/**
	 * The node that is currently being processed.
	 */
	public Node currentNode;
	
	/**
	 * Creates a new instance of the ParentNodeController class with the given node.
	 * @param node The node the controller should be in charge of.
	 */
	public ParentNodeController(Node node)
	{
		super(node);
		
		this.subNodes = new Vector<Node>();
		this.currentNode = null;
	}
	
	/**
	 * Adds a node to the controller.
	 * @param node
	 */
	public void addNode(Node node)
	{
		subNodes.add(node);
	}
	
	/**
	 *  
	 * @see pacman.entries.jcgrPacMan.behaviourtree.controllers.NodeController#reset()
	 */
	@Override
	public void reset()
	{
		super.reset();
		this.currentNode = subNodes.firstElement();
	}
}
