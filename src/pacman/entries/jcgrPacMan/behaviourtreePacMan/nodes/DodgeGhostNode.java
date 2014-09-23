/**
 * 
 */
package pacman.entries.jcgrPacMan.behaviourtreePacMan.nodes;

import pacman.entries.jcgrPacMan.behaviourtree.Context;
import pacman.entries.jcgrPacMan.behaviourtree.nodes.LeafNode;
import pacman.entries.jcgrPacMan.behaviourtreePacMan.PacManContext;
import pacman.game.Constants.GHOST;

/**
 * 
 * 
 * @author Jacob
 */
public class DodgeGhostNode extends LeafNode
{

	/**
	 * @param context
	 * @param name
	 */
	public DodgeGhostNode(PacManContext context, String name)
	{
		super(context, name);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @see pacman.entries.jcgrPacMan.behaviourtree.nodes.Node#start()
	 */
	@Override
	public void start()
	{
		// TODO Auto-generated method stub
		LogTask("Starting");
	}

	/**
	 * @return
	 * @see pacman.entries.jcgrPacMan.behaviourtree.nodes.Node#canUpdate()
	 */
	@Override
	public boolean canUpdate()
	{
		PacManContext tempContext = (PacManContext)this.context;
		return tempContext.pathToTarget.length > 0;
	}

	/**
	 * 
	 * @see pacman.entries.jcgrPacMan.behaviourtree.nodes.Node#doAction()
	 */
	@Override
	public void doAction()
	{
		PacManContext tempContext = (PacManContext)this.context;
		int[] tempPath = tempContext.pathToTarget;

		boolean ghostInTheWay = false;
		
		for(GHOST ghost : GHOST.values())
		{
			int ghostPosition = tempContext.game.getGhostCurrentNodeIndex(ghost);

			for (int pathNode : tempPath)
			{
				if (pathNode == ghostPosition)
				{
					ghostInTheWay = true;
				}
			}
		}
		
		if (ghostInTheWay) 
			this.getController().finishWithFailure();
		else 
			this.getController().finishWithSuccess();
	}

	/**
	 * 
	 * @see pacman.entries.jcgrPacMan.behaviourtree.nodes.Node#end()
	 */
	@Override
	public void end()
	{
		// TODO Auto-generated method stub
		LogTask("Ending");
	}
	
}
