package biosim.client.messages.protocol;

import biosim.client.messages.model.MNode;

public class InsertOrUpdateRequest extends RequestBody {

	private MNode _node;
	
	public InsertOrUpdateRequest() {
	}
	
	public InsertOrUpdateRequest(MNode node) {
		_node = node;
	}

	
	// BEGIN_GENERATED_CODE
	
	public biosim.client.messages.model.MNode getNode() {
	    return _node;
	}
	public void setNode(biosim.client.messages.model.MNode node0) {
	    _setNode(node0);
	}
	protected void _setNode(biosim.client.messages.model.MNode node0) {
	    biosim.client.messages.model.MNode before = _node;
	     _node = node0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.node, before, node0);
	}
	@Override
	public String toString() {
	    return m3.gwt.props.ToStringBuilder.toString(this, Context);
	}
	public static class InsertOrUpdateRequestContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext node = new m3.gwt.props.impl.AbstractPropertyContext<InsertOrUpdateRequest,biosim.client.messages.model.MNode>(this, "node", biosim.client.messages.model.MNode.class, 0, null, false) {
	    	    protected biosim.client.messages.model.MNode getImpl(InsertOrUpdateRequest bean) { return bean.getNode(); }
	    	    protected void setImpl(InsertOrUpdateRequest bean, biosim.client.messages.model.MNode value ) { bean.setNode(value);}
	    };
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("biosim.client.messages.protocol.RequestBody");
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.messages.protocol.InsertOrUpdateRequest");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.node);
	        return list;
	    }
	    public InsertOrUpdateRequest newInstance() {
	        return new InsertOrUpdateRequest();
	    }
	    InsertOrUpdateRequestContainerContext(Class<?> actualClass) {
	        super(biosim.client.messages.protocol.InsertOrUpdateRequest.class, InsertOrUpdateRequest.Context);
	    }
	}
	public static final InsertOrUpdateRequestContainerContext Context = new InsertOrUpdateRequestContainerContext(InsertOrUpdateRequest.class);

	// END_GENERATED_CODE
}
