/**
 * 
 */
package pacman.entries.jcgrPacMan.behaviourtree.nodes;

import pacman.entries.jcgrPacMan.behaviourtree.Context;

/**
 * A node that iterates over all its children and runs them.
 * If succeeds if ALL children succeed, but fails if just one 
 * child fails.
 * 
 * @author Jacob
 */
public class Sequence extends ParentNode
{

	/**
	 * Creates a new instance of the Sequence class with the given context.
	 * @param context The context of the game.
	 */
	public Sequence(Context context)
	{
		super(context);
	}
	
	/**
	 * Creates a new instance of the Sequence class with the given context and name.
	 * @param context The context of the game.
	 * @param name The name of the node.
	 */
	public Sequence(Context context, String name)
	{
		super(context, name);
	}

	/**
	 * Succeeds if ALL child nodes succeed in order.
	 * @see pacman.entries.jcgrPacMan.behaviourtree.nodes.ParentNode#childSucceeded()
	 */
	@Override
	public void childSucceeded()
	{
		int currentPosition = controller.subNodes.indexOf(controller.currentNode);
//		if (controller.subNodes.elementAt(controller.subNodes.size() - 1).getController().succeeded())
		if (currentPosition == (controller.subNodes.size() - 1))
		{
			//System.out.println("Sequence succeeded");
			this.getController().finishWithSuccess();
		}
		else
		{
			controller.currentNode = controller.subNodes.elementAt(currentPosition + 1);
			if (!controller.currentNode.canUpdate())
			{
				this.getController().finishWithFailure();
			}
		}
	}

	/**
	 * Fails if any child node fails.
	 * @see pacman.entries.jcgrPacMan.behaviourtree.nodes.ParentNode#childFailed()
	 */
	@Override
	public void childFailed()
	{
		controller.finishWithFailure();
	}

}
