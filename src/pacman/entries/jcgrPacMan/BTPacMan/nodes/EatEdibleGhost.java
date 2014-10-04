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
 * An action that makes tries to find an edible ghost nearby.
 * 
 * @author Jacob
 */
public class EatEdibleGhost extends Action
{
	private PacManContext pcContext;
	
	/**
	 * Creates a new instance of the EatEdibleGhost class.
	 * @param name The name of the action.
	 */
	public EatEdibleGhost(String name)
	{
		super(name);
	}

	/**
	 * @return SUCCESS if an edible ghost is nearby; 
	 * 		   FAILURE otherwise
	 * @see pacman.entries.jcgrPacMan.BT.Node#run(pacman.entries.jcgrPacMan.BT.Context)
	 */
	@Override
	public STATUS run(Context context)
	{
		pcContext = (PacManContext)context;

		STATUS result = STATUS.FAILURE;
		GHOST targetGhost = null;
		int distanceToGhost, distanceToClosestGhost = Integer.MAX_VALUE;
		
		// Find the closest edible ghost within a given radius.
		for(GHOST ghost : GHOST.values())
		{
			if(pcContext.game.getGhostEdibleTime(ghost) > 0)
			{
				distanceToGhost = pcContext.game.getShortestPathDistance(pcContext.currentPacManIndex, pcContext.game.getGhostCurrentNodeIndex(ghost));
				
				if(distanceToGhost < pcContext.EAT_GHOST_DISTANCE
					&& distanceToGhost < distanceToClosestGhost)
				{
					distanceToClosestGhost = distanceToGhost;
					targetGhost = ghost;
				}
			}
		}
		
		// Find movement towards ghost.
		if (targetGhost != null)
		{
			pcContext.nextMove = pcContext.game.getNextMoveTowardsTarget(pcContext.currentPacManIndex, pcContext.game.getGhostCurrentNodeIndex(targetGhost), DM.PATH);
			result = STATUS.SUCCESS;
		}
	
		return result;
	}

	/**
	 * @param context
	 * @see pacman.entries.jcgrPacMan.BT.Node#start(pacman.entries.jcgrPacMan.BT.Context)
	 */
	@Override
	public void start(Context context)
	{
		pcContext = (PacManContext)context;		
	}

	/**
	 * @see pacman.entries.jcgrPacMan.BT.Node#finish(pacman.entries.jcgrPacMan.BT.STATUS, pacman.entries.jcgrPacMan.BT.Context)
	 */
	@Override
	public void finish(STATUS status, Context context)
	{
	}

}
