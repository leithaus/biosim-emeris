package biosim.client.eventlist;

import m3.gwt.lang.Function1;

public interface FilteredList<T> extends ObservableList<T> {
	
	ObservableList<T> getSource();
	void setSource(ObservableList<T> source);

	void setFilter(Function1<T,Boolean> filter);
	Function1<T,Boolean> getFilter();
	
}
