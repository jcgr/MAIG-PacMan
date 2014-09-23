/**
 * 
 */
package pacman.entries.jcgrPacMan.behaviourtreePacMan;

import pacman.entries.jcgrPacMan.behaviourtree.*;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 * 
 * 
 * @author Jacob
 */
public class PacManContext implements Context
{
	public Game game;
	
	public MOVE currentMove, lastMove;
	
	public int currentNodeIndex;
	
	public int currentTarget, lastTarget;
	
	public int[] activePills;
	
	public int[] activePowerPills;
	
	public int[] targetNodeIndices;
	
	public int[] pathToTarget;
	
	public PacManContext()
	{
		lastMove = MOVE.NEUTRAL;
		currentMove = MOVE.NEUTRAL;
		activePills = new int[0];
		activePowerPills = new int[0];
		targetNodeIndices = new int[0];
		pathToTarget = new int[0];
	}
}
