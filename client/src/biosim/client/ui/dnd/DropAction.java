package biosim.client.ui.dnd;

import biosim.client.messages.model.MNode;

public interface DropAction<T extends MNode,U extends MNode> {

	boolean canDrop(T dragee, U dropTarget);
	
	void processDrop(T dragee, U dropTarget);
	
}
