/**
 * 
 */
package pacman.entries.jcgrPacMan.behaviourtreePacMan.nodes;

import pacman.entries.jcgrPacMan.behaviourtree.Context;
import pacman.entries.jcgrPacMan.behaviourtree.nodes.LeafNode;
import pacman.entries.jcgrPacMan.behaviourtreePacMan.PacManContext;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;

/**
 * 
 * 
 * @author Jacob
 */
public class DecideNextMoveNode extends LeafNode
{

	/**
	 * @param context
	 * @param name
	 */
	public DecideNextMoveNode(Context context, String name)
	{
		super(context, name);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @see pacman.entries.jcgrPacMan.behaviourtree.nodes.Node#start()
	 */
	@Override
	public void start()
	{
		LogTask("Starting");
	}

	/**
	 * @return
	 * @see pacman.entries.jcgrPacMan.behaviourtree.nodes.Node#canUpdate()
	 */
	@Override
	public boolean canUpdate()
	{
		return ((PacManContext)this.context).targetNodeIndices.length > 0;
	}

	/**
	 * 
	 * @see pacman.entries.jcgrPacMan.behaviourtree.nodes.Node#doAction()
	 */
	@Override
	public void doAction()
	{
		PacManContext tempContext = (PacManContext)this.context;
		
		tempContext.currentTarget = tempContext.game.getClosestNodeIndexFromNodeIndex(tempContext.currentNodeIndex, tempContext.targetNodeIndices, DM.PATH);
		MOVE nextMove = tempContext.game.getNextMoveTowardsTarget(tempContext.currentNodeIndex, tempContext.currentTarget, DM.PATH);
		
		tempContext.currentMove = nextMove;
		
		this.controller.finishWithSuccess();
	}

	/**
	 * 
	 * @see pacman.entries.jcgrPacMan.behaviourtree.nodes.Node#end()
	 */
	@Override
	public void end()
	{
		LogTask("Ending");
	}

}
