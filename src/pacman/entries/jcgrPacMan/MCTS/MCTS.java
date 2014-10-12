/**
 * 
 */
package pacman.entries.jcgrPacMan.MCTS;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pacman.controllers.examples.Legacy2TheReckoning;
import pacman.game.Game;
import pacman.game.Constants.MOVE;

/**
 * 
 * 
 * @author Jacob
 */
public class MCTS
{
	/**
	 * The maximum number of iterations to run when searching.
	 */
	private final int MAX_ITERATIONS = 100;
	
	/**
	 * The number of milliseconds the search is allowed to use.
	 */
	private final long ALLOWED_TIME = 40;

	/**
	 * The threshold for activating survival mode.
	 */
	public static final double SURVIVAL_THRESHOLD = 0.7;

	/**
	 * The minimum amount of times a node's children should be visited before UCT is applied for selecting the best child.
	 */
	public static final int VISIT_THRESHOLD = 3;
	
	/**
	 * The exploration constant.
	 */
	public static final double EXPLORATION_CONSTANT = 1.5;

	/**
	 * The maximum depth when simulating a playthrough.
	 */
	public static final double MAX_PATH_DEPTH = 80;

	/**
	 * How much time the ghosts have to think when advancing game (-1 = infinite)
	 */
	public static int GHOST_THINK_TIME = -1;

	/**
	 * The maze index at the root (used to check if level changes during simulation)
	 */
	public static int ROOT_MAZE_INDEX;

	/**
	 * The amount of pills at the root (used to calculate value of nodes)
	 */
	public static int PILLS_AT_ROOT;

	/**
	 * A boolean indicating whether the AI should focus on survival (true) or not.
	 */
	public static boolean SURVIVAL;

	/**
	 * Random generator used for getting random children.
	 */
	public static Random random = new Random();

	/**
	 * The strategy of the ghosts being competed against.
	 */
	public static Legacy2TheReckoning ghostStrategy = new Legacy2TheReckoning();

	/**
	 * The current iteration.
	 */
	private int currIteration;

	/**
	 * The root of the search tree.
	 */
	private TreeNode root;

	/**
	 * Runs a Monte-Carlo tree search starting from the current game state.
	 * @param game
	 * @return The best move from the root of the tree.
	 */
	public TreeNode search(Game game)
	{
		root = new TreeNode(game.copy(), null, 0);

		PILLS_AT_ROOT = game.getNumberOfActivePills();
		ROOT_MAZE_INDEX = game.getMazeIndex();

		TreeNode currNode = root;
		long timeSpent = 0;

		currIteration = 0;
		while (timeSpent < ALLOWED_TIME
				&& currIteration < MAX_ITERATIONS)
		{
			long timeAtIterationStart = System.currentTimeMillis();
			
			// Selection + Expansion
			currNode = selection();

			// Playout / simulation
			TreeNode tempNode = playout(currNode);

			// Backpropagation
			backpropagate(currNode, tempNode);
			currIteration++;
			timeSpent += System.currentTimeMillis() - timeAtIterationStart;
		}

		TreeNode bestNode = null;
		double bestNodeScore = -100;

		for (TreeNode tn : root.getChildren())
		{
			if (tn.getScore() > bestNodeScore)
			{
				bestNode = tn;
				bestNodeScore = tn.getScore();
			}
		}

		this.changeTactics(bestNode);

		return bestNode;
	}

	/**
	 * Selects the best child or expands the node. Selection/Expansion from MCTS.
	 * 
	 * @return The newly expanded node or the best child.
	 */
	private TreeNode selection()
	{
		TreeNode tempNode = root;

		while (!tempNode.isTerminalNode())
			if (!tempNode.isLeafNode())
				tempNode = tempNode.bestChild();
			else
				return tempNode.expand();

		return tempNode;
	}

	/**
	 * Simulates a play starting at the given node.
	 * 
	 * @param node
	 * @return The node that the simulation ends at.
	 */
	private TreeNode playout(TreeNode node)
	{
		TreeNode tempNode = node;

		while (!tempNode.isTerminalNode())
		{
			Game tempGame = tempNode.getGameState().copy();
			MOVE[] possibleMoves = tempGame.getPossibleMoves(tempGame.getPacmanCurrentNodeIndex()
//					, tempGame.getPacmanLastMoveMade()
					);
			MOVE m = possibleMoves[random.nextInt(possibleMoves.length)];

			tempGame.advanceGame(m, MCTS.ghostStrategy.getMove(tempGame, GHOST_THINK_TIME));

			while (!MCTS.pacManAtJunction(tempGame))
			{
				if (tempGame.wasPacManEaten())
					return new TreeNode(tempGame, tempNode, tempNode.getPathLength() + 1.0);

				tempGame.advanceGame(m, MCTS.ghostStrategy.getMove(tempGame, GHOST_THINK_TIME));
			}

			tempNode = new TreeNode(tempGame, tempNode, tempNode.getPathLength() + 1.0);
		}

		return tempNode;
	}

	/**
	 * Backpropagates the tree, using the given node as start point.
	 * 
	 * @param node
	 */
	public void backpropagate(TreeNode currNode, TreeNode endNode)
	{
		TreeNode tempNode = currNode;
		double sSurvival = endNode.getRewardSurvival();
		double sPill = endNode.getRewardPill();

		while (tempNode != null)
		{
			tempNode.updateValues(sPill, sSurvival);
			tempNode = tempNode.getParentNode();
		}
	}

	/**
	 * Changes the tactic based on the given node.
	 * 
	 * @param node
	 *            The node to use for changing tactics.
	 */
	private void changeTactics(TreeNode node)
	{
		if (node.getAvgSurvival() < SURVIVAL_THRESHOLD)
			SURVIVAL = true;
		else
			SURVIVAL = false;
	}

	/**
	 * Checks if PacMan is at a junction (with corners being regarded as junctions).
	 * 
	 * @param game
	 *            The game state to check for.
	 * @return True if PacMan is at a junction; False otherwise.
	 */
	public static boolean pacManAtJunction(Game game)
	{
		MOVE[] possibleMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex());
		List<MOVE> moveList = new ArrayList<MOVE>();
		for (MOVE m : possibleMoves)
		{
			moveList.add(m);
		}

		moveList.remove(game.getPacmanLastMoveMade());
		moveList.remove(game.getPacmanLastMoveMade().opposite());

		return moveList.size() != 0;
	}
}
