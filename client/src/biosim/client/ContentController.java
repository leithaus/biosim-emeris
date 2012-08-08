package biosim.client;

import static com.google.gwt.query.client.GQuery.$;

import java.util.Map;

import m3.gwt.lang.Function0;
import m3.gwt.lang.MapX;
import m3.gwt.lang.Pair;
import biosim.client.eventlist.FineGrainedListListener;
import biosim.client.eventlist.ListEvent;
import biosim.client.eventlist.ObservableList;
import biosim.client.eventlist.Observables;
import biosim.client.messages.model.FilterAcceptCriteria;
import biosim.client.messages.model.LocalAgent;
import biosim.client.messages.model.MNode;
import biosim.client.ui.NodeWidgetBuilder;
import biosim.client.ui.dnd.DndType;
import biosim.client.utils.GqueryUtils;

import com.google.gwt.query.client.Function;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class ContentController {
	
	private Map<MNode,NodeWidgetBuilder> _nodeToWidgetBuilderMap = MapX.create();
	FlowPanel _contentPanel = new FlowPanel();
	ObservableList<Pair<FilterAcceptCriteria,MNode>> _contentList = Observables.create();
	
	Function _nullCompletionCallback = new Function() {
		@Override
		public void f() {
		}
	};

	ContentController(final DndController dndController, final Function0<LocalAgent> localAgentGetter) {
		_contentList.addListener(new FineGrainedListListener<Pair<FilterAcceptCriteria,MNode>>() {
			@Override
			public void added(ListEvent<Pair<FilterAcceptCriteria,MNode>> event) {
				MNode node = event.getElement().getRight();
				NodeWidgetBuilder nwb = new NodeWidgetBuilder(node, dndController, DndType.Content, localAgentGetter);
				nwb.setFilterAcceptCriteria(event.getElement().getLeft());
				_contentPanel.add(nwb.getPanel());
				_nodeToWidgetBuilderMap.put(node, nwb);
			}
			@Override
			public void changed(ListEvent<Pair<FilterAcceptCriteria, MNode>> event) {
				MNode node = event.getElement().getRight();
				NodeWidgetBuilder nwb = new NodeWidgetBuilder(node, dndController, DndType.Content, localAgentGetter);
				nwb.setFilterAcceptCriteria(event.getElement().getLeft());
			}
			@Override
			public void removed(ListEvent<Pair<FilterAcceptCriteria,MNode>> event) {
				MNode node = event.getElement().getRight();
				final NodeWidgetBuilder nwb = _nodeToWidgetBuilderMap.get(node);
				hide(nwb, new Function() {
					public void f() {
						_contentPanel.remove(nwb.getPanel());							
					}
				});
				_nodeToWidgetBuilderMap.remove(node);
			}
		});
	}
	
	void show(NodeWidgetBuilder nwb, Function completionCallback) {
		Widget widget = nwb.getPanel();
		if ( completionCallback == null ) {
			completionCallback = _nullCompletionCallback;
		}
		final Function callback = completionCallback;
		$(widget).slideDown(500, new Function() {
			@Override
			public void f(Element e) {
				$(e).children(".vertCenter").each(new Function() {
					@Override
					public void f(Element e) {
						GqueryUtils.center(e);
					}
				}).each(callback);
			}
		});
	}
	
	public ObservableList<Pair<FilterAcceptCriteria,MNode>> getContent() {
		return _contentList;
	}

	void hide(NodeWidgetBuilder nwb, Function completionCallback) {
		nwb.setFilterAcceptCriteria(null);
		Widget widget = nwb.getPanel();
		if ( completionCallback == null ) {
			completionCallback = _nullCompletionCallback;
		}
		$(widget).slideUp(500, completionCallback);
	}

	public NodeWidgetBuilder builder(MNode node) {
		return _nodeToWidgetBuilderMap.get(node);
	}
	
}
