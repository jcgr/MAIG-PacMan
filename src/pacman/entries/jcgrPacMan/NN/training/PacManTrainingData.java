/**
 * 
 */
package pacman.entries.jcgrPacMan.NN.training;

import pacman.game.Constants.MOVE;
import dataRecording.DataTuple;

/**
 * Training data representing pacman
 * 
 * @author Jacob
 */
public class PacManTrainingData extends TrainingData
{
	public PacManTrainingData(DataTuple dt, int inputNodes, int outputNodes)
	{
		super(inputNodes, outputNodes);
		for (int i = 0; i < outputNodes; i++)
			output[i] = 0;
		
		switch (dt.DirectionChosen)
		{
			case UP:
				output[0] = 1.0;
				break;
			case RIGHT:
				output[1] = 1.0;
				break;
			case DOWN:
				output[2] = 1.0;
				break;
			case LEFT:
				output[3] = 1.0;
				break;
			case NEUTRAL:
				output[4] = 1.0;
				break;
		}
		
//		input[0] = dt.normalizeLevel(dt.mazeIndex);
//		input[1] = dt.normalizeLevel(dt.currentLevel);
//		input[0] = dt.normalizePosition(dt.pacmanPosition);
//		input[3] = dt.pacmanLivesLeft;
//		input[4] = dt.normalizeCurrentScore(dt.currentScore);
//		input[5] = dt.normalizeTotalGameTime(dt.totalGameTime);
//		input[6] = dt.normalizeCurrentLevelTime(dt.currentLevelTime);
//		input[1] = dt.normalizeNumberOfPills(dt.numOfPillsLeft);
//		input[2] = dt.normalizeNumberOfPowerPills(dt.numOfPowerPillsLeft);
		
		// Ghosts edible?
//		input[3] = dt.normalizeBoolean(dt.isBlinkyEdible);
//		input[4] = dt.normalizeBoolean(dt.isInkyEdible);
//		input[5] = dt.normalizeBoolean(dt.isPinkyEdible);
//		input[6] = dt.normalizeBoolean(dt.isSueEdible);
		
		// Ghost distance
//		input[7] = dt.normalizeDistance(dt.blinkyDist);
//		input[8] = dt.normalizeDistance(dt.inkyDist);
//		input[9] = dt.normalizeDistance(dt.pinkyDist);
//		input[10] = dt.normalizeDistance(dt.sueDist);
		
		// Ghost direction
//		input[10] = moveToDouble(dt.blinkyDir);
//		input[11] = moveToDouble(dt.inkyDir);
//		input[12] = moveToDouble(dt.pinkyDir);
//		input[13] = moveToDouble(dt.sueDir);
		input[0] = dt.moveUpValue;
		input[1] = dt.moveRightValue;
		input[2] = dt.moveDownValue;
		input[3] = dt.moveLeftValue;
	}

	public static double moveToDouble(MOVE move)
	{
		switch (move)
		{
			case NEUTRAL:
				return 0.00;
			case UP:
				return 0.25;
			case RIGHT:
				return 0.5;
			case DOWN:
				return 0.75;
			case LEFT:
				return 1.0;
			default:
				return -1.0;
		}
	}

	public static MOVE doubleToMove(double d)
	{
		if (d >= 0.00 && d < 0.25)
			return MOVE.NEUTRAL;
		if (d >= 0.25 && d < 0.50)
			return MOVE.UP;
		if (d >= 0.50 && d < 0.75)
			return MOVE.RIGHT;
		if (d >= 0.75 && d < 1.0)
			return MOVE.DOWN;
		if (d == 1.0)
			return MOVE.LEFT;
		
		return MOVE.NEUTRAL;
	}
}
