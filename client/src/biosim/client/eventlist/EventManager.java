package biosim.client.eventlist;

import java.util.List;

import m3.gwt.lang.Function1;
import m3.gwt.lang.ListX;

public class EventManager<T> {

	List<T> _listeners = ListX.create();
	
	public Handle add(final T t) {
		while(_listeners.contains(t)) {
			remove(t);
		}
		_listeners.add(t);
		return new Handle() {
			public void dispose() {
				remove(t);
			}
		};
	}
	
	public void remove(T t) {
		_listeners.remove(t);
	}
	
	public void fire(Function1<T,?> fun) {
		for ( T t : _listeners ) {
			fun.apply(t);
		}
	}

	public static <T> EventManager<T> create() {
		return new EventManager<T>();
	}
	
}
