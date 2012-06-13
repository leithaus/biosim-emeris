package biosim.client.model;


public class Address extends Node {

	private String _value;

	private Address() {
		super(null);
	}
	
	public Address(DataSet dataSet, String value) {
		super(dataSet);
		this._value = value;
	}
	
	public Address(DataSet dataSet, Uid random, String value) {
		super(dataSet, random);
		this._value = value;
	}

	@Override
	public String toHtmlString() {
		return _value.replace("\n", "<br>");
	}
	
	@Override
	public String getName() {
		return null;
	}
	
	@Override
	public String toString() {
		return "Address: " + _value;
	}
	
	@Override
	public String getIconUrl() {
		return "/images/address.png";
	}
	
	// BEGIN_GENERATED_CODE
	
	public java.lang.String getValue() {
	    return _value;
	}
	public void setValue(java.lang.String value0) {
	    _setValue(value0);
	}
	protected void _setValue(java.lang.String value0) {
	    java.lang.String before = _value;
	     _value = value0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.value, before, value0);
	}
	public static class AddressContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext value = new m3.gwt.props.impl.AbstractPropertyContext<Address,java.lang.String>(this, "value", java.lang.String.class, 0, null, false) {
	    	    protected java.lang.String getImpl(Address bean) { return bean.getValue(); }
	    	    protected void setImpl(Address bean, java.lang.String value ) { bean.setValue(value);}
	    };
	    public m3.gwt.props.PropertyContext uid = Node.Context.uid;
	    public m3.gwt.props.PropertyContext created = Node.Context.created;
	    public m3.gwt.props.PropertyContext dataSet = Node.Context.dataSet;
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.model.Node");
	        set = set.insert("biosim.client.model.Address");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.uid);
	        list = list.cons(this.created);
	        list = list.cons(this.dataSet);
	        list = list.cons(this.value);
	        return list;
	    }
	    public Address newInstance() {
	        return new Address();
	    }
	    AddressContainerContext(Class<?> actualClass) {
	        super(biosim.client.model.Address.class, Address.Context);
	    }
	}
	public static final AddressContainerContext Context = new AddressContainerContext(Address.class);

	// END_GENERATED_CODE
}
