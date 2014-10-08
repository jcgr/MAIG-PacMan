/**
 * 
 */
package pacman.entries.jcgrPacMan.MCTS;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;

import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 * 
 * 
 * @author Jacob
 */
public class TreeNode
{
	public double visits;
	public double totalValue;
	public MOVE moveTo;
	public TreeNode parent;
	public int depth;
	
	private Game gs;
	public List<TreeNode> children;
	private List<MOVE> actions;
	private boolean[] actionsTried;
	private EnumMap<GHOST, MOVE> ghostMoves;
	
	public TreeNode(MOVE moveTo, TreeNode parent, Game gs, int depth, boolean root)
	{
		this.moveTo = moveTo;
		this.parent = parent;
		this.gs = gs;
		this.depth = depth;
		this.children = new ArrayList<TreeNode>();
		this.actions = new ArrayList<MOVE>();
		
		MOVE[] possibleMoves = gs.getPossibleMoves(gs.getPacmanCurrentNodeIndex()); 
		for (MOVE m : possibleMoves)
			actions.add(m);
		
		actionsTried = new boolean[actions.size()];
		for (int action = 0; action < actions.size(); action++)
			actionsTried[action] = false;

		// The root node should not have any action happen.
		if (!root)
		{
			ghostMoves = new EnumMap<GHOST, MOVE>(GHOST.class);
			for (GHOST ghost : GHOST.values())
				ghostMoves.put(ghost, gs.getGhostLastMoveMade(ghost));

			gs.advanceGame(this.moveTo, ghostMoves);
		}
		
		this.totalValue = 0.0;
		this.visits = 1;
	}
	
	public TreeNode expand()
	{
		boolean expanded = false;
		TreeNode expandedNode = null;
		
		while (!expanded)
		{
			int nextTry = MCTS.random.nextInt(actions.size());
			if (!actionsTried[nextTry])
			{
				expandedNode = new TreeNode(actions.get(nextTry), this, gs.copy(), this.depth + 1, false);
				children.add(expandedNode);
				actionsTried[nextTry] = true;
				expanded = true;
				MCTS.expansions++;
			}
		}
		
		return expandedNode;
	}
	
	/**
	 * Selects the node's best child (randomly chosen if none of them have been visited)
	 * @return
	 */
	public TreeNode bestChild()
	{
		TreeNode selected = null;
		double bestValue = -100000.0;
		
		for (TreeNode child : children)
		{
			double uctValue = ((child.totalValue / child.visits) 
					+ (MCTS.EXPLORATION_CONSTANT * Math.sqrt((2 * Math.log(this.visits)) / child.visits)));
			
			if (uctValue > bestValue)
			{
				selected = child;
				bestValue = uctValue;
			}
		}
		
		return selected;
	}
	
	public double simulation()
	{
		double result = 0.0;
		TreeNode tempNode = this;
		Game tempGame = gs.copy();
		
		while (!tempNode.isTerminalNode())
		{
			MOVE[] possibleMoves = tempGame.getPossibleMoves(tempGame.getPacmanCurrentNodeIndex());
			MOVE nextPMMove = possibleMoves[MCTS.random.nextInt(possibleMoves.length)];
			
			EnumMap<GHOST, MOVE> validGhostMoves = new EnumMap<GHOST, MOVE>(GHOST.class);
			for (GHOST ghost : GHOST.values())
			{
				if(tempGame.getGhostEdibleTime(ghost) == 0 
					&& tempGame.getGhostLairTime(ghost) == 0)
				{
				possibleMoves = tempGame.getPossibleMoves(tempGame.getGhostCurrentNodeIndex(ghost));
				MOVE nextGMove = possibleMoves[MCTS.random.nextInt(possibleMoves.length)];
				validGhostMoves.put(ghost, nextGMove);
				}
			}
			
			tempGame.advanceGame(nextPMMove, validGhostMoves);
			tempNode = new TreeNode(nextPMMove, tempNode, tempGame, tempNode.depth + 1, false);
//			result += tempNode.simulation();
			System.out.print(tempNode.getReward() + " | ");
		}
		System.out.println();
		System.out.print(result);
		
//		return result;
		return tempNode.getReward();
	}
	
	public double getReward()
	{		
		if (gs.wasPacManEaten())
			return (-100000.0);
		
		if (gs.wasPowerPillEaten())
		{
			boolean ppActive = false;
			for (GHOST ghost : GHOST.values())
				if (gs.getGhostEdibleTime(ghost) > 0)
					ppActive = true;
			
			if (ppActive)
				return -500.0;
			else
				return 500.0;
		}

		for (GHOST ghost : GHOST.values())
			if (gs.wasGhostEaten(ghost))
				return (gs.getGhostCurrentEdibleScore() / 2);
		
		if (gs.wasPillEaten())
			return 10.0;
			
		
		return 0;
	}
	
	public void updateValues(double value)
	{
		this.visits++;
		this.totalValue += value;
	}
	
	public boolean isTerminalNode()
	{
		return (depth > MCTS.MAX_DEPTH
				|| gs.wasPacManEaten()
				|| (gs.getActivePillsIndices().length == 0
					&& gs.getActivePowerPillsIndices().length == 0));
	}
	
	public boolean isFullyExpanded()
	{
		return (children.size() == actions.size());
	}
}
