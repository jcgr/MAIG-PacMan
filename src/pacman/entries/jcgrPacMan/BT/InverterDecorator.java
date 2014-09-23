/**
 * 
 */
package pacman.entries.jcgrPacMan.BT;

/**
 * a decorator that inverts the result of a node 
 * (success -> failure or failure -> success).
 * 
 * @author Jacob
 */
public class InverterDecorator extends Decorator
{

	/**
	 * Creates a new instance of the Inverter class with the
	 * default name "Inverter".
	 * @param node The node to invert.
	 */
	public InverterDecorator(Node node)
	{
		super("Inverter", node);
	}

	/**
	 * Creates a new instance of the Inverter class.
	 * @param name The name of the inverter.
	 * @param node The node to invert.
	 */
	public InverterDecorator(String name, Node node)
	{
		super(name, node);
	}

	/**
	 * @param context
	 * @return
	 * @see pacman.entries.jcgrPacMan.BT.Node#run(pacman.entries.jcgrPacMan.BT.Context)
	 */
	@Override
	public STATUS run(Context context)
	{
		STATUS tempStatus = this.node.update(context);
		if (tempStatus == STATUS.RUNNING) return STATUS.RUNNING;
		else if (tempStatus == STATUS.SUCCESS) return STATUS.FAILURE;
		else if (tempStatus == STATUS.FAILURE) return STATUS.SUCCESS;
		else return tempStatus;
	}

	/**
	 * @see pacman.entries.jcgrPacMan.BT.Node#start(pacman.entries.jcgrPacMan.BT.Context)
	 */
	@Override
	public void start(Context context)
	{
		// TODO Auto-generated method stub
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
