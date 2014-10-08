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
	static final double EXPLORATION_CONSTANT = 0.5;
	static final int MAX_DEPTH = 20;
	static Random random = new Random();
	
	private final int maxIterations = 40;
	private int currIteration;
	private List<MOVE> actions;
	private TreeNode tree;
	private TreeNode currentNode;
	private Game gs;
	
	public MCTS()
	{
	}
	
	public List<TreeNode> search(Game gs)
	{
		this.gs = gs.copy();
		this.tree = new TreeNode(MOVE.NEUTRAL, null, gs, true);
		
		List<TreeNode> bestChoice = null;
		currIteration = 0;
		
		while (!terminate(currIteration))
		{
			treePolicy();
//			bestChoice = attemptOne();			
			currIteration++;
		}
		
		return bestChoice;
	}
	
	private void treePolicy()
	{
		currentNode = tree;
		
	}
	
	private List<TreeNode> attemptOne()
	{
		List<TreeNode> visited = new ArrayList<TreeNode>();
		TreeNode currNode = tree;
		
		visited.add(tree);
		
		// 1. Selection
		// Find the most valuable leaf node (can be root, if it has not
		// been expanded yet)
		while (!currNode.isLeafNode())
		{
			currNode = currNode.bestChild();
			visited.add(currNode);
		}
		
		// 2. Expansion
		// Expand the node and select its best child.
		currNode.expand();
		TreeNode newNode = currNode.bestChild();
		visited.add(newNode);
		
		// 3. Simulation
		// 
		double valueChange = newNode.simulation();
//		double valueChange = 0.0;
//		for (TreeNode tn : visited)
//			valueChange += tn.value();
		
		// 4. Backpropagate
		for (TreeNode tn : visited)
			tn.updateValues(valueChange);
		
		return visited;
	}
	
	private boolean terminate(int i)
	{
		return (i > this.maxIterations);
	}
}
