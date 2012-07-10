package biosim.client.ui.score;

import java.util.Map;

import m3.gwt.lang.Function1;
import m3.gwt.lang.MapX;
import biosim.client.eventlist.ObservableList;
import biosim.client.eventlist.ui.HorizontalPanelBuilder;
import biosim.client.eventlist.ui.ObservableListPanelAdapter;
import biosim.client.fun.Option;
import biosim.client.messages.model.HasBlob;
import biosim.client.messages.model.MNode;
import biosim.client.ui.ToolTipListener;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class EventScoreRow {

	final Function1<Widget,Widget> _tooltipGenerator = new Function1<Widget, Widget>() {
		public Widget apply(Widget t) {
			if ( _widgetToNodeMap.containsKey(t) ) {
				return createToolTipWidget(_widgetToNodeMap.get(t));
			} else {
				return null;
			}
		}
	};
	
	final Map<Widget,MNode> _widgetToNodeMap = MapX.create();
	
	final ToolTipListener _toolTipListener = new ToolTipListener(_tooltipGenerator);

	ObservableList<Option<MNode>> _model;
	
	Widget _widget;
	
	public EventScoreRow(ObservableList<Option<MNode>> model) {
		_model = model;
		
		_widget = ObservableListPanelAdapter.create(_model, new HorizontalPanelBuilder(), new Function1<Option<MNode>, Widget>() {
			@Override
			public Widget apply(Option<MNode> t) {
				return createCellWidget(t);
			}
		}).getWidget();
//			public Element createChildDiv() {
//				Element div = super.createChildDiv();
//				Style style = div.getStyle();
//				style.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
//				style.setBorderColor("black");
//				style.setBorderStyle(BorderStyle.SOLID);
//				style.setBorderWidth(1, Unit.PX);
//				return div;
//			}
	}
	
	public Widget createCellWidget(Option<MNode> o) {
		Widget cellWidget;
		if ( o.isDefined() ) {
			MNode node = o.get();
			if ( node instanceof HasBlob ) {
				cellWidget = new Image(((HasBlob)node).getBlobRef().getUrl());
			} else {
				cellWidget = new Image(node.getIconUrl());
			}
			_toolTipListener.attach(cellWidget);
			_widgetToNodeMap.put(cellWidget, node);
		} else {
			cellWidget = new Label();
		}
		cellWidget.setSize("75px", "75px");
		return cellWidget;
	}

	public Widget createToolTipWidget(MNode node) {
		Widget tooltipWidget;
		if ( node instanceof HasBlob ) {
			tooltipWidget = new Image(((HasBlob)node).getBlobRef().getUrl());
			tooltipWidget.setSize("400px", "400px");
		} else {
			tooltipWidget = new HTML(node.toHtmlString());
		}
		return tooltipWidget;
	}

	public Widget getWidget() {
		return _widget;
	}
	
}
