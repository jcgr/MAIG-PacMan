/**
 * 
 */
package pacman.entries.jcgrPacMan.NN;

import java.util.List;

import pacman.entries.jcgrPacMan.NN.training.PacManTrainingData;
import pacman.entries.jcgrPacMan.NN.training.TrainingData;
import pacman.entries.jcgrPacMan.NN.training.TrainingSet;
import dataRecording.DataSaverLoader;
import dataRecording.DataTuple;

public class Backpropagator
{
	public boolean log = false;
	private NeuralNetwork neuralNetwork;
	private double learningRate;
	private double startingLearningRate;
	private double currentEpoch;

	public Backpropagator(NeuralNetwork nn, double startingLearningRate)
	{
		this.neuralNetwork = nn;
		this.startingLearningRate = startingLearningRate;
		this.learningRate = startingLearningRate;
		this.currentEpoch = 0;
	}

	public void train(TrainingSet ts, double errorThreshold)
	{
		boolean terminatingCondition = true;

		double error;
		double sum = 0.0;
		double average = 25;
		int epoch = 1;
		int samples = 25;
		double[] errors = new double[samples];
		
		int learningIteration = 1;
		
		do
		{
			error = backpropagate(ts, errorThreshold);

			sum -= (errors[epoch % samples]);
			errors[epoch % samples] = error;
			sum += (errors[epoch % samples]);
			
			if (epoch > samples)
			{
				average = sum / samples;
			}

			epoch++;
			currentEpoch = epoch;
			
			if (currentEpoch % 2500 == 0)
			{
				learningIteration++;
				learningRate = 1.0 / learningIteration;
			}
			
			if (currentEpoch % 25 == 0)
//				System.out.println(currentEpoch + " = " + average + " ||| " + error);
			
			if (average < errorThreshold)
			{
				System.out.println("Woop, average (" + average + ") is lower than error after " + epoch + " epochs");
				break;
			}
//			System.out.println();
		} while (currentEpoch < 1000000);
//		} while (average > errorThreshold);
		
		System.out.println(average + " is average error " + learningRate);
//		System.out.println(Math.abs(errors[epoch % samples]) + " - " + sum / samples);
	}

	public double backpropagate(TrainingSet ts, double errorThreshold)
	{
		double error = 0;
		double change = 0;
		
//		System.out.println("Test");

		for (int i = 0; i <= ts.highestIndexOfSet(); i++)
		{
			TrainingData td = ts.getSpecificData(i);
			// Propagate the inputs forward
			neuralNetwork.setInputs(td.getInput());
			neuralNetwork.feedForward();

			// Backpropagate the errors
			List<NeuronLayer> neuronLayers = neuralNetwork.getLayers();
			int numberOfLayers = neuronLayers.size();

			// Calculate errors in output layer
			NeuronLayer outputLayer = neuronLayers.get(numberOfLayers - 1);
			for (int j = 0; j < outputLayer.getNeurons().size(); j++)
			{
				Neuron n = outputLayer.getNeurons().get(j);
				double output = n.getOutput();
				double err = output * (1 - output)
						* (td.getOutput()[j] - output);
				n.setError(err);

				List<Synapse> synapses = n.getInputs();
				int bias = outputLayer.getPreviousLayer().hasBias() ? 1 : 0;

				if (bias == 1)
				{
					Synapse s = synapses.get(0);

					double deltaWeight = learningRate * n.getError();
					s.setWeight(s.getWeight() + deltaWeight);
					
					change += deltaWeight;
					if (log) System.out.println(n.number + " bias change: " + s.getWeight());
				}
				
				for (int sIndex = bias; sIndex < synapses.size(); sIndex++)
				{
					Synapse s = synapses.get(sIndex);
					Neuron sn = s.getSourceNeuron();
					double snErrIncrease = s.getWeight() * n.getError();
					double snErr = sn.getError() + snErrIncrease;
					sn.setError(snErr);

					double deltaWeight = learningRate * n.getError()
							* sn.getOutput();
					s.setWeight(s.getWeight() + deltaWeight);
					
					change += deltaWeight;
					if (log) System.out.println("w" + sn.number + "" + n.number + " value: " + s.getWeight());
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

					double err = output * (1 - output) * (n.getError());
					n.setError(err);

					List<Synapse> synapses = n.getInputs();
					int previousLayerBias = tempLayer.getPreviousLayer()
							.hasBias() ? 1 : 0;

					if (previousLayerBias == 1)
					{
						Synapse s = synapses.get(0);

						double deltaWeight = learningRate * n.getError();
						s.setWeight(s.getWeight() + deltaWeight);
						
						change += deltaWeight;
						if (log) System.out.println(n.number + " bias change: " + s.getWeight());
					}
					
					for (int sIndex = previousLayerBias; sIndex < synapses
							.size(); sIndex++)
					{
						Synapse s = synapses.get(sIndex);
						Neuron sn = s.getSourceNeuron();
						double snErrIncrease = s.getWeight() * n.getError();
						double snErr = sn.getError() + snErrIncrease;
						sn.setError(snErr);

						double deltaWeight = learningRate * n.getError()
								* sn.getOutput();
						s.setWeight(s.getWeight() + deltaWeight);
						
						change += deltaWeight;
						if (log) System.out.println("w" + sn.number + "" + n.number + " value: " + s.getWeight());
					}
				}
			}

			double[] output = neuralNetwork.getOutput();
			error += sumError(output, td.getOutput());
//			for(double d : td.getOutput())
//			{
//				System.out.print(d + " - ");
//				if (d < 0 || d > 1)
//					System.out.print(" ERROR ");
//			}
//			System.out.println();
//			System.out.println(output[0] + " - " + td.getOutput()[0]);
		}

//		System.out.println(change);
//		return Math.abs(change);
		return Math.abs(error);

	}

	public double sumError(double[] actual, double[] expected)
	{
		if (actual.length != expected.length)
			throw new IllegalArgumentException(
					"The lengths of the actual and expected value arrays must be equal");

		double sum = 0;
		for (int i = 0; i < expected.length; i++)
				sum += Math.abs(expected[i] - actual[i]);

		return sum / 2;
	}
}