/**
 * 
 */
package pacman.entries.jcgrPacMan.EA;

import static pacman.game.Constants.DELAY;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Random;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.entries.jcgrPacMan.NN.NeuralNetwork;
import pacman.game.Game;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.util.IO;

/**
 * A class that evolves neural networks for PacMan.
 * 
 * @author Jacob
 */
public class Evolution
{
	/**
	 * The strategy of the ghosts during trial runs.
	 */
	private StarterGhosts ghostStrategy = new StarterGhosts();
	
	/**
	 * Random number generator for evolving networks.
	 */
	private Random random = new Random();
	
	/**
	 * The number of input nodes for the NN.
	 */
	private int NUMBER_OF_INPUT_NODES = 4;
	
	/**
	 * The nuber of hidden nodes in the NN.
	 */
	private int NUMBER_OF_HIDDEN_NODES = 5;
	
	/**
	 * The number of output nodes for the NN.
	 */
	private int NUMBER_OF_OUTPUT_NODES = 4;
	
	/**
	 * The number of generations to evolve over.
	 */
	private int GENERATIONS_TO_RUN = 50;
	
	/**
	 * The maximum age of a genotype, after which it is ignored, even if it
	 * is among the best candidates.
	 */
	private int MAX_AGE = 10;
	
	/**
	 * The maximum candidates being kept per evolution.
	 */
	private int CANDIDATES_PER_GENERATION = 20;
	
	/**
	 * The number of parents per generation.
	 */
	private int PARENTS_PER_GENERATION = 4;
	
	/**
	 * The number of trials to run when evaluating a NN.
	 */
	private int TRIALS_PER_EVALUATION = 100;
	
	/**
	 * The current genetion during evolution.
	 */
	private int generation = 0;
	
	/**
	 * The name of the file to save the evolved network to.
	 */
	private String fileName = "JcgrEvolution001.txt";

	/**
	 * A list of candidates from the previous generation.
	 */
	private List<Genotype> prevGenCandidates = new ArrayList<Genotype>();

	/**
	 * A list of candidates from the new generation.
	 */
	private List<Genotype> newGenCandidates = new ArrayList<Genotype>();

	/**
	 * A list of chosen parents.
	 */
	private List<Genotype> parents = new ArrayList<Genotype>();

	/**
	 * A list of that contains the scores of the candidates.
	 * Used for selecting which candidates to keep for the next generation.
	 */
	private List<Integer> results;
	
	/**
	 * Creates a new instance of the evolution class with default values.
	 */
	public Evolution()
	{
	}
	
	/**
	 * Creates a new instance of the evolution class with chosen nodes, 
	 * but otherwise default values.
	 * @param inputNodes The number of input nodes in the neural network to evolve.
	 * @param hiddenNodes The number of nodes in the hidden layer in the neural network to evolve.
	 * @param outputNodes The number of out nodes in the neural network to evolve.
	 */
	public Evolution(int inputNodes, int hiddenNodes, int outputNodes)
	{
		this.NUMBER_OF_INPUT_NODES = inputNodes;
		this.NUMBER_OF_HIDDEN_NODES = hiddenNodes;
		this.NUMBER_OF_OUTPUT_NODES = outputNodes;
	}
	
