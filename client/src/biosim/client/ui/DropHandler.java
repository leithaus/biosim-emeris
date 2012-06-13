package biosim.client.ui;

import biosim.client.model.Node;

public abstract class DropHandler {
	
	public boolean dropAllowed(Node draggee, Node dropTarget) { 
		return true;
	}

	public abstract void drop(Node draggee, Node dropTarget);

}
