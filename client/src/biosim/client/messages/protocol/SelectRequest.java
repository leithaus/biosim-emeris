package biosim.client.messages.protocol;

import m3.gwt.props.ApplyCodeGeneration;

@ApplyCodeGeneration
public class SelectRequest extends RequestBody {

	private String _shortClassname;
	
	public SelectRequest() {
	}
	
	// BEGIN_GENERATED_CODE
	
	public java.lang.String getShortClassname() {
	    return _shortClassname;
	}
	public void setShortClassname(java.lang.String shortClassname0) {
	    _setShortClassname(shortClassname0);
	}
	protected void _setShortClassname(java.lang.String shortClassname0) {
	    java.lang.String before = _shortClassname;
	     _shortClassname = shortClassname0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.shortClassname, before, shortClassname0);
	}
	@Override
	public String toString() {
	    return m3.gwt.props.ToStringBuilder.toString(this, Context);
	}
	public static class SelectRequestContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext shortClassname = new m3.gwt.props.impl.AbstractPropertyContext<SelectRequest,java.lang.String>(this, "shortClassname", java.lang.String.class, 0, null, false) {
	    	    protected java.lang.String getImpl(SelectRequest bean) { return bean.getShortClassname(); }
	    	    protected void setImpl(SelectRequest bean, java.lang.String value ) { bean.setShortClassname(value);}
	    };
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("biosim.client.messages.protocol.SelectRequest");
	        set = set.insert("biosim.client.messages.protocol.RequestBody");
	        set = set.insert("java.lang.Object");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.shortClassname);
	        return list;
	    }
	    public SelectRequest newInstance() {
	        return new SelectRequest();
	    }
	    SelectRequestContainerContext(Class<?> actualClass) {
	        super(biosim.client.messages.protocol.SelectRequest.class, SelectRequest.Context);
	    }
	}
	public static final SelectRequestContainerContext Context = new SelectRequestContainerContext(SelectRequest.class);

	// END_GENERATED_CODE
}
