/**
 * 
 */
package pacman.entries.jcgrPacMan.BTPacMan;

import pacman.controllers.Controller;
import pacman.entries.jcgrPacMan.BT.*;
import pacman.entries.jcgrPacMan.BTPacMan.nodes.EatEdibleGhost;
import pacman.entries.jcgrPacMan.BTPacMan.nodes.FindPathToTarget;
import pacman.entries.jcgrPacMan.BTPacMan.nodes.FindPossibleTargets;
import pacman.entries.jcgrPacMan.BTPacMan.nodes.FleeFromGhost;
import pacman.entries.jcgrPacMan.BTPacMan.nodes.RemoveGhostIndices;
import pacman.game.Game;
import pacman.game.Constants.MOVE;

/**
 * 
 * 
 * @author Jacob
 */
public class BTPacMan extends Controller<MOVE>
{
	private RootNode tree;
	
	private PacManContext context;
	
	public BTPacMan()
	{
		context = new PacManContext();
		createBT();
	}
	
	private void createBT()
	{
		Selector root = new Selector("RootSelector");
		
		Selector ghostBehaviour = new Selector("GhostBehaviour");
		ghostBehaviour.addNode(new FleeFromGhost("FleeFromGhost"));
		ghostBehaviour.addNode(new EatEdibleGhost("EatEdibleGhost"));
		
		Sequence chasePill = new Sequence("ChasePill");
		chasePill.addNode(new FindPossibleTargets("FindPossibleTargetsNode", true));
		chasePill.addNode(new RemoveGhostIndices("RemoveGhostIndices"));
		chasePill.addNode(new FindPathToTarget("FindPathToTarget"));
		
		root.addNode(ghostBehaviour);
		root.addNode(chasePill);
		
		tree = new RootNode(this, context, root);
	}
	
	/* (non-Javadoc)
	 * @see pacman.controllers.Controller#getMove(pacman.game.Game, long)
	 */
	public MOVE getMove(Game game, long timeDue)
	{
		context.game = game;
		context.activePills = game.getActivePillsIndices();
		context.activePowerPills = game.getActivePowerPillsIndices();
		context.currentPacManIndex = game.getPacmanCurrentNodeIndex();
		
		STATUS tempStatus = tree.run(context);
		
		if (tempStatus != STATUS.SUCCESS)
			return MOVE.NEUTRAL;
		else
			return context.nextMove;
	}
}
