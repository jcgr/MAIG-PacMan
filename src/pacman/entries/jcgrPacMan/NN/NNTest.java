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
public class NNTest
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		
	}

	/**
	 * Runs the test from "Classification by Backpropagation" 
	 * Example 9.1 in section 9.2.3.
	 */
	private static void classificationByBackpropagationTest()
	{
		// Initialize nn
		NeuralNetwork nn = new NeuralNetwork(3, 1, 1, 2);
		
		// Create and set the weights according to table 9.1
		// on page 8
		ArrayList<Double> weights = new ArrayList<Double>();
		weights.add(0.2);
		weights.add(0.4);
		weights.add(-0.5);
		weights.add(-0.4);

		weights.add(-0.3);
		weights.add(0.1);
		weights.add(0.2);
		weights.add(0.2);

		weights.add(-0.3);
		weights.add(-0.2);
		weights.add(0.1);
		
		nn.setWeights(weights);

		// Create inputs according to table 9.1 on page 8
		ArrayList<Double> inputs = new ArrayList<Double>();
		inputs.add(1.0);
		inputs.add(0.0);
		inputs.add(1.0);
		
		// Run the inputs through the NN and print the output.
		// Should be 0.474
		ArrayList<Double> outputs = nn.update(inputs);
		System.out.println(outputs.size());
		System.out.println(outputs.get(0));
	}
}
