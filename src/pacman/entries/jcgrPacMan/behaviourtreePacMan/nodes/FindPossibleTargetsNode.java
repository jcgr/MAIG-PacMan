/**
 * 
 */
package pacman.entries.jcgrPacMan.behaviourtreePacMan.nodes;

import pacman.entries.jcgrPacMan.behaviourtree.nodes.LeafNode;
import pacman.entries.jcgrPacMan.behaviourtreePacMan.PacManContext;

/**
 * 
 * 
 * @author Jacob
 */
public class FindPossibleTargetsNode extends LeafNode
{
	/**
	 * @param context
	 * @param name
	 */
	public FindPossibleTargetsNode(PacManContext context, String name)
	{
		super(context, name);
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
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * 
	 * @see pacman.entries.jcgrPacMan.behaviourtree.nodes.Node#doAction()
	 */
	@Override
	public void doAction()
	{
		PacManContext tempContext = ((PacManContext)context);
		
		int[] activePills = tempContext.activePills;
		int[] activePowerPills = tempContext.activePowerPills;
		
		int[] tempTargets = new int[activePills.length + activePowerPills.length];
		
		for(int i = 0; i < activePills.length; i++)
			tempTargets[i] = activePills[i];
		
		for(int i = 0; i < activePowerPills.length; i++)
			tempTargets[activePills.length + i] = activePowerPills[i];
		
		tempContext.targetNodeIndices = tempTargets;

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
