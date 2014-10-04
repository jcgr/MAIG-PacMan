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
	public Game game;
	
	public MOVE nextMove;
	
	public int currentPacManIndex;
	
	public final int MIN_GHOST_DISTANCE, FLEE_SEARCH_RANGE, EAT_GHOST_DISTANCE;
	
	public int[] activePills;
	
	public int[] activePowerPills;
	
	public int[] targetNodeIndices;
	
	public ArrayList<Integer> targetList;
	
	public PacManContext()
	{
		nextMove = MOVE.NEUTRAL;
		MIN_GHOST_DISTANCE = 10;
		FLEE_SEARCH_RANGE = MIN_GHOST_DISTANCE * 5;
		EAT_GHOST_DISTANCE = 25;
		activePills = new int[0];
		activePowerPills = new int[0];
		targetNodeIndices = new int[0];
		targetList = new ArrayList<Integer>();
	}
}
