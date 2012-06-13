package biosim.client.fun;


public final class Some<T> extends Option<T> {

	private final T value;
 
    public Some(T value) {
    	if ( value == null ) {
    		throw new NullPointerException("some's cannot hold nulls");
    	}
        this.value = value;
    }
 
    public T get() {
        return value;
    }
	
	public boolean isDefined() {
		return true;
	}
	
	public boolean isEmpty() {
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Some<?> other = (Some<?>) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Some(" + value.toString() + ")";
	}

	public static <T> Option<T> create(T value) {
		return new Some<T>(value);
	}

	
	
}