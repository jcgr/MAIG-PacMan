/**
 * 
 */
package pacman.entries.jcgrPacMan.behaviourtreePacMan;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Timer;

import pacman.controllers.*;
//import pacman.entries.jcgrPacMan.BTPacMan.nodes.DecideNextMoveNode;
//import pacman.entries.jcgrPacMan.BTPacMan.nodes.DodgeGhostNode;
//import pacman.entries.jcgrPacMan.BTPacMan.nodes.FindPathToNearestTargetNode;
//import pacman.entries.jcgrPacMan.BTPacMan.nodes.FindPossibleTargetsNode;
import pacman.entries.jcgrPacMan.behaviourtree.controllers.*;
import pacman.entries.jcgrPacMan.behaviourtree.nodes.*;
import pacman.entries.jcgrPacMan.behaviourtreePacMan.nodes.*;
import pacman.game.Game;
import pacman.game.GameView;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;

/**
 * 
 * 
 * @author Jacob
 */
public class BehaviourPacMan extends Controller<MOVE>
{	
	private Node rootNode;
	
	private PacManContext context;
	
	public BehaviourPacMan()
	{
		context = new PacManContext();
		this.CreateBT();
		this.rootNode.getController().safeStart();
	}
	
	private void CreateBT()
	{
		this.rootNode = new Selector(context, "Planner");
		
		Node play = new Sequence(context, "PlaySequence");
		play = new ResetDecorator(context, play, "ResetDecorator");
//		play = new ResetDecorator(context, play, "ResetDecorator");
		((ParentNodeController)play.getController()).addNode(new FindPossibleTargetsNode(context, "FindPossibleTargetsNode"));
		((ParentNodeController)play.getController()).addNode(new DecideNextMoveNode(context, "DecideNextMoveNode"));
//		((ParentNodeController)play.getController()).addNode(new FindPathToNearestTargetNode(context, "FindPathToNearestTargetNode"));
//		((ParentNodeController)play.getController()).addNode(new DodgeGhostNode(context, "DodgeGhostNode"));
		
//		((ParentNodeController)rootNode.getController()).addNode(play);
		this.rootNode = play;
	}
	
	/* (non-Javadoc)
	 * @see pacman.controllers.Controller#getMove(pacman.game.Game, long)
	 */
	public MOVE getMove(Game game,long timeDue)
	{
		context.game = game;
		context.currentNodeIndex = game.getPacmanCurrentNodeIndex();
//		context.activePills = new int[0];
		context.activePills = game.getActivePillsIndices();
		context.activePowerPills = game.getActivePowerPillsIndices();
		
//		int curTargets = context.activePills.clone().length + context.activePowerPills.clone().length;

//		int[] activePills = ((PacManContext)context).game.getActivePillsIndices();
		
		//get all active power pills
//		int[] activePowerPills = ((PacManContext)context).game.getActivePowerPillsIndices();
		
		//create a target array that includes all ACTIVE pills and power pills
//		((PacManContext)context).targetNodeIndices = new int[context.activePills.length + context.activePowerPills.length];
//		
//		for(int i = 0; i < context.activePills.length; i++)
//			((PacManContext)context).targetNodeIndices[i] = context.activePills[i];
//		
//		for(int i = 0; i < context.activePowerPills.length; i++)
//			((PacManContext)context).targetNodeIndices[context.activePills.length + i] = context.activePowerPills[i];	
		
		this.rootNode.doAction();

//		System.out.println();
//		System.out.println();
//		for(int i : context.pathToTarget)
//		{
//			System.out.print(i + ", ");
//		}

//		System.out.println(context.targetNodeIndices.length);
		
//		context.currentTarget = context.game.getClosestNodeIndexFromNodeIndex(context.currentNodeIndex, context.targetNodeIndices, DM.PATH);
//		MOVE nextMove = context.game.getNextMoveTowardsTarget(context.currentNodeIndex, context.currentTarget, DM.PATH);
//		MOVE actualMove = nextMove;
//
//		int posTargets = game.getActivePillsIndices().clone().length + game.getActivePowerPillsIndices().clone().length;
//		boolean found = false;
//		for (int i : context.targetNodeIndices)
//		{
//			if (i == context.currentTarget)
//				found = true;
//		}
//		System.out.println(found + " " + context.targetNodeIndices.length + " | " + posTargets);
////		
//		
//		
////		if ()
//		
//		if (context.currentTarget == context.lastTarget)
//			if (oppesiteOf(nextMove, context.lastMove))
//			{
//				int[] activePills = ((PacManContext)context).activePills;
//				int[] activePowerPills = ((PacManContext)context).activePowerPills;
//				boolean found = false;
//				//create a target array that includes all ACTIVE pills and power pills
//				int[] tempTargets = new int[activePills.length + activePowerPills.length];
////				((PacManContext)context).targetNodeIndices = new int[activePills.length + activePowerPills.length];
//				
//				for(int i = 0; i < activePills.length; i++)
//					tempTargets[i] = activePills[i];
////					((PacManContext)context).targetNodeIndices[i] = activePills[i];
//				
//				for(int i = 0; i < activePowerPills.length; i++)
//					tempTargets[activePills.length + i] = activePowerPills[i];
//				
//				for (int i : tempTargets)
//				{
//					if (i == context.currentTarget)
//						found = true;
//				}
//				System.out.println(found + " " + tempTargets.length);
//				actualMove = context.lastMove;
//			}
////				actualMove = MOVE.NEUTRAL;
////		
////			System.out.println(context.lastMove + " - Direction: " + nextMove + " (" + actualMove + ") to " + context.currentTarget);
//		System.out.println("Direction: " + nextMove + " to " + context.currentTarget + " | " + curTargets + " " + posTargets);
//		context.lastTarget = context.currentTarget;
//		context.lastMove = actualMove;

		System.out.println(context.currentMove + " to " + context.currentTarget);
		return context.currentMove;
//		int nearest = context.game.getClosestNodeIndexFromNodeIndex(context.currentNodeIndex, context.targetNodeIndices, DM.PATH);
//		return context.game.getNextMoveTowardsTarget(context.currentNodeIndex, context.game.getClosestNodeIndexFromNodeIndex(context.currentNodeIndex, context.targetNodeIndices, DM.PATH), DM.PATH);
		
//		return game.getNextMoveTowardsTarget(context.game.getPacmanCurrentNodeIndex(),context.game.getClosestNodeIndexFromNodeIndex(context.currentNodeIndex,context.targetNodeIndices,DM.PATH),DM.PATH);
	}

	private boolean oppesiteOf(MOVE currentMove, MOVE lastMove)
	{
		boolean oppesite = false;
		
		if (currentMove == MOVE.LEFT && lastMove == MOVE.RIGHT)
			oppesite = true;
		if (currentMove == MOVE.RIGHT && lastMove == MOVE.LEFT)
			oppesite = true;
		if (currentMove == MOVE.UP && lastMove == MOVE.DOWN)
			oppesite = true;
		if (currentMove == MOVE.DOWN && lastMove == MOVE.UP)
			oppesite = true;
		
		return oppesite;
	}
}
