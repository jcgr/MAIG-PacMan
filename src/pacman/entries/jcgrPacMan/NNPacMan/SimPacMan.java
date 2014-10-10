/**
 * 
 */
package pacman.entries.jcgrPacMan.NNPacMan;

import pacman.controllers.Controller;
import pacman.entries.jcgrPacMan.NN.simulation.SimNode;
import pacman.entries.jcgrPacMan.NN.simulation.Simulation;
import pacman.game.Game;
import pacman.game.Constants.MOVE;

/**
 * 
 * 
 * @author Jacob
 */
public class SimPacMan extends Controller<MOVE>
{
	int iterations = 0;
	Simulation sim;
	
	public SimPacMan()
	{
		sim = new Simulation();
	}
	
	/* (non-Javadoc)
	 * @see pacman.controllers.Controller#getMove(pacman.game.Game, long)
	 */
	public MOVE getMove(Game game, long timeDue)
	{
		int pmIndex = game.getPacmanCurrentNodeIndex();
		
		if (Simulation.pacManAtJunction(game))
		{
			SimNode ch = sim.simulate(game, 60);
			MOVE move = (ch == null ? MOVE.NEUTRAL : ch.moveTo);
			iterations++;
			return move;
		}

		iterations++;
		MOVE[] possibleMoves = game.getPossibleMoves(pmIndex, game.getPacmanLastMoveMade());
		return possibleMoves[0];
	}
}
