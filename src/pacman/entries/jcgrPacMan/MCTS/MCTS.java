/**
 * 
 */
package pacman.entries.jcgrPacMan.MCTS;

import java.util.EnumMap;
import java.util.Random;

import pacman.controllers.examples.Legacy;
import pacman.controllers.examples.Legacy2TheReckoning;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 * 
 * 
 * @author Jacob
 */
public class MCTS
{
	static final double EXPLORATION_CONSTANT = 100;
	static final int MAX_DEPTH = 60;
	static Random random = new Random();
	
	static int pillsAtRoot, rootMaze;
	static boolean survival = false;
	
	private final int maxIterations = 40;
	private int currIteration;
	private TreeNode tree;
	private Game gs;
	
	public MCTS()
	{
	}
	
	public TreeNode search(Game gs)
	{
//		expansions = 0;
		this.gs = gs.copy();
		this.tree = new TreeNode(gs.getPacmanLastMoveMade(), null, this.gs, 0, true);
		currIteration = 0;
			
		TreeNode v = null;
		double delta = 0.0;
		
//		changeTactics();
		
		while (!terminate(currIteration))
		{
//			pillsAtRoot = gs.getNumberOfPills();
			pillsAtRoot = gs.getActivePillsIndices().length;
			rootMaze = gs.getMazeIndex();
			v = treePolicy(tree);
			delta = defaultPolicy(v);
			backup(v, delta);
			currIteration++;
		}
		TreeNode bc = tree.bestChild();
		
		if (bc != null)
		{
//			if (tree.children.size() > 1)
//			{
//				// System.out.println(tree.children.size());
//				for (TreeNode tn : tree.children)
//				{
//					System.out.println(tn.moveTo + " - Visited: " + tn.visits + " - Value: " + tn.totalValue);
//				}
//				System.out.println(bc.moveTo);
////				System.out.println("Tree root: " + tree.visits);
//				System.out.println("--------------------");
//			}
		}
		
		return bc;
	}
	
	public void changeTactics()
	{
		for(GHOST ghost : GHOST.values())
		{
			if(gs.getGhostEdibleTime(ghost) == 0 && gs.getGhostLairTime(ghost) == 0)
			{
				double distanceToGhost = gs.getShortestPathDistance(
						gs.getPacmanCurrentNodeIndex(), gs.getGhostCurrentNodeIndex(ghost));
				
				if (distanceToGhost < 30)
				{
					survival = true;
					return;
				}
			}
		}
		
		survival = false;
	}
	
	private TreeNode treePolicy(TreeNode node)
	{
		TreeNode v = node;
		
		while (!v.isTerminalNode())
		{
			if (!v.isFullyExpanded())
				return v.expand();
			else
				v = v.bestChild();
		}
		
		return v;
	}
	
	private double defaultPolicy(TreeNode v)
	{
		Legacy2TheReckoning sg = new Legacy2TheReckoning();
		double result = 0.0;
		TreeNode tempNode = v;
		Game tempGame = v.getGameState().copy();

		while (!tempNode.isTerminalNode())
		{
			MOVE[] possibleMoves;
			MOVE nextPMMove = MOVE.NEUTRAL;
			if (pacManAtJunction(tempGame))
			{
				possibleMoves = tempGame.getPossibleMoves(tempGame.getPacmanCurrentNodeIndex()
				 , tempGame.getPacmanLastMoveMade()
						);
				nextPMMove = possibleMoves[MCTS.random.nextInt(possibleMoves.length)];
//				nextPMMove = tempNode.moveTo;
			}
//			else
//				possibleMoves = tempGame.getPossibleMoves(tempGame.getPacmanCurrentNodeIndex()
//						, tempGame.getPacmanLastMoveMade()
//						);
//			
//			MOVE nextPMMove = possibleMoves[MCTS.random.nextInt(possibleMoves.length)];
			
			EnumMap<GHOST, MOVE> validGhostMoves = sg.getMove(tempGame, -1);
//			EnumMap<GHOST, MOVE> validGhostMoves = new EnumMap<GHOST, MOVE>(GHOST.class);
//			for (GHOST ghost : GHOST.values())
//			{
//				
//				if (tempGame.getGhostEdibleTime(ghost) == 0 && tempGame.getGhostLairTime(ghost) == 0)
//				{
////					possibleMoves = tempGame.getPossibleMoves(tempGame.getGhostCurrentNodeIndex(ghost)
////							, tempGame.getGhostLastMoveMade(ghost)
////							);
////					MOVE nextGMove = possibleMoves[MCTS.random.nextInt(possibleMoves.length)];
//					
//					MOVE nextGMove = tempGame.getNextMoveTowardsTarget(tempGame.getGhostCurrentNodeIndex(ghost)
//							, tempGame.getPacmanCurrentNodeIndex()
//							, DM.PATH);
//					validGhostMoves.put(ghost, nextGMove);
//				}
//			}

			tempGame.advanceGame(nextPMMove, validGhostMoves);
			
//			if (tempGame.getMazeIndex() != MCTS.rootMaze)
//				break;
			
			tempNode = new TreeNode(nextPMMove, tempNode, tempGame, tempNode.depth + 1, false);
//			result += tempNode.getReward();
			// System.out.print(tempNode.getReward() + " | ");
		}
		// System.out.println();
		// System.out.print(result);

//		 return result;
		// System.out.println(tempNode.getReward() + " | " + result);
		return tempNode.getReward();
	}
	
	private void backup(TreeNode v, double delta)
	{
		TreeNode currNode = v;
		
		while (currNode != null)
		{
			currNode.updateValues(delta);
			currNode = currNode.parent;
		}
	}
	
	private boolean terminate(int i)
	{
		return (i >= this.maxIterations);
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
