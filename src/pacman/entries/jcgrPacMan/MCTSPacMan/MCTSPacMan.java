/**
 * 
 */
package pacman.entries.jcgrPacMan.MCTSPacMan;

import java.util.ArrayList;
import java.util.List;

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
	MOVE lastMoveMade;
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
		int pmIndex = game.getPacmanCurrentNodeIndex();
		
		if (MCTS.pacManAtJunction(game))
		{
//			System.out.println("AT JUNCTION!");
			TreeNode tn = mcts.search(game);
			MOVE move = tn == null ? MOVE.NEUTRAL : tn.moveTo;
			return move;
		}
		
		MOVE[] possibleMoves = game.getPossibleMoves(pmIndex, game.getPacmanLastMoveMade());
		return possibleMoves[0];
	}
}
