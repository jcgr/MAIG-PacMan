/**
 * 
 */
package pacman.entries.jcgrPacMan.NN;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that represents a layer of neurons.
 * 
 * Based on https://github.com/vivin/DigitRecognizingNeuralNetwork/tree/master/src/main/java/net/vivin/neural
 * 
 * @author Jacob
 */
public class NeuronLayer
{
	/**
	 * The neurons in the layer.
	 */
	private List<Neuron> neurons;

	/**
	 * The layer that was before this layer.
	 */
	private NeuronLayer previousLayer;

	/**
	 * The next layer.
	 */
	private NeuronLayer nextLayer;

	/**
	 * The bias for this layer.
	 */
	private Neuron bias;

	/**
	 * Creates a neuron layer with an empty list of neurons
	 * and no previous layer.
	 */
	public NeuronLayer()
	{
		neurons = new ArrayList<Neuron>();
		previousLayer = null;
	}

	/**
	 * Creates a neuron layer with an empty list of neurons
	 * and a previouslayer reference to the given input.
	 * @param previousLayer The layer that comes before this one.
	 */
	public NeuronLayer(NeuronLayer previousLayer)
	{
		this();
		this.previousLayer = previousLayer;
	}
	
	/**
	 * Creates a neuron layer with an empty list of neurons
	 * and a bias.
	 * @param bias The bias.
	 */
	public NeuronLayer(Neuron bias)
	{
		this();
		this.bias = bias;
		neurons.add(bias);
	}

	/**
	 * Creates a neuron layer with an empty list of neurons,
	 * a previouslayer reference to the given input and a bias.
	 * @param previousLayer The layer that comes before this one.
	 * @param bias The bias for the layer.
	 */
	public NeuronLayer(NeuronLayer previousLayer, Neuron bias)
	{
		this(previousLayer);
		this.bias = bias;
		neurons.add(bias);
	}

	/**
	 * Adds a neuron to the layer.
	 * @param neuron The neuron to add.
	 */
	public void addNeuron(Neuron neuron)
	{
		neurons.add(neuron);

		// If a previous layer exists, create a synapse between the new neuron
		// and every neuron in the previous layer.
		if (previousLayer != null)
			for (Neuron previousLayerNeuron : previousLayer.getNeurons())
				neuron.addInput(new Synapse(previousLayerNeuron, (Math.random() * 2.0) - 1.0));
	}

	/**
	 * Adds a neuron with pre-defined weights.
	 * @param neuron The neuron to add.
	 * @param weights The weights for the neuron (if bias weight is included, make it the first value).
	 */
	public void addNeuron(Neuron neuron, double[] weights)
	{
		neurons.add(neuron);

		// If a previous layer exists, create a synapse between the new neuron
		// and every neuron in the previous layer using the given weights.
		if (previousLayer != null)
		{
			if (previousLayer.getNeurons().size() != weights.length)
				throw new IllegalArgumentException(
						"The number of weights supplied must be equal to the "
						+ "number of neurons in the previous layer");
			else
			{
				List<Neuron> previousLayerNeurons = previousLayer.getNeurons();
				for (int i = 0; i < previousLayerNeurons.size(); i++)
					neuron.addInput(new Synapse(previousLayerNeurons.get(i),
							weights[i]));
			}
		}
	}

	/**
	 * Activates every neuron in the layer, making them
	 * calculate their weighted sum and their output.
	 */
	public void activate()
	{
		int biasCount = hasBias() ? 1 : 0;

		for (int i = biasCount; i < neurons.size(); i++)
			neurons.get(i).activate();
	}

	/**
	 * Gets the neurons from this layer.
	 * @return
	 */
	public List<Neuron> getNeurons()
	{
		return this.neurons;
	}

	/**
	 * Gets the previous layer
	 * @return The previous layer.
	 */
	public NeuronLayer getPreviousLayer()
	{
		return previousLayer;
	}

	/**
	 * Sets the previous layer.
	 * @param previousLayer The new previous layer.
	 */
	void setPreviousLayer(NeuronLayer previousLayer)
	{
		this.previousLayer = previousLayer;
	}

	/**
	 * Gets the next layer.
	 * @return The next layer.
	 */
	public NeuronLayer getNextLayer()
	{
		return nextLayer;
	}

	/**
	 * Sets the net layer.
	 * @param nextLayer The new next layer.
	 */
	void setNextLayer(NeuronLayer nextLayer)
	{
		this.nextLayer = nextLayer;
	}

	/**
	 * Determines if this layer is the output layer or not.
	 * @return True if it is the output layer; false otherwise.
	 */
	public boolean isOutputLayer()
	{
		return nextLayer == null;
	}

	/**
	 * Determines if the layer has a bias or not.
	 * @return True if it has a bias; false otherwise.
	 */
	public boolean hasBias()
	{
		return bias != null;
	}
}
