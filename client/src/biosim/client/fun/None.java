package biosim.client.fun;

public final class None<T> extends Option<T> {
 
	static final int _defaultHashCode = new Object().hashCode();
	static final None<?> _universal = new None<Object>();
	
    public None() {}
 
    public T get() {
        throw new UnsupportedOperationException("Cannot resolve value on None");
    }
        
    @SuppressWarnings("unchecked")
	public static <T> None<T> apply() {
    	return (None<T>) _universal;
    }
	
	public boolean isDefined() {
		return false;
	}
	
	public boolean isEmpty() {
		return true;
	}
	
    @Override
    public boolean equals(Object obj) {
    	return obj instanceof None;
    }
    
    @Override
    public int hashCode() {
    	return _defaultHashCode;
    }
    
    @Override
    public String toString() {
    	return "None";
    }

}
