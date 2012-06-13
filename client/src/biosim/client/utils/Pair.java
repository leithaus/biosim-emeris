package biosim.client.utils;


public class Pair<T,U> {

	final T _left;
	final U _right;
	
	public Pair(T left, U right) {
		_left = left;
		_right = right;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_left == null) ? 0 : _left.hashCode());
		result = prime * result + ((_right == null) ? 0 : _right.hashCode());
		return result;
	}

	@Override @SuppressWarnings("rawtypes")
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pair other = (Pair) obj;
		if (_left == null) {
			if (other._left != null)
				return false;
		} else if (!_left.equals(other._left))
			return false;
		if (_right == null) {
			if (other._right != null)
				return false;
		} else if (!_right.equals(other._right))
			return false;
		return true;
	}

	public static <T, U> Pair<T, U> create(T left, U right) {
		return new Pair<T, U> (left, right);
	}

	@Override
	public String toString() {
		return "(" + _left + ", " + _right + ")";
	}
	
}
