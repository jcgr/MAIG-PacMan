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
	
	private Game gs;
	private List<TreeNode> children;
	private List<MOVE> actions;
	private boolean[] actionsTried;
	private EnumMap<GHOST, MOVE> ghostMoves;
	
	public TreeNode(MOVE moveTo, TreeNode parent, Game gs, boolean root)
	{
		this.moveTo = moveTo;
		this.parent = parent;
		this.gs = gs;
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
	
	public void expand()
	{
//		boolean expanded = false;
//		
//		while (!expanded)
//		{
//			int nextTry = MCTS.random.nextInt(actions.size());
//			if (!actionsTried[nextTry])
//			{
//				children.add(new TreeNode(actions.get(nextTry), gs.copy(), false));
//				expanded = true;
//			}
//		}
		
		for (MOVE m : actions)
			children.add(new TreeNode(m, this, gs.copy(), false));
	}
	
	/**
	 * Selects the node's best child (randomly chosen if none of them have been visited)
	 * @return
	 */
	public TreeNode bestChild()
	{
//		boolean anyChildVisited = false;
		TreeNode selected = null;
		double bestValue = -100000.0;
		
//		for (TreeNode child : children)
//			if (child.visits > 0)
//				anyChildVisited = true;
//			
//		if (!anyChildVisited)
//			selected = children.get(MCTS.random.nextInt(this.children.size()));
//		else
			for (TreeNode child : children)
			{
				double uctValue = ((child.value() + child.totalValue) 
						+ (MCTS.EXPLORATION_CONSTANT * Math.sqrt((Math.log(this.visits)) / child.visits)));
				
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
		Game tempGame;
		
		for (MOVE m : actions)
		{
			tempGame = gs;
			TreeNode tempNode = new TreeNode(m, this, tempGame, false);
			result += tempNode.value();
		}
		
		return result;
	}
	
	public double value()
	{		
		if (gs.wasPacManEaten())
			return (-10000.0);
		
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
			
		
		return 0.0;
	}
	
	public void updateValues(double value)
	{
		this.visits++;
		this.totalValue += value;
	}
	
	public boolean isLeafNode()
	{
//		return this.isFullyExpanded();
		return (this.children.size() == 0);
	}
	
	public boolean isTerminalNode()
	{
		return (gs.wasPacManEaten());
	}
	
	public boolean isFullyExpanded()
	{
		return (children.size() == actions.size());
	}
}
