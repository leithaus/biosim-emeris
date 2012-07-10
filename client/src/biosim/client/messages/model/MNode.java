package biosim.client.messages.model;

import m3.gwt.props.ApplyCodeGeneration;

@ApplyCodeGeneration
public class MNode {

	// TDGlen only allocate a Uid when it is absolutely necessary
	private Uid _uid = Uid.random();
	
	public MNode() {
	}
	
	public MNode(Uid _uid) {
		this._uid = _uid;
	}

	public String getVisualId() {
		return null;
	}

	public String getName() {
		return null;
	}

	public String toHtmlString() {
		return null;
	}

	public String getIconUrl() {
		return null;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getUid() == null) ? 0 : getUid().hashCode());
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
		MNode other = (MNode) obj;
		if (getUid() == null) {
			if (other.getUid() != null)
				return false;
		} else if (!getUid().equals(other.getUid()))
			return false;
		return true;
	}

	public biosim.client.messages.model.Uid getUid() {
		if ( _uid == null ) {
			_uid = Uid.random();
		}
	    return _uid;
	}

	// TDGlen remove me
	public String canBeSeenBy(MConnection dropTarget) {
		return null;
	}
	// TDGlen remove me
	public boolean isParentOf(MNode node) {
		return false;
	}
	// TDGlen remove me
	public boolean hasChild(MNode node) {
		return false;
	}
	
	// BEGIN_GENERATED_CODE
	
	public void setUid(biosim.client.messages.model.Uid uid0) {
	    _setUid(uid0);
	}
	protected void _setUid(biosim.client.messages.model.Uid uid0) {
	    biosim.client.messages.model.Uid before = _uid;
	     _uid = uid0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.uid, before, uid0);
	}
	@Override
	public String toString() {
	    return m3.gwt.props.ToStringBuilder.toString(this, Context);
	}
	public static class MNodeContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext uid = new m3.gwt.props.impl.AbstractPropertyContext<MNode,biosim.client.messages.model.Uid>(this, "uid", biosim.client.messages.model.Uid.class, 0, null, false) {
	    	    protected biosim.client.messages.model.Uid getImpl(MNode bean) { return bean.getUid(); }
	    	    protected void setImpl(MNode bean, biosim.client.messages.model.Uid value ) { bean.setUid(value);}
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
