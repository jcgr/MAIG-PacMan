/**
 * 
 */
package pacman.entries.jcgrPacMan.MCTSPacMan;

import pacman.controllers.Controller;
import pacman.entries.jcgrPacMan.MCTS.MCTS;
import pacman.entries.jcgrPacMan.MCTS.TreeNode;
import pacman.game.Game;
import pacman.game.Constants.MOVE;

/**
 * 
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
		if (MCTS.pacManAtJunction(game))
		{
			TreeNode bestNode = mcts.search(game);
			MOVE move = (bestNode == null ? MOVE.NEUTRAL : bestNode.getMoveTo());
			return move;
		}

		MOVE[] possibleMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade());
		return possibleMoves[0];
	}
}
