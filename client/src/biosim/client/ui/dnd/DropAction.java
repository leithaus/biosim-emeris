package biosim.client.ui.dnd;

import biosim.client.model.Node;

public interface DropAction<T extends Node,U extends Node> {

	boolean canDrop(T dragee, U dropTarget);
	
	void processDrop(T dragee, U dropTarget);
	
}
