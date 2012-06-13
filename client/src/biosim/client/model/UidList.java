package biosim.client.model;

import java.util.List;

import m3.gwt.lang.ListX;
import m3.gwt.props.ApplyCodeGeneration;

@ApplyCodeGeneration
public class UidList {

	private List<Uid> _uids = ListX.create();
	
	// BEGIN_GENERATED_CODE
	
	public java.util.List<biosim.client.model.Uid> getUids() {
	    return _uids;
	}
	public void setUids(java.util.List<biosim.client.model.Uid> uids0) {
	    _setUids(uids0);
	}
	protected void _setUids(java.util.List<biosim.client.model.Uid> uids0) {
	    java.util.List<biosim.client.model.Uid> before = _uids;
	     _uids = uids0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.uids, before, uids0);
	}
	@Override
	public String toString() {
	    return m3.gwt.props.ToStringBuilder.toString(this, Context);
	}
	public static class UidListContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext uids = new m3.gwt.props.impl.AbstractPropertyContext<UidList,java.util.List<biosim.client.model.Uid>>(this, "uids", java.util.List.class, 0, biosim.client.model.Uid.class, false) {
	    	    protected java.util.List<biosim.client.model.Uid> getImpl(UidList bean) { return bean.getUids(); }
	    	    protected void setImpl(UidList bean, java.util.List<biosim.client.model.Uid> value ) { bean.setUids(value);}
	    };
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("biosim.client.model.UidList");
	        set = set.insert("java.lang.Object");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.uids);
	        return list;
	    }
	    public UidList newInstance() {
	        return new UidList();
	    }
	    UidListContainerContext(Class<?> actualClass) {
	        super(biosim.client.model.UidList.class);
	    }
	}
	public static final UidListContainerContext Context = new UidListContainerContext(UidList.class);

	// END_GENERATED_CODE
}
