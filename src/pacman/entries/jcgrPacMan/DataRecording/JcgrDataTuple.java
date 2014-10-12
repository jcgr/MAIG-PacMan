/**
 * 
 */
package pacman.entries.jcgrPacMan.DataRecording;

import java.util.ArrayList;
import java.util.List;

import pacman.game.Constants;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

/**
 * A custom version of the DataTuple class for recording data.
 * 
 * @author Jacob
 */
public class JcgrDataTuple
{
	/**
	 * The direction chosen.
	 */
	public MOVE DirectionChosen;
	
	/**
	 * The distance to the nearest pill.
	 */
	public int nearestPillDistance = 0;
	
	/**
	 * The direction to the nearest pill.
	 */
	public MOVE nearestPillDirection;

	/**
	 * The distance to the nearest ghost.
	 */
	public int nearestGhostDistance = 0;
	
	/**
	 * The direction to the nearest ghost.
	 */
	public MOVE nearestGhostDirection;

	//Util data - useful for normalization
	public int numberOfNodesInLevel;
	public int numberOfTotalPillsInLevel;
	public int numberOfTotalPowerPillsInLevel;
	
	public JcgrDataTuple(Game game, MOVE move)
	{
		int pmIndex = game.getPacmanCurrentNodeIndex();
		
		if(move == MOVE.NEUTRAL)
		{
			move = game.getPacmanLastMoveMade();
		}
		
		this.DirectionChosen = move;

		this.numberOfNodesInLevel = game.getNumberOfNodes();
		this.numberOfTotalPillsInLevel = game.getNumberOfPills();
		this.numberOfTotalPowerPillsInLevel = game.getNumberOfPowerPills();
		
		// Find nearest pill + direction to it.
		List<Integer> pills = new ArrayList<Integer>();
		
		int[] pillIndicies = game.getActivePillsIndices();
		for (int i : pillIndicies)
			pills.add(i);
		
		int[] powerPillIndicies = game.getActivePowerPillsIndices();
		for (int i : powerPillIndicies)
			pills.add(i);
		
		int[] activePills = new int[pills.size()];
		for (int i = 0; i < pills.size(); i++)
			activePills[i] = pills.get(i);
		
		int nearestPill = game.getClosestNodeIndexFromNodeIndex(
				pmIndex, activePills, DM.PATH);
		
		this.nearestPillDirection = game.getNextMoveTowardsTarget(
				pmIndex, nearestPill, DM.PATH);
		
		this.nearestPillDistance = (int) game.getDistance(
				pmIndex, nearestPill, DM.PATH);

		// Find nearest non-edible ghost + direction to it
		MOVE dirToGhost = MOVE.NEUTRAL;
		int ghostDistance = 10000;
		for (GHOST ghost : GHOST.values())
		{
			if(game.getGhostEdibleTime(ghost) == 0 && game.getGhostLairTime(ghost) == 0)
			{
				int distanceToGhost = game.getShortestPathDistance(
						pmIndex, game.getGhostCurrentNodeIndex(ghost));
				
				if (distanceToGhost < ghostDistance)
				{
					ghostDistance = distanceToGhost;
					dirToGhost = game.getNextMoveTowardsTarget(
							pmIndex, game.getGhostCurrentNodeIndex(ghost), DM.PATH);
				}
			}
		}
		
		this.nearestGhostDistance = (ghostDistance > 1000 ? numberOfNodesInLevel : ghostDistance);
		this.nearestGhostDirection = dirToGhost;	
	}
	
	public JcgrDataTuple(String data)
	{
		String[] dataSplit = data.split(";");
		
		this.DirectionChosen = MOVE.valueOf(dataSplit[0]);

		this.nearestPillDistance = Integer.parseInt(dataSplit[1]);
		this.nearestPillDirection = MOVE.valueOf(dataSplit[2]);

		this.nearestGhostDistance = Integer.parseInt(dataSplit[3]);
		this.nearestGhostDirection = MOVE.valueOf(dataSplit[4]);
		
		this.numberOfNodesInLevel = Integer.parseInt(dataSplit[5]);
		this.numberOfTotalPillsInLevel = Integer.parseInt(dataSplit[6]);
		this.numberOfTotalPowerPillsInLevel = Integer.parseInt(dataSplit[7]);
	}
	
