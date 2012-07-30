package biosim.client.ui;


import java.util.List;
import java.util.Map;
import java.util.Set;

import m3.gwt.lang.Function2;
import m3.gwt.lang.ListX;
import m3.gwt.lang.MapX;
import m3.gwt.lang.Pair;
import m3.gwt.lang.SetX;
import biosim.client.AsyncCallback;
import biosim.client.NodeBuilder;
import biosim.client.eventlist.ObservableList;
import biosim.client.messages.model.AgentServices;
import biosim.client.messages.model.FilterAcceptCriteria;
import biosim.client.messages.model.LocalAgent;
import biosim.client.messages.model.MAgent;
import biosim.client.messages.model.MNode;
import biosim.client.messages.model.Uid;

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
	
	Map<MNode,Widget> _filterNodeToWidgetMap = MapX.create();
	
	Filter _filter = new Filter();
	
    Button _addContent = new Button("Add Content");
	Button _clear = new Button("Clear");
	Button _addToScore = new Button("Add to Event Score");
	
	HorizontalPanel _buttons = new HorizontalPanel();
	
	LocalAgent _localAgent;
	
	Uid _mostRecentRequestUid;
	
	final Callback _callback;
	
	public FilterBar(Callback callback) {
		
		_callback = callback;
		
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
				_callback.addToEventScore(_filter);
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
        NodeBuilder nodeBuilder = new NodeBuilder(_filter.getLabels(), _filter.getConnections());
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
	
	public void addToFilter(MNode node) {
		if ( _filter.canAddFilter(node) ) {
			_filter = _filter.add(node);
			
			for ( MNode n : _filter._nodes ) {
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
	
	public void refresh() {
		_mostRecentRequestUid = Uid.random();

		if ( _filter._nodes.isEmpty() ) {
			_callback.getContentList().clear();
		} else {
			
			final AgentServices agentServices = _filter._nodes.head().getAgentServices();
			
			agentServices.query(_filter._nodes, _mostRecentRequestUid, new Function2<Uid, Iterable<FilterAcceptCriteria>, Void>() {
				@Override
				public Void apply(Uid requestUid, final Iterable<FilterAcceptCriteria> facIter) {
					
					// make sure this is still the most recent request
					if ( !requestUid.equals(_mostRecentRequestUid) ) {
						return null;
					}
					
					List<Uid> uids = ListX.create();
					for ( FilterAcceptCriteria fac : facIter ) {
						uids.add(fac.getNode());
					}
					
					agentServices.fetch(uids, false, new AsyncCallback<Iterable<MNode>>() {
						@Override
						public Void apply(Iterable<MNode> newContent) {
							
							Map<Uid,MNode> nodesByUid = MapX.create();
							for ( MNode n : newContent ) {
								if (!(n instanceof MAgent)) {
									nodesByUid.put(n.getUid(), n);
								}
							}
							
							ObservableList<Pair<FilterAcceptCriteria,MNode>> content = _callback.getContentList();
							Set<Pair<FilterAcceptCriteria,MNode>> removeUs = SetX.create();
							
							for ( FilterAcceptCriteria fac : facIter ) {
								MNode node = nodesByUid.get(fac.getNode());
								if (node == null) continue;
								Pair<FilterAcceptCriteria, MNode> pair = Pair.create(fac,node);
								int index = -1;
								for ( int i = 0 ; i < content.size() ; i++ ) {
									if ( content.get(0).getRight().equals(node) ) {
										index = i;
										removeUs.remove(i);
										break;
									}
								}
								if ( index == -1 ) {
									content.add(pair);
								} else {
									content.set(index, pair);
								}
								
								content.removeAll(removeUs);
								
							}
							
							return null;
						}
					});
	
					return null;
				}
			});
		}
	}
	
	public void setLocalAgent(LocalAgent localAgent) {
		_localAgent = localAgent;
	}
	
	public static interface Callback {
		void addToEventScore(Filter filter);
		ObservableList<Pair<FilterAcceptCriteria,MNode>> getContentList();
	}

}
