/**
 * 
 */
package pacman.entries.jcgrPacMan.behaviourtreePacMan.nodes;

import pacman.entries.jcgrPacMan.behaviourtree.Context;
import pacman.entries.jcgrPacMan.behaviourtree.nodes.LeafNode;
import pacman.entries.jcgrPacMan.behaviourtreePacMan.PacManContext;
import pacman.game.Constants.DM;

/**
 * 
 * 
 * @author Jacob
 */
public class FindPathToNearestTargetNode extends LeafNode
{

	/**
	 * @param context
	 * @param name
	 */
	public FindPathToNearestTargetNode(PacManContext context, String name)
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
		// TODO Auto-generated method stub
		LogTask("Starting");
	}

	/**
	 * @return
	 * @see pacman.entries.jcgrPacMan.behaviourtree.nodes.Node#canUpdate()
	 */
	@Override
	public boolean canUpdate()
	{
		PacManContext tempContext = (PacManContext)this.context;
		return tempContext.targetNodeIndices.length > 0;
	}

	/**
	 * 
	 * @see pacman.entries.jcgrPacMan.behaviourtree.nodes.Node#doAction()
	 */
	@Override
	public void doAction()
	{
		PacManContext tempContext = (PacManContext)this.context;

		int closestTarget = tempContext.game.getClosestNodeIndexFromNodeIndex(tempContext.currentNodeIndex, 
				tempContext.targetNodeIndices,
				DM.PATH);
		int[] pathToTarget = tempContext.game.getShortestPath(tempContext.currentNodeIndex, closestTarget);
		tempContext.pathToTarget = pathToTarget;
		
		this.getController().finishWithSuccess();
	}

	/**
	 * 
	 * @see pacman.entries.jcgrPacMan.behaviourtree.nodes.Node#end()
	 */
	@Override
	public void end()
	{
		// TODO Auto-generated method stub
		LogTask("Ending");
		
	}

}