	/**
	 * Creates a new instance of the evolution class with the given values.
	 * @param inputNodes The number of input nodes in the neural network to evolve.
	 * @param hiddenNodes The number of nodes in the hidden layer in the neural network to evolve.
	 * @param outputNodes The number of out nodes in the neural network to evolve.
	 * @param candidatesPerGeneration The number of candidates to keep per generation.
	 * @param parentsPerGeneration The number of parents to select for the next generation.
	 * @param generations The number of generations to run.
	 * @param trialsPerEvaulation The number of trials to run when evaulating a candidate.
	 * @param maxAge The maximum allowed age of a candidate before it is ignored when
	 * 				 looking for candidates to keep for next generation.
	 * @param fileName The filename to save the evolved NN to (can be left blank if
	 * 				   the .evolveAndReturn() is used instead of .evolveAndSave().
	 */
	public Evolution(int inputNodes, int hiddenNodes, int outputNodes
			, int candidatesPerGeneration, int parentsPerGeneration
			, int generations, int trialsPerEvaulation, int maxAge
			, String fileName)
	{
		this.NUMBER_OF_INPUT_NODES = inputNodes;
		this.NUMBER_OF_HIDDEN_NODES = hiddenNodes;
		this.NUMBER_OF_OUTPUT_NODES = outputNodes;
		
		this.CANDIDATES_PER_GENERATION = candidatesPerGeneration;
		this.PARENTS_PER_GENERATION = parentsPerGeneration;
		
		this.GENERATIONS_TO_RUN = generations;
		this.TRIALS_PER_EVALUATION = trialsPerEvaulation;
		this.MAX_AGE = maxAge;
	}
	
	/**
	 * Creates and evolves neural networks. When the evolution is done,
	 * the weights of the best NN is returned.
	 * @return The weights of the best NN evolved.
	 */
	public double[] evolveAndReturn()
	{
		evolve();
		
		Genotype finalGT = null; 
		
		results = new ArrayList<Integer>();
		for (Genotype gt : prevGenCandidates)
			results.add(gt.getFitness());

		// Add all scores to a list and sort it in descending order.
		List<Integer> highestValues = new ArrayList<Integer>();
		highestValues.addAll(results);
		Collections.sort(highestValues);
		Collections.reverse(highestValues);
		
		// Grab the highest score and look for the matching candidate.
		int resultToLookFor = highestValues.remove(0);
		for (int k = 0; k < results.size(); k++)
		{
			if (results.get(k) == (int)resultToLookFor)
			{
				finalGT = prevGenCandidates.get(k);
				break;
			}
		}
		
		// Return the best genotype
		return finalGT.values;
	}
	
	/**
	 * Creates and evolves neural networks. When the evolution is done,
	 * the weights of the best NN are saved to a file.
	 */
	public void evolveAndSave()
	{
		evolve();
		
		saveBestGenotype();
	}
	
	/**
	 * The evolution part of the algorithm.
	 */
	private void evolve()
	{
		print("-----------------");
		print("Starting evolution");
		createInitialCandidates();
		evaluateInitialCandidates();
		selectIndividualsForNextGeneration();
		generation++;
		
		print("Initial candidates: ");
		printCandidates(prevGenCandidates);
		print("-----------------");
		
		while (generation < GENERATIONS_TO_RUN)
		{
			selectParents();
			mutation();
			evaluateNewCandidates();
			selectIndividualsForNextGeneration();
			
			generation++;
			print("Iteration " + generation + " done");
			print("-----------------");
		}
		
		print("Evolution over");
		print("Final candidates");
		printCandidates(prevGenCandidates);
		print("-----------------");
	}
	
	/**
	 * Saves the best genotype to a file.
	 */
	private void saveBestGenotype()
	{
		Genotype finalGT = null; 
		
		results = new ArrayList<Integer>();
		for (Genotype gt : prevGenCandidates)
			results.add(gt.getFitness());

		// Add all scores to a list and sort it in descending order.
		List<Integer> highestValues = new ArrayList<Integer>();
		highestValues.addAll(results);
		Collections.sort(highestValues);
		Collections.reverse(highestValues);
		
		// Grab the highest score and look for the matching candidate.
		int resultToLookFor = highestValues.remove(0);
		for (int k = 0; k < results.size(); k++)
		{
			if (results.get(k) == (int)resultToLookFor)
			{
				finalGT = prevGenCandidates.get(k);
				break;
			}
		}
		
		print("Saving NN with score " + finalGT.getFitness());

		// Save the candidate.
		String data = Arrays.toString(finalGT.values);
		IO.saveFile(fileName, data, false);
	}
	
