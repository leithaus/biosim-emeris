package biosim.client.ui;

import java.util.Map;

import m3.gwt.lang.MapX;
import biosim.client.Biosim;
import biosim.client.model.Node;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class NodeDropWidgetBuilder {

	final FlowPanel _panel = new FlowPanel();
	
	Map<Node,Widget> _nodes = MapX.create();
	Biosim _biosim;
	Button _clear;
	
	public NodeDropWidgetBuilder(String title, Biosim biosim) {
		_biosim = biosim;
		_panel.setStylePrimaryName("tabFilterBar");
		_panel.addStyleName("ui-widget-content biosimbox ui-corner-all titledBorderDiv fixedHeight");
		_panel.add(new TitledBorderDiv(title));
		_panel.add(new ClearBothDiv());
		_panel.getElement().getStyle().setPosition(Position.RELATIVE);
		
		_clear = new Button("Clear");
		_clear.setStylePrimaryName("ui-button");
		_clear.setStyleName("ui-corner-all ui-widget-content ui-state-default", true);
		Style style = _clear.getElement().getStyle();
		style.setPosition(Position.ABSOLUTE);
		style.setBottom(.3, Unit.EM);
		style.setRight(.3, Unit.EM);
		style.setPadding(.5, Unit.EM);
		style.setDisplay(Display.NONE);
		
		_panel.add(_clear);
		_clear.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				clear();
			}
		});
		
	}
	
	public void add(Node node) {
		if ( !_nodes.containsKey(node) ) {
			Widget w = createFilterWidget(node.getVisualId());
			_panel.add(w);
			_nodes.put(node, w);
			_clear.getElement().getStyle().clearDisplay();
		}
	}

	public static Widget createFilterWidget(String text) {
		FocusPanel p = new FocusPanel();
		Label l = new Label(text);
		p.add(l);
		p.setStyleName("tabFilterBar-filter ui-widget-content ui-corner-all");
		return p;
	}

	public FlowPanel getPanel() {
		return _panel;
	}
	
	public void clear() {
		for ( Widget widget : _nodes.values() ) {
			_panel.remove(widget);
		}
	}
	
	class TitledBorderDiv extends FlowPanel {
		public TitledBorderDiv(String text) {
			setStylePrimaryName("titledBorderDivTitle");
			Label label = new Label(text);
			Style style = label.getElement().getStyle();
			style.setFontSize(14, Unit.PX);
			style.setFontWeight(FontWeight.BOLDER);
			style.setProperty("textAlign", "left");
			add(label);
		}
	}
	
	class ClearBothDiv extends FlowPanel {
		public ClearBothDiv() {
			setStylePrimaryName("clearBoth");
		}
	}
	
}
