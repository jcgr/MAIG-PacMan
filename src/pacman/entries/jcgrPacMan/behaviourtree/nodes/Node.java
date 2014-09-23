/**
 * 
 */
package pacman.entries.jcgrPacMan.behaviourtree.nodes;

import pacman.entries.jcgrPacMan.behaviourtree.Context;
import pacman.entries.jcgrPacMan.behaviourtree.controllers.NodeController;

/**
 * The basic node behaviour.
 * 
 * @author Jacob
 */
public abstract class Node
{
	protected Context context;
	
	protected String name;
	
	/**
	 * Creates a new instance of the Node class and saves the context it is working with.
	 * @param context The context of the game.
	 */
	public Node(Context context)
	{
		this.context = context;
	}
	
	/**
	 * Creates a new instance of the Node class and saves the context and name of the node.
	 * @param context The context of the game.
	 * @param name The name of the node.
	 */
	public Node(Context context, String name)
	{
		this(context);
		this.name = name;
	}
	
	/**
	 * Logs information.
	 * @param text The text to log.
	 */
    public void LogTask(String text)
    {
//    	System.out.println(name + ": " + text);
//    	Log.getLog().log(context, "Node: " + name + "; " + text);
    }

	/**
	 * Starts the node.
	 */
	public abstract void start();

	/**
	 * Checks if the node can be updated based on certain parameters.
	 * @return True if the node can be updated; false otherwise
	 */
	public abstract boolean canUpdate();
	
	/**
	 * Tells the node to do something.
	 */
	public abstract void doAction();
	
	/**
	 * Cleans up the node when it is done running.
	 */
	public abstract void end();

	/**
	 * Gets the NodeController in control of the node.
	 * @return The controller in charge of the node.
	 */
    public abstract NodeController getController();
}
