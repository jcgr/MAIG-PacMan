/**
 * 
 */
package pacman.entries.jcgrPacMan.BTPacMan;

import java.util.ArrayList;

import pacman.entries.jcgrPacMan.BT.Context;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 * The context for the PacMan game.
 * 
 * @author Jacob
 */
public class PacManContext extends Context
{
	/**
	 * The game for the current context.
	 */
	public Game game;
	
	/**
	 * The next move to make.
	 */
	public MOVE nextMove;
	
	/**
	 * PacMan's current index in the game.
	 */
	public int currentPacManIndex;
	
	/**
	 * How close a ghost can get before PacMan begins fleeing.
	 */
	public final int MIN_FLEE_DISTANCE;
	
	/**
	 * How far out PacMan should look for ghosts while fleeing 
	 * (used for predicting where to run)
	 */
	public final int FLEE_SEARCH_RANGE;
	
	/**
	 * How far PacMan will look for edible ghosts.
	 */
	public final int EAT_GHOST_DISTANCE;
	
	/**
	 * The pills active in the game.
	 */
	public int[] activePills;
	
	/**
	 * The active power pills.
	 */
	public int[] activePowerPills;
	
	/**
	 * The nodes that are used for targeting.
	 */
	public int[] targetNodeIndices;
	
	/**
	 * A list with the targets (used for easier manipulating compared to
	 * targetNodeIndicies).
	 */
	public ArrayList<Integer> targetList;
	
	public PacManContext()
	{
		nextMove = MOVE.NEUTRAL;
		
		MIN_FLEE_DISTANCE = 10;
		FLEE_SEARCH_RANGE = MIN_FLEE_DISTANCE * 5;
		EAT_GHOST_DISTANCE = 25;
		
		activePills = new int[0];
		activePowerPills = new int[0];
		targetNodeIndices = new int[0];
		
		targetList = new ArrayList<Integer>();
	}
}
