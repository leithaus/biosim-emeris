package biosim.client.messages.model;

import m3.gwt.props.ApplyCodeGeneration;
import biosim.client.model.Uid;

@ApplyCodeGeneration
public class MNode {

	private Uid _uid;
	private String _name;
	
	public MNode() {
	}
	
	public MNode(Uid _uid, String _name) {
		this._uid = _uid;
		this._name = _name;
	}
	
	// BEGIN_GENERATED_CODE
	
	public biosim.client.model.Uid getUid() {
	    return _uid;
	}
	public void setUid(biosim.client.model.Uid uid0) {
	    _setUid(uid0);
	}
	protected void _setUid(biosim.client.model.Uid uid0) {
	    biosim.client.model.Uid before = _uid;
	     _uid = uid0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.uid, before, uid0);
	}
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
	public static class MNodeContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext uid = new m3.gwt.props.impl.AbstractPropertyContext<MNode,biosim.client.model.Uid>(this, "uid", biosim.client.model.Uid.class, 0, null, false) {
	    	    protected biosim.client.model.Uid getImpl(MNode bean) { return bean.getUid(); }
	    	    protected void setImpl(MNode bean, biosim.client.model.Uid value ) { bean.setUid(value);}
	    };
	    public m3.gwt.props.PropertyContext name = new m3.gwt.props.impl.AbstractPropertyContext<MNode,java.lang.String>(this, "name", java.lang.String.class, 1, null, false) {
	    	    protected java.lang.String getImpl(MNode bean) { return bean.getName(); }
	    	    protected void setImpl(MNode bean, java.lang.String value ) { bean.setName(value);}
	    };
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.messages.model.MNode");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.uid);
	        list = list.cons(this.name);
	        return list;
	    }
	    public MNode newInstance() {
	        return new MNode();
	    }
	    MNodeContainerContext(Class<?> actualClass) {
	        super(biosim.client.messages.model.MNode.class);
	    }
	}
	public static final MNodeContainerContext Context = new MNodeContainerContext(MNode.class);

	// END_GENERATED_CODE
}
