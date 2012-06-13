package biosim.client.eventlist.ui;

import com.google.gwt.dom.client.Node;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

public class DivListPanelBuilder extends Widget implements ListPanelBuilder {

	public DivListPanelBuilder() {
		setElement(DOM.createDiv());
		getElement().getStyle().setFloat(com.google.gwt.dom.client.Style.Float.NONE);
	}

	@Override
	public Widget getWidget() {
		return this;
	}
	
	@Override
	public void insert(int index, Widget widget) {
		Element childDiv = createChildDiv();
		int childCount = getElement().getChildCount();
		if ( childCount == 0 ) {
			getElement().insertFirst(childDiv);
		} else if ( index < childCount ) {
			Node beforeNode = getElement().getChild(index);
			getElement().insertBefore(childDiv, beforeNode);
		} else {
			Node afterNode = getElement().getChild(childCount-1);
			getElement().insertAfter(childDiv, afterNode);
		}
		childDiv.insertFirst(widget.getElement());
	}

	public Element createChildDiv() {
		return DOM.createDiv();
	}
	
	@Override
	public void remove(int index, Widget widget) {
		final Node removeMe = getElement().getChild(index);
		getElement().removeChild(removeMe);				
	}
	
}
