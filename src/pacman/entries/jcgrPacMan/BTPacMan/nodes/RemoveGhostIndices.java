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
 * DOESN'T WORK PROPERLY
 * 
 * @author Jacob
 */
public class RemoveGhostIndices extends Action
{
	private PacManContext pcContext;
	
	/**
	 * @param name
	 */
	public RemoveGhostIndices(String name)
	{
		super(name);
	}
	
	private ArrayList<Integer> removeFromList(int index, ArrayList<Integer> list)
	{
		for (int i = 0; i < list.size(); i++)
		{
			if (index == i)
			{
				list.remove(i); 
				break;
			}
		}
		
		return list;
	}

	/**
	 * @param context
	 * @return
	 * @see pacman.entries.jcgrPacMan.BT.Node#run(pacman.entries.jcgrPacMan.BT.Context)
	 */
	@Override
	public STATUS run(Context context)
	{
		pcContext = (PacManContext)context;

		for(GHOST ghost : GHOST.values())
		{
			if (pcContext.game.getGhostEdibleTime(ghost) == 0 
					&& pcContext.game.getGhostLairTime(ghost) == 0)
			{
				ArrayList<Integer> tempTargets = pcContext.targetList;
				int ghostPosition = pcContext.game.getGhostCurrentNodeIndex(ghost);
				int[] ghostNeighbours = pcContext.game.getNeighbouringNodes(ghostPosition);
				
				for (int i : ghostNeighbours)
					tempTargets = removeFromList(i, tempTargets);
				
//				System.out.print(tempTargets.size());
//				System.out.println(" GP: " + ghostPosition);
//				
//				tempTargets = removeFromList(ghostPosition, tempTargets);
//				int[] pathToGhost = tempContext.game.getShortestPath(tempContext.currentPacManIndex, ghostPosition);
//				int pathLength = pathToGhost.length;
//				
//				if (pathLength >= 3)
//				{
//					tempTargets = removeFromList(pathToGhost[pathLength - 1], tempTargets);
//					tempTargets = removeFromList(pathToGhost[pathLength - 2], tempTargets);
//					tempTargets = removeFromList(pathToGhost[pathLength - 3], tempTargets);
//				}
//				else if (pathLength == 2)
//				{
//					tempTargets = removeFromList(pathToGhost[pathLength - 1], tempTargets);
//					tempTargets = removeFromList(pathToGhost[pathLength - 2], tempTargets);
//				}
//				else if (pathLength == 1)
//				{
//					tempTargets = removeFromList(pathToGhost[pathLength - 1], tempTargets);
//				}
				
				pcContext.targetList = tempTargets;
			}
		}
		
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

		ArrayList<Integer> tempTargets = pcContext.targetList;
		int targetAmout = tempTargets.size();
		int[] tempTargetNodeIndices = new int[targetAmout];
		
		for (int i = 0; i < targetAmout; i++)
			tempTargetNodeIndices[i] = tempTargets.get(i);
		
		pcContext.targetNodeIndices = tempTargetNodeIndices;
	}

}
