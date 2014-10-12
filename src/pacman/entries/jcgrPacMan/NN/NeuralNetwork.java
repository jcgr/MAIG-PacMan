/**
 * 
 */
package pacman.entries.jcgrPacMan.NN;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that represents an entire neural network.
 * 
 * Based on https://github.com/vivin/DigitRecognizingNeuralNetwork/tree/master/src/main/java/net/vivin/neural
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
	 * The name of the NN.
	 */
	private String name;

	/**
	 * Creates a new neural network with an empty
	 * list of layers.
	 * @param name The name of the neural network (used for logging/saving)
	 */
	public NeuralNetwork(String name)
	{
		layers = new ArrayList<NeuronLayer>();
		this.name = name;
	}

	/**
	 * Creates a neural network with only one hidden layer.
	 * @param name The name of the network (saving purposes)
	 * @param numberOfInputNodes The number of input nodes.
	 * @param numberOfOutputNodes The number of output nodes.
	 * @param numberOfHiddenNodes The number of nodes in the hidden layer.
	 * @return A neural network.
	 */
	public static NeuralNetwork createSingleHiddenLayerNeuralNetwork(String name, int numberOfInputNodes,
			int numberOfOutputNodes, int numberOfHiddenNodes)
	{
		NeuralNetwork neuralNetwork = new NeuralNetwork(name);

		Neuron bias = new Neuron();
		bias.setOutput(1.0);

		// Create input layer and add neurons
		NeuronLayer inputLayer = new NeuronLayer(bias);
		for (int i = 0; i < numberOfInputNodes; i++)
			inputLayer.addNeuron(new Neuron());

		// Create hidden layer and add neurons
		NeuronLayer hiddenLayer = new NeuronLayer(inputLayer, bias);
		for (int i = 0; i < numberOfHiddenNodes; i++)
			hiddenLayer.addNeuron(new Neuron());

		// Create output layer and add neurons
		NeuronLayer outputLayer = new NeuronLayer(hiddenLayer);
		for (int i = 0; i < numberOfOutputNodes; i++)
			outputLayer.addNeuron(new Neuron());

		// Add the layers to the neural network
		neuralNetwork.addLayer(inputLayer);
		neuralNetwork.addLayer(hiddenLayer);
		neuralNetwork.addLayer(outputLayer);

		return neuralNetwork;
	}

	/**
	 * Adds a layer to the hidden network.
	 * @param layer
	 */
	public void addLayer(NeuronLayer layer)
	{
		layers.add(layer);

		if (layers.size() == 1)
			input = layer;

		if (layers.size() > 1)
		{
			NeuronLayer previousLayer = layers.get(layers.size() - 2);
			previousLayer.setNextLayer(layer);
		}

		output = layers.get(layers.size() - 1);
	}

	/**
	 * Sets the inputs of the neural network to the given values.
	 * @param inputs An array with the chosen inputs.
	 */
	public void setInputs(double[] inputs)
	{
		if (input != null)
		{
			int hasBias = input.hasBias() ? 1 : 0;

			if (input.getNeurons().size() - hasBias != inputs.length)
				throw new IllegalArgumentException(
						"The number of inputs must equal the number of neurons in the input layer");
			else
			{
				List<Neuron> neurons = input.getNeurons();
				for (int i = hasBias; i < neurons.size(); i++)
					neurons.get(i).setOutput(inputs[i - hasBias]);
			}
		}
	}

	/**
	 * Gets the output of the neural network.
	 * @return An array containng the output of the neural network.
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
	 * Activates the network, activating all neurons in every layer.
	 */
	public void activate()
	{
		for (int i = 1; i < layers.size(); i++)
		{
			NeuronLayer layer = layers.get(i);
			layer.activate();
		}
	}

	/**
	 * Gets the layers of the network.
	 * @return
	 */
	public List<NeuronLayer> getLayers()
	{
		return layers;
	}

	/**
	 * Resets the weights of every neuron in every single layer.
	 */
	public void resetWeights()
	{
		// Reset each weight in the neural network to a random
		// value between -1.0 and 1.0
		for (NeuronLayer layer : layers)
			for (Neuron neuron : layer.getNeurons())
				for (Synapse synapse : neuron.getInputs())
					synapse.setWeight((Math.random() * 2.0) - 1.0);
	}

	/**
	 * Gets all the weights of the neural network.
	 * @return An array containing the weights of the neural network.
	 */
	public double[] getWeights()
	{
		List<Double> weights = new ArrayList<Double>();

		for (NeuronLayer layer : layers)
			for (Neuron neuron : layer.getNeurons())
				for (Synapse synapse : neuron.getInputs())
					weights.add(synapse.getWeight());

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
	 * Sets the weights of the entire neural network.
	 * @param weights The new weights of the neural network.
	 */
	public void setWeights(double[] weights)
	{
		int w = 0;
		
		if (weights.length != getWeights().length)
			throw new IllegalArgumentException("Cannot copy weights. Number of weights do not match.");

		for (NeuronLayer layer : layers)
			for (Neuron neuron : layer.getNeurons())
				for (Synapse synapse : neuron.getInputs())
				{
					synapse.setWeight(weights[w]);
					w++;
				}
	}

	/**
	 * Gets the name of the neural network.
	 * @return The name of the neural network.
	 */
	public String getName()
	{
		return this.name;
	}

}
