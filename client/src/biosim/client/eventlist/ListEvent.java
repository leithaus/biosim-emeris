package biosim.client.eventlist;

public class ListEvent<T> {

	int _index;
	T _element;
	ListEventType _type;
	T _previousValue;
	
	
	public ListEvent(int index, T element, ListEventType type) {
		this(index, element, type, null);
	}
	
	public ListEvent(int index, T element, ListEventType type, T previousValue) {
		_index = index;
		_element = element;
		_type = type;
		_previousValue = previousValue;
	}
	
	public int getIndex() {
		return _index;
	}
	
	public T getElement() {
		return _element;
	}
	
	public ListEventType getType() {
		return _type;
	}
	
	@Override
	public String toString() {
		return "ListEvent(type=" + _type + ")";
	}

	public T getPrevious() {
		return _previousValue;
	}
	
}
