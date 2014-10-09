/**
 * 
 */
package pacman.entries.jcgrPacMan.MCTS;

import java.util.EnumMap;
import java.util.Random;

import pacman.controllers.examples.Legacy2TheReckoning;
import pacman.entries.jcgrPacMan.BT.STATUS;
import pacman.game.Constants.GHOST;
import pacman.game.Game;
import pacman.game.Constants.MOVE;

/**
 * 
 * 
 * @author Jacob
 */
public class MCTS2
{
	public static final double EXPLORATION_CONSTANT = 80;
	public static final double MAX_PATH_TO_ROOT = 35;
	public static final int VISIT_THRESHOLD = 3;
	public static int ROOT_MAZE_INDEX;
	public static int PILLS_AT_ROOT;
	public static int POWER_PILLS_AT_ROOT;
	public static boolean SURVIVAL;
	public static Random random = new Random();
	public static Legacy2TheReckoning ghostStrategy = new Legacy2TheReckoning();
	
	private final int MAX_ITERATIONS = 40;
	
	private int currIteration;
	private TreeNode2 root;
	
	public MCTS2()
	{
	}
	
	public TreeNode2 search(Game game, int iteration)
	{
		PILLS_AT_ROOT = game.getNumberOfActivePills();
		POWER_PILLS_AT_ROOT = game.getNumberOfActivePowerPills();
		ROOT_MAZE_INDEX = game.getMazeIndex();
		currIteration = 0;
		root = new TreeNode2(game.copy(), null, game.getPacmanLastMoveMade(), 0);
		
		TreeNode2 currNode = root;
		
		System.out.println("Iteration " + iteration);
		
		while (currIteration < MAX_ITERATIONS)
		{
			currNode = selection();	
			currNode = playout(currNode);
			backpropagate(currNode);
			currIteration++;
		}
		
		TreeNode2 bc = root.bestChild();
		
		if (bc != null)
		{
			for (TreeNode2 child : root.children)
			{
				System.out.print(child.getMoveTo() + " | " + child.getScore());
				System.out.println(" and " + child.maxSSurvival + " " + child.maxSPill);
				
				for (TreeNode2 cc : child.children)
				{
					System.out.println(cc.getMoveTo() + " | " + cc.visits);
				}
			}
			System.out.println("Chosen move: " + bc.getMoveTo());
		}
		System.out.println("--------------");
		
		return root.bestChild();
	}
	
	private TreeNode2 selection()
	{
		TreeNode2 tempNode = root;
		
		while(!tempNode.isTerminalNode())
		{
			if (!tempNode.isLeafNode())
				tempNode = tempNode.bestChild();
			else
				return tempNode.expand();
		}
		
		return tempNode;
	}
	
	private TreeNode2 playout(TreeNode2 node)
	{
		TreeNode2 tempNode = node;
		Game tempGame = tempNode.getGameState();
		
		while (!tempNode.isTerminalNode())
		{
			MOVE nextPMMove = MOVE.NEUTRAL;
			if (pacManAtJunction(tempGame))
			{
				MOVE[] possibleMoves = tempGame.getPossibleMoves(tempGame.getPacmanCurrentNodeIndex(),
						tempGame.getPacmanLastMoveMade());
				nextPMMove = possibleMoves[random.nextInt(possibleMoves.length)];
			}
			
			EnumMap<GHOST, MOVE> ghostMoves = ghostStrategy.getMove(tempGame, -1);
			
			tempGame.advanceGame(nextPMMove, ghostMoves);
			tempNode = new TreeNode2(tempGame, tempNode, nextPMMove, tempNode.getPathLength() + 1.0);
		}
		
		return tempNode;
	}
	
	public void backpropagate(TreeNode2 node)
	{
		TreeNode2 tempNode = node;
		double sSurvival = node.getRewardSurvival();
		double sPill = node.getRewardPill();
		
//		System.out.println(sSurvival + " " + sPill);
		
		while (tempNode != null)
		{
			tempNode.updateValues(sPill, sSurvival);
			tempNode = tempNode.parent;
		}
	}
	
	public static boolean indexAtJunction(Game game, int index)
	{
		boolean atJunction = false;
		int[] juncts = game.getJunctionIndices();
		for (int i : juncts)
			if (i == index)
				atJunction = true;
		
		return atJunction;
	}
	
	public static boolean pacManAtJunction(Game game)
	{
		boolean atJunction = false;
		int pmIndex = game.getPacmanCurrentNodeIndex();
		int[] juncts = game.getJunctionIndices();
		for (int i : juncts)
			if (i == pmIndex)
				atJunction = true;
		
		return atJunction;
	}
}
