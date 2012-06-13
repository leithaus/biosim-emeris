package biosim.client.eventlist;

public class FineGrainedListListener<T> implements ListListener<T> {
	
	@Override
	public void event(ListEvent<T> event) {
		if ( event.getType() == ListEventType.Added ) added(event);
		else if ( event.getType() == ListEventType.Changed ) changed(event);
		else if ( event.getType() == ListEventType.Removed ) removed(event);
	}
	
	public void added(ListEvent<T> event) {
	}
	
	public void changed(ListEvent<T> event) {
	}
	
	public void removed(ListEvent<T> event) {
	}
	
}
