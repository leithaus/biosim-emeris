package biosim.client.ui;


import java.util.Map;

import m3.gwt.lang.MapX;
import biosim.client.Biosim;
import biosim.client.NodeBuilder;
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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class FilterBar {
	
	final FlowPanel _panel = new FlowPanel();
	
	Map<Node,Widget> _filterNodeToWidgetMap = MapX.create();
	
	Filter _filter = new Filter();
	
	Biosim _biosim;
    Button _addContent = new Button("Add Content");
	Button _clear = new Button("Clear");
	Button _addToScore = new Button("Add to Event Score");
	
	HorizontalPanel _buttons = new HorizontalPanel();
	
	
	public FilterBar(Biosim biosim) {
		_biosim = biosim;
		_panel.setStylePrimaryName("tabFilterBar");
		_panel.addStyleName("ui-widget-content biosimbox ui-corner-all titledBorderDiv fixedHeight");
		_panel.add(new TitledBorderDiv("Filters"));
		_panel.add(new ClearBothDiv());
		_panel.getElement().getStyle().setPosition(Position.RELATIVE);
		
		_buttons.addStyleName("filter-bar");
		
		initializeButton(_addContent, new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                addContent(event);
            }
        });
		initializeButton(_clear, new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				clearFilters();
			}
		});
		initializeButton(_addToScore, new ClickHandler() {
			@Override
			public void onClick(ClickEvent e) {
				_biosim.getEventScore().addStream(_filter);
			}
		});
		
//		$(clear.getElement()).hover(new Function() {
//			@Override
//			public void f(Element e) {
//				clear.setStyleName("ui-corner-all ui-widget-content ui-state-default", false);
//				clear.setStyleName("ui-corner-all ui-widget-content ui-state-active", true);
//			}
//			@Override
//			public boolean f(Event e) {
//				clear.setStyleName("ui-corner-all ui-widget-content ui-state-default", false);
//				clear.setStyleName("ui-corner-all ui-widget-content ui-state-active", true);
//				return false;
//			}
//		}, new Function(){
//			@Override
//			public boolean f(Event e) {
//				clear.setStyleName("ui-corner-all ui-widget-content ui-state-active", false);
//				clear.setStyleName("ui-corner-all ui-widget-content ui-state-default", true);
//				return false;
//			}
//		});
		
		_buttons.getElement().getStyle().setDisplay(Display.NONE);
		_panel.add(_buttons);		
	}
	
	void initializeButton(Button b, ClickHandler c) {
		b.setStylePrimaryName("filter-bar-button");
		b.setStyleName("ui-button ui-corner-all ui-widget-content ui-state-default", true);
		b.addClickHandler(c);
				
		_buttons.add(b);
	}

    void addContent(ClickEvent event) {
        NodeBuilder nodeBuilder = new NodeBuilder(_filter._labels, _filter._people);
	    nodeBuilder.showPopupMenu(event);
	}

	public void clearFilters() {
		for ( Widget w : _filterNodeToWidgetMap.values() ) {
			_panel.remove(w);
		}
		_filterNodeToWidgetMap.clear();
		_filter = new Filter();
		_buttons.getElement().getStyle().setDisplay(Display.NONE);
		refresh();
	}

	private void refresh() {
		Biosim.get().getDatabaseAccessLayer().fireRefreshContentPane();
	}
	
	public void addToFilter(Node node) {
		if ( _filter.canAddFilter(node) ) {
			_filter = _filter.add(node);
			
			for ( Node n : _filter._nodes ) {
				Widget w = _filterNodeToWidgetMap.get(n);
				if ( w == null && _filter.isVisible(n) ) {
					w = createFilterWidget(node.getVisualId());
					_filterNodeToWidgetMap.put(n, w);
					_panel.add(w);
				}
			}
			_buttons.getElement().getStyle().clearDisplay();
			refresh();
		}
	}
	
	public static Widget createFilterWidget(String text) {
		FocusPanel p = new FocusPanel();
		Label l = new Label(text);
		p.add(l);
		p.setStyleName("tabFilterBar-filter ui-widget-content ui-corner-all");
		return p;
	}
	
	public Filter getFilter() {
		return _filter;
	}

	public FlowPanel getPanel() {
		return _panel;
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
