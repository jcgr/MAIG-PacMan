/**
 * 
 */
package pacman.entries.jcgrPacMan.NN;

import java.util.ArrayList;
import java.util.Random;

/**
 * 
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
	 * The weights of the various inputs.
	 */
	public ArrayList<Double> inputWeights;
	
	/**
	 * Creates a new instance of the Neuron class and initializes
	 * its list of weights with random weights.
	 * 
	 * @param numInputs The amount of inputs the neuron shall take. 
	 */
	public Neuron(int numInputs)
	{
		Random random = new Random();
		this.numberOfInputs = numInputs + 1;
		this.inputWeights = new ArrayList<Double>();
		
		double weight;

		for (int i = 0; i < numberOfInputs + 1; i++)
		{
			// Random weight between 0.0 and 1.0
			weight = random.nextDouble();
			
			// Invert weight if nextInt is even
			if (random.nextInt(100) % 2 == 0) 
				weight *= -1.0;
			
			// Add weight
			inputWeights.add(weight);
		}
	}
	
}
