/**
 * 
 */
package pacman.entries.jcgrPacMan.BTPacMan.nodes;

import pacman.entries.jcgrPacMan.BT.Action;
import pacman.entries.jcgrPacMan.BT.Context;
import pacman.entries.jcgrPacMan.BT.STATUS;
import pacman.entries.jcgrPacMan.BTPacMan.PacManContext;
import pacman.game.Constants.DM;

/**
 * Finds a path to the closest pill.
 * 
 * @author Jacob
 */
public class FindPathToTarget extends Action
{

	/**
	 * @param name
	 */
	public FindPathToTarget(String name)
	{
		super(name);
	}

	/**
	 * @return SUCCESS no matter what, as it can always find a way to a pill.
	 * @see pacman.entries.jcgrPacMan.BT.Node#run(pacman.entries.jcgrPacMan.BT.Context)
	 */
	@Override
	public STATUS run(Context context)
	{
		PacManContext pcContext = (PacManContext)context;
		
		int nearest = pcContext.game.getClosestNodeIndexFromNodeIndex(pcContext.currentPacManIndex, pcContext.targetNodeIndices, DM.PATH);
		pcContext.nextMove = pcContext.game.getNextMoveTowardsTarget(pcContext.currentPacManIndex, nearest, DM.PATH);

		return STATUS.SUCCESS;
	}

	/**
	 * @see pacman.entries.jcgrPacMan.BT.Node#start(pacman.entries.jcgrPacMan.BT.Context)
	 */
	@Override
	public void start(Context context)
	{
		// Nothing
	}

	/**
	 * @see pacman.entries.jcgrPacMan.BT.Node#finish(pacman.entries.jcgrPacMan.BT.STATUS, pacman.entries.jcgrPacMan.BT.Context)
	 */
	@Override
	public void finish(STATUS status, Context context)
	{
		// Nothing
	}

}
