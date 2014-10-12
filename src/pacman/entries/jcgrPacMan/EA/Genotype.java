/**
 * 
 */
package pacman.entries.jcgrPacMan.EA;

/**
 * A class that represents a genotype for evolving neural networks. 
 * It contains the weights of a NN.
 * 
 * @author Jacob
 */
public class Genotype
{
	/**
	 * Values representing weights in a NN.
	 */
	public final double[] values;
	
	/**
	 * The generation the genotype is from.
	 */
	public final int generation;

	/**
	 * The fitness of the genotype.
	 */
	private int fitness = 0;
	
	/**
	 * Determines if the genotype has been evaluated or not.
	 * Used to prevent setting the fitness multiple times.
	 */
	private boolean evaluated = false;
	
	/**
	 * Creates a new instance of the Genotype class with the given values.
	 */
	public Genotype(double[] genotypeValues, int generation)
	{
		this.values = genotypeValues;
		this.generation = generation;
	}
	
	/**
	 * Gets the fitness of the genotype.
	 * @return A value indicating the average score of the genotype.
	 */
	public int getFitness()
	{
		return this.fitness;
	}
	
	/**
	 * Sets the fitness of the genotype, if it has not been set already.
	 * @param newFitness The new fitness value.
	 */
	public void setFitness(int newFitness)
	{
		if (evaluated)
			return;
					
		fitness = newFitness;
		evaluated = true;
	}
	
	/**
	 * Gets the age of the genotype based on the current generation.
	 * @param currGeneration The current generation.
	 * @return A value indicating how old the genotype is.
	 */
	public int getAge(int currGeneration)
	{
		return currGeneration - this.generation;
	}
}
