package biosim.client.eventlist.ui;

import com.google.gwt.user.client.ui.Widget;

public interface ListPanelBuilder {

	Widget getWidget();
	void insert(int index, Widget widget);
	void remove(int index, Widget widget);
	
}
