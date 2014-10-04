/**
 * 
 */
package pacman.entries.jcgrPacMan.BT;

/**
 * The basic node behaviour in the behaviour tree.
 * 
 * @author Jacob
 */
public abstract class Node
{
	/**
	 * The status of the node.
	 */
	protected STATUS status;
	
	/**
	 * The name of the node (mainly used for debugging)
	 */
	protected String name;

	/**
	 * Creates a new instance of the Node class with status
	 * set to INVALID and name set to "BasicNode".
	 */
	public Node()
	{
		this.status = STATUS.INVALID;
		this.name = "BasicNode";
	}

	/**
	 * Creates a new instance of the Node class with status
	 * set to INVALID and name set to the given name.
	 * @param name The name of the node.
	 */
	public Node(String name)
	{
		this.status = STATUS.INVALID;
		this.name = name;
	}
	
	/**
	 * Updates the node with the given context.
	 * @param context The context of the game.
	 * @return The status of the node after updating.
	 */
	public STATUS update(Context context)
	{
		if (this.status != STATUS.RUNNING)
			this.start(context);
		
		this.status = this.run(context);
		
		if (this.status != STATUS.RUNNING)
			this.finish(this.status, context);
		
		return this.status;
	}
	
	/**
	 * Logs the text to the console for debugging.
	 * @param text The text to log.
	 */
	public void Log(String text)
	{
		System.out.println(name + ": " + text);
	}
	
	/**
	 * Runs the node's logic.
	 * Override to implement actual logic for a node.
	 * 
	 * @param context The context of the game.
	 * @return The status of the node after running.
	 */
	public abstract STATUS run(Context context);
	
	/**
	 * Starts the node, setting variables to certain values 
	 * if necessary.
	 * @param context The context of the game.
	 */
	public abstract void start(Context context);

	/**
	 * Cleans up the node after running.
	 * @param status The status of the node.
	 * @param context The context of the game.
	 */
	public abstract void finish(STATUS status, Context context);
}
