package maig;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to store node information, e.g.
 * state, children, parent, accumulative reward, visited times
 * @author dariusv
 * @modified A. Hartzen
 *
 */
public class Node{
	
	public char[] state;
	public List<Node> children = new ArrayList<Node>();
	public Node parent = null;
	public int parentAction=-1;
	public float reward =0;
	public int timesvisited = 0;
	
	
	Node(char[] state){
		this.state = state;
	}
}
