/**
 * 
 */
package pacman.entries.jcgrPacMan.MCTSPacMan;

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
//		List<TreeNode> moves = mcts.search();
//		System.out.println(mcts == null);
		TreeNode tn = mcts.search(game);
		MOVE move = tn == null ? MOVE.NEUTRAL : tn.moveTo;
		return move;
	}
}
