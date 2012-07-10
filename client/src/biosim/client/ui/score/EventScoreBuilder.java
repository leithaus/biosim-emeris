package biosim.client.ui.score;


import m3.gwt.lang.Function1;
import biosim.client.eventlist.FilteredList;
import biosim.client.eventlist.ListEvent;
import biosim.client.eventlist.ListListener;
import biosim.client.eventlist.ObservableList;
import biosim.client.eventlist.Observables;
import biosim.client.eventlist.ui.ObservableListPanelAdapter;
import biosim.client.eventlist.ui.VerticalPanelBuilder;
import biosim.client.fun.None;
import biosim.client.fun.Option;
import biosim.client.fun.Some;
import biosim.client.messages.model.MNode;
import biosim.client.messages.model.NodeContainer;
import biosim.client.ui.Filter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class EventScoreBuilder {

	final ObservableList<Filter> _filters = Observables.create();
	
	final ObservableList<ObservableList<Option<MNode>>> _model = _filters.map(new Function1<Filter, ObservableList<Option<MNode>>>() {
		@Override
		public ObservableList<Option<MNode>> apply(final Filter filter) {
			return _visibleEvents.map(new Function1<MNode, Option<MNode>>() {
				@Override
				public Option<MNode> apply(MNode node) {
					if ( filter.accept(node) ) return Some.create(node);
					else return None.apply();
				}
			});
		}
	});

	final Button _clear = new Button("Clear");

	final VerticalPanel _panel = new VerticalPanel();
	
	final FilteredList<MNode> _visibleEvents;
	
	final Widget _widget;
	
	public EventScoreBuilder() {
		_clear.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				_model.clear();
			}
		});
		_visibleEvents = NodeContainer.get().nodes.filter(new Function1<MNode, Boolean>() {
			@Override
			public Boolean apply(MNode t) {
				boolean result = false;
				for ( Filter f : _filters ) {
					if ( f.accept(t) ) {
						result = true;
						break;
					}
				}
				return result;
			}
		});
		
		_widget = ObservableListPanelAdapter.create(_model, new VerticalPanelBuilder(), new Function1<ObservableList<Option<MNode>>, Widget>() {
			@Override
			public Widget apply(ObservableList<Option<MNode>> rowModel) {
				return new EventScoreRow(rowModel).getWidget();
			}
		}).getWidget();
		_panel.add(_widget);
		
		_panel.add(_clear);
		
		_clear.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent e) {
				_filters.clear();
			}
		});
		
		NodeContainer.get().nodes.addListener(new ListListener<MNode>() {
			public void event(biosim.client.eventlist.ListEvent<MNode> event) {
				refilter();				
			}
		});
		
		_filters.addListener(new ListListener<Filter>() {
			@Override
			public void event(ListEvent<Filter> event) {
				refilter();
				for ( ObservableList<Option<MNode>> events : _model ) {
					events.reapply();
				}
			}
		});
		
	}
	
	void refilter() {
		_visibleEvents.reapply();
	}
	
	public VerticalPanel getWidget() {
		return _panel;
	}
	
	public void addStream(Filter filter) {
		_filters.add(filter);
	}
	
}
