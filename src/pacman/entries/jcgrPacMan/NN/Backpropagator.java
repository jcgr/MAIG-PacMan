/**
 * 
 */
package pacman.entries.jcgrPacMan.NN;

import java.util.List;

import pacman.entries.jcgrPacMan.NN.training.TrainingData;
import pacman.entries.jcgrPacMan.NN.training.TrainingSet;

public class Backpropagator
{
	private boolean debugLogging = false;
	private NeuralNetwork neuralNetwork; // Neural network to train
	private int samplesToAverageOver = 25; // Number of samples to average errors over.
	private double learningRate = 0.0;
	private double startingLearningRate = 1.0; // Starting learning rate for the backpropagation algorithm
	private double errorThreshold = 0.05; // Learning stops after average error is below this value.
	private int maximumEpochs = 10000; // Maximum number of epochs to train on.
	private int epochsPerIteration = 2500; // Amount of epochs per learning iteration.
	private double maxWeightChange = 0.001; // Maximum amount of change in a single weight in the neural network during
	// training.
	private double maximumMisclassificationPercentage = 0.05; // Maximum amount of misclassified tuples allowed in an epoch.

	public Backpropagator(NeuralNetwork nn)
	{
		this.neuralNetwork = nn;
	}

	public Backpropagator(NeuralNetwork nn, double startingLearningRate, double errorThreshold, int maxEpochs,
			int epochsPerIteration, double maxWeightChange, double maxMisclassificationPercentage)
	{
		this(nn);
		this.startingLearningRate = startingLearningRate;
		this.maximumEpochs = maxEpochs;
		this.errorThreshold = errorThreshold;
		this.epochsPerIteration = epochsPerIteration;
		this.maxWeightChange = maxWeightChange;
		this.maximumMisclassificationPercentage = maxMisclassificationPercentage;
	}

	public void train(TrainingSet ts)
	{
		// Initialize learning rate
		learningRate = startingLearningRate;

		// Current epoch
		int epoch = 1;

		// Current learning iteration
		int learningIteration = 1;

		// Terminating condition variables
		TerminatingConditions tc;

		// Average error variables
		double errorSumOfSamples = 0.0;
		double epochAverageError = (double) samplesToAverageOver;
		double[] errors = new double[samplesToAverageOver];
		do
		{
			// Back propagate
			tc = backpropagate(ts);

			// Remove previous sample from error sum
			errorSumOfSamples -= (errors[epoch % samplesToAverageOver]);

			// Update sample value with new error value
			errors[epoch % samplesToAverageOver] = tc.errorSum;

			// Add new sample to error sum
			errorSumOfSamples += (errors[epoch % samplesToAverageOver]);

			// If enough samples have been collected, start calculating average.
			if (epoch > samplesToAverageOver)
			{
				epochAverageError = errorSumOfSamples / samplesToAverageOver;
			}

			// Increase epoch
			epoch++;

			// If enough epochs have passed, decrease learning rate.
			if (epoch % epochsPerIteration == 0)
			{
				learningIteration++;
				learningRate = startingLearningRate / learningIteration;
			}

			if (debugLogging)
				if (epoch % samplesToAverageOver == 0)
					System.out.println(epoch + " = " + epochAverageError + " ||| " + tc.errorSum);

		} while (epoch < maximumEpochs // Continue training until maximum epochs have passed
				&& epochAverageError > errorThreshold // or average error is less than or equal to error threshold
				&& tc.maxChange > maxWeightChange // or maximum change in weights is less than or equal to threshold
				&& tc.percentMisclassifiedTuples > maximumMisclassificationPercentage); // or percentage of misclassified tuples
	}

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

			// Calculate errors in output layer
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
					double deltaWeight = learningRate * n.getError();
					s.setWeight(s.getWeight() + deltaWeight);
				}

				for (int sIndex = bias; sIndex < synapses.size(); sIndex++)
				{
					Synapse s = synapses.get(sIndex);
					Neuron sn = s.getSourceNeuron();

					double snErrIncrease = s.getWeight() * n.getError();
					double snErr = sn.getError() + snErrIncrease;
					sn.setError(snErr);

					double deltaWeight = learningRate * n.getError() * sn.getOutput();
					s.setWeight(s.getWeight() + deltaWeight);

					if (maxChange < Math.abs(deltaWeight))
					{
						maxChange = Math.abs(deltaWeight);
					}
				}
			}

			// Calculate errors in hidden layers
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
						double deltaWeight = learningRate * n.getError();
						s.setWeight(s.getWeight() + deltaWeight);
					}

					for (int sIndex = previousLayerBias; sIndex < synapses.size(); sIndex++)
					{
						Synapse s = synapses.get(sIndex);
						Neuron sn = s.getSourceNeuron();

						double snErrIncrease = s.getWeight() * n.getError();
						double snErr = sn.getError() + snErrIncrease;
						sn.setError(snErr);

						double deltaWeight = learningRate * n.getError() * sn.getOutput();
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

		TerminatingConditions tc = new TerminatingConditions();
		tc.errorSum = Math.abs(error);
		tc.percentMisclassifiedTuples = misclassifiedTuples / (((double) ts.highestIndexOfSet() + 1));
		tc.maxChange = maxChange;

		return tc;
	}

	private double sumError(double[] actual, double[] expected)
	{
		if (actual.length != expected.length)
			throw new IllegalArgumentException(
					"The lengths of the actual and expected value arrays must be equal");

		double sum = 0;
		for (int i = 0; i < expected.length; i++)
			sum += Math.abs(expected[i] - actual[i]);

		return sum;
	}

	private class TerminatingConditions
	{
		public double errorSum;
		public double percentMisclassifiedTuples;
		public double maxChange;
	}
}