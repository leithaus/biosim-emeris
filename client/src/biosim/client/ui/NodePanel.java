package biosim.client.ui;

import biosim.client.DndController;
import biosim.client.eventlist.ObservableList;
import biosim.client.messages.model.MNode;
import biosim.client.ui.dnd.DndType;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class NodePanel<T extends MNode> extends SimplePanel {

	final FlowPanel _delegatePanel = new FlowPanel();
	final WidgetBuilder<T> _widgetBuilder;
	
	public NodePanel(ObservableList<T> nodes, final DndController dndController, final DndType type) {

		_widgetBuilder = new WidgetBuilder<T>(nodes, _delegatePanel) {
			
			public Widget buildWidget(T t) {
				return new NodeWidgetBuilder(t, dndController, type).getFlowPanel();
			}
			
		};
		
		setWidget(_delegatePanel);
	}
	
	public static <T extends MNode> NodePanel<T> create(ObservableList<T> nodes, final DndController dragController, DndType type) {
		return new NodePanel<T>(nodes, dragController, type);
	}
	
}
