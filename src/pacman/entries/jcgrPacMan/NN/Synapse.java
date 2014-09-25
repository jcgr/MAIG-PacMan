/**
 * 
 */
package pacman.entries.jcgrPacMan.NN;

/**
 * A class that represents a connection to a neuron with a weight for the
 * connection.
 * 
 * @author Jacob
 */
public class Synapse
{
	/**
	 * The neuron to connect to.
	 */
	private Neuron sourceNeuron;

	/**
	 * The weight.
	 */
	private double weight;

	/**
	 * Creates a new synapse with a connection to a neuron
	 * and a weight.
	 * @param sourceNeuron The neuron to connect to.
	 * @param weight The weight of the connection.
	 */
	public Synapse(Neuron sourceNeuron, double weight)
	{
		this.sourceNeuron = sourceNeuron;
		this.weight = weight;
	}

	/**
	 * Gets the source neuron.
	 * @return The source neuron.
	 */
	public Neuron getSourceNeuron()
	{
		return sourceNeuron;
	}

	/**
	 * Gets the weight of the connection.
	 * @return The weight.
	 */
	public double getWeight()
	{
		return weight;
	}

	/**
	 * Sets the weight of the connection.
	 * @param weight The new weight.
	 */
	public void setWeight(double weight)
	{
		this.weight = weight;
	}
}
