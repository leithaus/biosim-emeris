package biosim.client.eventlist;

import m3.gwt.lang.Function1;

import com.google.gwt.user.client.ui.ListBox;

public class Binder {

	final static Function1<Object, String> _toStringConverter = new Function1<Object, String>() {
		@Override
		public String apply(Object t) {
			return t.toString();
		}
	}; 
	
	public static <T> void attach(final ListBox listBox, ObservableList<T> list, final Function1<T,String> converter) {
		list.addListener(new FineGrainedListListener<T>() {
			@Override
			public void added(ListEvent<T> event) {
				listBox.addItem(converter.apply(event.getElement()));
			}
			@Override
			public void changed(ListEvent<T> event) {
				listBox.setItemText(event.getIndex(), converter.apply(event.getElement()));
			}
			@Override
			public void removed(ListEvent<T> event) {
				listBox.removeItem(event.getIndex());
			}
		});
	}

	@SuppressWarnings("unchecked")
	public static <T> void attach(final ListBox listBox, ObservableList<T> list) {
		attach(listBox, list, (Function1<T, String>)_toStringConverter);
	}
	
}
