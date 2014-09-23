/**
 * 
 */
package pacman.entries.jcgrPacMan.behaviourtree.nodes;

import pacman.entries.jcgrPacMan.behaviourtree.Context;
import pacman.entries.jcgrPacMan.behaviourtree.controllers.NodeController;

/**
 * A leaf node in the behaviour tree.
 * 
 * @author Jacob
 */
public abstract class LeafNode extends Node
{
	protected NodeController controller;
	
	/**
	 * Creates a new instance of the LeafNode class with the given context. Default name is "LeafNode".
	 * @param context The context of the game.
	 */
	public LeafNode(Context context)
	{
		super(context);
		this.name = "LeafNode";
		this.createController();
	}
	
	/**
	 * Creates a new instance of the LeafNode class with the given context and name.
	 * @param context The context of the game.
	 * @param name The name of the LeafNode.
	 */
	public LeafNode(Context context, String name)
	{
		super(context, name);
		this.createController();
	}

	/**
	 * Initializes the node controller.
	 */
	private void createController()
	{
		this.controller = new NodeController(this);
	}
	
	/**
	 * @see pacman.entries.jcgrPacMan.behaviourtree.nodes.Node#getController()
	 */
	@Override
	public NodeController getController()
	{
		return this.controller;
	}
}
