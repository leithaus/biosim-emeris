package biosim.client;

import static com.google.gwt.query.client.GQuery.$;

import java.util.Map;

import m3.gwt.lang.MapX;
import biosim.client.eventlist.FineGrainedListListener;
import biosim.client.eventlist.ListEvent;
import biosim.client.eventlist.ListListener;
import biosim.client.eventlist.ObservableList;
import biosim.client.eventlist.Observables;
import biosim.client.messages.model.MNode;
import biosim.client.messages.model.NodeContainer;
import biosim.client.ui.ContentCriteria;
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
	ObservableList<MNode> _contentList;
	Biosim _biosim;
	
	Function _nullCompletionCallback = new Function() {
		@Override
		public void f() {
		}
	};

	ListListener<MNode> _listener = new FineGrainedListListener<MNode>() {
		@Override
		public void added(ListEvent<MNode> event) {
			NodeWidgetBuilder nwb = _nodeToWidgetBuilderMap.get(event.getElement());
			if ( !nwb.isVisible() ) {
				show(nwb, null);
			}
		}
		public void removed(ListEvent<MNode> event) {
			NodeWidgetBuilder nwb = _nodeToWidgetBuilderMap.get(event.getElement());
			if ( nwb.isVisible() ) {
				hide(nwb, null);
			}
		}
	};

	ContentController(Biosim biosim, ObservableList<MNode> allNodes, final DndController dndController) {
		_biosim = biosim;
		allNodes.addListener(new FineGrainedListListener<MNode>() {
			@Override
			public void added(ListEvent<MNode> event) {
				NodeWidgetBuilder nwb = new NodeWidgetBuilder(event.getElement(), dndController, DndType.Content);
				_contentPanel.add(nwb.getPanel());
				nwb.getPanel().setVisible(false);
				_nodeToWidgetBuilderMap.put(event.getElement(), nwb);
			}
			public void changed(biosim.client.eventlist.ListEvent<MNode> event) {
				NodeWidgetBuilder nwb = _nodeToWidgetBuilderMap.get(event.getElement());
				nwb.rebuild();
			}
			@Override
			public void removed(ListEvent<MNode> event) {
				final NodeWidgetBuilder nwb = _nodeToWidgetBuilderMap.get(event.getElement());
				hide(nwb, new Function() {
					public void f() {
						_contentPanel.remove(nwb.getPanel());							
					}
				});
			}
		});
		_contentList = allNodes;
	}
	
	public void setContentList(ObservableList<MNode> newList) {
		ObservableList<MNode> oldList = _contentList;
		
		oldList.removeListener(_listener);
		
		Observables.diff(oldList, newList, new Observables.DiffHandler<MNode>() {
			@Override
			public void added(MNode t) {
				show(_nodeToWidgetBuilderMap.get(t), null);
			}
			@Override
			public void removed(MNode t) {
				hide(_nodeToWidgetBuilderMap.get(t), null);
			}
		});
		
		_contentList = newList;
		_contentList.addListener(_listener);
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

	public void refilterContent() {
		final ObservableList<MNode> content = Observables.create();
		for ( MNode node : NodeContainer.get().nodes ) {
			ContentCriteria accept = _biosim._filtersBar.getFilter().acceptVerbose(node);
			NodeWidgetBuilder builder = builder(node);
			if ( accept != null && builder != null ) {
				content.add(node);
				builder.setFilterAcceptCriteria(accept);
			}			
		}
		setContentList(content);
	}

}
