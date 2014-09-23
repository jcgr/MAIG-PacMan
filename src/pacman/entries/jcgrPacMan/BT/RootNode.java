/**
 * 
 */
package pacman.entries.jcgrPacMan.BT;

/**
 * The root of the behaviour tree.
 * 
 * @author Jacob
 */
public class RootNode extends Node
{
	/**
	 * The object the root node is attached to.
	 */
	private Object entity;
	
	/**
	 * The context of the game.
	 */
	private Context context;
	
	/**
	 * The node that represents the root node.
	 */
	private Node node;
	
	/**
	 * Creates a new instance of the RootNode class with the given
	 * values.
	 * @param entity The object to attach the root node to.
	 * @param context The context of the game.
	 * @param node The node that should take care of logic.
	 */
	public RootNode(Object entity, Context context, Node node)
	{
		super("Root");
		this.entity = entity;
		this.node = node;
		this.context = context;
		this.context.entity = entity;
	}
	
	/**
	 * @see pacman.entries.jcgrPacMan.BT.Node#run(pacman.entries.jcgrPacMan.BT.Context)
	 */
	@Override
	public STATUS run(Context context)
	{
		return this.node.update(context);
	}

	/**
	 * @see pacman.entries.jcgrPacMan.BT.Node#start(pacman.entries.jcgrPacMan.BT.Context)
	 */
	@Override
	public void start(Context context)
	{
		this.context.entity = context.entity;
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
