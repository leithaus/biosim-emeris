package biosim.client;

import biosim.client.messages.model.LocalAgent;
import biosim.client.messages.model.MNode;
import biosim.client.ui.FilterBar;
import biosim.client.ui.dnd.DndType;

import com.google.gwt.user.client.ui.Widget;

public interface DndController {

	void makeDraggable(
			DndType dndType
			, MNode node
			, Widget draggee
			, Widget dragHandle
	);

	void registerDropSite(
			DndType dndType
			, MNode node
			, Widget widget
	);

	public static interface Callback {
		FilterBar getFilterBar();
		void removeContentLinks(MNode node);
		ContentController getContentController();
		LocalAgent getLocalAgent();
		LabelTreeBuilder getLabelTreeBuilder();
	}	
}
