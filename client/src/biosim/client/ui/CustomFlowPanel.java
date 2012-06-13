package biosim.client.ui;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class CustomFlowPanel extends FlowPanel {
	
	private SimplePanel _clearPanel = new SimplePanel();
	private double _marginLeft = 10D;
	private Unit _units = Unit.PX;
	
	public CustomFlowPanel() {
		init();
	}
	
	public CustomFlowPanel(double marginLeft, Unit units) {
		setMarginLeft(marginLeft, units);
		init();
	}
	
	public SimplePanel getClearPanel() {
		return _clearPanel;
	}
	
	public void setMarginLeft(double marginLeft, Unit units) {
		_marginLeft = marginLeft;
		_units = units;
	}
	
	private void init() {
		_clearPanel.addStyleName("clearBoth");
		add(_clearPanel);
	}
	
	@Override
	public void add(Widget w) {
		int cnt = getWidgetCount();
		if(cnt == 0) {
			super.add(w);
		} else if (cnt == 1){
			w.addStyleName("fleft vertCenter");
			insert(w, getWidgetCount() - 1);
		} else {
			w.addStyleName("fleft vertCenter");
			w.getElement().getStyle().setMarginLeft(_marginLeft, _units);
			insert(w, getWidgetCount() - 1);
		}
	}
	
	@Override
	public void add(IsWidget child) {
		add(child.asWidget());
	}
	
	public void add(Widget w, String styleNames) {
		int cnt = getWidgetCount();
		if(cnt == 0) {
			super.add(w);
		} else if (cnt == 1){
			w.addStyleName(styleNames);
			insert(w, getWidgetCount() - 1);
		} else {
			w.addStyleName(styleNames);
			w.getElement().getStyle().setMarginLeft(_marginLeft, _units);
			insert(w, getWidgetCount() - 1);
		}
	}
	
	public void add(IsWidget child, String styleNames) {
		add(child.asWidget(), styleNames);
	}
}
