/**
 * 
 */
package pacman.entries.jcgrPacMan.behaviourtree.nodes;

import pacman.entries.jcgrPacMan.behaviourtree.Context;
import pacman.entries.jcgrPacMan.behaviourtree.controllers.NodeController;

/**
 * A node that can decorate another node, ie. change the behaviour of the other node.
 * 
 * @author Jacob
 */
public abstract class DecoratorNode extends Node
{
	Node node;
	
	/**
	 * Creates a new instance of the Decorator class with the given context and node.
	 * @param context The context of the game.
	 * @param node The node to decorate.
	 */
	public DecoratorNode(Context context, Node node)
	{
		super(context);
		this.name = "DecoratorNode";
		this.InitializeNode(node);
	}

	/**
	 * Creates a new instance of the Decorator class with the given context, node and name.
	 * @param context The context of the game.
	 * @param node The node to decorate.
	 * @param name The name of the decorator.
	 */
	public DecoratorNode(Context context, Node node, String name)
	{
		super(context, name);
		this.InitializeNode(node);
	}

	/**
	 * Initializes the decorator by attaching it to the given node.
	 * @param node The node to attach the decorator to.
	 */
	private void InitializeNode(Node node)
	{
		this.node = node;
		this.node.getController().setNode(this);
	}
	
	/**
	 * Starts the node the decorator is attached to.
	 * @see pacman.entries.jcgrPacMan.behaviourtree.nodes.Node#start()
	 */
	@Override
	public void start()
	{
		this.node.start();
	}
	
	/**
	 * Checks if the node the decorator is attached to can update.
	 * @return True if the node can be updated; false otherwise.
	 * @see pacman.entries.jcgrPacMan.behaviourtree.nodes.Node#canUpdate()
	 */
	@Override
	public boolean canUpdate()
	{
		return this.node.canUpdate();
	}
	
	/**
	 * Ends the node the decorator is attached to.
	 * 
	 * @see pacman.entries.jcgrPacMan.behaviourtree.nodes.Node#end()
	 */
	@Override
	public void end()
	{
		this.node.end();
	}
	
	/**
	 * Gets the controller that is in charge of the node the decorator is attached to.
	 * @return The controller of the node.
	 * @see pacman.entries.jcgrPacMan.behaviourtree.nodes.Node#getController()
	 */
	@Override
	public NodeController getController()
	{
		return this.node.getController();
	}
}
