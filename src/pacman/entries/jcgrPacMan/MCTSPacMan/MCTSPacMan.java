/**
 * 
 */
package pacman.entries.jcgrPacMan.MCTSPacMan;

import java.util.ArrayList;
import java.util.List;

import pacman.controllers.Controller;
import pacman.entries.jcgrPacMan.MCTS.MCTS;
import pacman.entries.jcgrPacMan.MCTS.MCTS2;
import pacman.entries.jcgrPacMan.MCTS.TreeNode;
import pacman.entries.jcgrPacMan.MCTS.TreeNode2;
import pacman.game.Game;
import pacman.game.Constants.MOVE;

/**
 * 
 * 
 * @author Jacob
 */
public class MCTSPacMan extends Controller<MOVE>
{
	int iterations = 0;
	MCTS2 mcts;
	
	public MCTSPacMan()
	{
		mcts = new MCTS2();
	}
	
	/* (non-Javadoc)
	 * @see pacman.controllers.Controller#getMove(pacman.game.Game, long)
	 */
	public MOVE getMove(Game game, long timeDue)
	{
		int pmIndex = game.getPacmanCurrentNodeIndex();
		
		if (MCTS.pacManAtJunction(game))
		{
//			System.out.println("AT JUNCTION!");
			TreeNode2 tn = mcts.search(game, iterations);
			MOVE move = tn == null ? MOVE.NEUTRAL : tn.getMoveTo();
			iterations++;
			return move;
		}

		iterations++;
		MOVE[] possibleMoves = game.getPossibleMoves(pmIndex, game.getPacmanLastMoveMade());
		return possibleMoves[0];
	}
}
