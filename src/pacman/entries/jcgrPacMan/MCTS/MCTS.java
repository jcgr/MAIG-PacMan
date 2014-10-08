/**
 * 
 */
package pacman.entries.jcgrPacMan.MCTS;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 * 
 * 
 * @author Jacob
 */
public class MCTS
{
	static final double EXPLORATION_CONSTANT = 1;
	static final int MAX_DEPTH = 10;
	static Random random = new Random();
	
	static int expansions;
	
	private final int maxIterations = 20;
	private int currIteration;
	private List<MOVE> actions;
	private TreeNode tree;
	private TreeNode currentNode;
	private Game gs;
	
	public MCTS()
	{
	}
	
	public TreeNode search(Game gs)
	{
		expansions = 0;
		this.gs = gs.copy();
		this.tree = new TreeNode(MOVE.NEUTRAL, null, gs, 0, true);
		currIteration = 0;
		
//		List<TreeNode> bestChoice = null;
		TreeNode v = null;
		double delta = 0.0;
		
		while (!terminate(currIteration))
		{
			v = treePolicy(tree);
			delta = defaultPolicy(v);
			backup(v, delta);
			System.out.println();
			currIteration++;
		}
//		System.out.println("Test");

//		System.out.print(tree.children.size() + " | ");
//		for (TreeNode tn : tree.children)
//		{
//			System.out.print(tn.moveTo + " - ");
//		}
//		System.out.println(tree.bestChild().moveTo);
		return tree.bestChild();
	}
	
	private TreeNode treePolicy(TreeNode v)
	{
		currentNode = v;
		
		while (!currentNode.isTerminalNode())
		{
			if (!currentNode.isFullyExpanded())
				return currentNode.expand();
			else
				currentNode = currentNode.bestChild();
		}
		
		return currentNode;
	}
	
	private double defaultPolicy(TreeNode v)
	{
		return v.simulation();
	}
	
	private void backup(TreeNode v, double delta)
	{
		TreeNode currNode = v;
		
		while (currNode != null)
		{
			currNode.visits += 1;
			currNode.totalValue += delta;
			currNode = currNode.parent;
		}
	}
	
	private boolean terminate(int i)
	{
		return (i > this.maxIterations);
	}
}
