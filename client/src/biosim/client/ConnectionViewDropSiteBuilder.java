package biosim.client;

import biosim.client.ui.dnd.DndType;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;


public class ConnectionViewDropSiteBuilder {

	DndController _dndController;
	Widget _widget = new SimplePanel();
	
	public ConnectionViewDropSiteBuilder(DndController dndController) {
		_dndController = dndController;
		_widget.setSize("100px", "100px");
		_dndController.registerDropSite(DndType.ViewConnection, null, _widget);
	}

	public Widget getWidget() {
		return _widget;
	}
	
}
