/**
 * 
 */
package pacman.entries.jcgrPacMan.BTPacMan;

import java.util.ArrayList;

import pacman.entries.jcgrPacMan.BT.Context;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 * 
 * 
 * @author Jacob
 */
public class PacManContext extends Context
{
	public Game game;
	
	public MOVE nextMove;
	
	public int currentPacManIndex;
	
	public final int MIN_GHOST_DISTANCE, EAT_GHOST_DISTANCE;
	
	public int[] activePills;
	
	public int[] activePowerPills;
	
	public int[] targetNodeIndices;
	
	public ArrayList<Integer> targetList;
	
	public PacManContext()
	{
		nextMove = MOVE.NEUTRAL;
		MIN_GHOST_DISTANCE = 10;
		EAT_GHOST_DISTANCE = 30;
		activePills = new int[0];
		activePowerPills = new int[0];
		targetNodeIndices = new int[0];
		targetList = new ArrayList<Integer>();
	}
}
