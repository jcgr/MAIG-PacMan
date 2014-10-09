/**
 * 
 */
package pacman.entries.jcgrPacMan.MCTS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 * 
 * 
 * @author Jacob
 */
public class TreeNode2
{
	public int visits;
	public TreeNode2 parent;
	public List<TreeNode2> children;

	private Game gs;
	private MOVE moveTo;
//	private List<MOVE> actions;
	private Map<MOVE, Boolean> actions;

	private double pathLength;
	public double avgSPill;
	public double avgSSurvival;
	public double maxSPill;
	public double maxSSurvival;
	public double totSPill;
	public double totSSurvival;

	public TreeNode2(Game game, TreeNode2 parent, MOVE moveTo, double pathLength)
	{
		this.visits = 0;
		this.parent = parent;
		this.children = new ArrayList<TreeNode2>();

		this.gs = game.copy();
		this.moveTo = moveTo;
//		this.actions = new ArrayList<MOVE>();
		this.actions = new HashMap<MOVE, Boolean>();
		MOVE[] possibleActions = gs.getPossibleMoves(gs.getPacmanCurrentNodeIndex()
				, gs.getPacmanLastMoveMade()
				);
		for (MOVE m : possibleActions)
		{
//			actions.add(m);
			actions.put(m, false);
		}

		this.pathLength = pathLength;
		this.avgSPill = 0.0;
		this.maxSPill = 0.0;
		this.totSPill = 0.0;
		this.avgSSurvival = 0.0;
		this.maxSSurvival = 0.0;
		this.totSSurvival = 0.0;
	}

	public TreeNode2 bestChild()
	{
		List<TreeNode2> unvisitedChildren = new ArrayList<TreeNode2>();
		for (TreeNode2 child : children)
			if (child.visits < MCTS2.VISIT_THRESHOLD)
				unvisitedChildren.add(child);
		
		if (unvisitedChildren.size() > 0)
			return unvisitedChildren.get(MCTS2.random.nextInt(unvisitedChildren.size()));
		
		TreeNode2 bestChild = null;
		double bestValue = -10000;
		
		for (TreeNode2 child : children)
		{
			double uctValue = (child.getScore()
					* (MCTS2.EXPLORATION_CONSTANT 
						* Math.sqrt(Math.log(this.visits) / (child.visits)))
					);
			
			if (uctValue > bestValue)
			{
				bestChild = child;
				bestValue = uctValue;
			}
		}

		return bestChild;
	}

	public TreeNode2 expand()
	{
//		System.out.println(children.size() + " | actions " + actions.size());
		Game tempGame;
		MOVE[] untriedActions = this.getUntriedActions();
		TreeNode2 newNode = null;
		
//		for (MOVE m : untriedActions)
//		{
			if (this.pathLength + 1 < MCTS2.MAX_PATH_TO_ROOT)
			{
				MOVE m = untriedActions[MCTS2.random.nextInt(untriedActions.length)];
				tempGame = gs.copy();
				tempGame.advanceGame(m, MCTS2.ghostStrategy.getMove(tempGame, -1));
				newNode = new TreeNode2(tempGame, this, m, this.pathLength + 1);
				children.add(newNode);
				actions.put(m, true);
			}
//		}
		
			return newNode;
	}
	
	private MOVE[] getUntriedActions()
	{
		int untried = 0;
		MOVE[] untriedActions;
		
		for (Map.Entry<MOVE, Boolean> entry : actions.entrySet())
			if (entry.getValue() == false)
				untried++;
		
//		System.out.println(untried + " | " + actions.size());
		
		untriedActions = new MOVE[untried];
		untried = 0;
		for (Map.Entry<MOVE, Boolean> entry : actions.entrySet())
		{
			if (entry.getValue() == false)
			{
				untriedActions[untried] = entry.getKey();
				untried++;
			}
		}
		
		return untriedActions;
	}
	
	public double getScore()
	{
//		return ((double)gs.getScore() / 1000.0) * maxSSurvival;
		if (MCTS2.SURVIVAL)
			return maxSSurvival;
		else
			return maxSSurvival + maxSPill;
	}
	
	public double getRewardSurvival()
	{
		if (gs.wasPacManEaten())
			return 0.0;
		else
			return 1.0;
	}
	
	public double getRewardPill()
	{
		double min = 0; //MCTS2.PILLS_AT_ROOT - (MCTS2.MAX_PATH_TO_ROOT * 2);
		double max = MCTS2.PILLS_AT_ROOT;
		double eaten = MCTS2.PILLS_AT_ROOT - gs.getNumberOfActivePills();

		if (min < 0)
			min = 0;
		if (eaten < 0)
			eaten = 0;
		
		double normalized = (eaten - min) / (max - min);		
		return normalized;
	}
	
	public void updateValues(double sPill, double sSurvival)
	{
		visits++;
		
		totSPill += sPill;
		totSSurvival += sSurvival;
		
		double average = (children.size() == 0 ? 1 : children.size());
		avgSPill = totSPill / average;
		avgSSurvival = totSSurvival / average;
		
		if(sPill > maxSPill)
			maxSPill = sPill;
		if (sSurvival > maxSSurvival)
			maxSSurvival = sSurvival;
	}
	
	public double getPathLength()
	{
		return this.pathLength;
	}
	
	public MOVE getMoveTo()
	{
		return this.moveTo;
	}
	
	public Game getGameState()
	{
		return this.gs;
	}

	public boolean isTerminalNode()
	{
		return (gs.wasPacManEaten()
				|| (pathLength > MCTS2.MAX_PATH_TO_ROOT * 2)
				|| (gs.getMazeIndex() != MCTS2.ROOT_MAZE_INDEX)
				);
	}
	
	public boolean isFullyExpanded()
	{
		return (children.size() == actions.size());
	}

	public boolean isLeafNode()
	{
//		System.out.println("children: " + children.size() + " | action: " + actions.size());
//		System.out.println(children.size() == actions.size());
		return (children.size() != actions.size());
		// return false;
	}
}
