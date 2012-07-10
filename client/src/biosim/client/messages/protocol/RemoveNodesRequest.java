package biosim.client.messages.protocol;

import java.util.List;

import m3.gwt.lang.ListX;
import m3.gwt.props.ApplyCodeGeneration;
import biosim.client.messages.model.MNode;
import biosim.client.messages.model.Uid;

@ApplyCodeGeneration
public class RemoveNodesRequest extends RequestBody {

	private List<Uid> _uidList;
	
	public RemoveNodesRequest() {
	}
	
	public RemoveNodesRequest(MNode n) {
		_uidList = ListX.create();
		_uidList.add(n.getUid());
	}

	// BEGIN_GENERATED_CODE
	
	public java.util.List<biosim.client.messages.model.Uid> getUidList() {
	    return _uidList;
	}
	public void setUidList(java.util.List<biosim.client.messages.model.Uid> uidList0) {
	    _setUidList(uidList0);
	}
	protected void _setUidList(java.util.List<biosim.client.messages.model.Uid> uidList0) {
	    java.util.List<biosim.client.messages.model.Uid> before = _uidList;
	     _uidList = uidList0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.uidList, before, uidList0);
	}
	@Override
	public String toString() {
	    return m3.gwt.props.ToStringBuilder.toString(this, Context);
	}
	public static class RemoveNodesRequestContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext uidList = new m3.gwt.props.impl.AbstractPropertyContext<RemoveNodesRequest,java.util.List<biosim.client.messages.model.Uid>>(this, "uidList", java.util.List.class, 0, biosim.client.messages.model.Uid.class, false) {
	    	    protected java.util.List<biosim.client.messages.model.Uid> getImpl(RemoveNodesRequest bean) { return bean.getUidList(); }
	    	    protected void setImpl(RemoveNodesRequest bean, java.util.List<biosim.client.messages.model.Uid> value ) { bean.setUidList(value);}
	    };
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("biosim.client.messages.protocol.RemoveNodesRequest");
	        set = set.insert("biosim.client.messages.protocol.RequestBody");
	        set = set.insert("java.lang.Object");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.uidList);
	        return list;
	    }
	    public RemoveNodesRequest newInstance() {
	        return new RemoveNodesRequest();
	    }
	    RemoveNodesRequestContainerContext(Class<?> actualClass) {
	        super(biosim.client.messages.protocol.RemoveNodesRequest.class, RemoveNodesRequest.Context);
	    }
	}
	public static final RemoveNodesRequestContainerContext Context = new RemoveNodesRequestContainerContext(RemoveNodesRequest.class);

	// END_GENERATED_CODE
}
