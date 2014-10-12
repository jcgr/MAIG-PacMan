/**
 * 
 */
package pacman.entries.jcgrPacMan.DataRecording;

import pacman.controllers.HumanController;
import pacman.controllers.KeyBoardInput;
import pacman.game.Game;
import pacman.game.Constants.MOVE;


/**
 * Own version of the DataCollectorController that saves
 * tuples with other data than the original one.
 * 
 * @author Jacob
 */
public class JcgrDataCollectorController extends HumanController
{
	public JcgrDataCollectorController(KeyBoardInput input)
	{
		super(input);
	}

	@Override
	public MOVE getMove(Game game, long dueTime)
	{
		MOVE move = super.getMove(game, dueTime);

		JcgrDataTuple data = new JcgrDataTuple(game, move);

		JcgrDataSaverLoader.SavePacManData(data);
		return move;
	}
}