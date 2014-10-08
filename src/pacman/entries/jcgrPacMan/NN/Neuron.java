/**
 * 
 */
package pacman.entries.jcgrPacMan.NN;

import java.util.List;
import java.util.ArrayList;

/**
 * A class that represents a neuron in a neural network.
 * 
 * @author Jacob
 */
public class Neuron
{
	/**
	 * The number of inputs the neuron takes.
	 */
	public int numberOfInputs;
	
	/**
	 * The inputs to this neuron.
	 */
	private List<Synapse> inputs;

	/**
	 * The output of the neuron.
	 */
	private double output;

	/**
	 * The total weighted sum.
	 */
	private double weightedSum;
	
	/**
	 * The error of this neuron.
	 */
	private double error;
	
	/**
	 * The number of the neuron (used for debugging)
	 */
	public int number;

	/**
	 * Creates a new neuron with an empty list of inputs.
	 */
	public Neuron()
	{
		inputs = new ArrayList<Synapse>();
		numberOfInputs = 0;
	}

	/**
	 * Adds a new input to the neuron.
	 * @param s The synapse input to add.
	 */
	public void addInput(Synapse s)
	{
		inputs.add(s);
		numberOfInputs++;
	}

	/**
	 * Calculated the weighted sum of the neuron.
	 */
	private void calculateWeightedSum()
	{
		weightedSum = 0;
		for (Synapse synapse : inputs)
			weightedSum += synapse.getWeight() * synapse.getSourceNeuron().getOutput();
	}

	/**
	 * Calculated the weighted sum of the neuron and the output of the neuron.
	 */
	public void activate()
	{
		calculateWeightedSum();
		this.output = Helper.sigmoidActivate(weightedSum);
	}

	/**
	 * Gets the neuron's output value.
	 * @return The output value.
	 */
	public double getOutput()
	{
		return this.output;
	}

	/**
	 * Sets the neuron's output value.
	 * @param output The new output value.
	 */
	public void setOutput(double output)
	{
		this.output = output;
	}

	/** 
	 * Gets the error value of the neuron.
	 * @return The error value.
	 */
	public double getError()
	{
		return error;
	}

	/**
	 * Sets the neuron's error value.
	 * @param error The new error value.
	 */
	public void setError(double error)
	{
		this.error = error;
	}

	/**
	 * Gets the list of inputs for the neuron.
	 * @return
	 */
	public List<Synapse> getInputs()
	{
		return this.inputs;
	}

	/**
	 * Gets an array containing the weights to the neuron.
	 * @return A double array of weights to the neuron.
	 */
	public double[] getWeights()
	{
		double[] weights = new double[inputs.size()];

		int i = 0;
		for (Synapse synapse : inputs)
		{
			weights[i] = synapse.getWeight();
			i++;
		}

		return weights;
	}
}
