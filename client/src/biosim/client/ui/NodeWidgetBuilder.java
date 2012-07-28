package biosim.client.ui;

import java.util.List;

import m3.gwt.lang.ListX;
import biosim.client.DndController;
import biosim.client.Globals;
import biosim.client.messages.model.FilterAcceptCriteria;
import biosim.client.messages.model.MImage;
import biosim.client.messages.model.MNode;
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
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class NodeWidgetBuilder {

	CustomFlowPanel _widget = new CustomFlowPanel();
	SimplePanel _wrapper = new SimplePanel();
	
	final MNode _node;
	final DndType _dndType;
	final DndController _dndController;
	
	Image _icon;
	Widget _content;
	CustomFlowPanel _sourceLabels = new CustomFlowPanel();
	List<Widget> _sourceLabelWidgets = ListX.create();
	FilterAcceptCriteria _filterAcceptCriteria;

	public NodeWidgetBuilder(MNode node, DndController dndController, DndType dndType) {
		_node = node;
		_dndType = dndType;
		_dndController = dndController;
		rebuild();
//		_dndController.makeDraggable(_dndType, _node, _wrapper, _wrapper);
	}
	
	public void rebuild() {
		
		_widget = new CustomFlowPanel();
		_widget.setStyleName(Globals._boxStyle, true);
		
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
		_dndController.makeDraggable(_dndType, _node, _widget, dragTexture);

		if ( _node.getIconUrl() != null ) {
			_icon = new Image(_node.getIconUrl());
			_icon.addStyleName("node-icon");
			_widget.add(_icon);
//			_dndController.makeDraggable(_dndType, _node, _widget, _icon);
		}
		
		if ( _node instanceof MImage ) {
			
			// Create a FlexTable into which the image will be displayed
			HorizontalPanel p = new HorizontalPanel();
			p.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			
			// Create the image element
			final String imageURL = ((MImage)_node).getBlobRef().getUrl();
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
		} else if ( _node.toHtmlString() != null ) {
			_content = new HTML(_node.toHtmlString());
			_widget.setHeight("70px");
		}

		if ( _content != null ) {
			_widget.add(_content);
//			_dndController.makeDraggable(_dndType, _node, _widget, _content);
		}
		
//		dndController.makeDraggable(_dndType, _node, _widget, _widget.getClearPanel());
//		_dndController.makeDraggable(_dndType, _node, _widget, _widget);
		
		_dndController.registerDropSite(_dndType, _node, _widget);
		
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
		
		_wrapper.setWidget(_widget);
		
	}
	
	public void setFilterAcceptCriteria(FilterAcceptCriteria filterAcceptCriteria) {
		_filterAcceptCriteria = filterAcceptCriteria;
		for ( Widget w : _sourceLabelWidgets ) {
			_sourceLabels.remove(w);
		}
		_sourceLabelWidgets.clear();
		if ( _filterAcceptCriteria != null ) {
			for ( String p : _filterAcceptCriteria.getPaths() ) {
				Widget w = FilterBar.createFilterWidget(p);
				_sourceLabelWidgets.add(w);
				_sourceLabels.add(w);
			}
		}
	}
	public FilterAcceptCriteria getFilterAcceptCriteria() {
		return _filterAcceptCriteria;
	}
	
	public MNode getNode() {
		return _node;
	}
	
	public Image getImage() {
		return _icon;
	}
	
	public Widget getPanel() {
		return _wrapper;
	}
	
	public FlowPanel getFlowPanel() {
		return _widget;
	}

	public boolean isVisible() {
		return _widget.isVisible();
	}
	
}
