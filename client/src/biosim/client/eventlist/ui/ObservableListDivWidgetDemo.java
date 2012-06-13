package biosim.client.eventlist.ui;

import m3.gwt.lang.Function1;
import biosim.client.eventlist.ListEvent;
import biosim.client.eventlist.ListListener;
import biosim.client.eventlist.ObservableList;
import biosim.client.eventlist.Observables;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ObservableListDivWidgetDemo {

	ObservableList<String> _stringList = Observables.create();
	
	ObservableList<String> _filteredList = _stringList.filter(new Function1<String,Boolean>() {
		@Override
		public Boolean apply(String t) {
			return t.length() > 5;
		}
	});
	
	ObservableList<Widget> _widgetList = _filteredList.map(new Function1<String, Widget>() {
		@Override
		public Widget apply(String text) {
			return new HTML(text);
		}
	});
	
	VerticalPanel _panel = new VerticalPanel();
	
	{
		final TextArea textArea = new TextArea();
		_panel.add(textArea);

		final TextBox index = new TextBox();
		_panel.add(index);

		final Button add = new Button("Add");
		_panel.add(add);
		add.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				int i;
				if ( index.getText().trim().length() == 0 ) {
					i = _stringList.size();
				} else {
					i = Integer.parseInt(index.getText());
				}
				_stringList.add(i, textArea.getText());
			}
		});

		final Button remove = new Button("Remove");
		_panel.add(remove);
		remove.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				int i;
				if ( index.getText().trim().length() == 0 ) {
					i = _stringList.size() - 1;
				} else {
					i = Integer.parseInt(index.getText());
				}
				_stringList.remove(i);
			}
		});
		
		final Label stringListLabel = new Label();
		_panel.add(stringListLabel);
		_stringList.addListener(new ListListener<String>() {
			@Override
			public void event(ListEvent<String> event) {
				stringListLabel.setText(_stringList.toString());
			}
		});

		final Label filterListLabel = new Label();
		_panel.add(filterListLabel);
		_filteredList.addListener(new ListListener<String>() {
			@Override
			public void event(ListEvent<String> event) {
				filterListLabel.setText(_filteredList.toString());
			}
		});

		
		Widget widget = ObservableListPanelAdapter.create(_filteredList, new HorizontalPanelBuilder(), new Function1<String, Widget>() {
			@Override
			public Widget apply(String s) {
				return new HTML(s);
			}
		}).getWidget();
		
		_panel.add(widget);

	}

	public VerticalPanel getPanel() {
		return _panel;
	}
	
}
