package biosim.client.eventlist.ui;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class HorizontalPanelBuilder implements ListPanelBuilder {

	HorizontalPanel _panel = new HorizontalPanel();
	
	public HorizontalPanelBuilder() {
	}

	@Override
	public Widget getWidget() {
		return _panel;
	}
	
	@Override
	public void insert(int index, Widget widget) {
		_panel.insert(widget, index);
	}
	
	@Override
	public void remove(int index, Widget widget) {
		_panel.remove(index);
	}
	
}
