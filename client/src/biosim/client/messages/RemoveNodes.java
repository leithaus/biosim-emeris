package biosim.client.messages;

import java.util.List;

import m3.gwt.lang.ListX;
import biosim.client.model.Node;
import biosim.client.model.Uid;


public class RemoveNodes extends MessageBody {

	private List<Uid> _uidList;
	
	private RemoveNodes() {
	}
	
	public RemoveNodes(Node n) {
		_uidList = ListX.create();
		_uidList.add(n.getUid());
	}

	// BEGIN_GENERATED_CODE
	
	public java.util.List<biosim.client.model.Uid> getUidList() {
	    return _uidList;
	}
	public void setUidList(java.util.List<biosim.client.model.Uid> uidList0) {
	    _setUidList(uidList0);
	}
	protected void _setUidList(java.util.List<biosim.client.model.Uid> uidList0) {
	    java.util.List<biosim.client.model.Uid> before = _uidList;
	     _uidList = uidList0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.uidList, before, uidList0);
	}
	@Override
	public String toString() {
	    return m3.gwt.props.ToStringBuilder.toString(this, Context);
	}
	public static class RemoveNodesContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext uidList = new m3.gwt.props.impl.AbstractPropertyContext<RemoveNodes,java.util.List<biosim.client.model.Uid>>(this, "uidList", java.util.List.class, 0, biosim.client.model.Uid.class, false) {
	    	    protected java.util.List<biosim.client.model.Uid> getImpl(RemoveNodes bean) { return bean.getUidList(); }
	    	    protected void setImpl(RemoveNodes bean, java.util.List<biosim.client.model.Uid> value ) { bean.setUidList(value);}
	    };
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.messages.RemoveNodes");
	        set = set.insert("biosim.client.messages.MessageBody");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.uidList);
	        return list;
	    }
	    public RemoveNodes newInstance() {
	        return new RemoveNodes();
	    }
	    RemoveNodesContainerContext(Class<?> actualClass) {
	        super(biosim.client.messages.RemoveNodes.class, RemoveNodes.Context);
	    }
	}
	public static final RemoveNodesContainerContext Context = new RemoveNodesContainerContext(RemoveNodes.class);

	// END_GENERATED_CODE
}
