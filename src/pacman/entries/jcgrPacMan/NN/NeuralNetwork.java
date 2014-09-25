/**
 * 
 */
package pacman.entries.jcgrPacMan.NN;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that represents an entire neural network.
 * 
 * @author Jacob
 */
public class NeuralNetwork
{
	/**
	 * The layers of the network.
	 */
	private List<NeuronLayer> layers;

	/**
	 * The input layer.
	 */
	private NeuronLayer input;

	/**
	 * The output layer.
	 */
	private NeuronLayer output;

	/**
	 * Creates a new neural network with an empty
	 * list of layers.
	 */
	public NeuralNetwork()
	{
		layers = new ArrayList<NeuronLayer>();
	}

	/**
	 * Adds a layer to the neural network.
	 * @param layer The layer to add.
	 */
	public void addLayer(NeuronLayer layer)
	{
		layers.add(layer);

		// If it is the first layer being added,
		// set it as the input layer as well.
		if (layers.size() == 1)
		{
			input = layer;
		}

		if (layers.size() > 1)
		{
			// Clear the output flag on the previous output layer, but only if
			// we have more than 1 layer
			NeuronLayer previousLayer = layers.get(layers.size() - 2);
			previousLayer.setNextLayer(layer);
		}

		// Set the output layer to the last layer in the list.
		output = layers.get(layers.size() - 1);
	}

	/**
	 * Set the input values in the input layer to the given values.
	 * @param inputs The new input values.
	 */
	public void setInputs(double[] inputs)
	{
		if (input != null)
		{

			int biasCount = input.hasBias() ? 1 : 0;

			if (input.getNeurons().size() - biasCount != inputs.length)
			{
				throw new IllegalArgumentException(
						"The number of inputs must equal the number of neurons in the input layer");
			}

			else
			{
				List<Neuron> neurons = input.getNeurons();
				for (int i = biasCount; i < neurons.size(); i++)
				{
					neurons.get(i).setOutput(inputs[i - biasCount]);
				}
			}
		}
	}
	
	/**
	 * Runs the neural network.
	 */
	public void run()
	{
		for (int i = 1; i < layers.size(); i++)
		{
			NeuronLayer layer = layers.get(i);
			layer.feedForward();
		}
	}

	/**
	 * Gets the output(s) for the neural network.
	 * @return The output(s).
	 */
	public double[] getOutput()
	{
		double[] outputs = new double[output.getNeurons().size()];

		int i = 0;
		for (Neuron neuron : output.getNeurons())
		{
			outputs[i] = neuron.getOutput();
			i++;
		}

		return outputs;
	}

	/**
	 * Resets all weights in the neural network.
	 */
	public void reset()
	{
		for (NeuronLayer layer : layers)
		{
			for (Neuron neuron : layer.getNeurons())
			{
				for (Synapse synapse : neuron.getInputs())
				{
					synapse.setWeight((Math.random() * 1) - 0.5);
				}
			}
		}
	}

	/**
	 * Gets the list of layers in the network.
	 * @return
	 */
	public List<NeuronLayer> getLayers()
	{
		return layers;
	}

	/**
	 * Gets all weights in the neural network.
	 * @return A double array containing all weights in the neural network.
	 */
	public double[] getWeights()
	{
		List<Double> weights = new ArrayList<Double>();

		// Get the weight from every synapse in every neuron in every layer.
		for (NeuronLayer layer : layers)
			for (Neuron neuron : layer.getNeurons())
				for (Synapse synapse : neuron.getInputs())
					weights.add(synapse.getWeight());

		// Convert the list to an array.
		double[] allWeights = new double[weights.size()];

		int i = 0;
		for (Double weight : weights)
		{
			allWeights[i] = weight;
			i++;
		}

		return allWeights;
	}

	/**
	 * Creates a perfect deep-copy of the neural network
	 * @return A perfect copy of the neural network.
	 */
	public NeuralNetwork copy()
	{
		NeuralNetwork copy = new NeuralNetwork();

		NeuronLayer previousLayer = null;
		for (NeuronLayer layer : layers)
		{
			NeuronLayer layerCopy;

			if (layer.hasBias())
			{
				Neuron bias = layer.getNeurons().get(0);
				Neuron biasCopy = new Neuron();
				biasCopy.setOutput(bias.getOutput());
				layerCopy = new NeuronLayer(null, biasCopy);
			}

			else
			{
				layerCopy = new NeuronLayer();
			}

			layerCopy.setPreviousLayer(previousLayer);

			int biasCount = layerCopy.hasBias() ? 1 : 0;

			for (int i = biasCount; i < layer.getNeurons().size(); i++)
			{
				Neuron neuron = layer.getNeurons().get(i);

				Neuron neuronCopy = new Neuron();
				neuronCopy.setOutput(neuron.getOutput());
				neuronCopy.setError(neuron.getError());

				if (neuron.getInputs().size() == 0)
				{
					layerCopy.addNeuron(neuronCopy);
				}

				else
				{
					double[] weights = neuron.getWeights();
					layerCopy.addNeuron(neuronCopy, weights);
				}
			}

			copy.addLayer(layerCopy);
			previousLayer = layerCopy;
		}

		return copy;
	}

