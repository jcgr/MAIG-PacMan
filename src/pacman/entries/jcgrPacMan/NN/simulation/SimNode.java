/**
 * 
 */
package pacman.entries.jcgrPacMan.NN.simulation;

import java.util.ArrayList;
import java.util.List;

import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;

/**
 * 
 * 
 * @author Jacob
 */
public class SimNode
{
	private SimNode parent;
	public MOVE moveTo;
	public List<SimNode> children;
	public Game gs;
	
	public int currDepth;
	public double survival;
	public double totPills;
	public double maxPills;
	
	private MOVE[] possibleMoves;
	private boolean ppActive;
	
	public SimNode(SimNode parent, MOVE moveTo, Game newGameState, int currDepth, boolean ppActive)
	{
		this.parent = parent;
		this.moveTo = moveTo;
		children = new ArrayList<SimNode>();
		this.gs = newGameState;
		this.currDepth = currDepth;
		this.ppActive = ppActive;
		
		survival = 0.0;
		totPills = 0.0;
		maxPills = 0.0;
		
		if (Simulation.pacManAtJunction(gs))
			possibleMoves = gs.getPossibleMoves(gs.getPacmanCurrentNodeIndex());
		else
			possibleMoves = gs.getPossibleMoves(gs.getPacmanCurrentNodeIndex()
					, gs.getPacmanLastMoveMade()
					);
	}
	
	public SimNode getBestChild()
	{
		SimNode bestChild = null;
		List<SimNode> bestChoices = new ArrayList<SimNode>();
		
		double bestValue = -100.0;
		
		for (SimNode child : children)
		{
			if (child.getScore() == bestValue)
				bestChoices.add(child);
			
			if (child.getScore() > bestValue)
			{
				bestValue = child.getScore();
				bestChoices.clear();
				bestChoices.add(child);
			}
		}
		
		if (bestChoices.size() == 1)
			return bestChoices.get(0);
		
//		System.out.println("lol");
		
		int pMIndex = gs.getPacmanCurrentNodeIndex();
		double maxDistance = -100;
		int closest = gs.getClosestNodeIndexFromNodeIndex(pMIndex, gs.getActivePillsIndices(), DM.PATH);
		List<Double> distances = new ArrayList<Double>();
		
		for (SimNode child : bestChoices)
		{
			int childIndex = gs.getNeighbour(pMIndex, child.moveTo);
			double currDistance = gs.getDistance(childIndex, closest, DM.PATH);
			
			if (currDistance > maxDistance)
				maxDistance = currDistance;
			
			distances.add(currDistance);
		}
		
		bestValue = -1000;
		for (int i = 0; i < distances.size(); i++)
		{
			double actualDistance = maxDistance - distances.get(i);
			if (actualDistance > bestValue)
			{
				bestValue = actualDistance;
				bestChild = bestChoices.get(i);
			}
		}
		
		return bestChild;
	}
	
	public List<SimNode> expand()
	{
		if (currDepth + 1 > Simulation.maxDepth)
			return children;
		
		for (MOVE m : possibleMoves)
		{
			Game tempGame = gs.copy();
			tempGame.advanceGame(m, Simulation.ghostStrategy.getMove(tempGame, -1));
			SimNode newNode = new SimNode(this, m, tempGame, currDepth + 1, ppActive);
			children.add(newNode);
		}
		
		return children;
	}
	
	public void updateStats(double newPills, double newSurvival)
	{
		totPills += newPills;
		
		if (newPills > maxPills)
			maxPills = newPills;
		if (newSurvival > survival)
			survival = newSurvival;
	}
	
	public double getScore()
	{
		return (maxPills * survival);// + (Simulation.random.nextDouble() / 1000);
	}
	
	public double getSurvivalReward()
	{
		return (gs.wasPacManEaten() ? 0.0 : 1.0);
	}
	
	public double getPillReward()
	{
		double min = 0; //MCTS2.PILLS_AT_ROOT - (MCTS2.MAX_PATH_TO_ROOT * 2);
		double max = Simulation.pillsAtRoot;
		double eaten = Simulation.pillsAtRoot - gs.getNumberOfActivePills();

		if (eaten < 0)
			eaten = Simulation.pillsAtRoot;
		
		double normalized = (eaten - min) / (max - min);
		
		return normalized;
	}
	
	public SimNode getParent()
	{
		return this.parent;
	}
	
	public boolean isTerminalNode()
	{
		return (gs.wasPacManEaten()
				|| (currDepth >= Simulation.maxDepth)
				|| (gs.getMazeIndex() != Simulation.rootMaze)
				);
	}
	
	public boolean isExpanded()
	{
		return children.size() == possibleMoves.length;
	}
}
