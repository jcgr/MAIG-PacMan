/**
 * 
 */
package pacman.entries.jcgrPacMan.MCTS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;

import pacman.controllers.examples.Legacy2TheReckoning;
import pacman.game.Constants.DM;
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

		MOVE[] possibleMoves = gs.getPossibleMoves(gs.getPacmanCurrentNodeIndex()
				, gs.getPacmanLastMoveMade()
				);
		for (MOVE m : possibleMoves)
			actions.add(m);

		actionsTried = new boolean[actions.size()];
		for (int action = 0; action < actions.size(); action++)
			actionsTried[action] = false;

		// The root node should not have any action happen.
		if (!root)
		{
			Legacy2TheReckoning sg = new Legacy2TheReckoning();
			ghostMoves = sg.getMove(gs, -1);
//			ghostMoves = new EnumMap<GHOST, MOVE>(GHOST.class);
//			for (GHOST ghost : GHOST.values())
//				ghostMoves.put(ghost, gs.getGhostLastMoveMade(ghost));

			gs.advanceGame(this.moveTo, ghostMoves);
		}

		this.totalValue = 0.0;
		this.visits = 1;
	}

	public TreeNode expand()
	{
//		for (MOVE m : actions)
//		{
//			TreeNode expandedNode = new TreeNode(m, this, gs.copy(), this.depth + 1, false);
//			children.add(expandedNode);
//		}
//		return this.bestChild();
		MOVE[] untriedActions = getUntriedActions();
		MOVE nextTry = untriedActions[MCTS.random.nextInt(untriedActions.length)];
		// System.out.println(nextTry.toString() + " was added");
		TreeNode expandedNode = new TreeNode(nextTry, this, gs.copy(), this.depth + 1, false);
		children.add(expandedNode);

		for (int i = 0; i < actions.size(); i++)
			if (actions.get(i) == nextTry)
				actionsTried[i] = true;

		return expandedNode;
	}

	private MOVE[] getUntriedActions()
	{
		List<MOVE> utActions = new ArrayList<MOVE>();

		for (int i = 0; i < actions.size(); i++)
			if (!actionsTried[i])
				utActions.add(actions.get(i));

		MOVE[] result = new MOVE[utActions.size()];
		for (int i = 0; i < utActions.size(); i++)
			result[i] = utActions.get(i);

		return result;
	}

	/**
	 * Selects the node's best child (randomly chosen if none of them have been visited)
	 * 
	 * @return
	 */
	public TreeNode bestChild()
	{
		TreeNode selected = null;
		double bestValue = -100000.0;

//		List<TreeNode> nodes = new ArrayList<TreeNode>();
//		
//		for (TreeNode child : children)
//			if (child.visits < 3)
//				nodes.add(child);
//		
//		if (nodes.size() > 0)
//			return nodes.get(MCTS.random.nextInt(nodes.size()));
		
		for (TreeNode child : children)
		{
//			double uctValue = ((child.totalValue / child.visits) 
//					+ (MCTS.EXPLORATION_CONSTANT 
//						* Math.sqrt(2 * (Math.log(this.visits))	/ child.visits)));
			double uctValue = ((child.totalValue) 
					+ (MCTS.EXPLORATION_CONSTANT 
						* Math.sqrt((Math.log(this.visits))	/ child.visits)));
			
//			uctValue += MCTS.EXPLORATION_CONSTANT * MCTS.random.nextDouble();
			
			if (uctValue > bestValue)
			{
				selected = child;
				bestValue = uctValue;
			}
		}

		return selected;
	}

	public double getReward()
	{
		double result = 0.0;

//		// Was PacMan eaten
		if (gs.wasPacManEaten())
		{
//			System.out.println("Retard coming through, going " + this.moveTo);
			result += 0.0;
		}
		else
			result += 1.0;

			// Normalizing pills eaten
//			double min = 0;
//			double max = MCTS.pillsAtRoot;
//			double eaten = max - gs.getActivePillsIndices().length;
//
//			if (eaten < min)
//				eaten = max;
//			
////			System.out.println(eaten);
//			
//			if (eaten < 0)
//			{
//				System.out.println("Wat! " + eaten);
//				System.out.println(max);
//				System.out.println(gs.getActivePillsIndices().length);
//				System.out.println("-------------------");
//			}
//			
//			double normalized = (eaten - min) / (max - min);
//				result += normalized;
//		
//		return result;
		
//		if (gs.getScore() > 0)
			return result * (gs.getScore() / 1000.0);
//		return gs.getScore() / 1000;
		
//		double closestGhost = 50;
////		System.out.println(Arrays.toString(GHOST.values()));
//		for (GHOST ghost : GHOST.values())
//		{
////			System.out.println(ghost == null);
//			if(gs.getGhostEdibleTime(ghost) == 0 && gs.getGhostLairTime(ghost) == 0)
//			{
//				double dist = gs.getDistance(gs.getPacmanCurrentNodeIndex()
//						, gs.getGhostCurrentNodeIndex(ghost)
//						, DM.PATH);
//				if (dist < closestGhost)
//					closestGhost = dist;
//			}
//		}
//		
//		return closestGhost;
	}

	public void updateValues(double value)
	{
		this.visits++;
		this.totalValue += value;
		// System.out.print("(" + visits + ", " + totalValue + ") - ");
	}
	
	public Game getGameState()
	{
		return this.gs;
	}

	public boolean isTerminalNode()
	{
		return (gs.wasPacManEaten() 
				|| depthCheck()
				|| (gs.getMazeIndex() != MCTS.rootMaze)
//				|| (gs.getActivePillsIndices().length == 0 
//					&& gs.getActivePowerPillsIndices().length == 0)
				);
	}
	
	private boolean depthCheck()
	{
		return depth > MCTS.MAX_DEPTH;
	}

	public boolean isFullyExpanded()
	{
		return (children.size() == actions.size());
	}
}
