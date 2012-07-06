package biosim.client;

import static com.google.gwt.query.client.GQuery.$;
import m3.gwt.lang.Function1;
import biosim.client.eventlist.ObservableList;
import biosim.client.eventlist.ui.FlowPanelBuilder;
import biosim.client.eventlist.ui.ObservableListPanelAdapter;
import biosim.client.model.Node;
import biosim.client.ui.NodeWidgetBuilder;
import biosim.client.ui.dnd.DndType;
import biosim.client.utils.GqueryUtils;

import com.google.gwt.query.client.Function;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

public class ContentControllerNew {
	
	Biosim _biosim;
	ObservableListPanelAdapter<NodeWidgetBuilder> _listPanelAdapter;
	
	Function _nullCompletionCallback = new Function() {
		@Override
		public void f() {
		}
	};

	ContentControllerNew(Biosim biosim, ObservableList<Node> contentList, final DndController dndController) {
		_biosim = biosim;
		
		ObservableList<NodeWidgetBuilder> nwbList = contentList.map(new Function1<Node, NodeWidgetBuilder>() {
			@Override
			public NodeWidgetBuilder apply(Node node) {
				return new NodeWidgetBuilder(node, dndController, DndType.Content);
			}
		});
		
		_listPanelAdapter = new ObservableListPanelAdapter<NodeWidgetBuilder>(nwbList, new FlowPanelBuilder(), new Function1<NodeWidgetBuilder, Widget>() {
			@Override
			public Widget apply(NodeWidgetBuilder nwb) {
				return nwb.getPanel();
			}
		});
	}
	
	void show(Widget widget, Function completionCallback) {
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

	public Widget getWidget() {
		return _listPanelAdapter.getWidget();
	}

}
