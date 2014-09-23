/**
 * 
 */
package pacman.entries.jcgrPacMan.behaviourtree.nodes;

import pacman.entries.jcgrPacMan.behaviourtree.Context;
import pacman.entries.jcgrPacMan.behaviourtree.controllers.NodeController;
import pacman.entries.jcgrPacMan.behaviourtree.controllers.ParentNodeController;

/**
 * A node that can hold child nodes.
 * 
 * @author Jacob
 */
public abstract class ParentNode extends Node
{
	/**
	 * The controller in charge of the node.
	 */
	protected ParentNodeController controller;

	/**
	 * Creates a new instance of the ParentNode class with the given context. Default name is "ParentNode".
	 * @param context The context of the game.
	 */
	public ParentNode(Context context)
	{
		super(context);
		this.name = "ParentNode";
		this.createController();
	}
	
	/**
	 * Creates a new instance of the ParentNode class with the given context and name.
	 * @param context The context of the game.
	 * @param name The name of the ParentNode.
	 */
	public ParentNode(Context context, String name)
	{
		super(context, name);
		this.createController();
	}

	/**
	 * 
	 * @see pacman.entries.jcgrPacMan.behaviourtree.nodes.Node#start()
	 */
	@Override
	public void start()
	{
		LogTask(name + " is starting");
		controller.currentNode = controller.subNodes.firstElement();
		if (controller.currentNode == null) { 
			LogTask("ERROR! currentNode is null!");			
		}
	}

	/**
	 * @see pacman.entries.jcgrPacMan.behaviourtree.nodes.Node#canUpdate()
	 */
	@Override
	public boolean canUpdate()
	{
		LogTask(name + " is checking conditions!");
		return controller.subNodes.size() > 0;
	}

	/**
	 * @see pacman.entries.jcgrPacMan.behaviourtree.nodes.Node#doAction()
	 */
	@Override
	public void doAction()
	{
//		LogTask(name + " is updating");
		
		// If the controller is finished, no point in doing anything.
		if (controller.finished()) { return; }
		// If the currentNode is null, an error has occoured.
		if (controller.currentNode == null)
		{
			LogTask("ERROR! currentNode is null");
			return;
		}
		
		Node tempNode = controller.currentNode;
		NodeController tempNodeController = tempNode.getController();

		// If currentNode exists...
		if (!tempNodeController.started())
		{
			// ... and is not stated, start it.
			tempNodeController.safeStart();
		}
		else if(tempNodeController.finished())
		{
			// ... and it is finished, end it properly.
			if (tempNodeController.succeeded()) { this.childSucceeded(); }
			if (tempNodeController.failed()) { this.childFailed(); } 
			
			tempNodeController.safeEnd();
		}
		else
		{
			// ... and it is ready to update, update it.
			tempNode.doAction();
		}
	}

	/**
	 * 
	 * @see pacman.entries.jcgrPacMan.behaviourtree.nodes.Node#end()
	 */
	@Override
	public void end()
	{
		LogTask(name + " is ending");
	}

	/**
	 * @see pacman.entries.jcgrPacMan.behaviourtree.nodes.Node#getController()
	 */
	@Override
	public NodeController getController()
	{
		return controller;
	}

	private void createController()
	{
		this.controller = new ParentNodeController(this);
	}
	
	/**
	 * The stuff that happens when a child node finishes successfully.
	 */
	public abstract void childSucceeded();
	
	/**
	 * The stuff that happens when a child node finishes with failure.
	 */
	public abstract void childFailed();
}
