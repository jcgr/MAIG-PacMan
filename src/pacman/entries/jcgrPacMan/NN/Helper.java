/**
 * 
 */
package pacman.entries.jcgrPacMan.NN;

/**
 * A class that contains methods shared between other classes.
 * 
 * @author Jacob
 */
public class Helper
{
	/**
	 * Calculates the sigmoid value, gives a certain input and a reponse.
	 * @param inputValue The input value.
	 * @return The sigmoid value.
	 */
	public static double sigmoidActivate(double inputValue)
	{
		return 1.0 / (1.0 + Math.exp(-1.0 * inputValue));
	}
}
