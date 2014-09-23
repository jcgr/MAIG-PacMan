/**
 * 
 */
package pacman.entries.jcgrPacMan.NN;

import java.util.ArrayList;

/**
 * A class that represents an entire neural network.
 * 
 * @author Jacob
 */
public class NeuralNetwork
{
	
	private final double BIAS = 1.0;
	private final double ACTIVATION_RESPONSE = 1.0;
	
	private int numberOfInputs;
	private int numberOfOutputs;
	private int numberOfHiddenLayers;
	private int neuronsPerHiddenLayer;
	
	private ArrayList<NeuronLayer> neuronLayers;
	
	/**
	 * Creates a new instance of the NeuralNetwork class with the given
	 * values.
	 * 
	 * @param numInputs The number of inputs.
	 * @param numOutputs The number of outputs.
	 * @param numHiddenLayers The number of hidden layers.
	 * @param neuronsPerHiddenLyr The amoutn of neurons per hidden layer.
	 */
	public NeuralNetwork(int numInputs, int numOutputs, int numHiddenLayers, int neuronsPerHiddenLyr)
	{
		this.numberOfInputs = numInputs;
		this.numberOfOutputs = numOutputs;
		this.numberOfHiddenLayers = numHiddenLayers;
		this.neuronsPerHiddenLayer = neuronsPerHiddenLyr;
		
		this.neuronLayers = new ArrayList<NeuronLayer>();
		
		this.createNetwork();
	}
	
	/**
	 * Sets up the neural network.
	 */
	public void createNetwork()
	{
		if (this.numberOfHiddenLayers > 0)
		{
			// Create first hidden layer
			neuronLayers.add(new NeuronLayer(neuronsPerHiddenLayer,	numberOfInputs));

			// Create remaining (if any) hidden layers
			for (int i = 0; i < numberOfHiddenLayers - 1; i++)
				neuronLayers.add(new NeuronLayer(neuronsPerHiddenLayer, neuronsPerHiddenLayer));
			
			// Create output layer
			neuronLayers.add(new NeuronLayer(numberOfOutputs, neuronsPerHiddenLayer));
		}
		else
		{
			neuronLayers.add(new NeuronLayer(numberOfOutputs, numberOfInputs));
		}
	}
	
	/**
	 * Gets a list containing the weights of all neurons in the network.
	 * 
	 * @return A list containing the weight of all neurons in the network.
	 */
	public ArrayList<Double> getWeights()
	{
		ArrayList<Double> weights = new ArrayList<Double>();
		
		for (int layer = 0; layer < numberOfHiddenLayers + 1; layer++)
		{
			NeuronLayer currLayer = neuronLayers.get(layer);
			
			for (int neuron = 0; neuron < currLayer.numberOfNeurons; neuron++)
			{
				Neuron currNeuron = currLayer.neurons.get(neuron);
				
				for (int weight = 0; weight < currNeuron.numberOfInputs; weight++)
				{
					weights.add(currNeuron.inputWeights.get(weight));
				}
			}
		}
		
		return weights;
	}
	
	/**
	 * Sets the weights in the network to the values given.
	 * 
	 * @param newWeights A list containing the new weights for
	 * 					 all neurons in the network.
	 */
	public void setWeights(ArrayList<Double> newWeights)
	{
		int currWeight = 0;
		
		for (int layer = 0; layer < numberOfHiddenLayers + 1; layer++)
		{
			NeuronLayer currLayer = neuronLayers.get(layer);
			
			for (int neuron = 0; neuron < currLayer.numberOfNeurons; neuron++)
			{
				Neuron currNeuron = currLayer.neurons.get(neuron);
				
				for (int weight = 0; weight < currNeuron.numberOfInputs; weight++)
				{
					currNeuron.inputWeights.set(weight, newWeights.get(currWeight));
					currWeight++;
				}
			}
		}
	}
	
	/**
	 * Gets the amount of weights in the network.
	 * 
	 * @return The number of weights in the network.
	 */
	public int getNumberOfWeights()
	{
		int weights = 0;
		
		for (int layer = 0; layer < numberOfHiddenLayers + 1; layer++)
		{
			NeuronLayer currLayer = neuronLayers.get(layer);
			
			for (int neuron = 0; neuron < currLayer.numberOfNeurons; neuron++)
			{
				Neuron currNeuron = currLayer.neurons.get(neuron);
				
				for (int weight = 0; weight < currNeuron.numberOfInputs; weight++)
				{
					weights++;
				}
			}
		}
		
		return weights;
	}
	
	public ArrayList<Double> update(ArrayList<Double> inputs)
	{
		ArrayList<Double> outputs = new ArrayList<Double>();
		
		int currWeight = 0;
		
		// If there's an incorrect amount of inputs, return an empty list
		// of outputs.
		if (inputs.size() != numberOfInputs)
			return outputs;
		
		// For each layer...
		for (int layerNum = 0; layerNum < numberOfHiddenLayers + 1; layerNum++)
		{
			// If we're processing layer two or later, use the outputs 
			// from previous layer as input for this layer.
			if (layerNum > 0)
			{
				inputs = new ArrayList<Double>();
				
				for (int i = 0; i < outputs.size(); i++)
					inputs.add(outputs.get(i));
			}
			
			// Reset outputs so it can take new values.
			outputs = new ArrayList<Double>();
			currWeight = 0;
			
			// Load the layer for easier use
			NeuronLayer currLayer = neuronLayers.get(layerNum);
			
			// For each neuron in the layer, calculate the sum of (inputs * weights),
			// then give that value to the Sigmoid function to get the output for that
			// neuron.
			for (int neuronNum = 0; neuronNum < currLayer.numberOfNeurons; neuronNum++)
			{
				// Load the neuron for easier use.
				Neuron currNeuron = currLayer.neurons.get(neuronNum);
				
				double totalInput = 0;
				int neuronNumberOfInputs = currNeuron.numberOfInputs;
				
				// For each weight...
				for (int weightNum = 0; weightNum < neuronNumberOfInputs - 1; weightNum++)
				{
					// Calculate the sum of the weight times the input
					totalInput += currNeuron.inputWeights.get(weightNum) * inputs.get(currWeight);
					currWeight++;
				}
				
				// Add the bias
				totalInput += currNeuron.inputWeights.get(neuronNumberOfInputs - 1) * BIAS;
				
				// Use the Sigmoid function to calculate the final output value,
				// then add that to the list of outputs.
				outputs.add(Sigmoid(totalInput, ACTIVATION_RESPONSE));
				
				currWeight = 0;
			}
		}
		
		return outputs;
	}
	
	/**
	 * Calculates the sigmoid value of the given input.
	 * 
	 * @param inputValue The value of the input to calculate the sigmoid
	 * 				 	 value for. 
	 * @param response The number that controls the "shape" of the sigmoid
	 * 				   curve.
	 * @return The sigmoid value.
	 */
	private double Sigmoid(double inputValue, double response)
	{
		double result = -inputValue / response;
		result = 1.0 + Math.pow(Math.E, result);
		result = 1.0 / result;
		
		return result;
	}
	
}
