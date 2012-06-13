package biosim.client.eventlist.ui;

import java.util.HashMap;
import java.util.Map;

import m3.gwt.lang.Function1;
import biosim.client.eventlist.FineGrainedListListener;
import biosim.client.eventlist.ListEvent;
import biosim.client.eventlist.ObservableList;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * Takes an observable list of widgets and sticks them in a series of div elements
 */
public class ObservableListPanelAdapter<T> {

	final ObservableList<T> _model;
	final Map<T, Widget> _modelToWidgetMap = new HashMap<T, Widget>();
	final Function1<T,Widget> _widgetGenerator;
	final ListPanelBuilder _listPanelBuilder;
	
	public ObservableListPanelAdapter(ObservableList<T> model, ListPanelBuilder listPanelBuilder, Function1<T,Widget> widgetGenerator) {
		_model = model;
		_listPanelBuilder = listPanelBuilder;
		_widgetGenerator = widgetGenerator;
		_model.addListener(new FineGrainedListListener<T>() {
			@Override
			public void added(ListEvent<T> event) {
				handleInsert(event.getIndex(), createWidget(event.getElement()), event.getElement());
			}
			@Override
			public void removed(ListEvent<T> event) {
				handleRemove(event.getIndex(), _modelToWidgetMap.get(event.getElement()), event.getElement());
			}
			@Override
			public void changed(ListEvent<T> event) {
				handleChange(event.getIndex(), event.getElement(), event.getPrevious());
			}
		});
	}
	
	public Widget createWidget(T t) {
		return _widgetGenerator.apply(t);
	}
	
	public void handleChange(int index, T value, T previousValue) {
		if ( !value.equals(previousValue) ) {
			handleRemove(index, _modelToWidgetMap.get(previousValue), previousValue);
			handleInsert(index, createWidget(value), value);
		}
	}

	public void handleInsert(int index, Widget widget, T t) {
		_modelToWidgetMap.put(t, widget);
		_listPanelBuilder.insert(index, widget);
	}

	public Element createChildDiv() {
		return DOM.createDiv();
	}
	
	public Widget getWidget() {
		return _listPanelBuilder.getWidget();
	}
	
	public void handleRemove(int index, Widget widget, T t) {
		_listPanelBuilder.remove(index, widget);
		_modelToWidgetMap.remove(t);
	}

	public static <T> ObservableListPanelAdapter<T> create(ObservableList<T> model, ListPanelBuilder listPanelBuilder, Function1<T,Widget> widgetGenerator) {
		return new ObservableListPanelAdapter<T>(model, listPanelBuilder, widgetGenerator);
	}

	
}
