/**
 * 
 */
package pacman.entries.jcgrPacMan.BT;

/**
 * A decorator that repeats its child node until 
 * the child node fails.
 * 
 * @author Jacob
 */
public class RepeatUntilFailDecorator extends Decorator
{

	/**
	 * Creates a new instance of the RepeatUntilFail class
	 * with the default name "RepeatUntilFail".
	 * @param node The node to repeat.
	 */
	public RepeatUntilFailDecorator(Node node)
	{
		super("RepeatUntilFail", node);
	}
	
	/**
	 * Creates a new instance of the RepeatUntilFail class.
	 * @param name The name of the repeatuntilfail node.
	 * @param node The node to repeat.
	 */
	public RepeatUntilFailDecorator(String name, Node node)
	{
		super(name, node);
	}

	/**
	 * Runs the child node until it fails.
	 * @return SUCCESS if the child node fails; RUNNING otherwise.
	 * @see pacman.entries.jcgrPacMan.BT.Node#run(pacman.entries.jcgrPacMan.BT.Context)
	 */
	@Override
	public STATUS run(Context context)
	{
		STATUS tempStatus = node.update(context);
		if (tempStatus != STATUS.FAILURE) 
			return STATUS.RUNNING;
		
		return STATUS.SUCCESS;
	}

	/**
	 * @see pacman.entries.jcgrPacMan.BT.Node#start(pacman.entries.jcgrPacMan.BT.Context)
	 */
	@Override
	public void start(Context context)
	{
		// Do nothing
	}

	/**
	 * @see pacman.entries.jcgrPacMan.BT.Node#finish(pacman.entries.jcgrPacMan.BT.STATUS, pacman.entries.jcgrPacMan.BT.Context)
	 */
	@Override
	public void finish(STATUS status, Context context)
	{
		// Do nothing
	}

}
