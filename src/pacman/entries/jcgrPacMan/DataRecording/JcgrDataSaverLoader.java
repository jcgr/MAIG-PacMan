/**
 * 
 */
package pacman.entries.jcgrPacMan.DataRecording;

import pacman.game.util.IO;

/**
 * A class for saving/loading data. Uses custom data tuples instead
 * of the original ones.
 * 
 * @author Jacob
 */
public class JcgrDataSaverLoader
{
	private static String FileName = "trainingData.txt";
	
	public static void SavePacManData(JcgrDataTuple data)
	{
		IO.saveFile(FileName, data.getSaveString(), true);
	}
	
	public static JcgrDataTuple[] LoadPacManData()
	{
		String data = IO.loadFile(FileName);
		String[] dataLine = data.split("\n");
		JcgrDataTuple[] dataTuples = new JcgrDataTuple[dataLine.length];
		
		for(int i = 0; i < dataLine.length; i++)
			dataTuples[i] = new JcgrDataTuple(dataLine[i]);
		
		return dataTuples;
	}
}
