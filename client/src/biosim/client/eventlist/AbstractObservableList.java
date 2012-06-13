package biosim.client.eventlist;

import java.util.AbstractList;
import java.util.List;

import m3.gwt.lang.Function1;
import m3.gwt.lang.ListX;

public abstract class AbstractObservableList<T> extends AbstractList<T> implements ObservableList<T> {
	
	private final EventManager<ListListener<T>> _eventManager = EventManager.create();

	List<Handle> _disposeHandles;

	public FilteredList<T> filter(Function1<T,Boolean> filter) {
		return new FilteredListImpl<T>(this, filter);
	}
	
	@Override
	public boolean remove(Object o) {
		int i = indexOf(o);
		if ( i >= 0 ) {
			remove(i);
			return true;
		} else {
			return false;
		}
	}
	
	protected void addDisposeHandle(Handle handle) {
		if ( _disposeHandles == null ) {
			_disposeHandles = ListX.create();
		}
		_disposeHandles.add(handle);
	}
	
	@Override
	public void dispose() {
		if ( _disposeHandles != null ) {
			for ( Handle handle : _disposeHandles ) {
				handle.dispose();
			}
		}
	}
	
	/**
	 * View map i.e. the mapper function is called any time an item is requested from the
	 * mapped list
	 */
	@Override
	public <U> ObservableList<U> view(final Function1<T, U> mapper) {
		return new MappedObservableList<T,U>(this, mapper);
	}

	/**
	 * Memoized map i.e. the mapper function is only called once per added item
	 * and once per changed
	 */
	@Override
	public <U> ObservableList<U> map(final Function1<T, U> mapper) {
		final ObservableList<T> source = this;
		final ObservableList<U> mappedList = new ObservableListImpl<U>() {
			@Override
			public void reapply() {
				int count = size();
				for ( int i = 0 ; i < count ; i++ ) {
					U current = get(i);
					U reapplied = mapper.apply(source.get(i));
					if ( !current.equals(reapplied) ) {
						set(i, reapplied);
					}
				}
			}
		};
		addListener(new FineGrainedListListener<T>() {
			@Override
			public void added(ListEvent<T> event) {
				mappedList.add(event.getIndex(), mapper.apply(event.getElement()));
			}
			@Override
			public void changed(ListEvent<T> event) {
				mappedList.set(event.getIndex(), mapper.apply(event.getElement()));
			}
			@Override
			public void removed(ListEvent<T> event) {
				mappedList.remove(event.getIndex());
			}
		});
		return mappedList;
	}
	
	@Override
	public void reapply() {
	}
	
	@Override
	public Handle addListener(ListListener<T> listener) {
		int count = size();
		for ( int i = 0 ; i < count ; i++ ) {
			listener.event(new ListEvent<T>(i, get(i), ListEventType.Added));
		}
		return getEventManager().add(listener);
	}
	
	@Override
	public void removeListener(ListListener<T> listener) {
		getEventManager().remove(listener);
	}
	
	protected EventManager<ListListener<T>> getEventManager() {
		return _eventManager;
	}
	
	protected void fireEvent(final ListEvent<T> event) {
		getEventManager().fire(new Function1<ListListener<T>, Void>() {
			@Override
			public Void apply(ListListener<T> t) {
				t.event(event);
				return null;
			}
		});
	}

}
