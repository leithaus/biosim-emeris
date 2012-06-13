package biosim.client.eventlist;

import java.util.List;

import m3.gwt.lang.ListX;

public class ObservableListImpl<T> extends AbstractObservableList<T> {
	
	private final List<T> _delegate = ListX.create();

	
    public void add(final int index, final T element) {
    	_delegate.add(index, element);
    	fireEvent(new ListEvent<T>(index, element, ListEventType.Added));
    }

    public T set(final int index, T element0) {
    	T previous = _delegate.set(index, element0);
    	fireEvent(new ListEvent<T>(index, element0, ListEventType.Changed, previous));
    	return previous;
    }

    public T remove(final int index) {
    	final T element = _delegate.remove(index);
    	fireEvent(new ListEvent<T>(index, element, ListEventType.Removed));
    	return element;
    }
    
    @Override
    public T get(int index) {
    	return _delegate.get(index);
    }
	
	@Override
	public int size() {
		return _delegate.size();
	}

}
