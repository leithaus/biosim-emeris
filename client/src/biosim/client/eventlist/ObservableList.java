package biosim.client.eventlist;

import java.util.List;

import m3.gwt.lang.Function1;

public interface ObservableList<T> extends List<T> {

	Handle addListener(ListListener<T> listener);
	void removeListener(ListListener<T> listener);
	
	FilteredList<T> filter(Function1<T,Boolean> filter);
	<U> ObservableList<U> map(Function1<T,U> mapper);
	<U> ObservableList<U> view(Function1<T, U> mapper);

	/**
	 * for filtered or map'ed lists this will reapply the function to each element.  for plain vanilla ObservableList's it is a noop
	 */
	void reapply();
	
	
	/**
	 * for view'ed, filter'ed or map'ed lists this will remove the listenered from the source list
	 */
	void dispose();
	
}
