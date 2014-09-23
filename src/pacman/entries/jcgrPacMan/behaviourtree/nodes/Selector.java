/**
 * 
 */
package pacman.entries.jcgrPacMan.behaviourtree.nodes;

import pacman.entries.jcgrPacMan.behaviourtree.Context;

/**
 * A node that checks through its children until one
 * of the children succeeds or ALL the children fail.
 * 
 * @author Jacob
 */
public class Selector extends ParentNode
{

	/**
	 * Creates a new instance of the Selector class with the given context.
	 * @param context The context of the game.
	 */
	public Selector(Context context)
	{
		super(context);
	}

	/**
	 * Creates a new instance of the Selector class with the given context and name.
	 * @param context The context of the game.
	 * @param name The name of the node.
	 */
	public Selector(Context context, String name)
	{
		super(context, name);
	}

	/**
	 * Succeeds if just one child succeedes.
	 * @see pacman.entries.jcgrPacMan.behaviourtree.nodes.ParentNode#childSucceeded()
	 */
	@Override
	public void childSucceeded()
	{
		controller.finishWithSuccess();
	}

	/**
	 * Fails if no child succeedss.
	 * @see pacman.entries.jcgrPacMan.behaviourtree.nodes.ParentNode#childFailed()
	 */
	@Override
	public void childFailed()
	{
		controller.currentNode = chooseNewNode();
		// If there is 
		if (controller.currentNode == null)
		{
			controller.finishWithFailure();
		}
	}
	
	/**
	 * Chooses the next node for the selector to consider.
	 * @return
	 */
	private Node chooseNewNode()
	{
		Node node = null;
		boolean foundNextNode = false;
		int currentPosition = controller.subNodes.indexOf(controller.currentNode);
		
		// Find the next node that can be updated.
		while(!foundNextNode)
		{
			// If we have reached the end...
			if (currentPosition == (controller.subNodes.size() - 1))
			{
				// ... there is no node to update.
				node = null;
				break;
			}
			
			// Increment the position by one
			currentPosition++;
			node = controller.subNodes.elementAt(currentPosition);
			if (node.canUpdate())
			{
				foundNextNode = true;
			}
		}
		
		return node;
	}

}
