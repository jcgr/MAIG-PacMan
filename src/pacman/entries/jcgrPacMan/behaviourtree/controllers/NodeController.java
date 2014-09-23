/**
 * 
 */
package pacman.entries.jcgrPacMan.behaviourtree.controllers;

import pacman.entries.jcgrPacMan.behaviourtree.Status;
import pacman.entries.jcgrPacMan.behaviourtree.nodes.Node;

/**
 * A class that is added to any Node that is created, and which controls the
 * state of the node.
 * 
 * @author Jacob
 */
public class NodeController
{
	/**
	 * The node the controller is in charge of.
	 */
	private Node node;
	
	/**
	 * The status of the node.
	 */
	protected Status status;
	
	protected boolean running;
	
	/**
	 * A value indicating whether the node is done or not.
	 */
	protected boolean done;
	
	/**
	 * Creates a new instance of the NodeController and sets the status to "Waiting".
	 * @param node The node the controller should be in charge of.
	 */
	public NodeController(Node node)
	{
		this.setNode(node);
		this.status = Status.WAITING;
		this.running = false;
		this.done = false;
	}
	
	/**
	 * Sets the node of the controller to the given node.
	 * @param node The node to be in control of.
	 */
	public void setNode(Node node)
	{
		this.node = node;
	}
	
	/**
	 * Changes the state of the controller to running and starts the node.
	 */
	public void safeStart()
	{
		this.status = Status.RUNNING;
		this.running = true;
//		this.done = false;
		this.node.start();
	}
	
	/**
	 * Changes the state of the node to waiting and ends the node.
	 */
	public void safeEnd()
	{
		this.status = Status.WAITING;
		this.running = false;
		this.done = false;
		this.node.end();
	}
	
	/**
	 * Changes the state of the node to succeesful.
	 */
	public void finishWithSuccess()
	{
		this.status = Status.SUCCESS;
		this.done = true;
        this.node.LogTask("Finished with success");
	}
	
	/**
	 * Changes the state of the node to failed.
	 */
	public void finishWithFailure()
	{
		this.status = Status.FAILED;
		this.done = true;
        this.node.LogTask("Finished with failure");
	}
	
	/**
	 * Checks if the node succeeded.
	 * @return True if the node's status is succeeded; false otherwise.
	 */
	public boolean succeeded()
	{
		return (this.status == Status.SUCCESS);
	}
	
	/**
	 * Checks if the node failed.
	 * @return True if the node's status is failed; false otherwise.
	 */
	public boolean failed()
	{
		return (this.status == Status.FAILED);
	}
	
	/**
	 * Checks if the node is finished.
	 * @return True if the node's status is has finished; False otherwise.
	 */
	public boolean finished()
	{
		return (this.done);
	}
	
	/**
	 * Checks if the node is started.
	 * @return True is the node's status is is started; false otherwise.
	 */
	public boolean started()
	{
		return (this.running);
	}
	
	/**
	 * Resets the status of the controller.
	 */
	public void reset()
	{
		this.done = false;
	}
}
