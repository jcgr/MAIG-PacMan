package pacman.entries.jcgrPacMan.MCTS;

import java.util.ArrayList;
import java.util.List;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 * A class that represents a node of the Monte Carlo search tree.
 * A node is a junction, or a state where PacMan gets eaten.
 * 
 * @author Jacob
 */
public class TreeNode
{
	/**
	 * The number of times the node has been visited.
	 */
	public int timesVisited;
	
	/**
	 * The node that is a parent of this one.
	 */
	private TreeNode parentNode;
	
	/**
	 * The children of this node.
	 */
	private List<TreeNode> children;

	/**
	 * The game state of the node.
	 */
	private Game gameState;
	
	/**
	 * The move that was made in order to get to this node.
	 */
	private MOVE moveTo;
	
	/**
	 * The actions that can be taken from this node.
	 */
	private List<MOVE> actions;

	/**
	 * The depth this node has in the tree.
	 */
	private double depthOfPathToNode;

	/**
	 * The average survival score of this node's children.
	 */
	private double avgSurvivalValue;

	/**
	 * The highest pill score of any of this node's children.
	 */
	private double maxPillValue;

	/**
	 * The highest survival score of any of this node's children.
	 */
	private double maxSurvivalValue;

	/**
	 * Creates a new instance of the TreeNode class with the given values.
	 * @param game The game state belonging to the node.
	 * @param parent The parent of the node.
	 * @param pathDepth The depth of the path leading to this node.
	 */
	public TreeNode(Game game, TreeNode parent, double pathDepth)
	{
		this.timesVisited = 0;
		this.parentNode = parent;
		this.children = new ArrayList<TreeNode>();

		this.gameState = game.copy();
		this.moveTo = gameState.getPacmanLastMoveMade();
		this.actions = new ArrayList<MOVE>();
		MOVE[] possibleActions = gameState.getPossibleMoves(gameState.getPacmanCurrentNodeIndex()
//				, gameState.getPacmanLastMoveMade()
				);
		for (MOVE m : possibleActions)
			actions.add(m);

		this.depthOfPathToNode = pathDepth;
		this.maxPillValue = 0.0;
		this.avgSurvivalValue = 0.0;
		this.maxSurvivalValue = 0.0;
	}

	/**
	 * Gets the best child of the node.
	 */
	public TreeNode bestChild()
	{
		// If there are children that have not been visited at least
		// VISIT_THRESHOLD times, visit one of them.
		List<TreeNode> unvisitedChildren = new ArrayList<TreeNode>();
		for (TreeNode child : children)
			if (child.timesVisited < MCTS.VISIT_THRESHOLD)
				unvisitedChildren.add(child);

		if (unvisitedChildren.size() > 0)
			return unvisitedChildren.get(MCTS.random.nextInt(unvisitedChildren.size()));
		
		// Find the best child based on UCT value.
		TreeNode bestChild = null;
		double highestValue = -10000;
		
		for (TreeNode child : children)
		{
			double uctValue = (child.getScore()
					+ (MCTS.EXPLORATION_CONSTANT * (Math.sqrt(Math.log(this.timesVisited) / (child.timesVisited))))
					);
			
			if (uctValue > highestValue)
			{
				bestChild = child;
				highestValue = uctValue;
			}
		}

		return bestChild;
	}

	/**
	 * Expands the node and returns one of the children created.
	 */
	public TreeNode expand()
	{
		for (MOVE m : actions)
		{
			Game tempGame = gameState.copy();
			tempGame.advanceGame(m, MCTS.ghostStrategy.getMove(tempGame, MCTS.GHOST_THINK_TIME));

			while (!MCTS.pacManAtJunction(tempGame))
			{
				if (tempGame.wasPacManEaten())
					break;

				tempGame.advanceGame(m, MCTS.ghostStrategy.getMove(tempGame, MCTS.GHOST_THINK_TIME));
			}

			TreeNode newNode = new TreeNode(tempGame, this, this.depthOfPathToNode + 1);
			children.add(newNode);
		}

		return this.bestChild();
	}
	
	/**
	 * Gets the score of the node.
	 */
	public double getScore()
	{
		
		if (MCTS.SURVIVAL)
			return maxSurvivalValue;
		// If there are no pills nearby, just use the survival score.
		else if (maxPillValue == 0.0)
			return maxSurvivalValue;
		else
			return maxSurvivalValue * maxPillValue;
	}
	
	/**
	 * Gets the survival reward value based on how far PacMan gets
	 * before it dies (see 4.4 in paper)
	 */
	public double getRewardSurvival()
	{
		// If we switch level, the survival rate is max
		if (gameState.getMazeIndex() != MCTS.ROOT_MAZE_INDEX)
			return 1.0;
		// If PacMan is not eaten, return max value
		if (!gameState.wasPacManEaten())
			return 1.0;

		// ... else return the normalized value based on how deep the node is.
		double min = 0;
		double max = MCTS.MAX_PATH_DEPTH;

		return (this.depthOfPathToNode - min) / (max - min);
	}
	
	/**
	 * Gets the pill reward value based on how many pills PacMan 
	 * has eaten at this node. (see 4.4 in paper)
	 */
	public double getRewardPill()
	{
		if (gameState.getMazeIndex() != MCTS.ROOT_MAZE_INDEX)
			return 1.0;
	
		double min = 0;
		double max = MCTS.PILLS_AT_ROOT;
		double eaten = MCTS.PILLS_AT_ROOT - gameState.getNumberOfActivePills();

		if (eaten < 0)
			eaten = MCTS.PILLS_AT_ROOT;
		
		double normalized = (eaten - min) / (max - min);		
		return normalized;
	}
	
	/**
	 * Updates the various score values (see 4.1 in paper)
	 * @param sPill The pill reward value.
	 * @param sSurvival The survival reward value.
	 */
	public void updateValues(double sPill, double sSurvival)
	{
		timesVisited++;

		double average = (children.size() == 0 ? 1 : children.size());

//		double sumMaxPill = 0;
		double sumMaxSurvival = 0;

		for (TreeNode child : children)
		{
//			sumMaxPill += child.maxPillValue;
			sumMaxSurvival += child.maxSurvivalValue;
		}

//		avgPillValue = sumMaxPill / average;
		avgSurvivalValue = sumMaxSurvival / average;
		
		if(sPill > maxPillValue)
			maxPillValue = sPill;
		if (sSurvival > maxSurvivalValue)
			maxSurvivalValue = sSurvival;
	}
	
	public TreeNode getParentNode()
	{
		return this.parentNode;
	}
	
	public List<TreeNode> getChildren()
	{
		return this.children;
	}
	
	public double getAvgSurvival()
	{
		return this.avgSurvivalValue;
	}
	
	public double getPathLength()
	{
		return this.depthOfPathToNode;
	}
	
	public MOVE getMoveTo()
	{
		return this.moveTo;
	}
	
	public Game getGameState()
	{
		return this.gameState;
	}

	/**
	 * Checks if this node is a terminal node (PacMan is eaten,
	 * the depth is higher than allowed or a new level is reached)
	 * @return True if any of the above conditions is fulfilled;
	 * 		   False otherwise.
	 */
	public boolean isTerminalNode()
	{
		return (gameState.wasPacManEaten()
				|| (depthOfPathToNode >= MCTS.MAX_PATH_DEPTH)
				|| (gameState.getMazeIndex() != MCTS.ROOT_MAZE_INDEX)
				);
	}

	public boolean isLeafNode()
	{
		return (children.size() != actions.size());
	}
}
