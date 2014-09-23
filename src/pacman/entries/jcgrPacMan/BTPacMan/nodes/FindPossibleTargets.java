/**
 * 
 */
package pacman.entries.jcgrPacMan.BTPacMan.nodes;

import java.util.ArrayList;

import pacman.entries.jcgrPacMan.BT.Action;
import pacman.entries.jcgrPacMan.BT.Context;
import pacman.entries.jcgrPacMan.BT.STATUS;
import pacman.entries.jcgrPacMan.BTPacMan.PacManContext;
import pacman.game.Constants.GHOST;

/**
 * An action that finds all pills that are still in the level.
 * 
 * @author Jacob
 */
public class FindPossibleTargets extends Action
{
	private PacManContext pcContext;
	private boolean moveResultToContextIndices;

	/**
	 * @param name The name of the action.
	 */
	public FindPossibleTargets(String name, boolean moveResultToContextIndices)
	{
		super(name);
		this.moveResultToContextIndices = moveResultToContextIndices;
	}

	/**
	 * Finds the pills still active.
	 * @return SUCCESS, as it cannot fail.
	 * @see pacman.entries.jcgrPacMan.BT.Node#run(pacman.entries.jcgrPacMan.BT.Context)
	 */
	@Override
	public STATUS run(Context context)
	{	
		pcContext = (PacManContext)context;
		
		ArrayList<Integer> targets = new ArrayList<Integer>();
		
		boolean powerPillActive = false;
		
		int ghostCounter = 0, ghostSpawnTimeTotal = 0;
		int[] ghostPositions = new int[4];
		int[] activePowerPills = pcContext.activePowerPills;
		int[] activePills = pcContext.activePills;
		
		// Figure out if a power pill is active.
		for (GHOST ghost : GHOST.values())
		{
			ghostPositions[ghostCounter] = pcContext.game.getGhostCurrentNodeIndex(ghost);
			ghostCounter++;
			ghostSpawnTimeTotal += pcContext.game.getGhostLairTime(ghost);
			
			if(pcContext.game.getGhostEdibleTime(ghost) > 0 && pcContext.game.getGhostLairTime(ghost) == 0)
				powerPillActive = true;
		}
		
		// If a power pill is not active and all ghosts have
		// spawned, add PPs to the list of targets
		if (!powerPillActive && (ghostSpawnTimeTotal == 0))
		{
			for(int i = 0; i < activePowerPills.length; i++)
			{
				int[] pathToPP = pcContext.game.getShortestPath(pcContext.currentPacManIndex, activePowerPills[i]);
				
				if (!pathContainsGhost(ghostPositions, pathToPP))
					targets.add(activePowerPills[i]);
			}
		}
		
		// If the list of targets is empty, add normal pills to it.
		if (targets.size() == 0)
			for(int i = 0; i < activePills.length; i++)
				targets.add(activePills[i]);
		
		pcContext.targetList = targets;
		
		return STATUS.SUCCESS;
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
		pcContext = (PacManContext)context;
		
		if (moveResultToContextIndices)
		{
			ArrayList<Integer> tempTargets = pcContext.targetList;
			int targetAmout = tempTargets.size();
			int[] tempTargetNodeIndices = new int[targetAmout];
			
			for (int i = 0; i < targetAmout; i++)
				tempTargetNodeIndices[i] = tempTargets.get(i);
			
			pcContext.targetNodeIndices = tempTargetNodeIndices;
		}
	}
	
	private boolean pathContainsGhost(int[] ghostPositions, int[] path)
	{
		for (int ghostPos : ghostPositions)
			for (int i : path)
				if (i == ghostPos) 
					return true;
		
		return false;
	}

}
