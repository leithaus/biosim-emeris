package biosim.client.eventlist;


import java.util.Collections;
import java.util.List;

import m3.gwt.lang.Function1;
import m3.gwt.lang.ListX;

public class FilteredListImpl<T> extends AbstractObservableList<T> implements FilteredList<T> {

	ListListener<T> _listListener = new FineGrainedListListener<T>() {
		public void added(ListEvent<T> event) {
			adjustFilteredToSourceIndexes(sourceToFilteredIndex(event.getIndex(), true), +1);
			if ( _filter.apply(event.getElement()) ) {
				doAdd(event.getIndex(), event.getElement());
			}
		}
		public void changed(ListEvent<T> event) {
			processFilterElement(event.getIndex(), event.getElement(), event);
		}
		public void removed(biosim.client.eventlist.ListEvent<T> event) {
			int filteredIndex = sourceToFilteredIndex(event.getIndex(), true);
			if ( filteredIndex != -1 && filteredIndex < _filteredToSourceIndexes.size() &&  _filteredToSourceIndexes.get(filteredIndex) == event.getIndex() ) {
				doRemove(filteredIndex, event.getElement());
			}
			adjustFilteredToSourceIndexes(filteredIndex, -1);				
		}
	};
	
	Function1<T,Boolean> _filter;
	ObservableList<T> _source;
	List<Integer> _filteredToSourceIndexes = ListX.create();
	Handle _listenerHandle;
	
	@SuppressWarnings("unchecked")
	public FilteredListImpl() {
		this((ObservableList<T>) Observables.empty(), (Function1<T, Boolean>) Observables.acceptEverythingFilter());
	}
	
	FilteredListImpl(ObservableList<T> source, Function1<T, Boolean> filter) {
		setSource(source);
		setFilter(filter);
		reapply();
	}
	
	/*
	 * if returnNearest then this returns a value in the range of 0 to sourceList.size() - 1
	 * else this returns a value in the range of -1 to sourceList.size() - 1
	 */
	int sourceToFilteredIndex(int sourceIndex, boolean returnNearest) {
		int filteredIndex = Collections.binarySearch(_filteredToSourceIndexes, sourceIndex);
		if ( filteredIndex < 0 ) {
			if (returnNearest) {
				filteredIndex = (-1 * filteredIndex) - 1;
			} else {
				filteredIndex = -1;
			}
		}
		return filteredIndex;
	}
	
	int filteredToSourceIndex(int filteredIndex) {
		return _filteredToSourceIndexes.get(filteredIndex);
	}
	
	protected void processFilterElement(int sourceIndex, T element, ListEvent<T> event) {
		boolean accept = _filter.apply(element);
		int filterIndex = sourceToFilteredIndex(sourceIndex, false);
		if ( filterIndex != -1 && !accept ) {
			doRemove(filterIndex, null);
		} else if ( filterIndex == -1 && accept ) {
			doAdd(sourceIndex, element);				
		} else if ( filterIndex != -1 && event != null ) {
			fireEvent(new ListEvent<T>(filterIndex, event.getElement(), event.getType(), event.getPrevious()));
		}
	}

	protected void doAdd(int sourceIndex, T element) {
		// find index to insert at
		int filteredIndex = sourceToFilteredIndex(sourceIndex, true);
		_filteredToSourceIndexes.add(filteredIndex, sourceIndex);
		fireEvent(new ListEvent<T>(filteredIndex, element, ListEventType.Added));
	}

	void adjustFilteredToSourceIndexes(int startIndex, int increment) {
		final int count = _filteredToSourceIndexes.size();
		for ( int i = startIndex ; i < count ; i++ ) {
			_filteredToSourceIndexes.set(i, _filteredToSourceIndexes.get(i) + increment);
		}
	}

	protected void doRemove(int filteredIndex, T element) {
		_filteredToSourceIndexes.remove(filteredIndex);
		if ( element != null ) {
			fireEvent(new ListEvent<T>(filteredIndex, element, ListEventType.Removed));
		}
	}

	@Override
	public void reapply() {
		int count = _source.size();
		for ( int i = 0 ; i < count ; i++ ) {
			processFilterElement(i, _source.get(i), null);
		}
	}

	@Override
	public ObservableList<T> getSource() {
		return _source;
	}
	@Override
	public void setSource(ObservableList<T> source) {
		ObservableList<T> oldSource = _source;
		if ( oldSource != null ) {
			_disposeHandles.remove(_listenerHandle);
			_listenerHandle.dispose();
			for ( int i = _filteredToSourceIndexes.size()-1 ; i >= 0 ; i-- ) {
				doRemove(i, oldSource.get(_filteredToSourceIndexes.get(i)));
			}
		}
		if ( source == null ) {
			source = new ObservableListImpl<T>();
		}
		_filteredToSourceIndexes.clear();
		_source = source;
		_listenerHandle = _source.addListener(_listListener);
		addDisposeHandle(_listenerHandle);
		
		reapply();
	}
	
	@Override
	public Function1<T, Boolean> getFilter() {
		return _filter;
	}
	
	@Override
	public void setFilter(Function1<T, Boolean> filter) {
		if ( filter == null ) {
			filter = Observables.acceptEverythingFilter();
		}
		_filter = filter;
		reapply();
	}
	
	@Override
	public T get(int index) {
		return _source.get(_filteredToSourceIndexes.get(index));
	}
	
	@Override
	public int size() {
		return _filteredToSourceIndexes.size();
	}
	
    public void add(final int index, final T element) {
    	throw new UnsupportedOperationException();
    }

    public T set(final int index, T element0) {
    	throw new UnsupportedOperationException();
    }

    public T remove(final int index) {
    	throw new UnsupportedOperationException();
    }
	
}