	/**
	 * Copies the weights from another neural network into the
	 * current one, assuming they have the same size.
	 * @param sourceNeuralNetwork The nn to copy weights from.
	 */
	public void copyWeightsFrom(NeuralNetwork sourceNeuralNetwork)
	{
		if (layers.size() != sourceNeuralNetwork.layers.size())
		{
			throw new IllegalArgumentException(
					"Cannot copy weights. Number of layers do not match ("
							+ sourceNeuralNetwork.layers.size()
							+ " in source versus " + layers.size()
							+ " in destination)");
		}

		int i = 0;
		for (NeuronLayer sourceLayer : sourceNeuralNetwork.layers)
		{
			NeuronLayer destinationLayer = layers.get(i);

			if (destinationLayer.getNeurons().size() != sourceLayer
					.getNeurons().size())
			{
				throw new IllegalArgumentException(
						"Number of neurons do not match in layer " + (i + 1)
								+ "(" + sourceLayer.getNeurons().size()
								+ " in source versus "
								+ destinationLayer.getNeurons().size()
								+ " in destination)");
			}

			int j = 0;
			for (Neuron sourceNeuron : sourceLayer.getNeurons())
			{
				Neuron destinationNeuron = destinationLayer.getNeurons().get(j);

				if (destinationNeuron.getInputs().size() != sourceNeuron
						.getInputs().size())
				{
					throw new IllegalArgumentException(
							"Number of inputs to neuron " + (j + 1)
									+ " in layer " + (i + 1)
									+ " do not match ("
									+ sourceNeuron.getInputs().size()
									+ " in source versus "
									+ destinationNeuron.getInputs().size()
									+ " in destination)");
				}

				int k = 0;
				for (Synapse sourceSynapse : sourceNeuron.getInputs())
				{
					Synapse destinationSynapse = destinationNeuron.getInputs()
							.get(k);

					destinationSynapse.setWeight(sourceSynapse.getWeight());
					k++;
				}

				j++;
			}

			i++;
		}
	}
	
	/*
	 * public static NeuralNetwork backpropgate( ArrayList<ArrayList<Double>>
	 * inputs, ArrayList<ArrayList<Double>> expectedOutputs, double
	 * learningRate, NeuralNetwork network) {
	 * 
	 * boolean terminatingCondition = false;
	 * 
	 * if (inputs.size() != expectedOutputs.size()) {
	 * System.out.println("FAILED!!!!"); return null; }
	 * 
	 * for (int trainingSet = 0; trainingSet < inputs.size(); trainingSet++) {
	 * if (terminatingCondition) break;
	 * 
	 * HashMap<Neuron, Double> errors = new HashMap<Neuron, Double>();
	 * 
	 * // Propagate the inputs forward (4-9)
	 * network.update(inputs.get(trainingSet));
	 * 
	 * // Backpropagate the errors // Output layer (11-12) NeuronLayer nLayer =
	 * network.neuronLayers.get(network.neuronLayers .size() - 1); for (int
	 * neuron = 0; neuron < nLayer.neurons.size(); neuron++) { Neuron n =
	 * nLayer.neurons.get(neuron); double err = (n.output * (1.0 - n.output))
	 * (expectedOutputs.get(trainingSet).get(neuron) - n.output); errors.put(n,
	 * err); System.out.println(err); }
	 * 
	 * if (network.numberOfHiddenLayers > 0) { for (int l =
	 * network.neuronLayers.size() - 2; l >= 0; l--) { nLayer =
	 * network.neuronLayers.get(l);
	 * 
	 * for (int neuron = 0; neuron < nLayer.neurons.size(); neuron++) { Neuron n
	 * = nLayer.neurons.get(neuron); double err = (n.output * (1.0 - n.output))
	 * (expectedOutputs.get(trainingSet).get(neuron) - n.output); errors.put(n,
	 * err); System.out.println(err); } } } }
	 * 
	 * return network; }
	 */

}
