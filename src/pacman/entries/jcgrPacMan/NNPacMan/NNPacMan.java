/**
 * 
 */
package pacman.entries.jcgrPacMan.NNPacMan;

import dataRecording.DataSaverLoader;
import dataRecording.DataTuple;
import pacman.controllers.Controller;
import pacman.entries.jcgrPacMan.NN.Backpropagator;
import pacman.entries.jcgrPacMan.NN.NeuralNetwork;
import pacman.entries.jcgrPacMan.NN.Neuron;
import pacman.entries.jcgrPacMan.NN.NeuronLayer;
import pacman.entries.jcgrPacMan.NN.training.PacManTrainingData;
import pacman.entries.jcgrPacMan.NN.training.TrainingSet;
import pacman.game.Game;
import pacman.game.Constants.MOVE;

/**
 * 
 * 
 * @author Jacob
 */
public class NNPacMan extends Controller<MOVE>
{
	
	NeuralNetwork nn;
	
	public NNPacMan()
	{
		trainNN();
	}
	
	private void trainNN()
	{
		int numberOfInputs = 14;
		int numberOfOutputs = 5;
		int hiddenLayerNeurons = 3;
		
		nn = new NeuralNetwork();
		
		Neuron bias = new Neuron();
		bias.setOutput(1.0);
		
		DataTuple[] dta = DataSaverLoader.LoadPacManData();
		TrainingSet ts = new TrainingSet();
		for (int i = 0; i < dta.length; i++)
			ts.AddTrainingData(new PacManTrainingData(dta[i], numberOfInputs, numberOfOutputs));
		
		NeuronLayer input = new NeuronLayer(bias);
		for (int i = 0; i < numberOfInputs; i++)
			input.addNeuron(new Neuron());
		
		NeuronLayer h1Layer = new NeuronLayer(input, bias);
		for (int i = 0; i < hiddenLayerNeurons; i++)
			h1Layer.addNeuron(new Neuron());
		
		NeuronLayer output = new NeuronLayer(h1Layer);
		for (int i = 0; i < numberOfOutputs; i++)
			output.addNeuron(new Neuron());
		
		nn.addLayer(input);
		nn.addLayer(h1Layer);
		nn.addLayer(output);
		
		nn.feedForward();
		
		double[] results = nn.getOutput();
		for(double d : results)
			System.out.println(d);
		
		Backpropagator bp = new Backpropagator(nn, 1.0);
		bp.train(ts, 0.05);
		double[] weights = nn.getWeights();
		for(double d : weights)
		{
			System.out.println(d + " - ");
		}
	}
	
	/* (non-Javadoc)
	 * @see pacman.controllers.Controller#getMove(pacman.game.Game, long)
	 */
	public MOVE getMove(Game game, long timeDue)
	{
		double[] input = new double[14];
		DataTuple dt = new DataTuple(game, lastMove);

//		input[0] = dt.normalizeLevel(dt.mazeIndex);
//		input[1] = dt.normalizeLevel(dt.currentLevel);
		input[0] = dt.normalizePosition(dt.pacmanPosition);
//		input[3] = dt.pacmanLivesLeft;
//		input[4] = dt.normalizeCurrentScore(dt.currentScore);
//		input[5] = dt.normalizeTotalGameTime(dt.totalGameTime);
//		input[6] = dt.normalizeCurrentLevelTime(dt.currentLevelTime);
		input[1] = dt.normalizeNumberOfPills(dt.numOfPillsLeft);
		input[2] = dt.normalizeNumberOfPowerPills(dt.numOfPowerPillsLeft);
		
		// Ghosts edible?
		input[3] = dt.normalizeBoolean(dt.isBlinkyEdible);
		input[4] = dt.normalizeBoolean(dt.isInkyEdible);
		input[5] = dt.normalizeBoolean(dt.isPinkyEdible);
		input[6] = dt.normalizeBoolean(dt.isSueEdible);
		
		// Ghost distance
		input[7] = dt.normalizeDistance(dt.blinkyDist);
		input[8] = dt.normalizeDistance(dt.inkyDist);
		input[9] = dt.normalizeDistance(dt.pinkyDist);
		input[10] = dt.normalizeDistance(dt.sueDist);
		
		// Ghost direction
		input[10] = PacManTrainingData.moveToDouble(dt.blinkyDir);
		input[11] = PacManTrainingData.moveToDouble(dt.inkyDir);
		input[12] = PacManTrainingData.moveToDouble(dt.pinkyDir);
		input[13] = PacManTrainingData.moveToDouble(dt.sueDir);
		
		nn.setInputs(input);
		nn.feedForward();

		double[] output = nn.getOutput();
		double finalOutput = -50000;
		MOVE chosenMove = MOVE.NEUTRAL;
		for (int i = 0; i < output.length; i++)
		{
			if (output[i] > finalOutput)
			{
				finalOutput = output[i];
				switch (i)
				{
				case 0:
					chosenMove = MOVE.UP;
					break;

				case 1:
					chosenMove = MOVE.RIGHT;
					break;

				case 2:
					chosenMove = MOVE.DOWN;
					break;

				case 3:
					chosenMove = MOVE.LEFT;
					break;

				case 4:
					chosenMove = MOVE.NEUTRAL;
					break;
				}
			}
		}
		
		System.out.println();
		double[] results = nn.getOutput();
		for(double d : results)
			System.out.print(d + " ||| ");
		System.out.println();
		System.out.println(chosenMove.toString());
		
//		System.out.println(output);
		return chosenMove;
	}

	public static MOVE doubleToMove(double d)
	{
		if (d >= -0.13 && d < 0.12)
		{
			return MOVE.NEUTRAL;
		}
		if (d >= 0.12 && d < 0.37)
		{
			return MOVE.UP;
		}
		if (d >= 0.37 && d < 0.62)
		{
			return MOVE.RIGHT;
		}
		if (d >= 0.62 && d < 0.87)
		{
			return MOVE.DOWN;
		}
		if (d >= 0.87 && d < 1.12)
		{
			return MOVE.LEFT;
		}
		return MOVE.NEUTRAL;
	}
}
