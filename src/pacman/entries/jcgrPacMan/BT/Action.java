/**
 * 
 */
package pacman.entries.jcgrPacMan.BT;

/**
 * The basic version of an action, which is what the specific implementation
 * of nodes inherits from.
 * 
 * @author Jacob
 */
public abstract class Action extends Node
{
	public Action(String name)
	{
		super(name);
	}
}
