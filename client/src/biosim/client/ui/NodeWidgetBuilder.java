package biosim.client.ui;

import java.util.List;

import m3.gwt.lang.ListX;
import biosim.client.Biosim;
import biosim.client.DndController;
import biosim.client.model.Node;
import biosim.client.ui.dnd.DndType;
import biosim.client.utils.GqueryUtils;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class NodeWidgetBuilder {

	CustomFlowPanel _widget = new CustomFlowPanel();
	Node _node;
	Image _icon;
	Widget _content;
	CustomFlowPanel _sourceLabels = new CustomFlowPanel();
	List<Widget> _sourceLabelWidgets = ListX.create();
	DndType _dndType;
	ContentCriteria _filterAcceptCriteria;

	public NodeWidgetBuilder(Node node, DndController dndController, DndType dndType) {
		_dndType = dndType;
		_node = node;
		
		_widget.setStyleName(Biosim._boxStyle, true);
		
		HTMLPanel dragHandle = new HTMLPanel("div", "");
//		Style style = dragHandle.getElement().getStyle();
//		style.setBackgroundImage("url('images/drag_background_wide.png')");
//		style.setPadding(10, Unit.PX);
		Image dragTexture = new Image("images/drag_background_wide.png");
		dragTexture.setWidth("20px");
		dragTexture.setHeight("100%");
		dragTexture.addStyleName("ui-corner-tl ui-corner-bl");
		Style textureStyle = dragTexture.getElement().getStyle();
		textureStyle.setMargin(0, Unit.PX);
//		textureStyle.setMarginBottom(-3, Unit.PX);
		dragHandle.add(dragTexture);
		_widget.add(dragHandle, "vertFill fleft ui-corner-tl ui-corner-bl");
		dndController.makeDraggable(_dndType, _node, _widget, dragTexture);

		if ( _node.getIconUrl() != null ) {
			_icon = new Image(_node.getIconUrl());
			_icon.addStyleName("node-icon");
			_widget.add(_icon);
			dndController.makeDraggable(_dndType, _node, _widget, _icon);
		}
		
		if ( node instanceof biosim.client.model.Image ) {
			
			// Create a FlexTable into which the image will be displayed
			HorizontalPanel p = new HorizontalPanel();
			p.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			
			// Create the image element
			final String imageURL = ((biosim.client.model.Image)node).getBlobRef().getUrl();
			Image i = new Image(imageURL);
			if (i.getHeight() > i.getWidth()) {
				i.setHeight("150px");
			} else {
				i.setWidth("150px");
			}
			p.add(i);
			
			// Create a button to display a full sized version of the image
			Button b = new Button("View...", new ClickHandler() {
			      public void onClick(ClickEvent event) {
			          Window.open(imageURL, "imageURL", "");
			        }
			});
			b.setTitle("View full sized image in a new window");
			p.add(b);
			
			_content = p;
			_widget.setHeight("175px");
		} else if ( node.toHtmlString() != null ) {
			_content = new HTML(node.toHtmlString());
			_widget.setHeight("70px");
		}

		if ( _content != null ) {
			_widget.add(_content);
			dndController.makeDraggable(_dndType, _node, _widget, _content);
		}
		
//		dndController.makeDraggable(_dndType, _node, _widget, _widget.getClearPanel());
		dndController.makeDraggable(_dndType, _node, _widget, _widget);
		
		dndController.registerDropSite(_dndType, _node, _widget);
		
		_widget.add(_sourceLabels);
		
		if ( _content != null ) {
			Scheduler.get().scheduleFinally(new ScheduledCommand() {
		        @Override
		        public void execute() {
	//	        	if(_icon != null) {
	//	        		GqueryUtils.center(_icon.getElement());
	//	        	}
		        	GqueryUtils.center(_content.getElement());
		        }
		    });
		} else {
			toString();
		}
		
	}
	
	public void setFilterAcceptCriteria(ContentCriteria filterAcceptCriteria) {
		_filterAcceptCriteria = filterAcceptCriteria;
		for ( Widget w : _sourceLabelWidgets ) {
			_sourceLabels.remove(w);
		}
		_sourceLabelWidgets.clear();
		if ( _filterAcceptCriteria != null ) {
			for ( String p : _filterAcceptCriteria.paths ) {
				Widget w = FilterBar.createFilterWidget(p);
				_sourceLabelWidgets.add(w);
				_sourceLabels.add(w);
			}
		}
	}
	public ContentCriteria getFilterAcceptCriteria() {
		return _filterAcceptCriteria;
	}
	
	public Node getNode() {
		return _node;
	}
	
	public Image getImage() {
		return _icon;
	}
	
	public Widget getContent() {
		return _content;
	}
	
	public FlowPanel getWidget() {
		return _widget;
	}

	public boolean isVisible() {
		return _widget.isVisible();
	}
	
}
