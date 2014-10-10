/**
 * 
 */
package pacman.entries.jcgrPacMan.NNPacMan;

import java.util.Arrays;

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
import pacman.game.util.IO;

/**
 * 
 * 
 * @author Jacob
 */
public class NNPacMan extends Controller<MOVE>
{
	private boolean loadWeights = true;
	private boolean loadArray = false;
	private String fileToLoad = "PacManTest1.txt";
	
	int numberOfInputs = 4;
	int numberOfOutputs = 5;
	int hiddenLayerNeurons = 3;
	NeuralNetwork nn;
	
	public NNPacMan()
	{
//		trainNN();
		nn = NeuralNetwork.createSingleHiddenLayerNeuralNetwork(
				"Test"
				, numberOfInputs
				, numberOfOutputs
				, hiddenLayerNeurons);
		if (loadWeights)
			nn.setWeights(loadWeights());
	}
	
	private void trainNN()
	{
		nn = new NeuralNetwork("PacMan");
		
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
		
		nn.activate();
		
		double[] results = nn.getOutput();
//		for(double d : results)
//			System.out.println(d);

		Backpropagator bp = new Backpropagator(nn);
		bp.train(ts);
//		double[] weights = nn.getWeights();
//		for(double d : weights)
//		{
//			System.out.println(d + " - ");
//		}
	}
	
	/* (non-Javadoc)
	 * @see pacman.controllers.Controller#getMove(pacman.game.Game, long)
	 */
	public MOVE getMove(Game game, long timeDue)
	{
		if (pacManAtJunction(game))
		{
		double[] input = new double[numberOfInputs];
		DataTuple dt = new DataTuple(game, lastMove);

		input[0] = dt.moveUpValue;
		input[1] = dt.moveRightValue;
		input[2] = dt.moveDownValue;
		input[3] = dt.moveLeftValue;
		
		nn.setInputs(input);
		nn.activate();

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
		
//		System.out.println();
//		double[] results = nn.getOutput();
//		for(double d : results)
//			System.out.print(d + " ||| ");
//		System.out.println();
//		System.out.println(chosenMove.toString());
		
//		System.out.println(output);
		return chosenMove;
		}
		

		MOVE[] possibleMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade());
		return possibleMoves[0];
	}
	
	public static boolean pacManAtJunction(Game game)
	{
		boolean atJunction = false;
		int pmIndex = game.getPacmanCurrentNodeIndex();
		int[] juncts = game.getJunctionIndices();
		for (int i : juncts)
			if (i == pmIndex)
				atJunction = true;
		
		return atJunction;
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

	private double[] loadWeights()
	{
		if (loadArray)
		{
			double[] weights =
			{};
			return weights;
		}

		String s = IO.loadFile(fileToLoad);
		String ss = s.substring(1, s.length() - 2);
		String[] weightStrings = ss.split(", ");
		double[] weights = new double[weightStrings.length];
		for (int i = 0; i < weightStrings.length; i++)
		{
			weights[i] = Double.parseDouble(weightStrings[i]);
		}

		return weights;
	}

	
	public static void newcontroller()
	{
		NNPacMan controller = new NNPacMan();
		controller.trainNN();
		String data = Arrays.toString(controller.nn.getWeights());
		IO.saveFile(controller.nn.getName() + ".txt", data, false);
	}
}
