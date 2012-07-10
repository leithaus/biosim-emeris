package biosim.client.messages.model;

import m3.gwt.props.ApplyCodeGeneration;

@ApplyCodeGeneration
public class MNamedNode extends MNode {

	private String _name;
	
	public MNamedNode() {
	}
	
	public MNamedNode(String _name) {
		this._name = _name;
	}
	
	@Override
	public String toHtmlString() {
		return getName();
	}
	
	@Override
	public String getVisualId() {
		return getName();
	}

	// BEGIN_GENERATED_CODE
	
	public java.lang.String getName() {
	    return _name;
	}
	public void setName(java.lang.String name0) {
	    _setName(name0);
	}
	protected void _setName(java.lang.String name0) {
	    java.lang.String before = _name;
	     _name = name0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.name, before, name0);
	}
	@Override
	public String toString() {
	    return m3.gwt.props.ToStringBuilder.toString(this, Context);
	}
	public static class MNamedNodeContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext name = new m3.gwt.props.impl.AbstractPropertyContext<MNamedNode,java.lang.String>(this, "name", java.lang.String.class, 0, null, false) {
	    	    protected java.lang.String getImpl(MNamedNode bean) { return bean.getName(); }
	    	    protected void setImpl(MNamedNode bean, java.lang.String value ) { bean.setName(value);}
	    };
	    public m3.gwt.props.PropertyContext uid = MNode.Context.uid;
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("biosim.client.messages.model.MNode");
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.messages.model.MNamedNode");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.uid);
	        list = list.cons(this.name);
	        return list;
	    }
	    public MNamedNode newInstance() {
	        return new MNamedNode();
	    }
	    MNamedNodeContainerContext(Class<?> actualClass) {
	        super(biosim.client.messages.model.MNamedNode.class, MNamedNode.Context);
	    }
	}
	public static final MNamedNodeContainerContext Context = new MNamedNodeContainerContext(MNamedNode.class);

	// END_GENERATED_CODE
}
