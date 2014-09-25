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
		int numberOfInputs = 21;
		int numberOfOutputs = 1;
		int hiddenLayerNeurons = 10;
		
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
		
		nn.run();
		
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
		double[] input = new double[21];
		DataTuple dt = new DataTuple(game, MOVE.NEUTRAL);

		input[0] = dt.normalizeLevel(dt.mazeIndex);
		input[1] = dt.normalizeLevel(dt.currentLevel);
		input[2] = dt.normalizePosition(dt.pacmanPosition);
		input[3] = dt.pacmanLivesLeft;
		input[4] = dt.normalizeCurrentScore(dt.currentScore);
		input[5] = dt.normalizeTotalGameTime(dt.totalGameTime);
		input[6] = dt.normalizeCurrentLevelTime(dt.currentLevelTime);
		input[7] = dt.normalizeNumberOfPills(dt.numOfPillsLeft);
		input[8] = dt.normalizeNumberOfPowerPills(dt.numOfPowerPillsLeft);
		// Ghosts edible?
		input[9] = dt.normalizeBoolean(dt.isBlinkyEdible);
		input[10] = dt.normalizeBoolean(dt.isInkyEdible);
		input[11] = dt.normalizeBoolean(dt.isPinkyEdible);
		input[12] = dt.normalizeBoolean(dt.isSueEdible);
		// Ghost distance
		input[13] = dt.normalizeDistance(dt.blinkyDist);
		input[14] = dt.normalizeDistance(dt.inkyDist);
		input[15] = dt.normalizeDistance(dt.pinkyDist);
		input[16] = dt.normalizeDistance(dt.sueDist);
		// Ghost direction
		input[17] = PacManTrainingData.moveToDouble(dt.blinkyDir);
		input[18] = PacManTrainingData.moveToDouble(dt.inkyDir);
		input[19] = PacManTrainingData.moveToDouble(dt.pinkyDir);
		input[20] = PacManTrainingData.moveToDouble(dt.sueDir);
		
		nn.setInputs(input);
		nn.run();
		double output = nn.getOutput()[0];
		
		System.out.println(output);
		return doubleToMove(output);
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
