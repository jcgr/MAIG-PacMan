/**
 * 
 */
package pacman.entries.jcgrPacMan.NN.training;

/**
 * The training data layout for the neural network.
 * 
 * @author Jacob
 */
public class TrainingData
{
	protected double[] input;
	protected double[] output;

	public TrainingData(int inputNodes, int outputNodes)
	{
		this.input = new double[inputNodes];
		this.output = new double[outputNodes];
	}

	public void setData(double[] input, double[] output)
	{
		this.input = input;
		this.output = output;
	}

	public double[] getInput()
	{
		return input;
	}

	public double[] getOutput()
	{
		return output;
	}
}
