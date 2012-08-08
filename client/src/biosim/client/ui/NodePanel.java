package biosim.client.ui;

import m3.gwt.lang.Function0;
import biosim.client.DndController;
import biosim.client.eventlist.ObservableList;
import biosim.client.messages.model.LocalAgent;
import biosim.client.messages.model.MNode;
import biosim.client.ui.dnd.DndType;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class NodePanel<T extends MNode> extends SimplePanel {

	final FlowPanel _delegatePanel = new FlowPanel();
	final WidgetBuilder<T> _widgetBuilder;
	
	public NodePanel(ObservableList<T> nodes, final DndController dndController, final DndType type, final Function0<LocalAgent> localAgentGetter) {

		_widgetBuilder = new WidgetBuilder<T>(nodes, _delegatePanel) {
			
			public Widget buildWidget(T t) {
				return new NodeWidgetBuilder(t, dndController, type, localAgentGetter).getFlowPanel();
			}
			
		};
		
		setWidget(_delegatePanel);
	}
	
	public static <T extends MNode> NodePanel<T> create(ObservableList<T> nodes, final DndController dragController, DndType type, Function0<LocalAgent> localAgentGetter) {
		return new NodePanel<T>(nodes, dragController, type, localAgentGetter);
	}
	
}
