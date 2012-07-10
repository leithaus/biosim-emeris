package biosim.client.ui;

import biosim.client.messages.model.MNode;

public abstract class DropHandler {
	
	public boolean dropAllowed(MNode draggee, MNode dropTarget) { 
		return true;
	}

	public abstract void drop(MNode draggee, MNode dropTarget);

}
