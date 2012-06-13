package biosim.client.eventlist;

import m3.gwt.lang.Function1;

public class MappedObservableList<T,U> extends AbstractObservableList<U> {
	
	final Function1<T,U> _mapper; 
	final ObservableList<T> _source;
	
	public MappedObservableList(ObservableList<T> source, Function1<T, U> mapper) {
		_mapper = mapper;
		_source = source;

		Handle handle = _source.addListener(new ListListener<T>() {
			@Override
			public void event(ListEvent<T> event) {
				U u = _mapper.apply(event.getElement());
				U previous = null;
				if ( event.getPrevious() != null ) {
					previous = _mapper.apply(event.getPrevious());
				}
				fireEvent(new ListEvent<U>(event.getIndex(), u, event.getType(), previous));
			}
		});
		addDisposeHandle(handle);
		
	}
	
	@Override
	public U get(int index) {
		return _mapper.apply(_source.get(index));
	}
	@Override
	public int size() {
		return _source.size();
	}

}
