/**
 * 
 */
package pacman.entries.jcgrPacMan.NN.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pacman.controllers.examples.Legacy2TheReckoning;
import pacman.entries.jcgrPacMan.MCTS.MCTS2;
import pacman.game.Game;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

/**
 * 
 * 
 * @author Jacob
 */
public class Simulation
{
	static Random random = new Random();
	static Legacy2TheReckoning ghostStrategy = new Legacy2TheReckoning();
	static int maxDepth;
	static int rootMaze;
	static int pillsAtRoot;
	static int powerPillsAtRoot;
	
	SimNode root;
	
	public SimNode simulate(Game game, int maxDepth)
	{
		this.maxDepth = maxDepth;
		this.rootMaze = game.getMazeIndex();
		pillsAtRoot = game.getNumberOfPills();
		powerPillsAtRoot = game.getNumberOfPowerPills();
		MOVE[] possibleMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex());
		List<SimNode> terminals = new ArrayList<SimNode>();
		List<SimNode> toVisit = new ArrayList<SimNode>();
//		System.out.println(powerPillActive(game));
		root = new SimNode(null, game.getPacmanLastMoveMade(), game, 0, powerPillActive(game));
		toVisit.add(root);
//		for (MOVE m : possibleMoves)
//		{
//			Game tempGame = game.copy();
//			tempGame.advanceGame(m, ghostStrategy.getMove(tempGame, -1));
//			Node newNode = new Node(root, m, tempGame, 1);
//			toVisit.add(newNode);
//		}
		
		while (!toVisit.isEmpty())
		{
			List<SimNode> newNodes = new ArrayList<SimNode>();
			SimNode tempNode = toVisit.remove(0);
			
			if (tempNode.isTerminalNode())
			{
				terminals.add(tempNode);
				continue;
			}
			
			if (!tempNode.isExpanded())
				newNodes = tempNode.expand();
			
			if (!newNodes.isEmpty())
				toVisit.addAll(newNodes);
		}
		
		while (!terminals.isEmpty())
		{
			SimNode tempNode = terminals.remove(0);
			double survival = tempNode.getSurvivalReward();
			double pills = tempNode.getPillReward();
			
			while (tempNode != null)
			{
				tempNode.updateStats(pills, survival);
				tempNode = tempNode.getParent();
			}
		}
		
		List<SimNode> children = root.children;
		
//		for (SimNode n : children)
//		{
//			System.out.println(n.moveTo + " | " + n.getScore());
//		}
//		System.out.println(root.getBestChild().moveTo);
//		System.out.println("----------------");
		
		SimNode choice = root.getBestChild();
		
		return choice;
	}
	
	public List<SimNode> simulateList(Game game, int maxDepth)
	{
		this.maxDepth = maxDepth;
		this.rootMaze = game.getMazeIndex();
		pillsAtRoot = game.getNumberOfPills();
		powerPillsAtRoot = game.getNumberOfPowerPills();
		MOVE[] possibleMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex());
		List<SimNode> terminals = new ArrayList<SimNode>();
		List<SimNode> toVisit = new ArrayList<SimNode>();
//		System.out.println(powerPillActive(game));
		root = new SimNode(null, game.getPacmanLastMoveMade(), game, 0, powerPillActive(game));
		toVisit.add(root);
//		for (MOVE m : possibleMoves)
//		{
//			Game tempGame = game.copy();
//			tempGame.advanceGame(m, ghostStrategy.getMove(tempGame, -1));
//			Node newNode = new Node(root, m, tempGame, 1);
//			toVisit.add(newNode);
//		}
		
		while (!toVisit.isEmpty())
		{
			List<SimNode> newNodes = new ArrayList<SimNode>();
			SimNode tempNode = toVisit.remove(0);
			
			if (tempNode.isTerminalNode())
			{
				terminals.add(tempNode);
				continue;
			}
			
			if (!tempNode.isExpanded())
				newNodes = tempNode.expand();
			
			if (!newNodes.isEmpty())
				toVisit.addAll(newNodes);
		}
		
		while (!terminals.isEmpty())
		{
			SimNode tempNode = terminals.remove(0);
			double survival = tempNode.getSurvivalReward();
			double pills = tempNode.getPillReward();
			
			while (tempNode != null)
			{
				tempNode.updateStats(pills, survival);
				tempNode = tempNode.getParent();
			}
		}
		
		List<SimNode> children = root.children;
		
//		for (SimNode n : children)
//		{
//			System.out.println(n.moveTo + " | " + n.getScore());
//		}
//		System.out.println(root.getBestChild().moveTo);
//		System.out.println("----------------");
		
//		SimNode choice = root.getBestChild();
		
		return children;
	}
	
	public static boolean powerPillActive(Game game)
	{
		boolean powerPillActive = false;
		for (GHOST ghost : GHOST.values())
		{
			if (game.getGhostEdibleTime(ghost) > 0 && game.getGhostLairTime(ghost) == 0)
				powerPillActive = true;
			if (game.getGhostLairTime(ghost) > 0)
				powerPillActive = true;
		}
		
		return powerPillActive;
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
