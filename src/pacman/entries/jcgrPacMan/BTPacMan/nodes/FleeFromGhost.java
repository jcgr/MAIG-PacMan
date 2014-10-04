/**
 * 
 */
package pacman.entries.jcgrPacMan.BTPacMan.nodes;

import java.util.ArrayList;
import java.util.List;

import pacman.entries.jcgrPacMan.BT.Action;
import pacman.entries.jcgrPacMan.BT.Context;
import pacman.entries.jcgrPacMan.BT.STATUS;
import pacman.entries.jcgrPacMan.BTPacMan.PacManContext;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

/**
 * An action that makes PacMan attempt to pick the smartest way away from ghosts.
 * 
 * @author Jacob
 */
public class FleeFromGhost extends Action
{
	private PacManContext pmContext;

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
		pmContext = (PacManContext)context;
		
		boolean flee = false;
		int distanceToGhost;
		STATUS result = STATUS.FAILURE;
		
		MOVE[] possibleMoves = pmContext.game.getPossibleMoves(pmContext.currentPacManIndex);
		Double[] possibleMovesCount = new Double[possibleMoves.length];
		
		for (int i = 0; i < possibleMovesCount.length; i++)
			possibleMovesCount[i] = 0.0;

		// Figure out if PacMan should flee
		for(GHOST ghost : GHOST.values())
		{
			if(pmContext.game.getGhostEdibleTime(ghost) == 0 && pmContext.game.getGhostLairTime(ghost) == 0)
			{
				distanceToGhost = pmContext.game.getShortestPathDistance(
						pmContext.currentPacManIndex, pmContext.game.getGhostCurrentNodeIndex(ghost));
				
				if (distanceToGhost < pmContext.MIN_GHOST_DISTANCE)
				{
					flee = true;
					result = STATUS.SUCCESS;
					break;
				}
			}
		}

		// If PacMan shouldn't flee, don't run the rest of this node.
		if (!flee)
			return result;
		
		// Figure out which way is most likely to guarantee escape.
		for(GHOST ghost : GHOST.values())
		{
			if(pmContext.game.getGhostEdibleTime(ghost) == 0 && pmContext.game.getGhostLairTime(ghost) == 0)
			{
				distanceToGhost = pmContext.game.getShortestPathDistance(
						pmContext.currentPacManIndex, pmContext.game.getGhostCurrentNodeIndex(ghost));
				
				if(distanceToGhost < pmContext.FLEE_SEARCH_RANGE)
				{
					MOVE toGhost = pmContext.game.getNextMoveTowardsTarget(
							pmContext.currentPacManIndex, pmContext.game.getGhostCurrentNodeIndex(ghost), DM.PATH);
					
					// Increment the possibilities that do not lead towards a ghost.
					for (int move = 0; move < possibleMoves.length; move++)
						if (possibleMoves[move] != toGhost)
							possibleMovesCount[move] += pmContext.FLEE_SEARCH_RANGE - distanceToGhost;
				}
			}
		}
		
		List<MOVE> topMoves = new ArrayList<MOVE>();
		Double topMoveValue = 0.0;
		
		// Find the move with the top rated value
		for (int move = 0; move < possibleMovesCount.length; move++)
			if (possibleMovesCount[move] > topMoveValue)
				topMoveValue = possibleMovesCount[move];
		
		// Add the moves that share the highest rating.
		for (int move = 0; move < possibleMoves.length; move++)
			if (possibleMovesCount[move].equals(topMoveValue))
				topMoves.add(possibleMoves[move]);
		
		if (topMoves.size() == 1)
		{
			// If only one move is rated higher than 0, return that one...
			pmContext.nextMove = topMoves.get(0);
//			System.out.println("1  option:  " + pmContext.nextMove);
//			System.out.println(Arrays.toString(possibleMoves));
//			System.out.println(Arrays.toString(possibleMovesCount));
//			System.out.println();
		}
		else //if (topMoves.size() > 1)
		{
			// ... else return the move that leads towards the closest pill
			// where a ghost is not in the way.

			// Contains pills that pacman can go for
			int[] pills = pmContext.game.getActivePillsIndices();
			
			// A list of pills for easier removing.
			List<Integer> pillList = new ArrayList<Integer>();
			for (int p : pills)
				pillList.add(p);
			
			for (int pill = 0; pill < 100; pill++)
			{
				// Adds pills from pillList to pills, as we need an array
				// to use .getClosestNodeIndexFromNodeIndex()
				pills = new int[pillList.size()];
				for (int p = 0; p < pillList.size(); p++)
					pills[p] = pillList.get(p);
				
				// Find move to closest pill
				int closestPill = pmContext.game.getClosestNodeIndexFromNodeIndex(
						pmContext.currentPacManIndex, pills, DM.PATH);
				MOVE tempMove = pmContext.game.getNextMoveTowardsTarget(
						pmContext.currentPacManIndex, closestPill, DM.PATH);
				
				// Check if the move is among the top moves
				if (topMoves.contains(tempMove))
				{
					pmContext.nextMove = tempMove;
					break;
				}
				
				// If move isn't among top moves, remove the pill from the list
				// and try again.
				for (int p = 0; p < pills.length; p++)
				{
					if (pillList.get(p) == closestPill)
					{
						pillList.remove(p);
						break;
					}
				}
			}
			
//			System.out.println("2+ options: " + pmContext.nextMove);
//			System.out.println(Arrays.toString(possibleMoves));
//			System.out.println(Arrays.toString(possibleMovesCount));
//			System.out.println();
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
