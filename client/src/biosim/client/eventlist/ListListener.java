package biosim.client.eventlist;

public interface ListListener<T> {

	void event(ListEvent<T> event);
	
}
