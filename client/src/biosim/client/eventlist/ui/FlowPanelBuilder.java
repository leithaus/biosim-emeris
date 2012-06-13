package biosim.client.eventlist.ui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class FlowPanelBuilder implements ListPanelBuilder {

	FlowPanel _panel = new FlowPanel();
	
	public FlowPanelBuilder() {
	}

	@Override
	public Widget getWidget() {
		return _panel;
	}
	
	@Override
	public void insert(int index, Widget widget) {
		_panel.insert(widget, index);
	}

	public Element createChildDiv() {
		return DOM.createDiv();
	}
	
	@Override
	public void remove(int index, Widget widget) {
		_panel.remove(index);
	}
	
}
