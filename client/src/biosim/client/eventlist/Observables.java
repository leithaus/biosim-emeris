package biosim.client.eventlist;

import m3.gwt.lang.Function1;

public class Observables {
	
	final private static class EmptyObservableList extends AbstractObservableList<Object> {
		public int size() {
			return 0;
		}
		public Object get(int index) {
			throw new IndexOutOfBoundsException();
		}		
	}
	
	final static ObservableList<Object> _emptyList = new EmptyObservableList();
	
	final static Function1<Object, Boolean> _acceptEverythingFilter = new Function1<Object, Boolean>() {
		@Override
		public Boolean apply(Object t) {
			return true;
		}
	};

	
	@SuppressWarnings("unchecked")
	public static <T> ObservableList<T> empty() {
		return (ObservableList<T>) _emptyList;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Function1<T, Boolean> acceptEverythingFilter() {
		return (Function1<T, Boolean>) _acceptEverythingFilter;
	}
	
	public static <T> ObservableList<T> create() {
		return new ObservableListImpl<T>();
	}

	public static <T> ObservableList<T> create(T...args) {
		ObservableList<T> list = create();
		for ( T t : args ) {
			list.add(t);
		}
		return list;
	}

	public static <T> void diff(ObservableList<T> source, ObservableList<T> target, DiffHandler<T> diffHandler) {
		// deletes
		for ( T t : source ) {
			if ( !target.contains(t) ) {
				diffHandler.removed(t);
			}
		}
		// adds
		for ( T t : target ) {
			if ( !source.contains(t) ) {
				diffHandler.added(t);
			}
		}
	}
	
	public static interface DiffHandler<T> {
		void added(T t);
		void removed(T t);
	}
	
}