	public String getSaveString()
	{
		StringBuilder stringbuilder = new StringBuilder();

		stringbuilder.append(this.DirectionChosen+";");
		
		stringbuilder.append(this.nearestPillDistance+";");
		stringbuilder.append(this.nearestPillDirection+";");
		
		stringbuilder.append(this.nearestGhostDistance+";");
		stringbuilder.append(this.nearestGhostDirection+";");

		stringbuilder.append(this.numberOfNodesInLevel+";");
		stringbuilder.append(this.numberOfTotalPillsInLevel+";");
		stringbuilder.append(this.numberOfTotalPowerPillsInLevel+";");
		
		return stringbuilder.toString();
	}

	/**
	 * Used to normalize distances. Done via min-max normalization.
	 * Supposes that minimum possible distance is 0. Supposes that
	 * the maximum possible distance is the total number of nodes in
	 * the current level.
	 * @param dist Distance to be normalized
	 * @return Normalized distance
	 */
	public double normalizeDistance(int dist)
	{
		if (dist > 20)
			dist = 20;
		return (((double)dist - 0.0) / (20.0 - 0.0)) * (1.0 - 0.0) + 0.0;
	}

	public double normalizeLevel(int level)
	{
		return ((level - 0.0) / (Constants.NUM_MAZES - 0.0)) * (1.0 - 0.0) + 0.0;
	}

	public double normalizePosition(int position)
	{
		return ((position - 0.0) / (this.numberOfNodesInLevel - 0.0)) * (1.0 - 0.0) + 0.0;
	}

	public double normalizeBoolean(boolean bool)
	{
		if (bool)
		{
			return 1.0;
		}
		else
		{
			return 0.0;
		}
	}

	public double normalizeNumberOfPills(int numOfPills)
	{
		return ((numOfPills - 0.0) / (this.numberOfTotalPillsInLevel - 0.0))
				* (1.0 - 0.0) + 0.0;
	}

	public double normalizeNumberOfPowerPills(int numOfPowerPills)
	{
		return ((numOfPowerPills - 0.0) / (this.numberOfTotalPowerPillsInLevel - 0.0))
				* (1.0 - 0.0) + 0.0;
	}

	public double normalizeTotalGameTime(int time)
	{
		return ((time - 0.0) / (Constants.MAX_TIME - 0.0)) * (1.0 - 0.0) + 0.0;
	}

	public double normalizeCurrentLevelTime(int time)
	{
		return ((time - 0.0) / (Constants.LEVEL_LIMIT - 0.0)) * (1.0 - 0.0) + 0.0;
	}

	/**
	 * 
	 * Max score value lifted from highest ranking PacMan controller on PacMan
	 * vs Ghosts website: http://pacman-vs-ghosts.net/controllers/110.04
	 * 
	 * @param score
	 * @return
	 */
	public double normalizeCurrentScore(int score)
	{
		return ((score - 0.0) / (82180.0 - 0.0)) * (1.0 - 0.0) + 0.0;
	}

	public double moveToDouble(MOVE move)
	{
		switch (move)
		{
			case NEUTRAL:
				return 0.00;
			case UP:
				return 0.25;
			case RIGHT:
				return 0.5;
			case DOWN:
				return 0.75;
			case LEFT:
				return 1.0;
			default:
				return -1.0;
		}
	}
	
	public MOVE doubleToMove(double d)
	{
		if (d >= -0.13 && d < 0.12)
			return MOVE.NEUTRAL;
		
		if (d >= 0.12 && d < 0.37)
			return MOVE.UP;

		if (d >= 0.37 && d < 0.62)
			return MOVE.RIGHT;

		if (d >= 0.62 && d < 0.87)
			return MOVE.DOWN;

		if (d >= 0.87 && d < 1.12)
			return MOVE.LEFT;

		return MOVE.NEUTRAL;
	}
}
