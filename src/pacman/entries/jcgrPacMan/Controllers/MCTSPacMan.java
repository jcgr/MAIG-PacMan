/**
 * 
 */
package pacman.entries.jcgrPacMan.Controllers;

import pacman.controllers.Controller;
import pacman.entries.jcgrPacMan.MCTS.MCTS;
import pacman.entries.jcgrPacMan.MCTS.TreeNode;
import pacman.game.Game;
import pacman.game.Constants.MOVE;

/**
 * A controller that uses MCTS for deciding on a move.
 * 
 * @author Jacob
 */
public class MCTSPacMan extends Controller<MOVE>
{
	MCTS mcts;
	
	public MCTSPacMan()
	{
		mcts = new MCTS();
	}
	
	/* (non-Javadoc)
	 * @see pacman.controllers.Controller#getMove(pacman.game.Game, long)
	 */
	public MOVE getMove(Game game, long timeDue)
	{
		MOVE[] possibleMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade());
		
		if (MCTS.pacManAtJunction(game))
		{
			TreeNode bestNode = mcts.search(game);
			MOVE move = (bestNode == null ? possibleMoves[0] : bestNode.getMoveTo());
			return move;
		}
		
		return possibleMoves[0];
	}
}
