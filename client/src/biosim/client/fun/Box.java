package biosim.client.fun;

public interface Box<T> {

	boolean isEmpty();
	boolean isFull();
	
	T get();
	
}
