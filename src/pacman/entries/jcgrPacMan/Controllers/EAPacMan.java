/**
 * 
 */
package pacman.entries.jcgrPacMan.Controllers;

import pacman.controllers.Controller;
import pacman.entries.jcgrPacMan.DataRecording.JcgrDataTuple;
import pacman.entries.jcgrPacMan.EA.Evolution;
import pacman.entries.jcgrPacMan.NN.NeuralNetwork;
import pacman.game.Game;
import pacman.game.Constants.MOVE;

/**
 * A controller that uses evolutionary elgorithms to evolve
 * a neural network, which is then used to play PacMan.
 * 
 * To use a saved neural network, use the NNPacMan controller instead.
 * 
 * @author Jacob
 */
public class EAPacMan extends Controller<MOVE>
{
	/**
	 * The number of input nodes.
	 */
	int numberOfInputs;
	
	/**
	 * The number of output nodes.
	 */
	int numberOfOutputs;
	
	/**
	 * The number of nodes in the hidden layer.
	 */
	int hiddenLayerNeurons;
	
	/**
	 * The neural network used by the controller.
	 */
	NeuralNetwork nn;

	/**
	 * Creates a new instance of the EAPacMan controller with the given values.
	 * @param inputNodes The number of input nodes in the neural network.
	 * @param hiddenNodes The number of nodes in the hidden layer in the neural network.
	 * @param outputNodes The number of out nodes in the neural network.
	 */
	public EAPacMan(int inputNodes, int hiddenLayerNodes, int outputNodes)
	{
		this.numberOfInputs = inputNodes;
		this.hiddenLayerNeurons = hiddenLayerNodes;
		this.numberOfOutputs = outputNodes;
		
		Evolution evo = new Evolution(numberOfInputs, hiddenLayerNeurons, numberOfOutputs);
		double[] weights = evo.evolveAndReturn();
		
		nn = NeuralNetwork.createSingleHiddenLayerNeuralNetwork(
				"EvolvedNN", numberOfInputs, numberOfOutputs, hiddenLayerNeurons);
		nn.setWeights(weights);
	}
	
	/**
	 * Creates a new instance of the EAPacMan controller class and evolves a neural
	 * network using the the given values.
	 * 
	 * @param inputNodes The number of input nodes in the neural network to evolve.
	 * @param hiddenNodes The number of nodes in the hidden layer in the neural network to evolve.
	 * @param outputNodes The number of out nodes in the neural network to evolve.
	 * @param candidatesPerGeneration The number of candidates to keep per generation.
	 * @param parentsPerGeneration The number of parents to select for the next generation.
	 * @param generations The number of generations to run.
	 * @param trialsPerEvaulation The number of trials to run when evaulating a candidate.
	 * @param maxAge The maximum allowed age of a candidate before it is ignored when
	 * 				 looking for candidates to keep for next generation.
	 */
	public EAPacMan(int inputNodes, int hiddenNodes, int outputNodes
			, int candidatesPerGeneration, int parentsPerGeneration
			, int generations, int trialsPerEvaulation, int maxAge)
	{

		Evolution evo = new Evolution(numberOfInputs, hiddenLayerNeurons, numberOfOutputs
				, candidatesPerGeneration, parentsPerGeneration
				, generations, trialsPerEvaulation, maxAge
				, "EvolvedNN.txt");
		double[] weights = evo.evolveAndReturn();
		
		nn = NeuralNetwork.createSingleHiddenLayerNeuralNetwork(
				"EvolvedNN", numberOfInputs, numberOfOutputs, hiddenLayerNeurons);
		nn.setWeights(weights);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see pacman.controllers.Controller#getMove(pacman.game.Game, long)
	 */
	public MOVE getMove(Game game, long timeDue)
	{
		// Get data based on the game.
		JcgrDataTuple dt = new JcgrDataTuple(game, lastMove);

		// Converts the data tuple to an input array.
		double[] input = new double[numberOfInputs];
		input[0] = dt.normalizeDistance(dt.nearestPillDistance);
		input[1] = dt.moveToDouble(dt.nearestPillDirection);
		input[2] = dt.normalizeDistance(dt.nearestGhostDistance);
		input[3] = dt.moveToDouble(dt.nearestGhostDirection);

		// Runs the neural network
		nn.setInputs(input);
		nn.activate();

		// Gets the output and converts it to a MOVE value.
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
}
