package biosim.client.model;

import com.google.gwt.json.client.JSONObject;

public class Phone extends Node {
	
	private String _value;

	private Phone() {
	}
	
	public Phone(DataSet dataSet, String value) {
		super(dataSet);
		this._value = value;
	}
	
	public Phone(DataSet dataSet, JSONObject jo) {
		super(dataSet, jo);
		this._value = jo.get("value").isString().stringValue();
	}
	
	@Override
	public String toHtmlString() {
		return _value;
	}
	
	@Override
	public String getName() {
		return null;
	}
	
	@Override
	public String toString() {
		return "Phone: " + _value;
	}
	
	@Override
	public String getIconUrl() {
		return "/images/phone.png";
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
	public static class PhoneContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext value = new m3.gwt.props.impl.AbstractPropertyContext<Phone,java.lang.String>(this, "value", java.lang.String.class, 0, null, false) {
	    	    protected java.lang.String getImpl(Phone bean) { return bean.getValue(); }
	    	    protected void setImpl(Phone bean, java.lang.String value ) { bean.setValue(value);}
	    };
	    public m3.gwt.props.PropertyContext uid = Node.Context.uid;
	    public m3.gwt.props.PropertyContext created = Node.Context.created;
	    public m3.gwt.props.PropertyContext dataSet = Node.Context.dataSet;
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.model.Node");
	        set = set.insert("biosim.client.model.Phone");
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
	    public Phone newInstance() {
	        return new Phone();
	    }
	    PhoneContainerContext(Class<?> actualClass) {
	        super(biosim.client.model.Phone.class, Phone.Context);
	    }
	}
	public static final PhoneContainerContext Context = new PhoneContainerContext(Phone.class);

	// END_GENERATED_CODE
}
