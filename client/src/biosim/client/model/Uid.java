package biosim.client.model;

import m3.gwt.props.ApplyCodeGeneration;

@ApplyCodeGeneration
public class Uid {

	private static final char[] CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray(); 
	
	private String _value;
	
	private Uid() {
	}
	
	public Uid(String value) {
		_value = value;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_value == null) ? 0 : _value.hashCode());
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
		Uid other = (Uid) obj;
		if (_value == null) {
			if (other._value != null)
				return false;
		} else if (!_value.equals(other._value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Uid(" + _value + ")";
	}
	/**
	 * Generate a random uuid of the specified length. Example: uuid(15) returns
	 * "VcydxgltxrVZSTV"
	 * 
	 * @param len
	 *            the desired number of characters
	 */
	public static Uid random() {
		return new Uid(random(32, CHARS.length));
	}

	/**
	 * Generate a random uuid of the specified length, and radix. Examples:
	 * <ul>
	 * <li>uuid(8, 2) returns "01001010" (8 character ID, base=2)
	 * <li>uuid(8, 10) returns "47473046" (8 character ID, base=10)
	 * <li>uuid(8, 16) returns "098F4D35" (8 character ID, base=16)
	 * </ul>
	 * 
	 * @param len
	 *            the desired number of characters
	 * @param radix
	 *            the number of allowable values for each character (must be <=
	 *            62)
	 */
	private static String random(int len, int radix) {
		if (radix > CHARS.length) {
			throw new IllegalArgumentException();
		}
		char[] uuid = new char[len];
		// Compact form
		for (int i = 0; i < len; i++) {
			uuid[i] = CHARS[(int)(Math.random()*radix)];
		}
		return new String(uuid);
	}

	public String asString() {
		return _value;
	}

	private void setValue(java.lang.String value0) {
	    _setValue(value0);
	}
	
	// BEGIN_GENERATED_CODE
	
	public java.lang.String getValue() {
	    return _value;
	}
	protected void _setValue(java.lang.String value0) {
	    java.lang.String before = _value;
	     _value = value0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.value, before, value0);
	}
	public static class UidContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext value = new m3.gwt.props.impl.AbstractPropertyContext<Uid,java.lang.String>(this, "value", java.lang.String.class, 0, null, false) {
	    	    protected java.lang.String getImpl(Uid bean) { return bean.getValue(); }
	    	    protected void setImpl(Uid bean, java.lang.String value ) { bean.setValue(value);}
	    };
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("biosim.client.model.Uid");
	        set = set.insert("java.lang.Object");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.value);
	        return list;
	    }
	    public Uid newInstance() {
	        return new Uid();
	    }
	    UidContainerContext(Class<?> actualClass) {
	        super(biosim.client.model.Uid.class);
	    }
	}
	public static final UidContainerContext Context = new UidContainerContext(Uid.class);

	// END_GENERATED_CODE
}
