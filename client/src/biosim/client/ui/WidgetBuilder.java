package biosim.client.ui;

import static com.google.gwt.query.client.GQuery.$;
import static com.google.gwt.query.client.plugins.Effects.Effects;

import java.util.Map;

import m3.gwt.lang.Function1;
import m3.gwt.lang.MapX;
import biosim.client.eventlist.FineGrainedListListener;
import biosim.client.eventlist.ListEvent;
import biosim.client.eventlist.ObservableList;

import com.google.gwt.query.client.Function;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public abstract class WidgetBuilder<T> {

	Map<T, Widget> _builtWidgets = MapX.create();
	Panel _panel;
	

	WidgetBuilder(ObservableList<T> list, Panel panel) {
		_panel = panel;
		list.addListener(new FineGrainedListListener<T>() {
			@Override
			public void added(ListEvent<T> event) {
				addElement(event.getElement());
			}
			@Override
			public void changed(ListEvent<T> event) {
				removeElement(event.getElement());
				addElement(event.getElement());
			}
			@Override
			public void removed(ListEvent<T> event) {
				removeElement(event.getElement());
			}
		});
	}
	
	public Map<T, Widget> getBuiltWidgets() {
		return _builtWidgets;
	}
	
	protected void addElement(T t) {
		Widget w = buildWidget(t);
		_builtWidgets.put(t, w);
		addWidgetToParent(w);
	}
	protected void removeElement(T t) {
		final Widget widget = _builtWidgets.get(t);
		$(widget).as(Effects).slideUp(2000, new Function() {
			public void f() {
				removeWidgetFromParent(widget);
			}
		});
	}
	
	public abstract Widget buildWidget(T t);
	
	protected Widget createParent() {
		return new FlowPanel();
	}
	
	public void addWidgetToParent(Widget w) {
		_panel.add(w);
	}
	
	public void removeWidgetFromParent(Widget w) {
		_panel.remove(w);
	}
	
	public Panel getPanel() {
		return _panel;
	}
	
	public static <T> WidgetBuilder<T> create(final ObservableList<T> list, final Function1<T,Widget> fun) {
		return new WidgetBuilder<T>(list, new FlowPanel()) {
			public Widget buildWidget(T t) {
				return fun.apply(t);
			}
		};
	}
	
}
