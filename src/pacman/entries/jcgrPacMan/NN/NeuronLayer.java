/**
 * 
 */
package pacman.entries.jcgrPacMan.NN;

import java.util.ArrayList;

/**
 * 
 * 
 * @author Jacob
 */
public class NeuronLayer
{
	
	/**
	 * The amount of neurons the layer holds.
	 */
	public int numberOfNeurons;
	
	/**
	 * The neurons the layer holds.
	 */
	public ArrayList<Neuron> neurons;
	
	public NeuronLayer(int numNeurons, int inputsPerNeuron)
	{
		this.numberOfNeurons = numNeurons;
		this.neurons = new ArrayList<Neuron>();
		
		for (int i = 0; i < numNeurons; i++)
			neurons.add(new Neuron(inputsPerNeuron));
	}
	
}
