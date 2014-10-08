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
	
	public MCTSPacMan()
	{
	}
	
	/* (non-Javadoc)
	 * @see pacman.controllers.Controller#getMove(pacman.game.Game, long)
	 */
	public MOVE getMove(Game game, long timeDue)
	{
		MCTS mcts = new MCTS(game);
		List<TreeNode> moves = mcts.search();
		return moves.get(1).moveTo;
	}
}
