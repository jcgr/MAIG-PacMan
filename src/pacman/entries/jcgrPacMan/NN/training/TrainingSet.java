/**
 * 
 */
package pacman.entries.jcgrPacMan.NN.training;

import java.util.ArrayList;

/**
 * 
 * 
 * @author Jacob
 */
public class TrainingSet
{
	private ArrayList<TrainingData> trainingDataList;
	private int currentIndex;
	private int highestIndex;

	public TrainingSet()
	{
		trainingDataList = new ArrayList<TrainingData>();
		currentIndex = -1;
		highestIndex = -1;
	}

	public TrainingData getNextTrainingData()
	{
		if (highestIndex == -1)
		{
			System.out.println("NO TRAINING DATA AVAILABLE!");
			return null;
		}
		currentIndex = currentIndex == highestIndex ? 0 : currentIndex + 1;
		return trainingDataList.get(currentIndex);
	}

	public TrainingData getSpecificData(int index)
	{
		if (index > highestIndex)
		{
			System.out.println("INDEX OUT OF BOUNDS!");
			return null;
		}
		return trainingDataList.get(index);
	}

	public int highestIndexOfSet()
	{
		return highestIndex;
	}

	public void AddTrainingData(TrainingData td)
	{
		highestIndex++;
		trainingDataList.add(td);
	}
}
