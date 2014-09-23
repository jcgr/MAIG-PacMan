/**
 * 
 */
package pacman.entries.jcgrPacMan.behaviourtree.nodes;

import pacman.entries.jcgrPacMan.behaviourtree.Context;

/**
 * 
 * 
 * @author Jacob
 */
public class ResetDecorator extends DecoratorNode
{
	/**
	 * @param context
	 * @param node
	 */
	public ResetDecorator(Context context, Node node)
	{
		super(context, node);
	}

	/**
	 * @param context
	 * @param node
	 * @param name
	 */
	public ResetDecorator(Context context, Node node, String name)
	{
		super(context, node, name);
	}

	/**
	 * 
	 * @see pacman.entries.jcgrPacMan.behaviourtree.nodes.Node#doAction()
	 */
	@Override
	public void doAction()
	{
		this.node.doAction();
		if (this.node.getController().finished())
		{
			this.node.getController().reset();
		}
	}

}
