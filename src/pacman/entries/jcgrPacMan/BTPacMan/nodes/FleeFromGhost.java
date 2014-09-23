/**
 * 
 */
package pacman.entries.jcgrPacMan.BTPacMan.nodes;

import pacman.entries.jcgrPacMan.BT.Action;
import pacman.entries.jcgrPacMan.BT.Context;
import pacman.entries.jcgrPacMan.BT.STATUS;
import pacman.entries.jcgrPacMan.BTPacMan.PacManContext;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;

/**
 * An action that makes PacMan run away from the closest ghost.
 * 
 * @author Jacob
 */
public class FleeFromGhost extends Action
{
	private PacManContext pcContext;

	/**
	 * Creates a new isntance of the FleeFromGhost action.
	 * @param name The name of the action.
	 */
	public FleeFromGhost(String name)
	{
		super(name);
	}

	/**
	 * @return SUCCESS if a ghost is nearby and a move away has been planned;
	 * 		   FAILURE otherwise.
	 * @see pacman.entries.jcgrPacMan.BT.Node#run(pacman.entries.jcgrPacMan.BT.Context)
	 */
	@Override
	public STATUS run(Context context)
	{
		pcContext = (PacManContext)context;
		// TODO: Needs to be smart when running away
		STATUS result = STATUS.FAILURE;
		int distanceToGhost, distanceToClosestGhost = Integer.MAX_VALUE;

		for(GHOST ghost : GHOST.values())
		{
			if(pcContext.game.getGhostEdibleTime(ghost) == 0 && pcContext.game.getGhostLairTime(ghost) == 0)
			{
				distanceToGhost = pcContext.game.getShortestPathDistance(pcContext.currentPacManIndex, pcContext.game.getGhostCurrentNodeIndex(ghost));
				
				if(distanceToGhost < pcContext.MIN_GHOST_DISTANCE
					&& distanceToGhost < distanceToClosestGhost)
				{
					pcContext.nextMove = pcContext.game.getNextMoveAwayFromTarget(pcContext.currentPacManIndex,pcContext.game.getGhostCurrentNodeIndex(ghost) , DM.PATH);
//					pcContext.game.getDistance(fromNodeIndex, toNodeIndex, DM.EUCLID)
					distanceToClosestGhost = distanceToGhost;
					result = STATUS.SUCCESS;
				}
			}
		}
	
		return result;
	}

	/**
	 * @see pacman.entries.jcgrPacMan.BT.Node#start(pacman.entries.jcgrPacMan.BT.Context)
	 */
	@Override
	public void start(Context context)
	{
	}

	/**
	 * @see pacman.entries.jcgrPacMan.BT.Node#finish(pacman.entries.jcgrPacMan.BT.STATUS, pacman.entries.jcgrPacMan.BT.Context)
	 */
	@Override
	public void finish(STATUS status, Context context)
	{
	}

}
