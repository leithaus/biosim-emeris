package biosim.client.fun;


public abstract class Option<T> {
    
	public abstract T get();
	
	public abstract boolean isDefined();
	public abstract boolean isEmpty();
    
	public static <T> Option<T> apply(T t) {
		if ( t == null ) return None.apply();
		else return new Some<T>(t);
	}
    
}
