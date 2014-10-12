/**
 * 
 */
package pacman.entries.jcgrPacMan.NN;

import java.util.List;

import pacman.entries.jcgrPacMan.NN.training.TrainingData;
import pacman.entries.jcgrPacMan.NN.training.TrainingSet;

public class Backpropagator
{	
	/**
	 * The neural network to backpropagate on.
	 */
	private NeuralNetwork neuralNetwork;
	
	/**
	 * The number of samples to average for when checking for a terminating condition.
	 */
	private int samplesToAverageOver = 25;
	
	/**
	 * The current learning rate.
	 */
	private double currentLearningRate = 0.0;
	
	/**
	 * The initial learning rate.
	 */
	private double startingLearningRate = 1.0;
	
	/**
	 * The maximum number of epochs to run.
	 */
	private int maximumEpochs = 50000;
	
	/**
	 * The number of epochs to run before the learning rate is modified.
	 */
	private int epochLearningRateChange = 2500;
	
	/**
	 * The maximum change in weights for terminating the algorithm
	 */
	private double maxWeightChange = 0.00001; 
	
	/**
	 * The maximum threshold for errors (a terminating condition)
	 */
	private double errorThreshold = 0.005;
	
	/**
	 * The maximum number of misclassified training tuples (a terminating condition).
	 */
	private double maximumMisclassificationPercentage = 0.05;

	/**
	 * Creates a new instance of the Backpropagater class with the given values.
	 */
	public Backpropagator(NeuralNetwork nn)
	{
		this.neuralNetwork = nn;
	}

	/**
	 * Creates a new instance of the Backpropagater class with the given values.
	 */
	public Backpropagator(NeuralNetwork nn, double startingLearningRate, double errorThreshold, int maxEpochs,
			int epochsPerIteration, double maxWeightChange, double maxMisclassificationPercentage)
	{
		this(nn);
		this.startingLearningRate = startingLearningRate;
		this.maximumEpochs = maxEpochs;
		this.errorThreshold = errorThreshold;
		this.epochLearningRateChange = epochsPerIteration;
		this.maxWeightChange = maxWeightChange;
		this.maximumMisclassificationPercentage = maxMisclassificationPercentage;
	}

	/**
	 * Trains the neural network using the given training set.
	 * @param ts The training set.
	 */
	public void train(TrainingSet ts)
	{
		currentLearningRate = startingLearningRate;
		int epoch = 1;
		int learningIteration = 1;
		TerminatingConditions tc;

		// Average error variables
		double sumOfErrors = 0.0;
		double epochAverageError = (double) samplesToAverageOver;
		double[] errors = new double[samplesToAverageOver];
		do
		{
			// Back propagate
			tc = backpropagate(ts);

			// Remove previous sample from the error list.
			sumOfErrors -= (errors[epoch % samplesToAverageOver]);

			// Update error list with a new sample.
			errors[epoch % samplesToAverageOver] = tc.errorSum;

			// Add new sample to error list
			sumOfErrors += (errors[epoch % samplesToAverageOver]);

			// If we have enough samples, calculate the average error value.
			if (epoch > samplesToAverageOver)
				epochAverageError = sumOfErrors / samplesToAverageOver;

			epoch++;

			// If enough epochs have passed, decrease learning rate.
			if (epoch % epochLearningRateChange == 0)
			{
				learningIteration++;
				currentLearningRate = startingLearningRate / learningIteration;
			}
		} 
		// Check the various terminating conditions:
		while (epoch < maximumEpochs // 1. Amount of epochs run.
				&& epochAverageError > errorThreshold // 2. The average error vs. allowed threshold.
				&& tc.maxChange > maxWeightChange // 3. The maximum weight change vs. allowed change.
				&& tc.percentMisclassifiedTuples > maximumMisclassificationPercentage); // 4. The number of misclassified tuples vs. allowed value.
		System.out.println("Learned after " + epoch + " with error " + epochAverageError + " | " + tc.maxChange + " | " + tc.percentMisclassifiedTuples);
	}