	/**
	 * Selects which candidates that should make it to the next generation.
	 */
	private void selectIndividualsForNextGeneration()
	{
		// Add candidates from the new generation and the old generation to a list.
		List<Genotype> allCandidates = new ArrayList<Genotype>();
		allCandidates.addAll(prevGenCandidates);
		allCandidates.addAll(newGenCandidates);
		
		// Save the genotypes results in a list...
		results = new ArrayList<Integer>();
		for (Genotype gt : allCandidates)
			results.add(gt.getFitness());
		
		// ... and sort it in descending order.
		List<Integer> sortedResults = new ArrayList<Integer>(); 
		sortedResults.addAll(results);
		Collections.sort(sortedResults);
		Collections.reverse(sortedResults);
		
		// Create a list and populate it with the best candidate from the
		// new and old generation.
		List<Genotype> currGen = new ArrayList<Genotype>();
		while (currGen.size() < CANDIDATES_PER_GENERATION)
		{
			int scoreToLookFor = sortedResults.remove(0);
			Genotype tempGT = null;

			for (int gt = 0; gt < allCandidates.size(); gt++)
			{
				if (allCandidates.get(gt).getFitness() == scoreToLookFor)
				{
					tempGT = allCandidates.remove(gt);
					break;
				}
			}
			
			// If a genotype is too old, ignore it.
			if (tempGT.getAge(generation) < MAX_AGE)
				currGen.add(tempGT);
		}
		
		prevGenCandidates = new ArrayList<Genotype>();
		prevGenCandidates.addAll(currGen);
	}
	
	/**
	 * Select the parents to use for the next generation.
	 */
	private void selectParents()
	{
		parents = new ArrayList<Genotype>();

		// Save the genotypes results in a list...
		results = new ArrayList<Integer>();
		for (Genotype gt : prevGenCandidates)
			results.add(gt.getFitness());

		// ... and sort it in descending order.
		List<Integer> highestValues = new ArrayList<Integer>();
		highestValues.addAll(results);
		Collections.sort(highestValues);
		Collections.reverse(highestValues);
		
		// Find the highest scoring genotypes and use them as parents.
		for (int i = 0; i < PARENTS_PER_GENERATION; i++)
		{
			int resultToLookFor = highestValues.remove(0);
			
			for (int k = 0; k < results.size(); k++)
			{
				if (results.get(k) == (int)resultToLookFor)
				{
					parents.add(prevGenCandidates.get(k));
					break;
				}
			}
		}
	}
	
	/**
	 * Creates mutations of the parents.
	 */
	private void mutation()
	{
		newGenCandidates = new ArrayList<Genotype>();
		
		// Select a parent, create a mutation, add it to the new generation
		// and repeat until enough candidates have been created.
		int currentParent = 0;
		while (newGenCandidates.size() < CANDIDATES_PER_GENERATION)
		{
			double[] parentValues = parents.get(currentParent).values;
			double[] mutatedValues = mutate(parentValues);
			
			newGenCandidates.add(new Genotype(mutatedValues, generation));
			
			currentParent++;
			if(currentParent >= PARENTS_PER_GENERATION)
				currentParent = 0;
		}
	}
	
	/**
	 * Returns a mutation of the given genotype.
	 * @param genotype A genotype.
	 * @return A mutation of the genotype.
	 */
	private double[] mutate(double[] genotype)
	{
		double[] mutatedValues = new double[genotype.length];
		
		for (int value = 0; value < mutatedValues.length; value++)
			mutatedValues[value] = genotype[value] + ((random.nextDouble() * 1.0) - 0.5);
		
		return mutatedValues;
	}
	
