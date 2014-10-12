/**
 * 
 */
package pacman.entries.jcgrPacMan.NN.training;

import pacman.entries.jcgrPacMan.DataRecording.JcgrDataTuple;

/**
 * Training data for PacMan.
 * 
 * @author Jacob
 */
public class PacManTrainingData extends TrainingData
{
	public PacManTrainingData(JcgrDataTuple dt, int inputNodes, int outputNodes)
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

		input[0] = dt.normalizeDistance(dt.nearestPillDistance);
		input[1] = dt.moveToDouble(dt.nearestPillDirection);
		input[2] = dt.normalizeDistance(dt.nearestGhostDistance);
		input[3] = dt.moveToDouble(dt.nearestGhostDirection);
	}
}
