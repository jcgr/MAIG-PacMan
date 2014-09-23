/**
 * 
 */
package pacman.entries.jcgrPacMan.BT;

/**
 * A node that change the result of the node it decorates.
 * 
 * @author Jacob
 */
public abstract class Decorator extends Node
{
	/**
	 * The node to decorate.
	 */
	protected Node node;
	
	/**
	 * Creates a new instanec of the Decorator class.
	 * @param name The name of the decorator.
	 * @param node The node to decorate.
	 */
	public Decorator(String name, Node node)
	{
		super(name);
		this.node = node;
	}
}