	/**
	 * Runs one iteration of the backprogation algorithm on the given training set.
	 * @param ts The training set.
	 * @return The values related to terminating conditions.
	 */
	private TerminatingConditions backpropagate(TrainingSet ts)
	{
		double error = 0;
		double maxChange = 0;
		double misclassifiedTuples = 0.0;
		
		for (int i = 0; i <= ts.highestIndexOfSet(); i++)
		{
			TrainingData td = ts.getSpecificData(i);
			boolean misclassified = false;

			// Propagate the inputs forward
			neuralNetwork.setInputs(td.getInput());
			neuralNetwork.activate();

			// Backpropagate the errors
			List<NeuronLayer> neuronLayers = neuralNetwork.getLayers();
			int numberOfLayers = neuronLayers.size();

			// Calculate errors in output layer and update the weights
			// from the previous layer
			NeuronLayer outputLayer = neuronLayers.get(numberOfLayers - 1);
			for (int j = 0; j < outputLayer.getNeurons().size(); j++)
			{
				Neuron n = outputLayer.getNeurons().get(j);
				double output = n.getOutput();

				if (!misclassified)
					if (n.getOutput() != td.getOutput()[j])
						misclassified = true;

				double errorValue = output * (1 - output) * (td.getOutput()[j] - output);
				n.setError(errorValue);
				List<Synapse> synapses = n.getInputs();
				int bias = outputLayer.getPreviousLayer().hasBias() ? 1 : 0;

				if (bias == 1)
				{
					Synapse s = synapses.get(0);
					double deltaWeight = currentLearningRate * n.getError();
					s.setWeight(s.getWeight() + deltaWeight);
				}

				for (int sIndex = bias; sIndex < synapses.size(); sIndex++)
				{
					Synapse s = synapses.get(sIndex);
					Neuron sn = s.getSourceNeuron();

					double snErrIncrease = s.getWeight() * n.getError();
					double snErr = sn.getError() + snErrIncrease;
					sn.setError(snErr);

					double deltaWeight = currentLearningRate * n.getError() * sn.getOutput();
					s.setWeight(s.getWeight() + deltaWeight);

					if (maxChange < Math.abs(deltaWeight))
					{
						maxChange = Math.abs(deltaWeight);
					}
				}
			}

			// Calculate errors in hidden layers and update the weights
			// from the previous layers.
			for (int l = numberOfLayers - 2; l > 0; l--)
			{
				NeuronLayer tempLayer = neuronLayers.get(l);
				int bias = tempLayer.hasBias() ? 1 : 0;
				for (int j = bias; j < tempLayer.getNeurons().size(); j++)
				{
					Neuron n = tempLayer.getNeurons().get(j);
					double output = n.getOutput();
					double errorValue = output * (1 - output) * (n.getError());
					n.setError(errorValue);
					List<Synapse> synapses = n.getInputs();
					int previousLayerBias = tempLayer.getPreviousLayer().hasBias() ? 1 : 0;

					if (previousLayerBias == 1)
					{
						Synapse s = synapses.get(0);
						double deltaWeight = currentLearningRate * n.getError();
						s.setWeight(s.getWeight() + deltaWeight);
					}

					for (int sIndex = previousLayerBias; sIndex < synapses.size(); sIndex++)
					{
						Synapse s = synapses.get(sIndex);
						Neuron sn = s.getSourceNeuron();

						double snErrIncrease = s.getWeight() * n.getError();
						double snErr = sn.getError() + snErrIncrease;
						sn.setError(snErr);

						double deltaWeight = currentLearningRate * n.getError() * sn.getOutput();
						s.setWeight(s.getWeight() + deltaWeight);

						if (maxChange < Math.abs(deltaWeight))
						{
							maxChange = Math.abs(deltaWeight);
						}
					}
				}
			}

			double[] output = neuralNetwork.getOutput();
			error += sumError(output, td.getOutput());

			if (misclassified)
				misclassifiedTuples++;
		}

		// Create a terminating condition class and store the relevant values.
		TerminatingConditions tc = new TerminatingConditions();
		tc.errorSum = Math.abs(error);
		tc.percentMisclassifiedTuples = misclassifiedTuples / (((double) ts.highestIndexOfSet() + 1));
		tc.maxChange = maxChange;

		return tc;
	}

	/**
	 * Sum the errors between the expected output and the actual output.
	 * @param actual
	 * @param expected
	 * @return
	 */
	private double sumError(double[] actual, double[] expected)
	{
		double sum = 0;
		for (int i = 0; i < expected.length; i++)
			sum += Math.abs(expected[i] - actual[i]);

		return sum;
	}

	/**
	 * A class that stores values related to terminating conditions.
	 */
	private class TerminatingConditions
	{
		/**
		 * The sum of errors.
		 */
		public double errorSum;
		
		/**
		 * The percentage of misclassified tuples.
		 */
		public double percentMisclassifiedTuples;
		
		/**
		 * The maximum change in any weight. 
		 */
		public double maxChange;
	}
}