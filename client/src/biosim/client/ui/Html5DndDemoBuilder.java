package biosim.client.ui;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.DragLeaveEvent;
import com.google.gwt.event.dom.client.DragLeaveHandler;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DragOverHandler;
import com.google.gwt.event.dom.client.DragStartEvent;
import com.google.gwt.event.dom.client.DragStartHandler;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.dom.client.DropHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Html5DndDemoBuilder {

	public static Widget create() {
		
		final Label draggable = new Label("hello");
		draggable.getElement().setDraggable(Element.DRAGGABLE_TRUE);

		draggable.addDragStartHandler(new DragStartHandler() {
			@Override
			public void onDragStart(DragStartEvent event) {
				
				event.setData("text", "hello world");
				
				int x = event.getNativeEvent().getClientX();
				int y = event.getNativeEvent().getClientY();
				
				int dx = x - draggable.getAbsoluteLeft();
				int dy = y - draggable.getAbsoluteTop();
				
				event.getDataTransfer().setDragImage(draggable.getElement(), dx, dy);
				
			}
		});
		
		final Label target = new Label("world");
		
		target.addDragOverHandler(new DragOverHandler() {
			@Override
			public void onDragOver(DragOverEvent event) {
				target.setText("WORLD");
			}
		});
		target.addDragLeaveHandler(new DragLeaveHandler() {
			@Override
			public void onDragLeave(DragLeaveEvent event) {
				target.setText("world");
			}
		});
		
		target.addDropHandler(new DropHandler() {
			@Override
			public void onDrop(DropEvent event) {
				event.preventDefault();
				String data = event.getData("text");
				target.setText(data);
			}
		});
		
		VerticalPanel vp = new VerticalPanel();
		
		vp.add(draggable);
		vp.add(target);
		
		return vp;
		
	}
	
}