	/**
	 * Evaluates candidates from the new generation.
	 */
	private void evaluateNewCandidates()
	{
		for (int i = 0; i < newGenCandidates.size(); i++)
		{
			// Create a NN using the weights saved in the genotype ...
			Genotype gt = newGenCandidates.get(i);
			NeuralNetwork nn = NeuralNetwork.createSingleHiddenLayerNeuralNetwork(
					"tempName", NUMBER_OF_INPUT_NODES, NUMBER_OF_OUTPUT_NODES, NUMBER_OF_HIDDEN_NODES);
			
			nn.setWeights(gt.values);
			
			// ... and run trials to find the fitness of the genotype.
			EvolutionTestController etc = new EvolutionTestController(
					nn, NUMBER_OF_INPUT_NODES, NUMBER_OF_HIDDEN_NODES, NUMBER_OF_OUTPUT_NODES);
			
			gt.setFitness((int)this.runExperiment(etc, ghostStrategy, TRIALS_PER_EVALUATION));
		}
	}
	
	/**
	 * Evaluates the initial candidates. Only used once.
	 */
	private void evaluateInitialCandidates()
	{
		// For every genotype in the previous generation (which is the first generation
		// initially)
		for (int i = 0; i < prevGenCandidates.size(); i++)
		{
			// Create a NN using the weights saved in the genotype ...
			Genotype gt = prevGenCandidates.get(i);
			NeuralNetwork nn = NeuralNetwork.createSingleHiddenLayerNeuralNetwork(
					"tempName", NUMBER_OF_INPUT_NODES, NUMBER_OF_OUTPUT_NODES, NUMBER_OF_HIDDEN_NODES);
			
			nn.setWeights(gt.values);

			// Create a NN using the weights saved in the genotype ...
			EvolutionTestController etc = new EvolutionTestController(
					nn, NUMBER_OF_INPUT_NODES, NUMBER_OF_HIDDEN_NODES, NUMBER_OF_OUTPUT_NODES);
			
			gt.setFitness((int)this.runExperiment(etc, ghostStrategy, TRIALS_PER_EVALUATION));
		}
	}
	
	/**
	 * Creates the initial candidates for the evolution.
	 */
	private void createInitialCandidates()
	{
		prevGenCandidates = new ArrayList<Genotype>();
		
		// Figure out how many weights we are dealing with.
		int numberOfWeights = NeuralNetwork.createSingleHiddenLayerNeuralNetwork(
				"tempName", NUMBER_OF_INPUT_NODES, NUMBER_OF_OUTPUT_NODES, NUMBER_OF_HIDDEN_NODES).getWeights().length;
		
		// Create genotypes with randomized values.
		for (int i = 0; i < CANDIDATES_PER_GENERATION; i++)
		{
			double[] genotypeValues = new double[numberOfWeights];
			
			for (int k = 0; k < numberOfWeights; k++)
				genotypeValues[k] = ((random.nextDouble() * 2.0) - 1.0);
			
			prevGenCandidates.add(new Genotype(genotypeValues, generation));
		}
	}
	
	/**
	 * Prints the fitness of the given genotypes to the console for debugging reasons.
	 * @param cands The candidates to print the fitness for.
	 */
	private void printCandidates(List<Genotype> cands)
	{
		for (Genotype gt : cands)
			System.out.println(gt.getFitness());
	}
	
	/**
	 * Prints the given string to the console. Easy way of controlling whether to write anything or not.
	 * @param s The string
	 */
	private void print(String s)
	{
		System.out.println(s);
	}
	
	/**
     * Runs multiple trials with the given controller and returns the average score.
     * Taken from the Executor.java class and modified to return the average score.
     *
     * @param pacManController The Pac-Man controller
     * @param ghostController The Ghosts controller
     * @param trials The number of trials to be executed
	 * @return The average score of the trials.
	 */
	private double runExperiment(Controller<MOVE> pacManController, Controller<EnumMap<GHOST,MOVE>> ghostController, int trials)
    {
		double avgScore = 0;

		Random rnd = new Random(0);
		Game game;

		for (int i = 0; i < trials; i++)
		{
			game = new Game(rnd.nextLong());

			while (!game.gameOver())
				game.advanceGame(pacManController.getMove(game.copy(), System.currentTimeMillis() + DELAY),
						ghostController.getMove(game.copy(), System.currentTimeMillis() + DELAY));

			avgScore += game.getScore();
		}

		return avgScore / trials;
    }
}
