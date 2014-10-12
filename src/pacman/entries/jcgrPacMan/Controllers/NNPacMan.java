/**
 * 
 */
package pacman.entries.jcgrPacMan.Controllers;

import java.util.Arrays;

import pacman.controllers.Controller;
import pacman.entries.jcgrPacMan.DataRecording.JcgrDataSaverLoader;
import pacman.entries.jcgrPacMan.DataRecording.JcgrDataTuple;
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
 * A controller that uses neural networks for decision making.
 * Can be used for neural networks evolved by evolutionary algorithms.
 * 
 * @author Jacob
 */
public class NNPacMan extends Controller<MOVE>
{
	/**
	 * Determines if the weights for the NN should be loaded or
	 * be trained based on training data. 
	 */
	private boolean loadWeights = true;
	
	/**
	 * Determines if the weights to load come from a file or from an array. 
	 */
	private boolean loadArray = false;
	
	/**
	 * The name of the file to load weights from.
	 */
	private String fileToLoad = "PacMan.txt";
	
	/**
	 * The number of input nodes.
	 */
	private int numberOfInputs = 4;
	
	/**
	 * The number of output nodes.
	 */
	private int numberOfOutputs = 5;
	
	/**
	 * The number of nodes in the hidden layer.
	 */
	private int hiddenLayerNeurons = 3;
	
	/**
	 * The neural network used by the controller.
	 */
	private NeuralNetwork nn;

	public NNPacMan()
	{
		nn = NeuralNetwork.createSingleHiddenLayerNeuralNetwork(
				"Test", numberOfInputs, numberOfOutputs, hiddenLayerNeurons);
		if (loadWeights)
			nn.setWeights(loadWeights());
	}

	/**
	 * Trains a NN using the backpropagation algorithm.
	 */
	private void trainNN()
	{
		nn = new NeuralNetwork("PacMan");
		Neuron bias = new Neuron();
		bias.setOutput(1.0);
		
		JcgrDataTuple[] dta = JcgrDataSaverLoader.LoadPacManData();
		
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
		
		Backpropagator bp = new Backpropagator(nn);
		bp.train(ts);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pacman.controllers.Controller#getMove(pacman.game.Game, long)
	 */
	public MOVE getMove(Game game, long timeDue)
	{
		double[] input = new double[numberOfInputs];
		JcgrDataTuple dt = new JcgrDataTuple(game, lastMove);

		input[0] = dt.normalizeDistance(dt.nearestPillDistance);
		input[1] = dt.moveToDouble(dt.nearestPillDirection);
		input[2] = dt.normalizeDistance(dt.nearestGhostDistance);
		input[3] = dt.moveToDouble(dt.nearestGhostDirection);

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
		return chosenMove;
	}

	/**
	 * Loads weights, either from a file or from an array.
	 * @return
	 */
	private double[] loadWeights()
	{
		if (loadArray)
		{
			// Weights from an evolved network.
			double[] weights =
			{ -1.352210467429133, 0.693770631657026, 1.9828646884970138, -1.191542333775948, 2.404301946875892,
					-0.3111630832243133, -0.9363967943806176, -1.396583970996491, 1.386299804100525, 1.8574552199050305,
					-5.736154913663062, 5.567909935190945, 3.26647419404033, 4.908600358771296, -0.6448661585415818,
					0.4724395852888629, 0.3940164090631493, -0.16388258462721317, -6.4405764728948025, 1.9377073959388058,
					-4.977755081880498, 1.2323595110446268, -2.4175111332236274, 3.1594050177553763, 3.0528958655033325,
					3.5101911776309587, -0.5618166079719616, 1.2448703349646864, -2.5839954509132754, 0.973814308316551,
					-1.6100060954494082, 1.141627249151182, 1.8146553012500917, 0.26579475967433663, -3.5376878502334366,
					2.8653566518933054, -2.5445186536689643, -1.1641744542369226, 1.8810756604993295, 1.493638396150355,
					1.5586451533893482, -2.273822027022458, -2.5080987516252247, 2.1818423134510154, 1.8951333175941076,
					-4.850538476059338, 1.6735675563476144, 0.2788359678849046, 2.3345495939195833 };
			return weights;
		}
		
		String s = IO.loadFile(fileToLoad);
		String ss = s.substring(1, s.length() - 2);
		
		String[] weightStrings = ss.split(", ");
		double[] weights = new double[weightStrings.length];
		
		for (int i = 0; i < weightStrings.length; i++)
			weights[i] = Double.parseDouble(weightStrings[i]);

		return weights;
	}
	
	/**
	 * Creates a new controller, trains it using backpropagation and saves it to a file.
	 */
	public static void newcontroller()
	{
		NNPacMan controller = new NNPacMan();
		controller.trainNN();
		String data = Arrays.toString(controller.nn.getWeights());
		IO.saveFile("Jcgr" + controller.nn.getName() + "BackpropagatedNN.txt", data, false);
	}
}
